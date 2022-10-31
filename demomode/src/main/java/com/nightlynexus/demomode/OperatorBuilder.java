package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION.SDK_INT;
import static com.nightlynexus.demomode.DemoMode.putString;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/statusbar/OperatorNameViewController.java#170
// Added in 28 (https://android.googlesource.com/platform/frameworks/base/+/21d1bf1eaf9aca894e86d38d59ebd797d0649a56%5E%21/).
// Removed in 33 (https://android.googlesource.com/platform/frameworks/base/+/cbf3513180833629d15524c457f3bdf94b1abe85%5E%21/).
// This might have been removed accidentally in 33. Check back later.
@RequiresApi(28)
public final class OperatorBuilder {
  String name;

  public OperatorBuilder() {
    if (SDK_INT < 28 || SDK_INT >= 33) {
      throw new IllegalArgumentException("The operator can only be set on SDK levels [28, 32].");
    }
  }

  public OperatorBuilder name(@Nullable String name) {
    this.name = name;
    return this;
  }

  public @Nullable Intent build() {
    Bundle extras = new Bundle(1);
    putString(extras, "name", name);
    return DemoMode.build("operator", extras);
  }
}
