package com.nightlynexus.demomode;

import android.content.Intent;
import androidx.annotation.Nullable;

public interface DemoModeInitializer {
  /**
   * The setting in the system settings for enabling Demo Mode.
   */
  enum DemoModeSetting {
    ENABLED,
    DISABLED,
    DISABLED_NEVER_SET
  }

  /**
   * The result of an attempt to grant a system permission.
   * <p>FAILURE is normally the result of a user denying "su" access to the process.
   * <p>SU_NOT_FOUND means that the "su" binary was not found; the user may not be rooted and cannot
   * grant the app the permission from the device.
   */
  enum GrantPermissionResult {
    SUCCESS,
    FAILURE,
    SU_NOT_FOUND
  }

  /**
   * Get the Intent for going to the Demo Mode screen in the system settings. This is useful as an
   * alternative to {@link #setDemoModeSetting} (which requires the WRITE_SECURE_SETTINGS
   * permission) if the Demo Mode setting is disabled.
   *
   * @return The Intent for going to the Demo Mode screen in the system settings or null if no known
   * Intent exists.
   */
  @Nullable Intent demoModeSystemSettingsScreenIntent();

  /**
   * Gets the current setting for Demo Mode in the system settings. The Demo Mode setting must be
   * enabled before Demo Mode can be turned on.
   */
  DemoModeSetting getDemoModeSetting();

  /**
   * Sets the Demo Mode setting in the system settings. The Demo Mode setting must be enabled before
   * Demo Mode can be turned on.
   */
  void setDemoModeSetting(DemoModeSetting setting);

  /**
   * Check for the android.permission.WRITE_SECURE_SETTINGS permission that is required to toggle
   * Demo Mode on and off in the system settings.
   */
  boolean hasWriteSecureSettingsPermission();

  /**
   * Attempts to grants the android.permission.WRITE_SECURE_SETTINGS permission that is required to
   * toggle Demo Mode on and off in the system settings, using a blocking call to "su."
   * <p>Remember to declare the android.permission.WRITE_SECURE_SETTINGS permission in the manifest.
   */
  GrantPermissionResult grantWriteSecureSettingsPermission();

  /**
   * Check for the android.permission.DUMP permission that is required for the Demo Mode broadcasts
   * to have an effect.
   */
  boolean hasDumpPermission();

  /**
   * Attempts to grant the android.permission.DUMP permission that is required for the Demo Mode
   * broadcasts to have an effect, using a blocking call to "su."
   * <p>Remember to declare the android.permission.DUMP permission in the manifest.
   */
  GrantPermissionResult grantDumpPermission();
}
