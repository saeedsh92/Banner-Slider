package ss.com.bannerslider.indicators;

import android.content.Context;
import android.os.Build;
import androidx.core.content.res.ResourcesCompat;

import ss.com.bannerslider.R;


/**
 * @author S.Shahini
 * @since 11/27/16
 */

public class CircleIndicator extends IndicatorShape {

    public CircleIndicator(Context context, int indicatorSize, boolean mustAnimateChanges) {
        super(context, indicatorSize, mustAnimateChanges);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indicator_circle_unselected, null));
        } else {
            setBackgroundDrawable(getResources().getDrawable(R.drawable.indicator_circle_unselected));
        }
    }

    @Override
    public void onCheckedChange(boolean isChecked) {
        super.onCheckedChange(isChecked);
        if (isChecked) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indicator_circle_selected, null));
            } else {
                setBackgroundDrawable(getResources().getDrawable(R.drawable.indicator_circle_selected));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indicator_circle_unselected, null));
            } else {
                setBackgroundDrawable(getResources().getDrawable(R.drawable.indicator_circle_unselected));
            }
        }
    }
}
