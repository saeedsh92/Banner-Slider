package ss.com.bannerslider.indicators;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ss.com.bannerslider.R;

/**
 * @author S.Shahini
 * @since 11/26/16
 */
public abstract class IndicatorShape extends ImageView {
    private boolean isChecked = false;
    private int indicatorSize;
    private boolean mustAnimateChange;

    public static final int CIRCLE = 0;
    public static final int SQUARE = 1;
    public static final int ROUND_SQUARE = 2;
    public static final int DASH = 3;

    private static final int ANIMATION_DURATION = 150;

    public IndicatorShape(Context context, int indicatorSize, boolean mustAnimateChange) {
        super(context);
        this.indicatorSize = indicatorSize;
        this.mustAnimateChange = mustAnimateChange;
        setup();
    }


    private void setup() {
        if (this.indicatorSize == 0) {
            indicatorSize = (int) getResources().getDimension(R.dimen.default_indicator_size);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);
        int margin = getResources().getDimensionPixelSize(R.dimen.default_indicator_margins);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.setMargins(margin, 0, margin, 0);
        setLayoutParams(layoutParams);
    }

    public void onCheckedChange(boolean isChecked) {
        if (this.isChecked != isChecked) {
            if (mustAnimateChange) {
                if (isChecked) {
                    scale();
                } else {
                    descale();
                }
            }else {
                if (isChecked) {
                    scale(0);
                } else {
                    descale(0);
                }
            }
            this.isChecked = isChecked;
        }
    }

    private void scale() {
        scale(ANIMATION_DURATION);
    }

    private void scale(int duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.1f, 1f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        startAnimation(scaleAnimation);
    }


    private void descale() {
        descale(ANIMATION_DURATION);
    }

    private void descale(int duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.1f, 1f, 1.1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        startAnimation(scaleAnimation);
    }

    public void setMustAnimateChange(boolean mustAnimateChange){
        this.mustAnimateChange=mustAnimateChange;
    }


}
