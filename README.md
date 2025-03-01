Demo Mode
=====================

See Demo Mode in action: [The Demo Mode Settings app](https://play.google.com/store/apps/details?id=com.nightlynexus.demomodesettings) uses every available Demo Mode modifier.
<br/>
<br/>
<a href='https://play.google.com/store/apps/details?id=com.nightlynexus.demomodesettings'><img alt='Get Demo Mode Settings on Google Play' src='/images/play_icon.png' height="150"/></a>
<a href='https://play.google.com/store/apps/details?id=com.nightlynexus.demomodesettings'><img alt='Get Demo Mode Settings on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' height="150"/></a>

--------

Control Android’s Demo Mode from your app.

This library wraps Android’s Demo Mode controls in an understandable API for debugging or screenshots.
<br/>Sending Demo Mode broadcasts is as easy as
<br/>`sendBroadcast(new NotificationsBuilder().visible(false).build())`
<br/>
<br/>
![](images/example.jpg)

Note that the system-signed DUMP permission is needed to send broadcasts to alter the Demo Mode state.
<br/>Also, Demo Mode must be enabled in the system settings. This can be done in the system settings app. This library can set this system setting with `DemoModePermissions.setDemoModeAllowed(context, true)` but requires the system-signed WRITE_SECURE_SETTINGS permission to do so.

To grant these system-signed permissions to an app via adb, run the following commands. Do not forget to add the permissions to the app’s manifest, as well.
<br/>`adb shell pm grant <com.example.app> android.permission.DUMP`
<br/>`adb shell pm grant <com.example.app> android.permission.WRITE_SECURE_SETTINGS`

Why?
--------

Android (Marshmallow and above) has a public SystemUI Tuner. Users can find the SystemUI Tuner in the device settings app. Developers can send the corresponding SystemUI Tuner broadcasts from the adb shell, but applications need the [system-signed DUMP permission](https://android.googlesource.com/platform/frameworks/base/+/332641fc24cb79a58e658a25d5963f3059d66837/core/res/AndroidManifest.xml#4953) to send these broadcasts.

The [documentation on Demo Mode](https://android.googlesource.com/platform/frameworks/base/+/332641fc24cb79a58e658a25d5963f3059d66837/packages/SystemUI/docs/demo_mode.md) is unclear about what extras should be sent with the broadcasts and is incorrect in some places. (For example, the ["sync" and "eri" icons](https://android.googlesource.com/platform/frameworks/base/+/332641fc24cb79a58e658a25d5963f3059d66837/packages/SystemUI/src/com/android/systemui/tuner/DemoModeFragment.java#40) cannot currently be changed via [broadcasts](https://android.googlesource.com/platform/frameworks/base/+/332641fc24cb79a58e658a25d5963f3059d66837/packages/SystemUI/src/com/android/systemui/statusbar/phone/DemoStatusIcons.java#133).)

This library makes Demo Mode easier to use in Android applications by handling permissions and wrapping the implementation of creating the broadcast Intents in an understandable and working API.

The current implementation of this library is based off [AOSP commit 332641fc24cb79a58e658a25d5963f3059d66837](https://android.googlesource.com/platform/frameworks/base/+/332641fc24cb79a58e658a25d5963f3059d66837).

The relevant implementation code that receives the broadcast is in [`DemoMode`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/demomode/DemoMode.java), [`BatteryControllerImpl`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/policy/BatteryControllerImpl.java), [`Clock`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/policy/Clock.java), [`DemoStatusIcons`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/phone/DemoStatusIcons.java), [`NetworkControllerImpl`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java), [`NotificationIconAreaController`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/phone/NotificationIconAreaController.java#662), [`OperatorNameViewController`](https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/OperatorNameViewController.java#170), and  [`DemoDeviceBasedSatelliteDataSource`](https://android.googlesource.com/platform/frameworks/base/+/332641fc24cb79a58e658a25d5963f3059d66837/packages/SystemUI/src/com/android/systemui/statusbar/pipeline/satellite/data/demo/DemoDeviceBasedSatelliteDataSource.kt).

Download
--------

Gradle:

```groovy
implementation 'com.nightlynexus.demomode:demomode:0.8.1'
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
