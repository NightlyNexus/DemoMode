package com.nightlynexus.demomode;

import android.content.Intent;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION.SDK_INT;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java
@RequiresApi(23)
public final class MobileNetworkBuilder extends NetworkBuilder {
  public enum DataType {
    ONE_X("1x"),
    THREE_G("3g"),
    FOUR_G("4g"),
    FOUR_G_PLUS("4g+"),
    FIVE_G("5g"),
    FIVE_G_E("5ge"),
    FIVE_G_PLUS("5g+"),
    E("e"),
    G("g"),
    H("h"),
    H_PLUS("h+"),
    LTE("lte"),
    LTE_PLUS("lte+"),
    DIS("dis"),
    NOT("not"),
    HIDE("");

    final String name;

    DataType(String name) {
      this.name = name;
    }
  }

  String mobile;
  String datatype;
  String slot;
  String roam;
  String level;
  String inflate;
  String activity;

  // https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java#1383
  // There appears to be an off-by-one error (missing the -1) in SystemUI. There are only five
  // levels and the disconnected state, so limit the level here.
  /** -1 level for network state disconnected. **/
  public MobileNetworkBuilder mobile(@Nullable Boolean mobile, @Nullable DataType dataType,
      @IntRange(from = 0, to = 8) int slot, @Nullable Boolean roam,
      @IntRange(from = -1, to = 4) @Nullable Integer level, @Nullable Boolean inflate,
      @Nullable DataActivity activity) {
    this.mobile = mobile == null ? null : mobile ? "show" : "";
    if (slot < 0 || slot > 8) {
      throw new IllegalArgumentException("slot must [0, 8]. Actual: " + slot);
    }
    datatype = dataType == null ? null : dataType.name;
    this.slot = Integer.toString(slot);
    // https://android.googlesource.com/platform/frameworks/base/+/0f0de13c37082f9443e3f0c8cc413188ec66d3fe%5E%21/
    if (SDK_INT >= 26) {
      this.roam = roam == null ? null : roam ? "show" : "";
    } else {
      if (roam != null) {
        if (dataType != null) {
          throw new IllegalArgumentException("dataType and roam cannot both be specified on SDK levels <26.");
        }
        if (roam) {
          datatype = "roam";
        }
      }
    }
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
    this.inflate = inflate == null ? null : inflate ? "true" : "false";
    // https://android.googlesource.com/platform/frameworks/base/+/0f0de13c37082f9443e3f0c8cc413188ec66d3fe%5E%21/
    if (activity != null && SDK_INT < 26) {
      throw new IllegalArgumentException("activity cannot be specified on SDK levels <26.");
    }
    this.activity = activity == null ? null : activity.name;
    return this;
  }

  @Override public Intent build() {
    Intent result = super.build()
        .putExtra("mobile", mobile)
        .putExtra("datatype", datatype)
        .putExtra("slot", slot);
    // https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java#1376
    // containsKey check.
    if (roam != null) {
      result.putExtra("roam", roam);
    }
    result.putExtra("level", level);
    // https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java#1386
    // containsKey check.
    if (inflate != null) {
      result.putExtra("inflate", inflate);
    }
    return result.putExtra("activity", activity);
  }
}
