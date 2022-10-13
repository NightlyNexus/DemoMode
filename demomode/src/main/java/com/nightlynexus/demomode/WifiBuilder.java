package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(23)
public final class WifiBuilder {
  private String fully;
  private String wifi;
  private String level;

  public WifiBuilder fully(@Nullable Boolean fully) {
    this.fully = fully == null ? null : fully ? "true" : "false";
    return this;
  }

  /** -1 level for wifi state disconnected. **/
  public WifiBuilder wifi(@Nullable Boolean wifi, @Nullable Integer level) {
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
    return this;
  }

  public Intent build() {
    Bundle extras = new Bundle(3);
    extras.putString("fully", fully);
    extras.putString("wifi", wifi);
    extras.putString("level", level);
    return DemoMode.build("network", extras);
  }
}
