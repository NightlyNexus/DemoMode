package com.nightlynexus.demomode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION.SDK_INT;
import static com.nightlynexus.demomode.DemoMode.putStringIfNotNull;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/phone/DemoStatusIcons.java
// sync, eri, managed_profile, and secure are mentioned (here https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/tuner/DemoModeFragment.java#39 and here https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/tests/SystemUIDemoModeController/src/com/example/android/demomodecontroller/DemoModeController.java#245) but not implemented.
@RequiresApi(23)
public final class SystemIconsBuilder {
  public enum ZenMode {
    /** Max API 29. **/
    IMPORTANT("important"),
    /** Max API 29. **/
    NONE("none"),
    @RequiresApi(29)
    DND("dnd"),
    HIDE("");

    final String name;

    ZenMode(String name) {
      this.name = name;
    }
  }

  public enum BluetoothMode {
    CONNECTED("connected"),
    /** Max API 29. **/
    DISCONNECTED("disconnected"),
    HIDE("");

    final String name;

    BluetoothMode(String name) {
      this.name = name;
    }
  }

  String volume;
  String zen;
  String bluetooth;
  String location;
  String alarm;
  String tty;
  String mute;
  String speakerphone;
  String cast;
  String hotspot;

  public SystemIconsBuilder vibrate(@Nullable Boolean vibrate) {
    volume = vibrate == null ? null : vibrate ? "vibrate" : "";
    return this;
  }

  // https://android.googlesource.com/platform/frameworks/base/+/ad29e938d4976913a62f9c01713583e5b4c278b2%5E%21/
  // https://android.googlesource.com/platform/frameworks/base/+/833bb0f6044cae0777248579a433f4b5c750b1ab%5E%21/
  @SuppressLint("NewApi") // We check the ZenMode SDK level requirement below.
  public SystemIconsBuilder zen(@Nullable ZenMode zen) {
    if (zen == ZenMode.IMPORTANT || zen == ZenMode.NONE) {
      if (SDK_INT >= 30) {
        throw new IllegalArgumentException(zen + " is unsupported on SDK levels >=30.");
      }
    } else if (zen == ZenMode.DND) {
      if (SDK_INT < 30) {
        throw new IllegalArgumentException(zen + " is unsupported on SDK levels <30.");
      }
    }
    this.zen = zen == null ? null : zen.name;
    return this;
  }

  // https://android.googlesource.com/platform/frameworks/base/+/ad29e938d4976913a62f9c01713583e5b4c278b2%5E%21/
  public SystemIconsBuilder bluetooth(@Nullable BluetoothMode bluetooth) {
    if (bluetooth == BluetoothMode.DISCONNECTED) {
      if (SDK_INT >= 30) {
        throw new IllegalArgumentException(bluetooth + " is unsupported on SDK levels >=30.");
      }
    }
    this.bluetooth = bluetooth == null ? null : bluetooth.name;
    return this;
  }

  public SystemIconsBuilder location(@Nullable Boolean location) {
    this.location = location == null ? null : location ? "show" : "";
    return this;
  }

  public SystemIconsBuilder alarm(@Nullable Boolean alarm) {
    this.alarm = alarm == null ? null : alarm ? "show" : "";
    return this;
  }

  public SystemIconsBuilder tty(@Nullable Boolean tty) {
    this.tty = tty == null ? null : tty ? "show" : "";
    return this;
  }

  public SystemIconsBuilder mute(@Nullable Boolean mute) {
    this.mute = mute == null ? null : mute ? "show" : "";
    return this;
  }

  public SystemIconsBuilder speakerphone(@Nullable Boolean speakerphone) {
    this.speakerphone = speakerphone == null ? null : speakerphone ? "show" : "";
    return this;
  }

  public SystemIconsBuilder cast(@Nullable Boolean cast) {
    this.cast = cast == null ? null : cast ? "show" : "";
    return this;
  }

  public SystemIconsBuilder hotspot(@Nullable Boolean hotspot) {
    this.hotspot = hotspot == null ? null : hotspot ? "show" : "";
    return this;
  }

  public @Nullable Intent build() {
    Bundle extras = new Bundle(10);
    putStringIfNotNull(extras, "volume", volume);
    putStringIfNotNull(extras, "zen", zen);
    putStringIfNotNull(extras, "bluetooth", bluetooth);
    putStringIfNotNull(extras, "location", location);
    putStringIfNotNull(extras, "alarm", alarm);
    putStringIfNotNull(extras, "tty", tty);
    putStringIfNotNull(extras, "mute", mute);
    putStringIfNotNull(extras, "speakerphone", speakerphone);
    putStringIfNotNull(extras, "cast", cast);
    putStringIfNotNull(extras, "hotspot", hotspot);
    return DemoMode.build("status", extras);
  }
}
