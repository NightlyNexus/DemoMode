package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

@RequiresApi(23)
public final class ClockBuilder {
  private String millis;
  private String hhmm;

  public ClockBuilder setTimeInMilliseconds(long millis) {
    this.millis = Long.toString(millis);
    this.hhmm = null;
    return this;
  }

  public ClockBuilder setTimeInHoursAndMinutes(String hhmm) {
    if (hhmm == null || hhmm.length() != 4) {
      throw new IllegalArgumentException("hhmm must be four digits or null. Actual: " + hhmm);
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

  public Intent build() {
    Bundle extras = new Bundle(2);
    extras.putString("millis", millis);
    extras.putString("hhmm", hhmm);
    return DemoMode.build("clock", extras);
  }
}
