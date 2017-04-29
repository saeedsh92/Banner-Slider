package ss.com.bannerslider.views;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.LayoutDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.views.fragments.BannerFragment;
import ss.com.bannerslider.views.fragments.EmptyViewFragment;

/**
 * @author S.Shahini
 * @since 11/23/16
 */

public class BannerAdapter extends FragmentStatePagerAdapter {
    private List<Banner> banners = new ArrayList<>();

    private boolean isLooping = false;
    @LayoutRes
    private int emptyView;
    private boolean isRtl;

    public BannerAdapter(FragmentManager fm, boolean isLooping) {
        super(fm);
        this.isLooping = isLooping;
        isRtl=false;
    }

    public BannerAdapter(FragmentManager fm, boolean isLooping,int layoutDirection) {
        super(fm);
        this.isLooping = isLooping;
        if (layoutDirection==LayoutDirection.RTL){
            isRtl=true;
        }
    }

    public void addBanner(Banner banner) {
        if (!banners.isEmpty() && isRtl){
            this.banners.add(0,banner);
        }else {
            this.banners.add(banner);
        }
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (banners.isEmpty() && emptyView > 0) {
            return EmptyViewFragment.newInstance(emptyView);
        }
        if (isLooping) {
            if (position == 0) {
                    return BannerFragment.newInstance(banners.get(banners.size() - 1));
            } else if (position == banners.size() + 1) {
                    return BannerFragment.newInstance(banners.get(0));
            } else {
                return BannerFragment.newInstance(banners.get(position - 1));
            }
        } else {
            return BannerFragment.newInstance(banners.get(position));
        }
    }

    @Override
    public int getCount() {
        if (banners.isEmpty()) {
            if (emptyView > 0) {
                return 1;
            } else {
                return 0;
            }
        }
        if (isLooping) {
            return banners.size() + 2;
        } else {
            return banners.size();
        }
    }

    public void setEmptyView(int emptyView) {
        this.emptyView = emptyView;
        notifyDataSetChanged();
    }
}
