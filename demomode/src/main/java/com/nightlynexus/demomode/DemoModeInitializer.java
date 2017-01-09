package com.nightlynexus.demomode;

import android.content.Intent;
import android.support.annotation.Nullable;

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
   * FAILURE is normally the result of a user denying "su" access to the process.
   * SU_NOT_FOUND means that the "su" binary was not found; the user may not be rooted and can not
   * grant the app the permission from the device.
   */
  enum GrantPermissionResult {
    SUCCESS,
    FAILURE,
    SU_NOT_FOUND
  }

  /**
   * Get the Intent for going to the system Demo Mode screen. This is useful as an alternative to
   * #setDemoModeSetting (requiring the WRITE_SECURE_SETTINGS permission) if the Demo Mode setting
   * is disabled.
   *
   * @return The Intent for going to the Demo Mode screen or null if no known Intent exists.
   */
  @Nullable Intent demoModeScreenIntent();

  /**
   * Gets the current setting for Demo Mode in system settings.
   */
  DemoModeSetting getDemoModeSetting();

  /**
   * Sets the Demo Mode setting in system settings.
   * Demo Mode has to be enabled first before being able to be toggled.
   * This call will attempt to use "su" to grant the WRITE_SECURE_SETTINGS permission to write the
   * setting, if need be. Calls to "su" are blocking.
   */
  GrantPermissionResult setDemoModeSetting(DemoModeSetting setting);

  /**
   * Check for the DUMP permission that is required for the Demo Mode broadcasts to have an effect.
   */
  boolean hasBroadcastPermission();

  /**
   * Grants the DUMP permission that is required for the Demo Mode broadcasts to have an effect,
   * using a call to "su." Calls to "su" are blocking.
   */
  GrantPermissionResult grantBroadcastPermission();
}
