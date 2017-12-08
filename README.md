# Banner-Slider
This library is forked from https://github.com/saeedsh92/Banner-Slider  
Banner slider is an easy to use library for making beautiful sliders in your android app.

<img src="https://github.com/saeedsh92/Banner-Slider/blob/master/Screenshot_1481531647.png?raw=true" width="350">


## How to download
### Gradle
add jitpack in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

add this line to your module build.gradle dependecies block:

    compile 'com.github.interchen:Banner-Slider:1.9.0'


## How use this library
### XML

```xml
<ss.com.bannerslider.views.BannerSlider
   android:id="@+id/banner_slider1"
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   />
```

### Java
```java
    BannerSlider bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);
    List<Banner> banners=new ArrayList<>();
    //add banner using image url
    banners.add(new RemoteBanner("Put banner image url here ...", this));
    //add banner using resource drawable
    banners.add(new DrawableBanner(R.drawable.yourDrawable));
    bannerSlider.setBanners(banners);
```

implement BannerListener
```java
@Override
public void onLoadBanner(Context context, Banner banner, ImageView imageView) {
    RemoteBanner remoteBanner = (RemoteBanner) banner;
    Picasso.with(context).load(remoteBanner.getUrl()).into(imageView);
}
```

## You want more customization?
### Xml
#### Changing slides automatically in specifed periods
interval attribute get miliseconds.
```xml
  app:interval="5000"
```


#### Loop slides
```xml
  app:loopSlides="true"
```


#### Use default page indicators
currently banner slider has 4 styles.
```xml
  app:defaultIndicators="circle"
```


#### Choose default banner to show up first
you must pass banner position to it:
```xml
  app:defaultBanner="1"
```


#### Enable/disable indicators animations
in default, animations are enabled
```xml
  app:animateIndicators="true"
```


#### Use custom Indicators
if you also set default indicators, then this attributes will ignored
```xml
  app:selected_slideIndicator="@drawable/selected_slide_indicator"
  app:unselected_slideIndicator="@drawable/unselected_slide_indicator"
```


#### How set empty view, when banners not received from server yet?
```xml
  app:emptyView="@layout/layout_empty_view"
```


#### Change indicator sizes
```xml
  app:indicatorSize="12dp"
```


#### Set OnBannerClickListener
```java
bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
  @Override
  public void onClick(int position) {
      Toast.makeText(MainActivity.this, "Banner with position " + String.valueOf(position) + " clicked!", Toast.LENGTH_SHORT).show();
    }
  });
```
#### Remove all banners
```java
    bannerSlider.removeAllBanners();
```
## Licence
Copyright 2016 Saeed Shahini

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Author
Saeed shahini

email: saeedshahiniit@gmail.com

github: https://github.com/saeedsh92

