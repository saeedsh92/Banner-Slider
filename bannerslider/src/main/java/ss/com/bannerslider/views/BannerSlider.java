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
import ss.com.bannerslider.views.indicators.IndicatorShape;

/**
 * @author S.Shahini
 * @since 11/23/16
 */

public class BannerSlider extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "BannerSlider";
    private List<Banner> banners = new ArrayList<>();
    private AppCompatActivity hostActivity;
    private ViewPager viewPager;
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
    private BannerAdapter bannerAdapter;
    private int defaultBanner = 0;
    @LayoutRes
    private int emptyView;

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
                Log.e(TAG, "parseCustomAttributes: ");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            setup();
        }

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
                    viewPager = new ViewPager(getContext());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        viewPager.setId(View.generateViewId());
                    } else {
                        int id = Math.abs(new Random().nextInt((5000 - 1000) + 1) + 1000);
                        viewPager.setId(id);
                    }
                    viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    viewPager.addOnPageChangeListener(BannerSlider.this);
                    addView(viewPager);

                    bannerAdapter = new BannerAdapter(hostActivity.getSupportFragmentManager(), mustLoopSlides);
                    viewPager.setAdapter(bannerAdapter);
                    bannerAdapter.setEmptyView(emptyView);

                    slideIndicatorsGroup = new SlideIndicatorsGroup(getContext(), selectedSlideIndicator, unSelectedSlideIndicator, defaultIndicator, indicatorSize, mustAnimateIndicators);
                    addView(slideIndicatorsGroup);

                    setupTimer();
                }
            });
        }

    }

    public void addBanner(final Banner banner) {
        post(new Runnable() {
            @Override
            public void run() {
                banners.add(banner);
                bannerAdapter.addBanner(banner);
                banner.setPosition(banners.size() - 1);
                banner.setOnBannerClickListener(onBannerClickListener);
                slideIndicatorsGroup.onSlideAdd();
                if (banners.size() == 1 && defaultBanner == 0) {
                    slideIndicatorsGroup.onSlideChange(0);
                    if (mustLoopSlides) {
                        viewPager.setCurrentItem(1);
                    }
                }
            }
        });

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

    public int getCurrentSlidePosition() {
        return viewPager.getCurrentItem();
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
        for (Banner banner :
                banners) {
            banner.setOnBannerClickListener(onBannerClickListener);
        }
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
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
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
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                            }
                        }
                    });
                }
            }, slideShowInterval, slideShowInterval);
        }
    }

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
}
