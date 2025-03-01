package com.nightlynexus.demomodesample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.nightlynexus.demomode.BatteryBuilder;
import com.nightlynexus.demomode.ClockBuilder;
import com.nightlynexus.demomode.DemoMode;
import com.nightlynexus.demomode.DemoModePermissions;
import com.nightlynexus.demomode.DemoModePermissions.GrantPermissionResult;
import com.nightlynexus.demomode.MobileNetworkBuilder;
import com.nightlynexus.demomode.NetworkBuilder;
import com.nightlynexus.demomode.NotificationsBuilder;
import com.nightlynexus.demomode.SystemIconsBuilder;
import com.nightlynexus.demomode.WifiNetworkBuilder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.os.Build.VERSION.SDK_INT;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.nightlynexus.demomode.DemoModePermissions.GrantPermissionResult.FAILURE;
import static com.nightlynexus.demomode.DemoModePermissions.GrantPermissionResult.SUCCESS;
import static com.nightlynexus.demomode.DemoModePermissions.GrantPermissionResult.SU_NOT_FOUND;
import static com.nightlynexus.demomode.DemoModePermissions.grantDumpPermission;
import static com.nightlynexus.demomode.DemoModePermissions.grantWriteSecureSettingsPermission;
import static com.nightlynexus.demomode.DemoModePermissions.hasDumpPermission;
import static com.nightlynexus.demomode.DemoModePermissions.hasWriteSecureSettingsPermission;
import static com.nightlynexus.demomode.DemoModePermissions.isDemoModeAllowed;
import static com.nightlynexus.demomode.DemoModePermissions.setDemoModeAllowed;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public final class DemoModeActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.demo_mode);
    View grantPermissions = findViewById(R.id.grant_permissions);
    View enter = findViewById(R.id.enter);
    View exit = findViewById(R.id.exit);
    if (SDK_INT < 23) {
      Toast.makeText(this, R.string.no_op, LENGTH_LONG).show();
      return;
    }

    Executor suPermissionExecutor = Executors.newSingleThreadExecutor();
    grantPermissions.setOnClickListener(v -> {
      boolean hasWriteSystemSettings = hasWriteSecureSettingsPermission(this);
      boolean hasDump = hasDumpPermission(this);
      if (hasWriteSystemSettings && hasDump) {
        grantPermissionsSuccess();
      } else {
        suPermissionExecutor.execute(() -> {
          GrantPermissionResult writeSecureSettingsResult;
          if (!hasWriteSystemSettings) {
            writeSecureSettingsResult = grantWriteSecureSettingsPermission(this);
          } else {
            writeSecureSettingsResult = SUCCESS;
          }
          GrantPermissionResult dumpResult;
          if (!hasDump) {
            dumpResult = grantDumpPermission(this);
          } else {
            dumpResult = SUCCESS;
          }

          runOnUiThread(() -> {
            if (writeSecureSettingsResult == SU_NOT_FOUND || dumpResult == SU_NOT_FOUND) {
              Toast.makeText(DemoModeActivity.this, R.string.su_not_found, LENGTH_LONG).show();
              return;
            }
            if (writeSecureSettingsResult == FAILURE || dumpResult == FAILURE) {
              Toast.makeText(DemoModeActivity.this, R.string.grant_permissions_failure,
                  LENGTH_LONG).show();
              return;
            }

            grantPermissionsSuccess();
          });
        });
      }
    });
    enter.setOnClickListener(v -> {
      boolean needsWriteSystemSettingsPermission;
      if (isDemoModeAllowed(this) != TRUE) {
        if (hasWriteSecureSettingsPermission(this)) {
          setDemoModeAllowed(this, TRUE);
          needsWriteSystemSettingsPermission = false;
        } else {
          needsWriteSystemSettingsPermission = true;
        }
      } else {
        needsWriteSystemSettingsPermission = false;
      }
      boolean needsDumpPermission = !hasDumpPermission(this);
      if (needsWriteSystemSettingsPermission || needsDumpPermission) {
        suPermissionExecutor.execute(() -> {
          GrantPermissionResult writeSecureSettingsResult;
          if (needsWriteSystemSettingsPermission) {
            writeSecureSettingsResult = grantWriteSecureSettingsPermission(this);
          } else {
            writeSecureSettingsResult = SUCCESS;
          }
          GrantPermissionResult dumpResult;
          if (needsDumpPermission) {
            dumpResult = grantDumpPermission(this);
          } else {
            dumpResult = SUCCESS;
          }

          runOnUiThread(() -> {
            if (writeSecureSettingsResult == SU_NOT_FOUND || dumpResult == SU_NOT_FOUND) {
              Toast.makeText(DemoModeActivity.this, R.string.su_not_found, LENGTH_LONG).show();
              return;
            }
            if (writeSecureSettingsResult == FAILURE || dumpResult == FAILURE) {
              Toast.makeText(DemoModeActivity.this, R.string.grant_permissions_failure, LENGTH_LONG)
                  .show();
              return;
            }

            enterSuccess();
          });
        });
      } else {
        enterSuccess();
      }
    });
    exit.setOnClickListener(v -> {
      if (hasDumpPermission(this)) {
        exitSuccess();
      } else {
        suPermissionExecutor.execute(() -> {
          GrantPermissionResult dumpResult = grantDumpPermission(this);

          runOnUiThread(() -> {
            if (dumpResult == SU_NOT_FOUND) {
              Toast.makeText(DemoModeActivity.this, R.string.su_not_found, LENGTH_LONG).show();
              return;
            }
            if (dumpResult == FAILURE) {
              Toast.makeText(DemoModeActivity.this, R.string.grant_permissions_failure, LENGTH_LONG)
                  .show();
              return;
            }
            exitSuccess();
          });
        });
      }
    });
  }

  void grantPermissionsSuccess() {
    Toast.makeText(DemoModeActivity.this, R.string.grant_permissions_success, LENGTH_SHORT).show();
  }

  @RequiresApi(23)
  void enterSuccess() {
    DemoModePermissions.setDemoModeOn(this, true);
    sendBroadcast(new ClockBuilder().setTimeInHoursAndMinutes("1200").build());
    sendBroadcast(new SystemIconsBuilder()
        .cast(TRUE)
        .mute(TRUE)
        .speakerphone(TRUE)
        .tty(TRUE)
        .build());
    sendBroadcast(new BatteryBuilder().level(100).plugged(FALSE).build());
    WifiNetworkBuilder wifiNetworkBuilder = new WifiNetworkBuilder()
        .show(TRUE)
        .level(4);
    if (SDK_INT >= 26) {
      wifiNetworkBuilder.activity(NetworkBuilder.DataActivity.INOUT);
    }
    sendBroadcast(wifiNetworkBuilder.build());
    MobileNetworkBuilder mobileNetworkBuilder = new MobileNetworkBuilder().show(TRUE)
        .dataType(MobileNetworkBuilder.DataType.LTE_PLUS)
        .slot(0)
        .level(0);
    if (SDK_INT >= 26) {
      mobileNetworkBuilder.activity(NetworkBuilder.DataActivity.INOUT);
    }
    if (SDK_INT >= 34) {
      mobileNetworkBuilder.networkName(MobileNetworkBuilder.DEFAULT_NETWORK_NAME);
    }
    sendBroadcast(mobileNetworkBuilder.build());
    sendBroadcast(new NotificationsBuilder().visible(FALSE).build());
  }

  @RequiresApi(23)
  void exitSuccess() {
    sendBroadcast(DemoMode.buildExit());
    DemoModePermissions.setDemoModeOn(this, false);
  }
}
