# Deprecated
### I created this project in 2016. many things have changed and there are many other new great alternatives for creating sliders in android. so I decided to deprecate banner-slider.

# Banner-Slider
Banner slider is an easy to use library for making beautiful sliders in your android app.

<img src="https://github.com/saeedsh92/Banner-Slider/blob/master/Screenshot_1481531647.png?raw=true" width="350">

## How to download
### Gradle
add this line to your module build.gradle dependecies block:

    compile 'com.ss.bannerslider:bannerslider:2.0.0'

### Maven

    <dependency>
      <groupId>com.ss.bannerslider</groupId>
      <artifactId>bannerslider</artifactId>
      <version>2.0.0</version>
      <type>pom</type>
    </dependency>

## How use this library
### XML

```xml
<ss.com.bannerslider.Slider
   android:id="@+id/banner_slider1"
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   />
```

### Java
### Step 1: extend SliderAdapter
in first step you must create an adapter to adapt your data model with slides. example implemented adapter with 3 slides:
```java
public class MainSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg");
                break;
            case 1:
                viewHolder.bindImageSlide("https://assets.materialup.com/uploads/20ded50d-cc85-4e72-9ce3-452671cf7a6d/preview.jpg");
                break;
            case 2:
                viewHolder.bindImageSlide("https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png");
                break;
        }
    }
}
```
### Step 2: specify your image loading service
you must specify image loading service for loading images(for better flexibility and prevent adding unnecessary libraries).
for example if you work with picasso for loading images in your project, you must implement ImageLoadingService interface like below:
```java
public class PicassoImageLoadingService implements ImageLoadingService {
    public Context context;

    public PicassoImageLoadingService(Context context) {
        this.context = context;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        Picasso.with(context).load(url).into(imageView);
    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        Picasso.with(context).load(resource).into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        Picasso.with(context).load(url).placeholder(placeHolder).error(errorDrawable).into(imageView);
    }
}
```
### Step 3: initialize slider in your Application class
```java
Slider.init(YOUR IMAGE LOADING SERVICE);
```

### Step 4: set your adapter on slider
```java
slider = findViewById(R.id.banner_slider1);
        slider.setAdapter(new MainSliderAdapter());
```
## You want more customization?
#### Changing slides automatically in specifed periods
interval attribute get miliseconds.
```xml
  app:slider_interval="5000"
```


#### Loop slides
```xml
  app:slider_loopSlides="true"
```

#### Choose default banner to show up first
```java
slider.setSelectedSlide(2);
```

you must pass banner position to it:
```xml
  app:slider_defaultBanner="1"
```


#### Enable/disable indicators animations
in default, animations are enabled
```xml
  app:slider_animateIndicators="true"
```


#### Use custom Indicators
if you also set default indicators, then this attributes will ignored
```xml
  app:slider_selectedSlideIndicator="@drawable/selected_slide_indicator"
  app:slider_unselectedSlideIndicator="@drawable/unselected_slide_indicator"
```


#### How set empty view, when banners not received from server yet?
```xml
  app:slider_emptyView="@layout/layout_empty_view"
```


#### Change indicator sizes
```xml
  app:slider_indicatorSize="12dp"
```


#### Set OnBannerClickListener
```java
slider.setOnSlideClickListener(new OnSlideClickListener() {
            @Override
            public void onSlideClick(int position) {
                //Do what you want
            }
        });
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

