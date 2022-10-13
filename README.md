Demo Mode
=====================

Control Android’s Demo Mode from your app.

This library wraps Android’s Demo Mode controls in an understandable API for debugging or screenshots.
<br/>Sending Demo Mode broadcasts is as easy as
<br/>`sendBroadcast(new NotificationsBuilder().visible(false).build())`
<br/>
<br/>
![](images/example.jpg)

Why?
--------

Android (Marshmallow and above) has a public SystemUI Tuner. Users can find the SystemUI Tuner in the device settings app. Developers can send the corresponding SystemUI Tuner broadcasts from the adb shell, but applications need the [system-signed DUMP permission](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/core/res/AndroidManifest.xml#3758) to send these broadcasts.

The [documentation on Demo Mode](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/docs/demo_mode.md) is unclear about what extras should be sent with the broadcasts and is incorrect in some places. (For example, the ["sync" and "eri" icons](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/tuner/DemoModeFragment.java#39) cannot currently be changed via [broadcasts](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/phone/DemoStatusIcons.java#124).)

This library makes Demo Mode easier to use in Android applications by handling permissions and wrapping the implementation of creating the broadcast Intents in an understandable and working API.

The current implementation of this library is based off [AOSP commit 1291b83a2fb8ae8a095d50730f75013151f6ce3f](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f).

The relevant implementation code that receives the broadcast is in [`DemoMode`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/demomode/DemoMode.java), [`BatteryControllerImpl`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/policy/BatteryControllerImpl.java), [`Clock`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/policy/Clock.java), [`DemoStatusIcons`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/phone/DemoStatusIcons.java), [`NetworkControllerImpl`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java), [`NotificationIconAreaController`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/phone/NotificationIconAreaController.java#662), and [`OperatorNameViewController`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/OperatorNameViewController.java#170).

Download
--------

Gradle:

```groovy
implementation 'com.nightlynexus.demomode:demomode:0.2.0'
```

License
--------

    Copyright 2016 Eric Cochran

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
