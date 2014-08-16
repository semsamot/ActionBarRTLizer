ActionBar RTLizer (ActionBar RTL Arranger)
================

A library that can RTLize android "ActionBar"!

It is not a custom ActionBar or anything else!
Its only a piece of code that can re-arrange the android ActionBar in RTL direction.

Using (only 3 simple steps)
================
1)
Define a private variable of ActionBarRtlizer class in your activity class.

```java
    private ActionBarRtlizer rtlizer;
```

2)
In overrided "onCreate" method of your activity, after calling "setContentView" method, add these two lines:

```java
    rtlizer = new ActionBarRtlizer(this);
    rtlizer.rtlize();
```

3)
In overrided "onResume" method of your activity, add:

```java
    if (rtlizer != null)
      rtlizer.rtlize();
```

Compile your app and enjoy of this awesome RTLization!


Known issues:
================
- In API Level 10 and lower versions, the main activity(launcher activity)'s ActionBar is not rtlized properly. (will be fixed soon, with the help of God)
