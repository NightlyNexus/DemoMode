package com.nightlynexus.demomode;

import android.os.Bundle;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION.SDK_INT;
import static com.nightlynexus.demomode.DemoMode.putString;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java
@RequiresApi(23)
public final class WifiNetworkBuilder extends NetworkBuilder {
  String wifi;
  String level;
  DataActivity activity; // Required.
  String ssid;

  /**
   * @param show null will cause all other parameters to be ignored.
   */
  public WifiNetworkBuilder show(@Nullable Boolean show) {
    wifi = show == null ? null : show ? "show" : "";
    return this;
  }

  /**
   * @param level -1 for disconnected network state.
   */
  public WifiNetworkBuilder level(@IntRange(from = -1, to = 4) @Nullable Integer level) {
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
    return this;
  }

  // https://android.googlesource.com/platform/frameworks/base/+/5c88ffb5072b96662b34cb8139151707f424318a%5E%21/#F9
  @RequiresApi(26)
  public WifiNetworkBuilder activity(DataActivity activity) {
    if (activity == null) {
      throw new NullPointerException("activity == null");
    }
    if (SDK_INT < 26) {
      throw new IllegalStateException("activity cannot be specified on SDK levels <26.");
    }
    this.activity = activity;
    return this;
  }

  public WifiNetworkBuilder ssid(@Nullable String ssid) {
    this.ssid = ssid;
    return this;
  }

  @Override void addExtras(Bundle extras) {
    if (wifi == null) {
      // Nothing here will have an effect.
      return;
    }
    if (activity == null) {
      throw new IllegalStateException("Missing required activity.");
    }
    putString(extras, "wifi", wifi);
    putString(extras, "level", level);
    putString(extras, "activity", activity.name);
    putString(extras, "ssid", ssid);
  }
}
