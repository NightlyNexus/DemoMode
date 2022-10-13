package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(23)
public final class NetworkBuilder {
  public enum Datatype {
    ONE_X("1x"),
    THREE_G("3g"),
    FOUR_G("4g"),
    FOUR_G_PLUS("4g+"),
    E("e"),
    G("g"),
    H("h"),
    LTE("lte"),
    LTE_PLUS("lte+"),
    ROAM("roam"),
    HIDE("");

    final String name;

    Datatype(String name) {
      this.name = name;
    }
  }

  private String airplane;
  private String fully;
  private String mobile;
  private String datatype;
  private String slot;
  private String level;
  private String carriernetworkchange;
  private String sims;
  private String nosim;

  public NetworkBuilder airplane(@Nullable Boolean airplane) {
    this.airplane = airplane == null ? null : airplane ? "show" : "";
    return this;
  }

  public NetworkBuilder fully(@Nullable Boolean fully) {
    this.fully = fully == null ? null : fully ? "true" : "false";
    return this;
  }

  /** -1 level for network state disconnected. **/
  public NetworkBuilder mobile(@Nullable Boolean mobile, @Nullable Datatype datatype, int slot,
      @Nullable Integer level) {
    this.mobile = mobile == null ? null : mobile ? "show" : "";
    this.datatype = datatype == null ? null : datatype.name;
    if (slot < 0 || slot > 8) {
      throw new IllegalArgumentException("slot must [0, 8]. Actual: " + slot);
    }
    this.slot = Integer.toString(slot);
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

  public NetworkBuilder carrierNetworkChange(@Nullable Boolean carrierNetworkChange) {
    carriernetworkchange = carrierNetworkChange == null ? null : carrierNetworkChange ? "show" : "";
    return this;
  }

  public NetworkBuilder sims(@Nullable Integer sims) {
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

  public NetworkBuilder nosim(@Nullable Boolean nosim) {
    this.nosim = nosim == null ? null : nosim ? "show" : "";
    return this;
  }

  public Intent build() {
    Bundle extras = new Bundle(9);
    extras.putString("airplane", airplane);
    extras.putString("fully", fully);
    extras.putString("mobile", mobile);
    extras.putString("datatype", datatype);
    extras.putString("slot", slot);
    extras.putString("level", level);
    extras.putString("carriernetworkchange", carriernetworkchange);
    extras.putString("sims", sims);
    extras.putString("nosim", nosim);
    return DemoMode.build("network", extras);
  }
}
