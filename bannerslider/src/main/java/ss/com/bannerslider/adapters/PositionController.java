package ss.com.bannerslider.adapters;

import android.util.Log;

/**
 * @author S.Shahini
 * @since 4/7/18
 */

public class PositionController {
    private static final String TAG = "PositionController";
    private SliderRecyclerViewAdapter recyclerViewAdapter;
    private SliderAdapter sliderAdapter;
    private boolean loop;

    public PositionController(SliderAdapter sliderAdapter, boolean loop) {
        this.sliderAdapter = sliderAdapter;
        this.loop = loop;
    }

    public int getUserSlidePosition(int position) {
        if (loop) {
            if (position == 0) {
                return recyclerViewAdapter.getItemCount() - 3;
            } else if (position == recyclerViewAdapter.getItemCount() - 1) {
                return 0;
            } else {
                return position - 1;
            }
        } else {
            return position;
        }
    }

    public int getRealSlidePosition(int position) {
        if (!loop) {
            return position;
        } else {
            if (position >= 0 && position < sliderAdapter.getItemCount()) {
                return position + 1;
            } else {
                Log.e(TAG, "setSelectedSlide: Invalid Item Position");
                return 1;
            }
        }
    }

    public int getLastUserSlidePosition() {
        return sliderAdapter.getItemCount() - 1;
    }

    public int getFirstUserSlidePosition() {
        return 0;
    }

    public void setRecyclerViewAdapter(SliderRecyclerViewAdapter recyclerViewAdapter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    public int getNextSlide(int currentPosition) {
        if (currentPosition < recyclerViewAdapter.getItemCount() - 1) {
            return currentPosition + 1;
        } else {
            return loop ? 1 : 0;
        }
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}
