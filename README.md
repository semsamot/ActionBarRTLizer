ActionBar RTLizer (ActionBar RTL Arranger)
================

A library that can RTLize android `ActionBar`!

It is not a custom `ActionBar` or anything else!
Its only a piece of code that can re-arrange the android `ActionBar` in RTL direction.

Usage (only 4 simple steps)
================
1.
Add this line to `build.gradle` file inside your app project folder:
```groovy
dependencies {
    // other stuff
    .
    .
    compile 'info.semsamot:actionbar-rtlizer:1.+@aar'
}
```

2.
Define a private variable of `ActionBarRtlizer` class in your activity class.

```java
private ActionBarRtlizer rtlizer;
```

3.
In overrided `onCreate` method of your activity, after calling `setContentView` method, add these two lines:

```java
rtlizer = new ActionBarRtlizer(this);
rtlizer.rtlize();
```

4.
In overrided `onResume` method of your activity, add:

```java
if (rtlizer != null)
    rtlizer.rtlize();
```

Then compile your app and enjoy of this awesome RTLization!


Compatibility:
================
This library is compatible with API Level 7+

Known issues:
================
- In API Level 10 and lower versions, the main activity(launcher activity)'s ActionBar is not rtlized properly. (will be fixed soon, with the help of God)

Apps using this library:
================
If you have interested in using this library in your app, then you may send me your app name, so i can put that on the list and have proud that your app used this library.

Donate:
================
With money? **No**

If you like this library, you can make a donation by clicking on the star in top of this page. Its so simple!

Tags:
action bar rtl, rtl action bar, action bar in rtl, action bar rtl direction, android action bar rtl, action bar rtl arranger, action bar rtlizer, actionbar rtl, rtl actionbar
