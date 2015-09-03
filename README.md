# HashtagView
Fully customizable widget for representing data like hashtags collection and similiar.

![sample1](https://github.com/greenfrvr/hashtag-view/blob/master/screenshots/screen1.png)
![sample2](https://github.com/greenfrvr/hashtag-view/blob/master/screenshots/screen2.png)
![sample3](https://github.com/greenfrvr/hashtag-view/blob/master/screenshots/screen3.png)


## Demo
Downlaod latest demo app from Play Market:

<a href="https://play.google.com/store/apps/details?id=com.greenfrvr.hashtagview.sample">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>

## Gradle Dependency (jCenter)
Easily reference the library in your Android projects using this dependency in your module's build.gradle file:

```
dependencies {
    compile 'com.github.greenfrvr:hashtag-view:1.0.0@aar'
}
```

In case of any issues with jCenter (library can't be resolved)

Add repository to your app's build.gradle file:
```
repositories {
    maven {
        url 'https://dl.bintray.com/greenfrvr/maven/'
    }
}
```
This will reference Bintray's Maven repository that contains hashtags widget directly, rather than going through jCenter first.
