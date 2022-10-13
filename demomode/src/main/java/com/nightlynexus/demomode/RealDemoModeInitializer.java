package com.nightlynexus.demomode;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION.SDK_INT;
import static com.nightlynexus.demomode.DemoModeInitializer.DemoModeSetting.DISABLED;
import static com.nightlynexus.demomode.DemoModeInitializer.DemoModeSetting.DISABLED_NEVER_SET;
import static com.nightlynexus.demomode.DemoModeInitializer.DemoModeSetting.ENABLED;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.FAILURE;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.SUCCESS;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.SU_NOT_FOUND;
import static java.nio.charset.StandardCharsets.UTF_8;

@RequiresApi(23)
final class RealDemoModeInitializer implements DemoModeInitializer {
  static final String SYSTEMUI_DEMO_ALLOWED = "sysui_demo_allowed";
  static final String PERMISSION_WRITE_SECURE_SETTINGS =
      "android.permission.WRITE_SECURE_SETTINGS";
  static final String PERMISSION_DUMP = "android.permission.DUMP";

  final Context context;

  RealDemoModeInitializer(Context context) {
    this.context = context;
  }

  @Nullable @Override public Intent demoModeSystemSettingsScreenIntent() {
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

  @Override public DemoModeSetting getDemoModeSetting() {
    ContentResolver resolver = context.getContentResolver();
    String setting = Settings.Global.getString(resolver, SYSTEMUI_DEMO_ALLOWED);
    if (setting == null) {
      return DISABLED_NEVER_SET;
    }
    return setting.equals("0") ? DISABLED : ENABLED;
  }

  @RequiresPermission(PERMISSION_WRITE_SECURE_SETTINGS)
  @Override public void setDemoModeSetting(DemoModeSetting setting) {
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
  }

  @Override public boolean hasWriteSecureSettingsPermission() {
    return hasPermission(PERMISSION_WRITE_SECURE_SETTINGS);
  }

  @Override public GrantPermissionResult grantWriteSecureSettingsPermission() {
    return grantPermission(PERMISSION_WRITE_SECURE_SETTINGS);
  }

  @Override public boolean hasDumpPermission() {
    return hasPermission(PERMISSION_DUMP);
  }

  @Override public GrantPermissionResult grantDumpPermission() {
    return grantPermission(PERMISSION_DUMP);
  }

  boolean hasPermission(String permission) {
    return context.checkSelfPermission(permission) == PERMISSION_GRANTED;
  }

  GrantPermissionResult grantPermission(String permission) {
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
      outputStream.write(command.getBytes(UTF_8));
      outputStream.flush();
      outputStream.close();
      process.waitFor();
    } catch (IOException | InterruptedException ignored) {
    }
    process.destroy();
    return hasPermission(permission) ? SUCCESS : FAILURE;
  }

  @SuppressWarnings("deprecation")
  boolean hasHandler(Intent intent) {
    PackageManager packageManager = context.getPackageManager();
    if (SDK_INT > 33) {
      return !packageManager.queryIntentActivities(
          intent, PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL)).isEmpty();
    }
    return !packageManager.queryIntentActivities(
        intent, PackageManager.MATCH_ALL).isEmpty();
  }
}
