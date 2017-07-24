package ss.com.bannerslider.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ss.com.bannerslider.R;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerAdapter;
import ss.com.bannerslider.views.IAttributeChange;
import ss.com.bannerslider.views.SlideIndicatorsGroup;
import ss.com.bannerslider.views.indicators.IndicatorShape;

/**
 * @author S.Shahini
 * @since 11/23/16
 */

public class BannerSlider extends FrameLayout implements ViewPager.OnPageChangeListener, IAttributeChange {
    private static final String TAG = "BannerSlider";
    private List<Banner> banners = new ArrayList<>();
    private AppCompatActivity hostActivity;
    private CustomViewPager viewPager;
    private OnBannerClickListener onBannerClickListener;

    //Custom attributes
    private Drawable selectedSlideIndicator;
    private Drawable unSelectedSlideIndicator;
    private int defaultIndicator;
    private int indicatorSize;
    private boolean mustAnimateIndicators;
    private boolean mustLoopSlides;

    private SlideIndicatorsGroup slideIndicatorsGroup;
    private int slideShowInterval = 1000;
    private Timer timer;
    private int defaultBanner = 0;
    @LayoutRes
    private int emptyView;
    private boolean hideIndicators = false;

    private List<Banner> bannersQueue = new ArrayList<>();
    private boolean setupIsCalled = false;
    private boolean mustWrapContent;

    public BannerSlider(Context context) {
        super(context);
    }

