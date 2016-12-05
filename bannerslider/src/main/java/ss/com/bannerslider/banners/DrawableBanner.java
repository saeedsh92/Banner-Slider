package ss.com.bannerslider.banners;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * @author S.Shahini
 * @since 11/23/16
 */

public class DrawableBanner extends Banner implements Parcelable {
    private int drawable;

    public DrawableBanner(@DrawableRes int drawable) {
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }

    protected DrawableBanner(Parcel in) {
        drawable = (int) in.readValue(Drawable.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(drawable);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DrawableBanner> CREATOR = new Parcelable.Creator<DrawableBanner>() {
        @Override
        public DrawableBanner createFromParcel(Parcel in) {
            return new DrawableBanner(in);
        }

        @Override
        public DrawableBanner[] newArray(int size) {
            return new DrawableBanner[size];
        }
    };


}