# Banner-Slider
Banner slider is an easy to use library for making beautiful sliders in your android app.

<img src="https://github.com/saeedsh92/Banner-Slider/blob/master/Screenshot_1481531647.png?raw=true" width="350">


## How to download
### Gradle
add this line to your module build.gradle dependecies block:

    compile 'com.ss.bannerslider:bannerslider:1.6.1'
    
### Maven

    <dependency>
      <groupId>com.ss.bannerslider</groupId>
      <artifactId>bannerslider</artifactId>
      <version>1.6.1</version>
      <type>pom</type>
    </dependency>

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
  //add banner using image url
  bannerSlider.addBanner(new RemoteBanner("Put banner image url here ..."));
  //add banner using resource drawable
  bannerSlider.addBanner(new DrawableBanner(R.drawable.yourDrawable));
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

