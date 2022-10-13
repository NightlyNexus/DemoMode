package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/phone/StatusBarDemoMode.java
@RequiresApi(23)
public final class BarsBuilder {
  public enum BarsMode {
    OPAQUE("opaque"),
    TRANSLUCENT("translucent"),
    SEMI_TRANSPARENT("semi-transparent"),
    TRANSPARENT("transparent"),
    WARNING("warning");

    final String name;

    BarsMode(String name) {
      this.name = name;
    }
  }

  String mode;

  public BarsBuilder mode(@Nullable BarsMode mode) {
    this.mode = mode == null ? null : mode.name;
    return this;
  }

  public Intent build() {
    Bundle extras = new Bundle(1);
    extras.putString("mode", mode);
    return DemoMode.build("bars", extras);
  }
}