    public BannerSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseCustomAttributes(attrs);
    }

    public BannerSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseCustomAttributes(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BannerSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseCustomAttributes(attrs);
    }

    private void parseCustomAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.BannerSlider);
            try {
                indicatorSize = typedArray.getDimensionPixelSize(R.styleable.BannerSlider_indicatorSize, getResources().getDimensionPixelSize(R.dimen.default_indicator_size));
                selectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_selected_slideIndicator);
                unSelectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_unselected_slideIndicator);
                defaultIndicator = typedArray.getInt(R.styleable.BannerSlider_defaultIndicators, IndicatorShape.DASH);
                mustAnimateIndicators = typedArray.getBoolean(R.styleable.BannerSlider_animateIndicators, true);
                slideShowInterval = typedArray.getInt(R.styleable.BannerSlider_interval, 0);
                mustLoopSlides = typedArray.getBoolean(R.styleable.BannerSlider_loopSlides, false);
                defaultBanner = typedArray.getInteger(R.styleable.BannerSlider_defaultBanner, defaultBanner);
                emptyView = typedArray.getResourceId(R.styleable.BannerSlider_emptyView, 0);
                hideIndicators = typedArray.getBoolean(R.styleable.BannerSlider_hideIndicators, false);

                Log.e(TAG, "parseCustomAttributes: ");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        }
        if (!isInEditMode()) {
            setup();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void setup() {
        if (!isInEditMode()) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (getContext() instanceof AppCompatActivity) {
                        hostActivity = (AppCompatActivity) getContext();
                    } else {
                        throw new RuntimeException("Host activity must extend AppCompatActivity");
                    }
                    boolean mustMakeViewPagerWrapContent = getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT;

                    viewPager = new CustomViewPager(getContext(),mustMakeViewPagerWrapContent);
                    viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        viewPager.setId(View.generateViewId());
                    } else {
                        int id = Math.abs(new Random().nextInt((5000 - 1000) + 1) + 1000);
                        viewPager.setId(id);
                    }
                    viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    viewPager.addOnPageChangeListener(BannerSlider.this);
                    addView(viewPager);
                    if (!hideIndicators) {
                        slideIndicatorsGroup = new SlideIndicatorsGroup(getContext(), selectedSlideIndicator, unSelectedSlideIndicator, defaultIndicator, indicatorSize, mustAnimateIndicators);
                        addView(slideIndicatorsGroup);
                    }

                    setupTimer();
                    setupIsCalled = true;
                    renderRemainingBanners();
                }
            });
        }

    }

    public void setBanners(List<Banner> banners) {
        if (setupIsCalled) {
            this.banners = banners;

            for (int i = 0; i < banners.size(); i++) {
                banners.get(i).setPosition(i);
                banners.get(i).setOnBannerClickListener(onBannerClickListener);
                banners.get(i).setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            stopTimer();
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            setupTimer();
                        }
                        return false;
                    }
                });
                slideIndicatorsGroup.onSlideAdd();
            }

            BannerAdapter bannerAdapter;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                bannerAdapter = new BannerAdapter(hostActivity.getSupportFragmentManager(), mustLoopSlides, getLayoutDirection(), banners);
            } else {
                bannerAdapter = new BannerAdapter(hostActivity.getSupportFragmentManager(), mustLoopSlides, banners);
            }

            viewPager.setAdapter(bannerAdapter);

            if (mustLoopSlides) {
                if (Build.VERSION.SDK_INT>=17){
                    if (getLayoutDirection() == LAYOUT_DIRECTION_LTR) {
                        viewPager.setCurrentItem(1, false);
                        slideIndicatorsGroup.onSlideChange(0);
                    } else {
                        viewPager.setCurrentItem(banners.size(), false);
                        slideIndicatorsGroup.onSlideChange(banners.size() - 1);
                    }
                }else {
                    viewPager.setCurrentItem(banners.size(), false);
                    slideIndicatorsGroup.onSlideChange(banners.size() - 1);
                }

            }
        } else {
            bannersQueue.addAll(banners);
        }


    }

    private void renderRemainingBanners() {
        setBanners(bannersQueue);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mustLoopSlides) {
            if (position == 0) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(banners.size(), false);
                    }
                }, 400);
                if (slideIndicatorsGroup != null) {
                    slideIndicatorsGroup.onSlideChange(banners.size() - 1);
                }
            } else if (position == banners.size() + 1) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(1, false);
                    }
                }, 400);
                if (slideIndicatorsGroup != null) {
                    slideIndicatorsGroup.onSlideChange(0);
                }
            } else {
                if (slideIndicatorsGroup != null) {
                    slideIndicatorsGroup.onSlideChange(position - 1);
                }
            }
        } else {
            slideIndicatorsGroup.onSlideChange(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                stopTimer();
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                if (timer == null) {
                    setupTimer();
                }

                break;
        }
    }

    private void setupTimer() {
        if (slideShowInterval > 0) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ((AppCompatActivity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!mustLoopSlides) {
                                if (viewPager.getCurrentItem() == banners.size() - 1) {
                                    viewPager.setCurrentItem(0, true);
                                } else {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                }
                            } else {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    if (getLayoutDirection() == LAYOUT_DIRECTION_LTR) {
                                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                    } else {
                                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                                    }
                                } else {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                                }
                            }
                        }
                    });
                }
            }, slideShowInterval, slideShowInterval);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    // Setters
    ///////////////////////////////////////////////////////////////////////////

    public void setDefaultIndicator(final int indicator) {
        post(new Runnable() {
            @Override
            public void run() {
                defaultIndicator = indicator;
                slideIndicatorsGroup.changeIndicator(indicator);
                if (mustLoopSlides) {
                    if (viewPager.getCurrentItem() == 0) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(banners.size(), false);
                            }
                        }, 400);
                        if (slideIndicatorsGroup != null) {
                            slideIndicatorsGroup.onSlideChange(banners.size() - 1);
                        }
                    } else if (viewPager.getCurrentItem() == banners.size() + 1) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(1, false);
                            }
                        }, 400);
                        if (slideIndicatorsGroup != null) {
                            slideIndicatorsGroup.onSlideChange(0);
                        }
                    } else {
                        if (slideIndicatorsGroup != null) {
                            slideIndicatorsGroup.onSlideChange(viewPager.getCurrentItem() - 1);
                        }
                    }
                } else {
                    slideIndicatorsGroup.onSlideChange(viewPager.getCurrentItem());
                }
            }
        });

    }

    public void setCustomIndicator(Drawable selectedSlideIndicator, Drawable unSelectedSlideIndicator) {
        this.selectedSlideIndicator = selectedSlideIndicator;
        this.unSelectedSlideIndicator = unSelectedSlideIndicator;
        slideIndicatorsGroup.changeIndicator(selectedSlideIndicator, unSelectedSlideIndicator);
        if (mustLoopSlides) {
            if (viewPager.getCurrentItem() == 0) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(banners.size(), false);
                    }
                }, 400);
                if (slideIndicatorsGroup != null) {
                    slideIndicatorsGroup.onSlideChange(banners.size() - 1);
                }
            } else if (viewPager.getCurrentItem() == banners.size() + 1) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(1, false);
                    }
                }, 400);
                if (slideIndicatorsGroup != null) {
                    slideIndicatorsGroup.onSlideChange(0);
                }
            } else {
                if (slideIndicatorsGroup != null) {
                    slideIndicatorsGroup.onSlideChange(viewPager.getCurrentItem() - 1);
                }
            }
        } else {
            slideIndicatorsGroup.onSlideChange(viewPager.getCurrentItem());
        }
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
        for (Banner banner :
                banners) {
            banner.setOnBannerClickListener(onBannerClickListener);
        }
    }

    public void setCurrentSlide(final int position) {
        post(new Runnable() {
            @Override
            public void run() {
                if (viewPager != null) {
                    viewPager.setCurrentItem(position);
                }
            }
        });
    }

    public void setInterval(int interval) {
        this.slideShowInterval = interval;
        onIntervalChange();
    }

    public void setIndicatorSize(int indicatorSize) {
        this.indicatorSize = indicatorSize;
        onIndicatorSizeChange();
    }

    public void setLoopSlides(boolean loopSlides) {
        this.mustLoopSlides = loopSlides;
    }

    public void setMustAnimateIndicators(boolean mustAnimateIndicators) {
        this.mustAnimateIndicators = mustAnimateIndicators;
        onAnimateIndicatorsChange();
    }

    public void setHideIndicators(boolean hideIndicators) {
        this.hideIndicators = hideIndicators;
        onHideIndicatorsValueChanged();
    }

    // Getters
    ///////////////////////////////////////////////////////////////////////////

    public int getCurrentSlidePosition() {
        if (viewPager == null)
            return -1;
        return viewPager.getCurrentItem();
    }

    // Events
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onIndicatorSizeChange() {
        if (!hideIndicators) {
            if (slideIndicatorsGroup != null) {
                removeView(slideIndicatorsGroup);
            }
            slideIndicatorsGroup = new SlideIndicatorsGroup(getContext(), selectedSlideIndicator, unSelectedSlideIndicator, defaultIndicator, indicatorSize, mustAnimateIndicators);
            addView(slideIndicatorsGroup);
            for (int i = 0; i < banners.size(); i++) {
                slideIndicatorsGroup.onSlideAdd();
            }
        }
    }

    @Override
    public void onSelectedSlideIndicatorChange() {

    }

    @Override
    public void onUnselectedSlideIndicatorChange() {

    }

    @Override
    public void onDefaultIndicatorsChange() {

    }

    @Override
    public void onAnimateIndicatorsChange() {
        if (slideIndicatorsGroup != null) {
            slideIndicatorsGroup.setMustAnimateIndicators(mustAnimateIndicators);
        }
    }

    @Override
    public void onIntervalChange() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        setupTimer();
    }

    @Override
    public void onLoopSlidesChange() {

    }

    @Override
    public void onDefaultBannerChange() {

    }

    @Override
    public void onEmptyViewChange() {

    }

    @Override
    public void onHideIndicatorsValueChanged() {
        if (slideIndicatorsGroup != null) {
            removeView(slideIndicatorsGroup);
        }
        if (!hideIndicators) {
            slideIndicatorsGroup = new SlideIndicatorsGroup(getContext(), selectedSlideIndicator, unSelectedSlideIndicator, defaultIndicator, indicatorSize, mustAnimateIndicators);
            addView(slideIndicatorsGroup);
            for (int i = 0; i < banners.size(); i++) {
                slideIndicatorsGroup.onSlideAdd();
            }
        }
    }

    public void removeAllBanners(){
        this.banners.clear();
        this.slideIndicatorsGroup.removeAllViews();
        this.slideIndicatorsGroup.setSlides(0);
        invalidate();
        requestLayout();
    }
}
