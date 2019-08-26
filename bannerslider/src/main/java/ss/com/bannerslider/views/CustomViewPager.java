package ss.com.bannerslider.views;

import android.content.Context;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Saeed shahini on 12/23/2016.
 */

public class CustomViewPager extends ViewPager {
    private boolean mustWrapContent=true;
    public CustomViewPager(Context context,boolean mustWrapContent) {
        super(context);
        this.mustWrapContent=mustWrapContent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mustWrapContent) {
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
