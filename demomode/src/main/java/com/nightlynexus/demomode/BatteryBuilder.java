package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

public final class BatteryBuilder {
  private String level;
  private String plugged;

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

  public Intent build() {
    Bundle extras = new Bundle(2);
    extras.putString("level", level);
    extras.putString("plugged", plugged);
    return DemoMode.build("battery", extras);
  }
}
