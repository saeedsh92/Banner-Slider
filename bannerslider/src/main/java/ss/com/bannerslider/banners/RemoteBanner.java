package ss.com.bannerslider.banners;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author S.Shahini
 * @since 11/23/16
 */

public class RemoteBanner extends Banner implements Parcelable {
    private String url;
    private Drawable placeHolder;
    private Drawable errorDrawable;

    public RemoteBanner(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Drawable getPlaceHolder() {
        return placeHolder;
    }

    public RemoteBanner setPlaceHolder(Drawable placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public Drawable getErrorDrawable() {
        return errorDrawable;
    }

    public RemoteBanner setErrorDrawable(Drawable errorDrawable) {
        this.errorDrawable = errorDrawable;
        return this;
    }

    protected RemoteBanner(Parcel in) {
        url = in.readString();
        placeHolder = (Drawable) in.readValue(Drawable.class.getClassLoader());
        errorDrawable = (Drawable) in.readValue(Drawable.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        if (placeHolder!=null) {
            dest.writeParcelable(((BitmapDrawable) placeHolder).getBitmap(), flags);
        }
        if (errorDrawable!=null) {
            dest.writeParcelable(((BitmapDrawable) errorDrawable).getBitmap(), flags);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RemoteBanner> CREATOR = new Parcelable.Creator<RemoteBanner>() {
        @Override
        public RemoteBanner createFromParcel(Parcel in) {
            return new RemoteBanner(in);
        }

        @Override
        public RemoteBanner[] newArray(int size) {
            return new RemoteBanner[size];
        }
    };
}