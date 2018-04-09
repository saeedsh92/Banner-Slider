package ss.com.bannerslider;

public enum SlideType {
    //Custom,Video And Gif Slides will add in future
    IMAGE(0);
    private final int value;

    SlideType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}