package com.nightlynexus.demomode;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.annotation.WorkerThread;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION.SDK_INT;
import static com.nightlynexus.demomode.DemoModePermissions.GrantPermissionResult.FAILURE;
import static com.nightlynexus.demomode.DemoModePermissions.GrantPermissionResult.SUCCESS;
import static com.nightlynexus.demomode.DemoModePermissions.GrantPermissionResult.SU_NOT_FOUND;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.nio.charset.StandardCharsets.UTF_8;

@RequiresApi(23)
public final class DemoModePermissions {
  static final String DEMO_MODE_ALLOWED = "sysui_demo_allowed";
  static final String DEMO_MODE_ON = "sysui_tuner_demo_on";
  static final String PERMISSION_WRITE_SECURE_SETTINGS =
      "android.permission.WRITE_SECURE_SETTINGS";
  static final String PERMISSION_DUMP = "android.permission.DUMP";

  /**
   * The result of an attempt to grant a system permission.
   * <p>FAILURE is normally the result of a user denying "su" access to the process.
   * <p>SU_NOT_FOUND means that the "su" binary was not found; the user may not be rooted and cannot
   * grant the app the permission from the device.
   */
  public enum GrantPermissionResult {
    SUCCESS,
    FAILURE,
    SU_NOT_FOUND
  }

  /**
   * Get the Intent for going to the Demo Mode screen in the system settings. This is useful as an
   * alternative to {@link #setDemoModeSystemSettingEnabled(Context, Boolean)} (which requires the
   * WRITE_SECURE_SETTINGS permission) if the Demo Mode setting is disabled.
   *
   * @return The Intent for going to the Demo Mode screen in the system settings or null if no known
   * Intent exists.
   */
  @Nullable public static Intent demoModeSystemSettingsScreenIntent(Context context) {
    Intent demoMode = new Intent("com.android.settings.action.DEMO_MODE");
    if (hasHandler(context, demoMode)) {
      return demoMode;
    }
    demoMode.setAction("com.android.settings.action.EXTRA_SETTINGS");
    demoMode.setPackage("com.android.systemui");
    if (hasHandler(context, demoMode)) {
      return demoMode;
    }
    return null;
  }

  /**
   * Get the current setting for Demo Mode in the system settings. The Demo Mode setting must be
   * enabled before Demo Mode can be turned on with a broadcast.
   *
   * @return null if and only if the Demo Mode system setting has never been set (effectively
   * disabled).
   */
  @WorkerThread
  @Nullable public static Boolean isDemoModeSystemSettingEnabled(Context context) {
    ContentResolver resolver = context.getContentResolver();
    String setting = Settings.Global.getString(resolver, DEMO_MODE_ALLOWED);
    return setting == null ? null : setting.equals("0") ? FALSE : TRUE;
  }

  /**
   * Set the Demo Mode setting in the system settings. The Demo Mode setting must be enabled before
   * Demo Mode can be turned on.
   *
   * @param enabled null to set the Demo Mode system setting in its unset state (effectively
   *                disabled).
   */
  @RequiresPermission(PERMISSION_WRITE_SECURE_SETTINGS)
  @WorkerThread
  public static void setDemoModeSystemSettingEnabled(Context context, @Nullable Boolean enabled) {
    ContentResolver resolver = context.getContentResolver();
    Settings.Global.putString(resolver, DEMO_MODE_ALLOWED,
        enabled == null ? null : enabled ? "1" : "0");
  }

  /**
   * Check if Demo Mode is on.
   *
   * @return null if and only if Demo Mode has never been turned on.
   */
  @WorkerThread
  @Nullable public static Boolean isDemoModeOn(Context context) {
    ContentResolver resolver = context.getContentResolver();
    String setting = Settings.Global.getString(resolver, DEMO_MODE_ON);
    return setting == null ? null : setting.equals("0") ? FALSE : TRUE;
  }

  /**
   * Set Demo Mode on, off, or in its initial unset state. If the DUMP permission is granted, it's
   * easier to use {@link DemoMode#buildEnter()} and {@link DemoMode#buildExit())} instead.
   *
   * @param on null to set Demo Mode to its initial unset state.
   */
  @RequiresPermission(PERMISSION_WRITE_SECURE_SETTINGS)
  @WorkerThread
  public static void setDemoModeOn(Context context, @Nullable Boolean on) {
    ContentResolver resolver = context.getContentResolver();
    Settings.Global.putString(resolver, DEMO_MODE_ON, on == null ? null : on ? "1" : "0");
  }

  /**
   * Check if the android.permission.WRITE_SECURE_SETTINGS permission that is required to toggle
   * Demo Mode on and off in the system settings is granted.
   */
  public static boolean hasWriteSecureSettingsPermission(Context context) {
    return hasPermission(context, PERMISSION_WRITE_SECURE_SETTINGS);
  }

  /**
   * Attempt to grants the android.permission.WRITE_SECURE_SETTINGS permission that is required to
   * toggle Demo Mode on and off in the system settings, using a blocking call to "su."
   * <p>Remember to declare the android.permission.WRITE_SECURE_SETTINGS permission in the manifest.
   */
  @WorkerThread
  public static GrantPermissionResult grantWriteSecureSettingsPermission(Context context) {
    return grantPermission(context, PERMISSION_WRITE_SECURE_SETTINGS);
  }

  /**
   * Check if the android.permission.DUMP permission that is required for the Demo Mode broadcasts
   * to have an effect is granted.
   */
  public static boolean hasDumpPermission(Context context) {
    return hasPermission(context, PERMISSION_DUMP);
  }

  /**
   * Attempt to grant the android.permission.DUMP permission that is required for the Demo Mode
   * broadcasts to have an effect, using a blocking call to "su."
   * <p>Remember to declare the android.permission.DUMP permission in the manifest.
   */
  @WorkerThread
  public static GrantPermissionResult grantDumpPermission(Context context) {
    return grantPermission(context, PERMISSION_DUMP);
  }

  static boolean hasPermission(Context context, String permission) {
    return context.checkSelfPermission(permission) == PERMISSION_GRANTED;
  }

  @WorkerThread
  static GrantPermissionResult grantPermission(Context context, String permission) {
    if (hasPermission(context, permission)) {
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
    return hasPermission(context, permission) ? SUCCESS : FAILURE;
  }

  @SuppressWarnings("deprecation")
  static boolean hasHandler(Context context, Intent intent) {
    PackageManager packageManager = context.getPackageManager();
    if (SDK_INT > 33) {
      return !packageManager.queryIntentActivities(
          intent, PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL)).isEmpty();
    }
    return !packageManager.queryIntentActivities(
        intent, PackageManager.MATCH_ALL).isEmpty();
  }

  private DemoModePermissions() {
    throw new AssertionError("No instances.");
  }
}
