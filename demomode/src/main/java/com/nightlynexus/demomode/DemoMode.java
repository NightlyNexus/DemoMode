package com.nightlynexus.demomode;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(23)
public final class DemoMode {
  static final String ACTION_DEMO_MODE = "com.android.systemui.demo";

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
