package com.nightlynexus.demomode;

import android.os.Bundle;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION.SDK_INT;
import static com.nightlynexus.demomode.DemoMode.putStringIfNotNull;

// https://android.googlesource.com/platform/frameworks/base/+/332641fc24cb79a58e658a25d5963f3059d66837/packages/SystemUI/src/com/android/systemui/statusbar/pipeline/satellite/data/demo/DemoDeviceBasedSatelliteDataSource.kt#52
@RequiresApi(35)
public final class SatelliteNetworkBuilder extends NetworkBuilder {
  public enum Connection {
    UNKNOWN("Unknown"),
    OFF("Off"),
    ON("On"),
    CONNECTED("Connected");

    final @Nullable String name;

    Connection(@Nullable String name) {
      this.name = name;
    }
  }

  String satellite;
  Connection connection; // Required.
  String level; // Required.

  public SatelliteNetworkBuilder() {
    if (SDK_INT < 35) {
      throw new IllegalArgumentException("The satellite can only be set on SDK levels >=35.");
    }
  }

  /**
   * @param show false will cause all other parameters to be ignored.
   */
  public SatelliteNetworkBuilder show(boolean show) {
    // The unset input is the same as the hide input.
    // https://android.googlesource.com/platform/frameworks/base/+/332641fc24cb79a58e658a25d5963f3059d66837/packages/SystemUI/src/com/android/systemui/statusbar/pipeline/satellite/data/demo/DemoDeviceBasedSatelliteDataSource.kt#52
    satellite = show ? "show" : null;
    return this;
  }

  public SatelliteNetworkBuilder connection(Connection connection) {
    if (connection == null) {
      throw new NullPointerException("connection == null");
    }
    this.connection = connection;
    return this;
  }

  public SatelliteNetworkBuilder level(@IntRange(from = 0, to = 4) int level) {
    switch (level) {
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
        throw new IllegalArgumentException("level must be [0, 4]. Actual: " + level);
    }
    return this;
  }

  @Override void addExtras(Bundle extras) {
    if (satellite == null) {
      // Nothing here will have an effect.
      return;
    }
    if (connection == null) {
      throw new IllegalStateException(
          "Missing required connection. Consider using Connection.Unknown.");
    }
    if (level == null) {
      throw new IllegalStateException("Missing required level. Consider using 0.");
    }
    putStringIfNotNull(extras, "satellite", satellite);
    putStringIfNotNull(extras, "connection", connection.name);
    putStringIfNotNull(extras, "level", level);
  }
}
