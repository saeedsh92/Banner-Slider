package ss.com.bannerslider.banners;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class BitmapBanner extends Banner implements Parcelable {
    private Bitmap bitmap;

    public BitmapBanner(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    protected BitmapBanner(Parcel in) {
        bitmap = (Bitmap) in.readValue(Drawable.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bitmap);
    }

    @SuppressWarnings("unused")
    public static final Creator<BitmapBanner> CREATOR = new Creator<BitmapBanner>() {
        @Override
        public BitmapBanner createFromParcel(Parcel in) {
            return new BitmapBanner(in);
        }

        @Override
        public BitmapBanner[] newArray(int size) {
            return new BitmapBanner[size];
        }
    };


}