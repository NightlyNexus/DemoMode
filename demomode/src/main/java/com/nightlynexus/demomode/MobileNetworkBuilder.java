package com.nightlynexus.demomode;

import android.content.Intent;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION.SDK_INT;
import static java.lang.Boolean.FALSE;

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

  /**
   * @param show null will cause all other parameters to be ignored.
   */
  public MobileNetworkBuilder show(@Nullable Boolean show) {
    mobile = show == null ? null : show ? "show" : "";
    return this;
  }

  public MobileNetworkBuilder dataType(@Nullable DataType dataType) {
    // https://android.googlesource.com/platform/frameworks/base/+/0f0de13c37082f9443e3f0c8cc413188ec66d3fe%5E%21/#F12
    if (SDK_INT < 26 && roam != null) {
      throw new IllegalStateException("dataType and roam cannot both be specified on SDK levels <26. Remove the roam parameter first.");
    }
    datatype = dataType == null ? null : dataType.name;
    return this;
  }

  // https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java#1341
  // Slot defaults to 0.
  public MobileNetworkBuilder slot(int slot) {
    this.slot = Integer.toString(slot);
    return this;
  }

  public MobileNetworkBuilder roam(@Nullable Boolean roam) {
    // https://android.googlesource.com/platform/frameworks/base/+/0f0de13c37082f9443e3f0c8cc413188ec66d3fe%5E%21/#F12
    if (SDK_INT < 26) {
      if (datatype != null) {
        throw new IllegalStateException("dataType and roam cannot both be specified on SDK levels" +
            " <26. Remove the dataType parameter first.");
      }
      if (roam == FALSE) {
        throw new IllegalArgumentException("roam cannot be false on SDK levels <26.");
      }
    }
    this.roam = roam == null ? null : roam ? "show" : "";
    return this;
  }

  // https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java#1383
  // There appears to be an off-by-one error (missing the - 1) in SystemUI. There are only five
  // levels and the disconnected state, so limit the level here.
  /**
   * @param level -1 for disconnected network state.
   */
  public MobileNetworkBuilder level(@IntRange(from = -1, to = 4) @Nullable Integer level) {
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

  public MobileNetworkBuilder inflate(@Nullable Boolean inflate) {
    this.inflate = inflate == null ? null : inflate ? "true" : "false";
    return this;
  }

  // https://android.googlesource.com/platform/frameworks/base/+/5c88ffb5072b96662b34cb8139151707f424318a%5E%21/#F9
  @RequiresApi(26)
  public MobileNetworkBuilder activity(@Nullable DataActivity activity) {
    if (SDK_INT < 26) {
      throw new IllegalStateException("activity cannot be specified on SDK levels <26.");
    }
    this.activity = activity == null ? null : activity.name;
    return this;
  }

  @Override public Intent build() {
    Intent result = super.build()
        .putExtra("mobile", mobile);
    if (SDK_INT >= 26) {
      result.putExtra("datatype", datatype);
    } else {
      if (roam != null) {
        // Roam is never false.
        if (datatype != null || roam.length() == 0) {
          throw new AssertionError();
        }
        result.putExtra("datatype", "roam");
      }
    }
    // https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java#1341
    // Slot defaults to 0.
    if (slot == null) {
      result.putExtra("slot", "0");
    } else {
      result.putExtra("slot", slot);
    }
    if (SDK_INT >= 26) {
      // https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java#1376
      // containsKey check.
      if (roam != null) {
        result.putExtra("roam", roam);
      }
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
