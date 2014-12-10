Version 2 is released (uploading to maven ASAP)

ActionBar RTLizer (ActionBar RTL Arranger)
================

A library that can RTLize android `ActionBar`!

It is not a custom `ActionBar` or anything else!
Its only a piece of code that can re-arrange the android `ActionBar` in RTL direction.

Usage (only 3 simple steps)
================
**1)** Add this line to `build.gradle` file inside your app project folder:
```groovy
dependencies {
    // other stuff
    .
    .
    compile 'info.semsamot:actionbar-rtlizer:1.+@aar'
}
```

**2)** Define a private variable of `ActionBarRtlizer` class in your activity class.

```java
private ActionBarRtlizer rtlizer;
```

**3)** In `onCreateOptionsMenu` method of your activity, after calling `setContentView` method, add these two lines:

```java
rtlizer = new ActionBarRtlizer(this);
ViewGroup actionBarView = rtlizer.getActionBarView();
ViewGroup homeView = (ViewGroup)rtlizer.findViewByClass("HomeView", actionBarView);

rtlizer.flipActionBarUpIconIfAvailable(homeView);
RtlizeEverything.rtlize(actionBarView);
RtlizeEverything.rtlize(homeView);
```

Then compile your app and enjoy of this awesome RTLization!

Alternative features
================
You can retrieve `ActionBarView`, `ActionMenuView` and `HomeView` after rtlization has been completed.
Fortunately you can listen for rtlization completed event.

An example of using these features is that you can animate ActionBar menu items after rtlization. it's so simple! (see the below sample)

```java
rtlizer.setOnRtlizeFinishedListener(new ActionBarRtlizer.OnRtlizeFinishedListener() {
    @Override
    public void onRtlizeFinished()
    {
        Animation rotateAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        View actionMenuItem = rtlizer.getActionMenuView().findViewById(
                R.id.action_item);
        actionMenuItem.setAnimation(rotateAnim);
    }
});
```

Provided methods for retrieving ActionBar view and its children:

```java
// All return value types are of ViewGroup class.
getActionBarView()
getHomeViewContainer() // returns null on lower API versions than 17
getHomeView()
getActionMenuView()
```

Compatibility
================
This library is compatible with API Level 7+

Known issues
================
- In API Level 10 and lower versions, the main activity(launcher activity)'s ActionBar is not rtlized properly. (will be fixed soon, with the help of God)

Apps using this library
================
If you have interested in using this library in your app, then you may send me your app name, so i can put that on the list and have proud that your app used this library.

Donate
================
With money? **No**

If you like this library, you can make a donation by **clicking** on the **star** in top of this page. Its so simple!

License
================
> Copyright 2014 semsamot

> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at

>     http://www.apache.org/licenses/LICENSE-2.0

> Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Tags:
action bar rtl, rtl action bar, action bar in rtl, action bar rtl direction, android action bar rtl, action bar rtl arranger, action bar rtlizer, actionbar rtl, rtl actionbar
