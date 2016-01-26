# HashtagView
### Version 1.2.1 available! 

[![Join the chat at https://gitter.im/greenfrvr/hashtag-view](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/greenfrvr/hashtag-view?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-HashtagView-green.svg?style=flat)](https://android-arsenal.com/details/1/2447)
[![Android Gems](http://www.android-gems.com/badge/greenfrvr/hashtag-view.svg?branch=master)](http://www.android-gems.com/lib/greenfrvr/hashtag-view)

Fully customizable widget for representing data like hashtags collection and similiar.

![sample1](https://github.com/greenfrvr/hashtag-view/blob/master/screenshots/screen1.png)

## Demo
Downlaod latest demo app from Play Market:

<a href="https://play.google.com/store/apps/details?id=com.greenfrvr.hashtagview.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-AC-global-none-all-co-pr-py-PartBadges-Oct1515-1">
  <img alt="Get it on Google Play" 
        src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png"
        height = 60/>
</a>

## Gradle Dependency
[ ![Download](https://api.bintray.com/packages/greenfrvr/maven/hashtag-view/images/download.svg) ](https://bintray.com/greenfrvr/maven/hashtag-view/_latestVersion)

Easily reference the library in your Android projects using this dependency in your module's build.gradle file:

```Gradle 
dependencies {
    compile 'com.github.greenfrvr:hashtag-view:1.2.1'
}
```

or

```Gradle
dependencies {
    compile ('com.github.greenfrvr:hashtag-view:1.2.1@aar'){
        transitive=true
    }
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

First of all there are three ways to fill `HashtagView` with data. 

1. If you need only displaying your data you can use `HashtagView.setData(List<String> data);` method.
2. If you want some more complex behavior or you want to use your data models, then you can use `HashtagViewsetData(List<T> list, DataTransform<T> transformer)` method.
<br/>First you setting up some items collection, then you're telling how you want to display your data using `DataTransform` interface.
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
          spannableString.setSpan(new ForegroundColorSpan(color1), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          spannableString.setSpan(new ForegroundColorSpan(color2), 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          return spannableString;
      }
});
```
As you may notice implementing `DataTransform.prepare()` method let you define `Spannable` representation of each item.<br/>
3. If you use widget in `selectionMode` and you want some items to be preselected, then you can use `HashtagViewsetData(List<T> list, DataTransform<T> transformer, DataSelector<T> selector)` method.
<br/>This method works just like previous one, but additional interface `DataSelector` allows you to specify which items should be in selected state, before user can select something. 
For example to select each second item it can be implemented like this:
```java
HashtagView.setData(persons, transformer, new HashtagView.DataSelector<Person>() {
      @Override
      public boolean preselect(Person item) {
          return persons.indexOf(item) % 2 == 1;
      }
});
```
Notice that items won't be preselected if widget is not in `selectionMode`.
<br/>Also while in `selectionMode` you can use `HashtagView.DataStateTransform` instead of `HashtagView.DataTransform`. `DataStateTransform` allows you to define how your items will be displayed in selected state, for example you can define different `Spannable` representations for selected and non-selected states. *(Take a look at "styles sample" in demo app)*
```java
HashtagView.DataTransform<String> stateTransform = new HashtagView.DataStateTransform<String>() {
      @Override
      public CharSequence prepare(Person item) {
          String label = "@" + item.firstName.getCharAt(0) + item.midName.getCharAt(0) + item.lastName;
          SpannableString spannableString = new SpannableString(label);
          spannableString.setSpan(new SuperscriptSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          spannableString.setSpan(new ForegroundColorSpan(color1), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          spannableString.setSpan(new ForegroundColorSpan(color2), 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          return spannableString;
      }
      @Override
      public CharSequence prepareSelected(Person item) {
          String label = "@" + item.firstName.getCharAt(0) + item.midName.getCharAt(0) + item.lastName;
          SpannableString spannableString = new SpannableString(label);
          spannableString.setSpan(new SuperscriptSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          spannableString.setSpan(new ForegroundColorSpan(color1), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          spannableString.setSpan(new StrikethroughSpan(), 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          return spannableString;
      }
};
```

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
    <!-- Item text ellipsize mode (just like in TextView). -->
    <attr name="tagEllipsize" format="enum">
        <enum name="start" value="0"/>
        <enum name="middle" value="1"/>
        <enum name="end" value="2"/>
        <enum name="marquee" value="3"/>
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
    <!-- Item selected state left drawable resource. -->
    <attr name="tagSelectedDrawableLeft" format="reference"/>
    <!-- Item right drawable resource. -->
    <attr name="tagDrawableRight" format="reference"/>
    <!-- Item selected state right drawable resource. -->
    <attr name="tagSelectedDrawableRight" format="reference"/>
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
    <!-- Item maximal width. -->
    <attr name="tagMaxWidth" format="dimension"/>
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
    <!-- Defines row items distribution based on items weight. -->
    <attr name="rowDistribution" format="enum">
        <enum name="left" value="0"/>
        <enum name="middle" value="1"/>
        <enum name="right" value="2"/>
        <enum name="random" value="3"/>
    </attr>
    <!-- Defines if each item will wrap its content, or widget will fill all given width. -->
    <attr name="rowMode" format="enum">
        <enum name="wrap" value="0"/>
        <enum name="stretch" value="1"/>
        <enum name="equal" value="2"/>
    </attr>
    <!-- Defines fixed rows quantity, can be considered as horizontal mode (require HorizontalScrollView wrapping)-->
    <attr name="rowsQuantity" format="integer"/>
    <!-- Enables selection mode (don't forget to use <selectors>). -->
    <attr name="selectionMode" format="boolean"/>
    <!-- Enables dynamic mode (allows to add/remove items dynamically). -->
    <attr name="dynamicMode" format="boolean"/>
```
Also you can set up custom typeface by `HashtagView.setTypeface(Typeface)`.
If you want to use some `<selector>` backgrounds you can set `tagBackground` property, `tagForeground` property can be used in case if you want to use `<ripple>` drawables.

## Events
There are two type of events that can be handled by `HashtagView`.

- **Item click event**. 

Setting up item click listener 

```java
HashtagView.addOnTagClickListener(new HashtagView.TagsClickListener() {
      @Override
      public void onItemClicked(Object item) {
          Person p = (Person) item;
      }
});
```
- **Item selection event**. 

Setting up item selection listener. From version 1.1.1 selection callback is returning selection state for exact item, i.e. it returns data model and its selection state, true - for selected state, false - for non-selected.

```java
HashtagView.addOnTagSelectListener(new HashtagView.TagsSelectListener() {
      @Override
      public void onItemSelected(Object item, boolean selected) {
          Person p = (Person) item;
      }
});
```
Both callbacks returns object of corresponding type defined in `HashtagView.setData()` method. To get list of all selected items call `HashtagView.getSelectedItems()`. Also only one listener can be used at a time, i.e. if widget is in `selectionMode`  then `HashtagView.TagsSelectListener` will handle click events, but not `HashtagView.TagsClickListener`. 

All listeners implemented using Observer pattern, so you can set multiple listeners for both types of events. To remove some specific listener use `HashtagView.removeOnTagClickListener(TagsClickListener listener)` or `HashtagView.removeOnTagSelectListener(TagsSelectListener listener)` or you can remove all available listeners by calling `HashtagView.removeListeners()`. 
*Note: be attentive and do not let listener to remove itself.*
        
## Dynamic mode
To be able add and remove tags dynamically you should set dynamic mode to `true` (via code or xml). After that you can use next methods: 
- `HastagView.addItem()` 
returns true if item added successfully, false if item can't be added (in case `HastagView` already contains such item)
- `HastagView.removeItem()`
returns true if item removed successfully, false if item can't be removed (in case `HashtagView` doesn't contain such item)
