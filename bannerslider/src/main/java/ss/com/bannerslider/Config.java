package ss.com.bannerslider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * @author S.Shahini
 * @since 3/5/18
 */

public class Config {
    public static final int NOT_SELECTED = -1;
    protected boolean hideIndicators = false;
    protected boolean loopSlides = true;
    protected int indicatorSize = NOT_SELECTED;
    protected Drawable selectedSlideIndicator;
    protected Drawable unselectedSlideIndicator;
    protected boolean animateIndicators = true;
    protected int slideChangeInterval = 0;
    protected int emptyView = NOT_SELECTED;

    private Config() {

    }

    public static class Builder {
        private Config config = new Config();
        private Context context;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder hideIndicators(boolean hideIndicators) {
            config.hideIndicators = hideIndicators;
            return this;
        }

        public Builder loopSlides(boolean loopSlides) {
            config.loopSlides = loopSlides;
            return this;
        }

        public Builder indicatorSize(int indicatorSize) {
            config.indicatorSize = indicatorSize;
            return this;
        }

        public Builder selectedSlideIndicator(Drawable selectedSlideIndicator) {
            config.selectedSlideIndicator = selectedSlideIndicator;
            return this;
        }

        public Builder unselectedSlideIndicator(Drawable unselectedSlideIndicator) {
            config.unselectedSlideIndicator = unselectedSlideIndicator;
            return this;
        }

        public Builder animateIndicators(boolean animateIndicators) {
            config.animateIndicators = animateIndicators;
            return this;
        }

        public Builder slideChangeInterval(int slideChangeInterval) {
            config.slideChangeInterval = slideChangeInterval;
            return this;
        }

        public Builder emptyView(int emptyView) {
            config.emptyView = emptyView;
            return this;
        }


        public Config build() {
            if (config.selectedSlideIndicator == null) {
                config.selectedSlideIndicator = ContextCompat.getDrawable(context, R.drawable.indicator_circle_selected);
            }

            if (config.unselectedSlideIndicator == null) {
                config.unselectedSlideIndicator = ContextCompat.getDrawable(context, R.drawable.indicator_circle_unselected);
            }

            if (config.indicatorSize == NOT_SELECTED) {
                config.indicatorSize = context.getResources().getDimensionPixelSize(R.dimen.default_indicator_size);
            }

            return config;
        }
    }


}
