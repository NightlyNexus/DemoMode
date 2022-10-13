package com.nightlynexus.demomode;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java
@RequiresApi(23)
public final class WifiNetworkBuilder extends NetworkBuilder {
  String wifi;
  String level;
  String activity;
  String ssid;

  /** -1 level for wifi state disconnected. **/
  public WifiNetworkBuilder wifi(@Nullable Boolean wifi, @Nullable Integer level,
      @Nullable DataActivity activity, @Nullable String ssid) {
    this.wifi = wifi == null ? null : wifi ? "show" : "";
    if (level == null) {
      this.level = null;
    } else {
      switch (level) {
        case -1:
          this.level = "null";
          break;
        case 0:
          this.level = "0";
          break;
        case 1:
          this.level = "1";
          break;
        case 2:
          this.level = "2";
          break;
        case 3:
          this.level = "3";
          break;
        case 4:
          this.level = "4";
          break;
        default:
          throw new IllegalArgumentException("level must be [-1, 4] or null. Actual: " + level);
      }
    }
    this.activity = activity == null ? null : activity.name;
    this.ssid = ssid;
    return this;
  }

  @Override public Intent build() {
    return super.build()
        .putExtra("wifi", wifi)
        .putExtra("level", level)
        .putExtra("activity", activity)
        .putExtra("ssid", ssid);
  }
}
