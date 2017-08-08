package ss.com.bannerslider.banners;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * @author S.Shahini
 * @since 11/23/16
 */

public class RemoteBanner extends Banner implements Parcelable {
    private String url;
    private int placeHolder = 0;
    private Drawable errorDrawable;

    public RemoteBanner(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public RemoteBanner setPlaceHolder(@DrawableRes int placeHolder) {
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
        placeHolder = in.readInt();
        errorDrawable = (Drawable) in.readValue(Drawable.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        if (placeHolder!=0) {
            dest.writeInt(placeHolder);
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