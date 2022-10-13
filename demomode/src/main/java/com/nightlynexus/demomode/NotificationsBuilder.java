package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/phone/NotificationIconAreaController.java#662
@RequiresApi(23)
public final class NotificationsBuilder {
  String visible;

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
