package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

public final class SystemIconsBuilder {
  public enum ZenMode {
    IMPORTANT("important"),
    NONE("none"),
    HIDE("");

    final String name;

    ZenMode(String name) {
      this.name = name;
    }
  }

  public enum BluetoothMode {
    CONNECTED("connected"),
    DISCONNECTED("disconnected"),
    HIDE("");

    final String name;

    BluetoothMode(String name) {
      this.name = name;
    }
  }

  private String volume;
  private String zen;
  private String bluetooth;
  private String location;
  private String alarm;
  private String tty;
  private String mute;
  private String speakerphone;
  private String cast;
  private String hotspot;

  public SystemIconsBuilder vibrate(@Nullable Boolean vibrate) {
    volume = vibrate == null ? null : vibrate ? "vibrate" : "";
    return this;
  }

  public SystemIconsBuilder zen(@Nullable ZenMode zen) {
    this.zen = zen == null ? null : zen.name;
    return this;
  }

  public SystemIconsBuilder bluetooth(@Nullable BluetoothMode bluetooth) {
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

  public Intent build() {
    Bundle extras = new Bundle(10);
    extras.putString("volume", volume);
    extras.putString("zen", zen);
    extras.putString("bluetooth", bluetooth);
    extras.putString("location", location);
    extras.putString("alarm", alarm);
    extras.putString("tty", tty);
    extras.putString("mute", mute);
    extras.putString("speakerphone", speakerphone);
    extras.putString("cast", cast);
    extras.putString("hotspot", hotspot);
    return DemoMode.build("status", extras);
  }
}
