package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

public final class NotificationsBuilder {
  private String visible;

  public NotificationsBuilder visible(@Nullable Boolean visible) {
    this.visible = visible == null ? null : visible ? "true" : "false";
    return this;
  }

  public Intent build() {
    Bundle extras = new Bundle(1);
    extras.putString("visible", visible);
    return DemoMode.build("notifications", extras);
  }
}
