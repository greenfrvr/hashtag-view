# HashtagView

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-HashtagView-green.svg?style=flat)](https://android-arsenal.com/details/1/2447)
[![Android Gems](http://www.android-gems.com/badge/greenfrvr/hashtag-view.svg?branch=master)](http://www.android-gems.com/lib/greenfrvr/hashtag-view)

Fully customizable widget for representing data like hashtags collection and similiar.

![sample1](https://github.com/greenfrvr/hashtag-view/blob/master/screenshots/screen1.png)

## Demo
Downlaod latest demo app from Play Market:

<a href="https://play.google.com/store/apps/details?id=com.greenfrvr.hashtagview.sample">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>

## Gradle Dependency
[ ![Download](https://api.bintray.com/packages/greenfrvr/maven/hashtag-view/images/download.svg) ](https://bintray.com/greenfrvr/maven/hashtag-view/_latestVersion)

Easily reference the library in your Android projects using this dependency in your module's build.gradle file:

```Gradle 
dependencies {
    compile 'com.github.greenfrvr:hashtag-view:1.0.0@aar'
}
```
Library available on both jCenter and Maven Central, but in case of any issues (library can't be resolved) use Bintray repo.

Add repository to your app's build.gradle file:

```Gradle
repositories {
    maven {
        url 'https://dl.bintray.com/greenfrvr/maven/'
    }
}
```
This will reference Bintray's Maven repository that contains hashtags widget directly, rather than going through jCenter first.

## Data

First of all there are two ways to fill `HashtagView` with data. 

1. If you need only displaying your data you can use `HashtagView.setData(List<String> data);` method.
2. If you want some more complex behavior or you want to use your data models, then you can use `HashtagViewsetData(List<T> list, DataTransform<T> transformer)` method.

First you setting up some items collection, then you're telling how you want to display your data using `DataTransform` interface.
For example you have model 

```java
public class Person {
    int id;
    String firstName;
    String midName
    String lastName;
}
```
Now when passing list of `Person`'s, we implementing `DataTransform` interface

```java
HashtagView.setData(persons, new HashtagView.DataTransform<Person>() {
    @Override
    public CharSequence prepare(Person item) {
        String label = "@" + item.firstName.getCharAt(0) + item.midName.getCharAt(0) + item.lastName;
        SpannableString spannableString = new SpannableString(label);
        spannableString.setSpan(new SuperscriptSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color3), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
        }
};)
```
As you may notice implementing `DataTransform.prepare()` method let you define `Spannable` representation of each item.

## Customizing
All attributes can be defined in layout .xml file or programmatically. Below is a list of available attributes.

##### Text attributes
```xml
    <!-- Item text color. -->
    <attr name="tagTextColor" format="color"/>
    <!-- Item text size. -->
    <attr name="tagTextSize" format="dimension"/>
    <!-- Item text gravity, works only in stretch mode. -->
    <attr name="tagTextGravity" format="enum">
        <enum name="left" value="3"/>
        <enum name="right" value="5"/>
        <enum name="center" value="17"/>
    </attr>
```
##### Background and drawables
```xml
    <!-- Item background color or resource. -->
    <attr name="tagBackground" format="color|reference"/>
    <!-- Item foreground color or resource. -->
    <attr name="tagForeground" format="color|reference"/>
    <!-- Item left drawable resource. -->
    <attr name="tagDrawableLeft" format="reference"/>
    <!-- Item right drawable resource. -->
    <attr name="tagDrawableRight" format="reference"/>
    <!-- Item drawable padding. -->
    <attr name="tagDrawablePadding" format="dimension"/>
```
##### Spacing 
```xml
    <!-- Item left and right margins, total distance between two items in a row is be 2 * tagMergin. -->
    <attr name="tagMargin" format="dimension"/>
    <!-- Item left padding. -->
    <attr name="tagPaddingLeft" format="dimension"/>
    <!-- Item right padding. -->
    <attr name="tagPaddingRight" format="dimension"/>
    <!-- Item top padding. -->
    <attr name="tagPaddingTop" format="dimension"/>
    <!-- Item bottom padding. -->
    <attr name="tagPaddingBottom" format="dimension"/>
    <!-- Item minimal width. -->
    <attr name="tagMinWidth" format="dimension"/>
    <!-- Row top and bottom margins, total distance between two rows is 2 * rowMargin. -->
    <attr name="rowMargin" format="dimension"/>
```
##### Specific properties
```xml
    <!-- Defines gravity of row items distribution. -->
    <attr name="rowGravity" format="enum">
        <enum name="left" value="3"/>
        <enum name="right" value="5"/>
        <enum name="center" value="17"/>
    </attr>
    <!-- Defines if each item will wrap its content, or widget will fill all given width. -->
    <attr name="rowMode" format="enum">
        <enum name="wrap" value="0"/>
        <enum name="stretch" value="1"/>
    </attr>
    <!-- Enables selection mode (don't forget to use <selectors>). -->
    <attr name="selectionMode" format="boolean"/>
```
Also you can set up custom typeface by `HashtagView.setTypeface(Typeface)`.
If you want to use some `<selector>` backgrounds you can set `tagBackground` property, `tagForeground` property can be used in case if you want to use `<ripple>` drawables.

## Events
There are two type of events that can be handled by `HashtagView`.

- **Item click event**. 

Setting up item click listener 

```java
HashtagView.setOnTagClickListener(new HashtagView.TagsClickListener() {
            @Override
            public void onItemClicked(Object item) {
              Person p = (Person) item;
            }
        });
```
- **Item selection event**. 

Setting up item click listener 

```java
HashtagView.setOnTagSelectListener(new HashtagView.TagsSelectListener() {
            @Override
            public void onItemSelected(Object item) {
              Person p = (Person) item;
            }
        });
```
Both callbacks returns object of corresponding type defined in `HashtagView.setData()` method. To get list of all selected items call `HashtagView.getSelectedItems()`. Also only one listener can be used at a time, i.e. if widget is in `selectionMode`  then `HashtagView.TagsSelectListener` will handle click events, but not `HashtagView.TagsClickListener`.
        
