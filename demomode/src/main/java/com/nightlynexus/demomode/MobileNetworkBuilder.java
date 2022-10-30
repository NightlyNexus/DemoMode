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
    NO_DATA(null),
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
    ROAM("roam"),
    DIS("dis"),
    NOT("not"),
    HIDE("");

    final @Nullable String name;

    DataType(@Nullable String name) {
      this.name = name;
    }
  }

  String mobile;
  DataType datatype; // Required.
  String slot; // Required.
  String roam;
  String level;
  String inflate;
  DataActivity activity; // Required.

  /**
   * @param show null will cause all other parameters to be ignored.
   */
  public MobileNetworkBuilder show(@Nullable Boolean show) {
    mobile = show == null ? null : show ? "show" : "";
    return this;
  }

  public MobileNetworkBuilder dataType(DataType dataType) {
    if (dataType == null) {
      throw new NullPointerException("dataType == null");
    }
    // https://android.googlesource.com/platform/frameworks/base/+/0f0de13c37082f9443e3f0c8cc413188ec66d3fe%5E%21/#F12
    if (SDK_INT >= 26 && dataType == DataType.ROAM) {
      throw new IllegalArgumentException("roam cannot be set as a data type on SDK levels >=26. " +
          "Use MobileNetworkBuilder.roam.");
    }
    datatype = dataType;
    return this;
  }

  // https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/connectivity/NetworkControllerImpl.java#1341
  // Slot would default to 0, so we require it to be set explicitly.
  public MobileNetworkBuilder slot(@IntRange(from = 0, to = 8) int slot) {
    if (slot < 0 || slot > 8) {
      throw new IllegalArgumentException("slot must [0, 8]. Actual: " + slot);
    }
    this.slot = Integer.toString(slot);
    return this;
  }

  public MobileNetworkBuilder roam(@Nullable Boolean roam) {
    // https://android.googlesource.com/platform/frameworks/base/+/0f0de13c37082f9443e3f0c8cc413188ec66d3fe%5E%21/#F12
    if (SDK_INT < 26) {
      throw new IllegalStateException(
          "roam cannot be set on SDK levels <26. Set roam as a data type.");
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
  public MobileNetworkBuilder activity(DataActivity activity) {
    if (activity == null) {
      throw new NullPointerException("activity == null");
    }
    if (SDK_INT < 26) {
      throw new IllegalStateException("activity cannot be specified on SDK levels <26.");
    }
    this.activity = activity;
    return this;
  }

  @Override public Intent build() {
    if (datatype == null) {
      throw new IllegalStateException("Missing required data type.");
    }
    if (slot == null) {
      throw new IllegalStateException("Missing required slot.");
    }
    if (activity == null) {
      throw new IllegalStateException("Missing required activity.");
    }
    Intent result = super.build()
        .putExtra("mobile", mobile)
        .putExtra("datatype", datatype.name)
        .putExtra("slot", slot);
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
    return result.putExtra("activity", activity.name);
  }
}
