Change Log
==========

Version 0.5.0 *(2022-10-22)*
----------------------------

* Lots of breaking API changes.
* Reworked the MobileNetworkBuilder and WifiNetworkBuilder APIs.
* Fixed MobileNetworkBuilder.inflate and MobileNetworkBuilder.activity not being set without MobileNetworkBuilder.level being set.
* Fixed MobileNetworkBuilder.roam always being set to on on SDK versions <26.
* Renamed NetworkBuilder.nosim to NetworkBuilder.noSim.
* Added IntRange annotations.
* DemoModePermissions.demoModeSystemSettingsScreenIntent now always returns an Intent targeting the SystemUI package.

Version 0.4.0 *(2022-10-14)*
----------------------------

* Breaking API change: DemoModePermissions.isDemoModeSystemSettingEnabled is now DemoModePermissions.isDemoModeAllowed.
* Breaking API change: DemoModePermissions.setDemoModeSystemSettingEnabled is now DemoModePermissions.setDemoModeAllowed.

Version 0.3.0 *(2022-10-13)*
----------------------------

* Lots of breaking API changes due to name changes.
* Added helper methods for checking if Demo Mode is on and setting it on or off via writing to the system settings instead of sending a broadcast.

Version 0.2.0 *(2022-10-13)*
----------------------------

* Lots of breaking API changes.
* Updated to the latest Demo Mode features. Added runtime SDK version checks for many of the additions, but no promises: some features might fail silently on old Android versions.
* Removed the initializer type in favor of static methods in DemoModePermissions.

Version 0.1.2 *(2017-05-01)*
----------------------------

 * Breaking API change: Require SDK version 17+.
 * Breaking API change: Fix name of NetworkBuilder.carrierNetworkChange.

Version 0.1.1 *(2017-01-14)*
----------------------------

 * New: Add wifi "fully" connected parameter.


Version 0.1.0 *(2017-01-08)*
----------------------------

 * Initial version.
