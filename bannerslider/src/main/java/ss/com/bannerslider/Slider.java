package ss.com.bannerslider;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

import ss.com.bannerslider.adapters.PositionController;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.adapters.SliderRecyclerViewAdapter;
import ss.com.bannerslider.event.OnSlideChangeListener;
import ss.com.bannerslider.event.OnSlideClickListener;
import ss.com.bannerslider.indicators.IndicatorShape;

/**
 * @author S.Shahini
 * @since 12/16/17
 */

public class Slider extends FrameLayout {
    private static final String TAG = "Slider";

    public OnSlideChangeListener onSlideChangeListener;
    public OnSlideClickListener onSlideClickListener;
    public RecyclerView recyclerView;
    public SliderRecyclerViewAdapter adapter;
    public SlideIndicatorsGroup slideIndicatorsGroup;
    public int pendingPosition = RecyclerView.NO_POSITION;
    public SliderAdapter sliderAdapter;
    public Config config;
    public int selectedSlidePosition = 0;
    public Timer timer;
    public PositionController positionController;
    public static ImageLoadingService imageLoadingService;
    private View emptyView;

    public Slider(@NonNull Context context) {
        super(context);
        setupViews(null);
    }

    public Slider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupViews(attrs);
    }

    public Slider(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews(attrs);
    }

    private void setupViews(AttributeSet attributeSet) {

        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.Slider);
            try {
                config = new Config.Builder(getContext())
                        .animateIndicators(typedArray.getBoolean(R.styleable.Slider_slider_animateIndicators, true))
                        .emptyView(typedArray.getResourceId(R.styleable.Slider_slider_emptyView, Config.NOT_SELECTED))
                        .indicatorSize(typedArray.getDimensionPixelSize(R.styleable.Slider_slider_indicatorSize, 0))
                        .loopSlides(typedArray.getBoolean(R.styleable.Slider_slider_loopSlides, false))
                        .slideChangeInterval(typedArray.getInteger(R.styleable.Slider_slider_interval, 0))
                        .selectedSlideIndicator(typedArray.getDrawable(R.styleable.Slider_slider_selectedSlideIndicator))
                        .unselectedSlideIndicator(typedArray.getDrawable(R.styleable.Slider_slider_unselectedSlideIndicator))
                        .hideIndicators(typedArray.getBoolean(R.styleable.Slider_slider_hideIndicators, false))
                        .build();
            } catch (Exception e) {
                Log.e("Slider", "setupViews: ".toString());
            } finally {
                typedArray.recycle();
            }
        } else {
            config = new Config.Builder(getContext()).build();
        }

        setupRv();
        setupSlideIndicator();
    }

    private void setupRv() {
        recyclerView = new RecyclerView(getContext());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!config.loopSlides) return;

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (selectedSlidePosition == 0) {
                        recyclerView.scrollToPosition(adapter.getItemCount() - 2);
                        onImageSlideChange(adapter.getItemCount() - 2);
                    } else if (selectedSlidePosition == adapter.getItemCount() - 1) {
                        recyclerView.scrollToPosition(1);
                        onImageSlideChange(1);
                    }
                }
            }
        });
        if (config.emptyView != Config.NOT_SELECTED) {
            emptyView = LayoutInflater.from(getContext()).inflate(config.emptyView, this, false);
            addView(emptyView);
        }
    }

    private void setupSlideIndicator() {
        if (!config.hideIndicators) {
            slideIndicatorsGroup = new SlideIndicatorsGroup(getContext(),
                    config.selectedSlideIndicator,
                    config.unselectedSlideIndicator,
                    0,
                    config.indicatorSize,
                    config.animateIndicators);
        }
    }

    public void onImageSlideChange(int position) {
        Log.d(TAG, "onImageSlideChange() called with: position = [" + position + "]");
        selectedSlidePosition = position;
        int userSlidePosition = positionController.getUserSlidePosition(position);
        if (slideIndicatorsGroup != null)
            slideIndicatorsGroup.onSlideChange(userSlidePosition);
        if (onSlideChangeListener != null) {
            onSlideChangeListener.onSlideChange(userSlidePosition);
        }
    }

    public void setSelectedSlide(int position, boolean animate) {
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            if (animate) {
                recyclerView.smoothScrollToPosition(position);
            } else {
                recyclerView.scrollToPosition(position);
            }
            onImageSlideChange(position);
        } else {
            pendingPosition = position;
        }
    }

    public void setSelectedSlide(int position) {
        setSelectedSlide(positionController.getRealSlidePosition(position), true);
    }

    private void onAdapterAttached() {
        if (pendingPosition != RecyclerView.NO_POSITION) {
            recyclerView.smoothScrollToPosition(pendingPosition);
            onImageSlideChange(pendingPosition);
            pendingPosition = RecyclerView.NO_POSITION;

        }
    }

    public void setSlideChangeListener(OnSlideChangeListener onSlideChangeListener) {
        this.onSlideChangeListener = onSlideChangeListener;
    }

    public void setOnSlideClickListener(OnSlideClickListener onSlideClickListener) {
        this.onSlideClickListener = onSlideClickListener;
        if (adapter != null)
            adapter.setOnSlideClickListener(onSlideClickListener);
    }

    public SliderAdapter getAdapter() {
        return this.sliderAdapter;
    }

    public void setAdapter(SliderAdapter sliderAdapter) {
        if (sliderAdapter != null && recyclerView != null) {
            this.sliderAdapter = sliderAdapter;
            if (indexOfChild(recyclerView) == -1) {
                if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                } else {
                    recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }

                addView(recyclerView);
            }

            recyclerView.setNestedScrollingEnabled(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            positionController = new PositionController(sliderAdapter, config.loopSlides);
            adapter = new SliderRecyclerViewAdapter(sliderAdapter, sliderAdapter.getItemCount() > 1 && config.loopSlides, recyclerView.getLayoutParams(), new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        stopTimer();
                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                        startTimer();
                    }
                    return false;
                }
            }, positionController);

            recyclerView.setAdapter(adapter);
            positionController.setRecyclerViewAdapter(adapter);

            //Show default selected slide
            selectedSlidePosition = config.loopSlides ? 1 : 0;
            recyclerView.scrollToPosition(selectedSlidePosition);
            onImageSlideChange(selectedSlidePosition);
            pendingPosition = RecyclerView.NO_POSITION;
            onAdapterAttached();

            SsSnapHelper snapHelper = new SsSnapHelper(new SsSnapHelper.OnSelectedItemChange() {
                @Override
                public void onSelectedItemChange(int position) {
                    onImageSlideChange(position);
                }
            });
            recyclerView.setOnFlingListener(null);
            snapHelper.attachToRecyclerView(recyclerView);
            if (slideIndicatorsGroup != null && sliderAdapter.getItemCount() > 1) {
                if (indexOfChild(slideIndicatorsGroup) == -1) {
                    addView(slideIndicatorsGroup);
                }
                slideIndicatorsGroup.setSlides(sliderAdapter.getItemCount());
                slideIndicatorsGroup.onSlideChange(0);
            }
            if (emptyView != null) {
                emptyView.setVisibility(GONE);
            }
        }
    }

    public Config getConfig() {
        return this.config;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    private void startTimer() {
        if (config.slideChangeInterval > 0) {
            stopTimer();
            timer = new Timer();
            timer.schedule(new SliderTimerTask(), config.slideChangeInterval, config.slideChangeInterval);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    public static void init(ImageLoadingService imageLoadingService) {
        Slider.imageLoadingService = imageLoadingService;
    }

    public static ImageLoadingService getImageLoadingService() {
        if (imageLoadingService == null) {
            throw new IllegalStateException("ImageLoadingService is null, you should call init method first");
        }
        return imageLoadingService;
    }

    private class SliderTimerTask extends TimerTask {
        @Override
        public void run() {
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int nextSlidePosition = positionController.getNextSlide(selectedSlidePosition);
                        recyclerView.smoothScrollToPosition(nextSlidePosition);
                        onImageSlideChange(nextSlidePosition);

                    }
                });
            }
        }
    }

    public void setInterval(int interval) {
        config.slideChangeInterval = interval;
        stopTimer();
        startTimer();
    }

    public void setLoopSlides(boolean loopSlides) {
        config.loopSlides = loopSlides;
        adapter.setLoop(loopSlides);
        positionController.setLoop(loopSlides);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(loopSlides ? 1 : 0);
        onImageSlideChange(loopSlides ? 1 : 0);
    }

    public void setAnimateIndicators(boolean shouldAnimate) {
        config.animateIndicators = shouldAnimate;
        if (slideIndicatorsGroup != null)
            slideIndicatorsGroup.setMustAnimateIndicators(shouldAnimate);
    }

    public void hideIndicators() {
        config.hideIndicators = true;
        if (slideIndicatorsGroup != null)
            slideIndicatorsGroup.setVisibility(GONE);
    }

    public void showIndicators() {
        config.hideIndicators = false;
        if (slideIndicatorsGroup != null)
            slideIndicatorsGroup.setVisibility(VISIBLE);
    }

    public void setIndicatorSize(int indicatorSize) {
        config.indicatorSize = indicatorSize;
        refreshIndicators();
    }

    public void setSelectedSlideIndicator(Drawable selectedSlideIndicator) {
        config.selectedSlideIndicator = selectedSlideIndicator;
        refreshIndicators();
    }

    public void setUnSelectedSlideIndicator(Drawable unSelectedSlideIndicator) {
        config.unselectedSlideIndicator = unSelectedSlideIndicator;
        refreshIndicators();
    }

    private void refreshIndicators() {
        if (!config.hideIndicators && sliderAdapter != null) {
            if (slideIndicatorsGroup != null) {
                removeView(slideIndicatorsGroup);
            }
            slideIndicatorsGroup = new SlideIndicatorsGroup(getContext(), config.selectedSlideIndicator, config.unselectedSlideIndicator, 0, config.indicatorSize, config.animateIndicators);
            addView(slideIndicatorsGroup);
            for (int i = 0; i < sliderAdapter.getItemCount(); i++) {
                slideIndicatorsGroup.onSlideAdd();
            }
        }
    }

    public void setIndicatorStyle(int indicatorStyle) {
        switch (indicatorStyle) {
            case IndicatorShape.CIRCLE:
                config.selectedSlideIndicator = ContextCompat.getDrawable(getContext(), R.drawable.indicator_circle_selected);
                config.unselectedSlideIndicator = ContextCompat.getDrawable(getContext(), R.drawable.indicator_circle_unselected);
                break;
            case IndicatorShape.SQUARE:
                config.selectedSlideIndicator = ContextCompat.getDrawable(getContext(), R.drawable.indicator_square_selected);
                config.unselectedSlideIndicator = ContextCompat.getDrawable(getContext(), R.drawable.indicator_square_unselected);
                break;
            case IndicatorShape.DASH:
                config.selectedSlideIndicator = ContextCompat.getDrawable(getContext(), R.drawable.indicator_dash_selected);
                config.unselectedSlideIndicator = ContextCompat.getDrawable(getContext(), R.drawable.indicator_dash_unselected);
                break;
            case IndicatorShape.ROUND_SQUARE:
                config.selectedSlideIndicator = ContextCompat.getDrawable(getContext(), R.drawable.indicator_round_square_selected);
                config.unselectedSlideIndicator = ContextCompat.getDrawable(getContext(), R.drawable.indicator_round_square_unselected);
                break;
        }

        refreshIndicators();
    }
}
