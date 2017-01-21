Demo Mode
=====================

Control Android's Demo Mode from your app.

Puts the Demo Mode controls into an understandable API to use for debugging or in screenshot apps.
<br/>Sending Demo Mode broadcasts is as easy as
<br/>`sendBroadcast(new NotificationsBuilder().visible(false).build())`.
<br/>
<br/>Get a [`DemoModeInitializer`](demomode/src/main/java/com/nightlynexus/demomode/DemoModeInitializer.java) via `DemoMode.initializer(Context)` to handle granting permissions from an app.
<br/>
<br/>
![](images/example.jpg)

Why?
--------

Android (Mashmallow and above) has a SystemUI Tuner. Users can find it in the device Settings app, developers can send the corresponding broadcasts from the adb shell, but applications need the [system-signed DUMP permission](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/core/res/AndroidManifest.xml#2085) to send these broadcasts.

The [documentation on Demo Mode](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/packages/SystemUI/docs/demo_mode.md) is unclear what extras should be sent with the broadcasts and is wrong in some places. (For example, the ["sync" and "eri" icons](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/packages/SystemUI/src/com/android/systemui/tuner/DemoModeFragment.java#42) cannot currently be changed by the [broadcasts](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/packages/SystemUI/src/com/android/systemui/statusbar/phone/DemoStatusIcons.java#61).)

This library seeks to make Demo Mode easier to use in applications by handling permissions and wrapping the implementation of creating the broadcast Intents in an understandable and working API.

The current implementation of this library is based off [AOSP commit b8f0e69ef087798b46447a7352461a5120186646](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646).

The relevant implementation code that receives the broadcast is in [`DemoMode`](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/packages/SystemUI/src/com/android/systemui/DemoMode.java), [`BatteryControllerImpl`](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/packages/SystemUI/src/com/android/systemui/statusbar/policy/BatteryControllerImpl.java), [`NetworkControllerImpl`](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkControllerImpl.java), [`PhoneStatusBar`](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/packages/SystemUI/src/com/android/systemui/statusbar/phone/PhoneStatusBar.java), [`DemoStatusIcons`](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/packages/SystemUI/src/com/android/systemui/statusbar/phone/DemoStatusIcons.java), and [`Clock`](https://android.googlesource.com/platform/frameworks/base/+/b8f0e69ef087798b46447a7352461a5120186646/packages/SystemUI/src/com/android/systemui/statusbar/policy/Clock.java)

Download
--------

Gradle:

```groovy
compile 'com.nightlynexus.demomode:demomode:0.1.1'
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
