package com.nightlynexus.demomode;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.M;
import static com.nightlynexus.demomode.DemoModeInitializer.DemoModeSetting.DISABLED;
import static com.nightlynexus.demomode.DemoModeInitializer.DemoModeSetting.DISABLED_NEVER_SET;
import static com.nightlynexus.demomode.DemoModeInitializer.DemoModeSetting.ENABLED;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.FAILURE;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.SUCCESS;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.SU_NOT_FOUND;

final class RealDemoModeInitializer implements DemoModeInitializer {
  private static final String SYSTEMUI_DEMO_ALLOWED = "sysui_demo_allowed";
  private static final String PERMISSION_WRITE_SECURE_SETTINGS =
      "android.permission.WRITE_SECURE_SETTINGS";
  private static final String PERMISSION_DUMP = "android.permission.DUMP";

  private final Context context;

  RealDemoModeInitializer(Context context) {
    this.context = context;
  }

  @Nullable @Override public Intent demoModeScreenIntent() {
    Intent demoMode = new Intent("com.android.settings.action.DEMO_MODE");
    if (hasHandler(demoMode)) {
      return demoMode;
    }
    demoMode.setAction("com.android.settings.action.EXTRA_SETTINGS");
    demoMode.setPackage("com.android.systemui");
    if (hasHandler(demoMode)) {
      return demoMode;
    }
    return null;
  }

  @TargetApi(JELLY_BEAN_MR1) @Override public DemoModeSetting getDemoModeSetting() {
    ContentResolver resolver = context.getContentResolver();
    String setting = Settings.Global.getString(resolver, SYSTEMUI_DEMO_ALLOWED);
    if (setting == null) {
      return DISABLED_NEVER_SET;
    }
    return setting.equals("0") ? DISABLED : ENABLED;
  }

  @TargetApi(JELLY_BEAN_MR1) @Override
  public GrantPermissionResult setDemoModeSetting(DemoModeSetting setting) {
    if (setting == getDemoModeSetting()) {
      return SUCCESS;
    }
    GrantPermissionResult result = grantPermission(PERMISSION_WRITE_SECURE_SETTINGS);
    if (result != SUCCESS) {
      return result;
    }
    String value;
    switch (setting) {
      case ENABLED:
        value = "1";
        break;
      case DISABLED:
        value = "0";
        break;
      case DISABLED_NEVER_SET:
        value = null;
        break;
      default:
        throw new AssertionError("No DemoModeSetting type: " + setting);
    }
    ContentResolver resolver = context.getContentResolver();
    Settings.Global.putString(resolver, SYSTEMUI_DEMO_ALLOWED, value);
    return SUCCESS;
  }

  @Override public boolean hasBroadcastPermission() {
    return hasPermission(PERMISSION_DUMP);
  }

  @Override public GrantPermissionResult grantBroadcastPermission() {
    return grantPermission(PERMISSION_DUMP);
  }

  @TargetApi(M) private boolean hasPermission(String permission) {
    return context.checkSelfPermission(permission) == PERMISSION_GRANTED;
  }

  private GrantPermissionResult grantPermission(String permission) {
    if (hasPermission(permission)) {
      return SUCCESS;
    }
    Process process;
    try {
      process = Runtime.getRuntime().exec("su");
    } catch (IOException e) {
      return SU_NOT_FOUND;
    }
    OutputStream outputStream = process.getOutputStream();
    String packageName = context.getPackageName();
    String command =
        new StringBuilder(10 + packageName.length() + permission.length()).append("pm grant ")
            .append(packageName)
            .append(" ")
            .append(permission)
            .toString();
    try {
      outputStream.write(command.getBytes());
      outputStream.flush();
      outputStream.close();
      process.waitFor();
    } catch (IOException | InterruptedException ignored) {
    }
    process.destroy();
    return hasPermission(permission) ? SUCCESS : FAILURE;
  }

  private boolean hasHandler(Intent intent) {
    return !context.getPackageManager().queryIntentActivities(intent, 0).isEmpty();
  }
}
