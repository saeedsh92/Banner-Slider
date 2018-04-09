package ss.com.bannerslider.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ss.com.bannerslider.SlideType;
import ss.com.bannerslider.event.OnSlideClickListener;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;


/**
 * @author S.Shahini
 * @since 12/16/17
 */
public class SliderRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ViewGroup.LayoutParams imageViewLayoutParams;
    private OnSlideClickListener onSlideClickListener;
    private SliderAdapter sliderAdapter;
    private boolean loop;
    private View.OnTouchListener itemOnTouchListener;
    private PositionController positionController;

    public SliderRecyclerViewAdapter(SliderAdapter iSliderAdapter, boolean loop, ViewGroup.LayoutParams imageViewLayoutParams, View.OnTouchListener itemOnTouchListener, PositionController positionController) {
        this.sliderAdapter = iSliderAdapter;
        this.imageViewLayoutParams = imageViewLayoutParams;
        this.loop = loop;
        this.itemOnTouchListener = itemOnTouchListener;
        this.positionController = positionController;
    }

    public void setOnSlideClickListener(OnSlideClickListener onSlideClickListener) {
        this.onSlideClickListener = onSlideClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SlideType.IMAGE.getValue()) {
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(imageViewLayoutParams);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new ImageSlideViewHolder(imageView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (!loop) {
            sliderAdapter.onBindImageSlide(position, (ImageSlideViewHolder) holder);
        } else {
            if (position == 0) {
                sliderAdapter.onBindImageSlide(positionController.getLastUserSlidePosition(), (ImageSlideViewHolder) holder);
            } else if (position == getItemCount() - 1) {
                sliderAdapter.onBindImageSlide(positionController.getFirstUserSlidePosition(), (ImageSlideViewHolder) holder);
            } else {
                sliderAdapter.onBindImageSlide(position - 1, (ImageSlideViewHolder) holder);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSlideClickListener != null)
                    onSlideClickListener.onSlideClick(positionController.getUserSlidePosition(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnTouchListener(itemOnTouchListener);
    }

    @Override
    public int getItemCount() {
        return sliderAdapter.getItemCount() + (loop ? 2 : 0);
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }


}
