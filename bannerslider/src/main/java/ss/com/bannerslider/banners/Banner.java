package ss.com.bannerslider.banners;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import ss.com.bannerslider.events.OnBannerClickListener;

/**
 * @author S.Shahini
 * @since 11/23/16
 */

public class Banner implements Parcelable {
    private int id;
    private int position;
    private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    private OnBannerClickListener onBannerClickListener;
    private View.OnTouchListener onTouchListener;

    public Banner() {

    }

    public int getId() {
        return id;
    }

    public Banner setId(int id) {
        this.id = id;
        return this;
    }

    public int getPosition() {
        return position;
    }

    public Banner setPosition(int position) {
        this.position = position;
        return this;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public Banner setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    public Banner(Parcel in) {
        id = in.readInt();
        position = in.readInt();
        scaleType = (ImageView.ScaleType) in.readValue(ImageView.ScaleType.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(position);
        dest.writeValue(scaleType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Banner> CREATOR = new Parcelable.Creator<Banner>() {
        @Override
        public Banner createFromParcel(Parcel in) {
            return new Banner(in);
        }

        @Override
        public Banner[] newArray(int size) {
            return new Banner[size];
        }
    };

    public Banner setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
        return this;
    }

    public OnBannerClickListener getOnBannerClickListener() {
        return this.onBannerClickListener;
    }

    public View.OnTouchListener getOnTouchListener() {
        return onTouchListener;
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }
}