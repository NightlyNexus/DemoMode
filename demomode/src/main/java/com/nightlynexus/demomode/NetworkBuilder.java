package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java
@RequiresApi(23)
public class NetworkBuilder {
  public enum DataActivity {
    NONE(null),
    INOUT("inout"),
    IN("in"),
    OUT("out");

    final @Nullable String name;

    DataActivity(@Nullable String name) {
      this.name = name;
    }
  }

  String airplane;
  String fully;
  String sims;
  String nosim;
  String carriernetworkchange;

  public final NetworkBuilder airplane(@Nullable Boolean airplane) {
    this.airplane = airplane == null ? null : airplane ? "show" : "";
    return this;
  }

  // https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java#1262
  /** This affects both the wifi and mobile icons. */
  public final NetworkBuilder fully(@Nullable Boolean fully) {
    this.fully = fully == null ? null : fully ? "true" : "false";
    return this;
  }

  public final NetworkBuilder sims(@Nullable @IntRange(from = 1, to = 8) Integer sims) {
    if (sims == null) {
      this.sims = null;
    } else {
      if (sims < 1 || sims > 8) {
        throw new IllegalArgumentException("sims must be [1, 8] or null. Actual: " + sims);
      }
      this.sims = Integer.toString(sims);
    }
    return this;
  }

  public final NetworkBuilder noSim(@Nullable Boolean noSim) {
    nosim = noSim == null ? null : noSim ? "show" : "";
    return this;
  }

  public final NetworkBuilder carrierNetworkChange(@Nullable Boolean carrierNetworkChange) {
    carriernetworkchange = carrierNetworkChange == null ? null : carrierNetworkChange ? "show" : "";
    return this;
  }

  public Intent build() {
    Bundle extras = new Bundle(5);
    extras.putString("airplane", airplane);
    extras.putString("fully", fully);
    extras.putString("sims", sims);
    extras.putString("nosim", nosim);
    extras.putString("carriernetworkchange", carriernetworkchange);
    return DemoMode.build("network", extras);
  }
}
