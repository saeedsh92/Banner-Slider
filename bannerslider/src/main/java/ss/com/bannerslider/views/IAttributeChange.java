package ss.com.bannerslider.views;

/**
 * @author S.Shahini
 * @since 12/11/16
 */

public interface IAttributeChange {
    void onIndicatorSizeChange();
    void onSelectedSlideIndicatorChange();
    void onUnselectedSlideIndicatorChange();
    void onDefaultIndicatorsChange();
    void onAnimateIndicatorsChange();
    void onIntervalChange();
    void onLoopSlidesChange();
    void onDefaultBannerChange();
    void onEmptyViewChange();
    void onHideIndicatorsValueChanged();
}
