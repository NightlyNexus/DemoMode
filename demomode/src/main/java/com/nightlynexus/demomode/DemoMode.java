package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

// https://android.googlesource.com/platform/frameworks/base/+/1291b83a2fb8ae8a095d50730f75013151f6ce3f/packages/SystemUI/src/com/android/systemui/demomode/DemoMode.java
@RequiresApi(23)
public final class DemoMode {
  static final String ACTION_DEMO_MODE = "com.android.systemui.demo";

  /**
   * @return The Intent to broadcast for simply entering Demo Mode. This is unnecessary with other
   * broadcasts, as any other Demo Mode broadcast will implicitly enter Demo Mode.
   */
  public static Intent buildEnter() {
    return new Intent(ACTION_DEMO_MODE).putExtra("command", "enter");
  }

  /**
   * @return The Intent to broadcast for exiting Demo Mode.
   */
  public static Intent buildExit() {
    return new Intent(ACTION_DEMO_MODE).putExtra("command", "exit");
  }

  static @Nullable Intent build(String command, Bundle extras) {
    if (extras.isEmpty()) {
      return null;
    }
    return new Intent(ACTION_DEMO_MODE)
        .putExtras(extras)
        .putExtra("command", command);
  }

  static void putStringIfNotNull(Bundle bundle, String key, @Nullable String value) {
    if (value != null) {
      bundle.putString(key, value);
    }
  }

  private DemoMode() {
    throw new AssertionError("No instances.");
  }
}
