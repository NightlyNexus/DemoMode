Change Log
==========

Version 0.8.1 *(2025-02-26)*
----------------------------

* Fixed SatelliteNetworkBuilder.show(false) crashing when building.

Version 0.8.0 *(2025-02-24)*
----------------------------

* Breaking API change: SatelliteNetworkBuilder must now set the show parameter.

Version 0.7.0 *(2025-02-24)*
----------------------------

* Breaking API change: DemoModePermissions.demoModeSystemSettingsScreenIntent now takes PackageManager as its parameter.
* Breaking API change: MobileNetworkBuilder now requires setting the network name via networkName(String). Consider using MobileNetworkBuilder.DEFAULT_NETWORK_NAME.
* Added carrierid, networkname, slice, and ntn support in MobileNetworkBuilder.
* Added satellite support with connection and level parameters in SatelliteNetworkBuilder.
* Fixed SDK < 26 crashing trying to build network intents.

Version 0.6.0 *(2022-10-31)*
----------------------------

* build() now returns a null Intent when broadcasting the Intent would have no effect.

Version 0.5.7 *(2022-10-30)*
----------------------------

* Parameters that have effects invariably are now required to be set explicitly.

Version 0.5.6 *(2022-10-30)*
----------------------------

* Added roam as a data type for SDK_INT < 26.

Version 0.5.5 *(2022-10-30)*
----------------------------

* Replaced null DataType with DataType.NO_DATA.

Version 0.5.4 *(2022-10-30)*
----------------------------

* Reordered DataActivity.NONE.

Version 0.5.3 *(2022-10-29)*
----------------------------

* Replaced null DataActivity with DataActivity.NONE.

Version 0.5.2 *(2022-10-27)*
----------------------------

* Fixed missing IntRange on MobileNetworkBuilder.slot.

Version 0.5.1 *(2022-10-22)*
----------------------------

* Fixed MobileNetworkBuilder.dataType name.

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
