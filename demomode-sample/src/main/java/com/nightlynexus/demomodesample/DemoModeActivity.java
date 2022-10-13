package com.nightlynexus.demomodesample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.nightlynexus.demomode.BatteryBuilder;
import com.nightlynexus.demomode.ClockBuilder;
import com.nightlynexus.demomode.DemoMode;
import com.nightlynexus.demomode.DemoModeInitializer;
import com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult;
import com.nightlynexus.demomode.NetworkBuilder;
import com.nightlynexus.demomode.SystemIconsBuilder;
import com.nightlynexus.demomode.WifiBuilder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.os.Build.VERSION.SDK_INT;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.nightlynexus.demomode.DemoModeInitializer.DemoModeSetting.ENABLED;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.FAILURE;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.SUCCESS;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.SU_NOT_FOUND;
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
    DemoModeInitializer demoModeInitializer = DemoMode.initializer(this);
    grantPermissions.setOnClickListener(v -> {
      boolean hasWriteSystemSettings = demoModeInitializer.hasWriteSecureSettingsPermission();
      boolean hasDump = demoModeInitializer.hasDumpPermission();
      if (hasWriteSystemSettings && hasDump) {
        grantPermissionsSuccess();
      } else {
        suPermissionExecutor.execute(() -> {
          GrantPermissionResult writeSecureSettingsResult;
          if (!hasWriteSystemSettings) {
            writeSecureSettingsResult = demoModeInitializer.grantWriteSecureSettingsPermission();
          } else {
            writeSecureSettingsResult = SUCCESS;
          }
          GrantPermissionResult dumpResult;
          if (!hasDump) {
            dumpResult = demoModeInitializer.grantDumpPermission();
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
      if (demoModeInitializer.getDemoModeSetting() != ENABLED) {
        if (demoModeInitializer.hasWriteSecureSettingsPermission()) {
          demoModeInitializer.setDemoModeSetting(ENABLED);
          needsWriteSystemSettingsPermission = false;
        } else {
          needsWriteSystemSettingsPermission = true;
        }
      } else {
        needsWriteSystemSettingsPermission = false;
      }
      boolean needsDumpPermission = !demoModeInitializer.hasDumpPermission();
      if (needsWriteSystemSettingsPermission || needsDumpPermission) {
        suPermissionExecutor.execute(() -> {
          GrantPermissionResult writeSecureSettingsResult;
          if (needsWriteSystemSettingsPermission) {
            writeSecureSettingsResult = demoModeInitializer.grantWriteSecureSettingsPermission();
          } else {
            writeSecureSettingsResult = SUCCESS;
          }
          GrantPermissionResult dumpResult;
          if (needsDumpPermission) {
            dumpResult = demoModeInitializer.grantDumpPermission();
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
      if (demoModeInitializer.hasDumpPermission()) {
        exitSuccess();
      } else {
        suPermissionExecutor.execute(() -> {
          GrantPermissionResult dumpResult = demoModeInitializer.grantDumpPermission();

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

  void enterSuccess() {
    sendBroadcast(new ClockBuilder().setTimeInHoursAndMinutes("1200").build());
    sendBroadcast(new SystemIconsBuilder().cast(TRUE)
        .zen(SystemIconsBuilder.ZenMode.IMPORTANT)
        .mute(TRUE)
        .speakerphone(TRUE)
        .tty(TRUE)
        .build());
    sendBroadcast(new BatteryBuilder().level(100).plugged(FALSE).build());
    sendBroadcast(new WifiBuilder().wifi(TRUE, 0).build());
    sendBroadcast(new NetworkBuilder().mobile(true, NetworkBuilder.Datatype.LTE_PLUS, 0, 4)
        .build());
  }

  void exitSuccess() {
    sendBroadcast(DemoMode.buildExit());
  }
}
