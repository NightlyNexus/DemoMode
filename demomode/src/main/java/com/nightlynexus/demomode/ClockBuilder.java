package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static com.nightlynexus.demomode.DemoMode.putString;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/policy/Clock.java
@RequiresApi(23)
public final class ClockBuilder {
  String millis;
  String hhmm;

  public ClockBuilder setTimeInMilliseconds(long millis) {
    this.millis = Long.toString(millis);
    this.hhmm = null;
    return this;
  }

  public ClockBuilder setTimeInHoursAndMinutes(String hhmm) {
    if (hhmm == null) {
      throw new NullPointerException("hhmm == null");
    }
    if (hhmm.length() != 4) {
      throw new IllegalArgumentException("hhmm must be a string of four digits. Actual: " + hhmm);
    }
    this.millis = null;
    this.hhmm = hhmm;
    return this;
  }

  public ClockBuilder clearSetTime() {
    millis = null;
    hhmm = null;
    return this;
  }

  public @Nullable Intent build() {
    Bundle extras = new Bundle(2);
    putString(extras, "millis", millis);
    putString(extras, "hhmm", hhmm);
    return DemoMode.build("clock", extras);
  }
}
