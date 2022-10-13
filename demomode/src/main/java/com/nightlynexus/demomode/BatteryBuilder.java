package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/policy/BatteryControllerImpl.java
@RequiresApi(23)
public final class BatteryBuilder {
  String level;
  String plugged;
  String powersave;
  String present;

  public BatteryBuilder level(@Nullable Integer level) {
    if (level == null) {
      this.level = null;
    } else {
      int value = level;
      if (value < 0 || value > 100) {
        throw new IllegalArgumentException("level must be [0, 100] or null. Actual: " + value);
      }
      this.level = Integer.toString(value);
    }
    return this;
  }

  public BatteryBuilder plugged(@Nullable Boolean plugged) {
    this.plugged = plugged == null ? null : plugged ? "true" : "false";
    return this;
  }

  public BatteryBuilder powerSave(@Nullable Boolean powerSave) {
    powersave = powerSave == null ? null : powerSave ? "true" : "false";
    return this;
  }

  public BatteryBuilder present(@Nullable Boolean present) {
    this.present = present == null ? null : present ? "true" : "false";
    return this;
  }

  public Intent build() {
    Bundle extras = new Bundle(4);
    extras.putString("level", level);
    extras.putString("plugged", plugged);
    extras.putString("powersave", powersave);
    extras.putString("present", present);
    return DemoMode.build("battery", extras);
  }
}
