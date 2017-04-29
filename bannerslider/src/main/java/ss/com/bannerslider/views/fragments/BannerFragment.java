package ss.com.bannerslider.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.AdjustableImageView;

/**
 * @author S.Shahini
 * @since 11/23/16
 */

public class BannerFragment extends Fragment {
    private Banner banner;

    public BannerFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        banner = getArguments().getParcelable("banner");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (banner != null) {

            final AdjustableImageView imageView = new AdjustableImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(banner.getScaleType());
            if (banner instanceof DrawableBanner) {
                DrawableBanner drawableBanner = (DrawableBanner) banner;
                Picasso.with(getContext()).load(drawableBanner.getDrawable()).into(imageView);
            } else {
                final RemoteBanner remoteBanner = (RemoteBanner) banner;
                if (remoteBanner.getErrorDrawable() == null && remoteBanner.getPlaceHolder() == null) {
                    Picasso.with(getActivity()).load(remoteBanner.getUrl()).into(imageView);
                } else {
                    if (remoteBanner.getPlaceHolder() != null && remoteBanner.getErrorDrawable() != null) {
                        Picasso.with(getActivity()).load(remoteBanner.getUrl()).placeholder(remoteBanner.getPlaceHolder()).error(remoteBanner.getErrorDrawable()).into(imageView);
                    } else if (remoteBanner.getErrorDrawable() != null) {
                        Picasso.with(getActivity()).load(remoteBanner.getUrl()).error(remoteBanner.getErrorDrawable()).into(imageView);
                    } else if (remoteBanner.getPlaceHolder() != null) {
                        Picasso.with(getActivity()).load(remoteBanner.getUrl()).placeholder(remoteBanner.getPlaceHolder()).into(imageView);
                    }
                }
            }
            imageView.setOnTouchListener(banner.getOnTouchListener());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnBannerClickListener onBannerClickListener = banner.getOnBannerClickListener();
                    if (onBannerClickListener != null) {
                        onBannerClickListener.onClick(banner.getPosition());
                    }
                }
            });

            return imageView;
        } else {
            throw new RuntimeException("banner cannot be null");
        }
    }

    public static BannerFragment newInstance(Banner banner) {
        Bundle args = new Bundle();
        args.putParcelable("banner", banner);
        BannerFragment fragment = new BannerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
