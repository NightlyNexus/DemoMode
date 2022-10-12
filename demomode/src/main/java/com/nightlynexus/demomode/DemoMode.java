package com.nightlynexus.demomode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION.SDK_INT;

public final class DemoMode {
  private static final String ACTION_DEMO_MODE = "com.android.systemui.demo";

  /**
   * Creates a DemoModeInitializer instance for help granting permissions to use Demo Mode.
   * Requires API level 23 or higher.
   */
  @RequiresApi(23) public static DemoModeInitializer initializer(Context context) {
    if (SDK_INT < 23) {
      throw new AssertionError("Demo mode not available before Marshmallow.");
    }
    return new RealDemoModeInitializer(context);
  }

  /**
   * @return The Intent to broadcast for simply entering Demo Mode. This is unnecessary with other
   * broadcasts, as any other Demo Mode broadcast will implicitly enter Demo Mode.
   */
  public static Intent buildEnter() {
    return build("enter", null);
  }

  /**
   * @return The Intent to broadcast for exiting Demo Mode.
   */
  public static Intent buildExit() {
    return build("exit", null);
  }

  static Intent build(String command, @Nullable Bundle extras) {
    Intent intent = new Intent(ACTION_DEMO_MODE);
    if (extras != null) {
      intent.putExtras(extras); // Copies Bundle contents.
    }
    intent.putExtra("command", command);
    return intent;
  }

  private DemoMode() {
    throw new AssertionError("No instances.");
  }
}
