package com.nightlynexus.demomodesample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
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

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.M;
import static com.nightlynexus.demomode.DemoModeInitializer.DemoModeSetting.ENABLED;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.FAILURE;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.SUCCESS;
import static com.nightlynexus.demomode.DemoModeInitializer.GrantPermissionResult.SU_NOT_FOUND;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public final class DemoModeActivity extends Activity {
  @SuppressLint("NewApi") @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.demo_mode);
    View grantPermissions = findViewById(R.id.grant_permissions);
    View enter = findViewById(R.id.enter);
    View exit = findViewById(R.id.exit);
    if (SDK_INT < M) {
      Toast.makeText(this, R.string.no_op, Toast.LENGTH_LONG).show();
      return;
    }
    final DemoModeInitializer demoModeInitializer = DemoMode.initializer(this);
    grantPermissions.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        grantWriteSettingsAndDumpPermissions(demoModeInitializer, new Runnable() {
          @Override public void run() {
            Toast.makeText(DemoModeActivity.this, R.string.grant_permissions_success,
                Toast.LENGTH_SHORT).show();
          }
        });
      }
    });
    enter.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        grantWriteSettingsAndDumpPermissions(demoModeInitializer, new Runnable() {
          @Override public void run() {
            sendBroadcast(new ClockBuilder().setTimeInHoursAndMinutes("1200").build());
            sendBroadcast(new SystemIconsBuilder().cast(TRUE)
                .zen(SystemIconsBuilder.ZenMode.IMPORTANT)
                .mute(TRUE)
                .speakerphone(TRUE)
                .tty(TRUE)
                .build());
            sendBroadcast(new BatteryBuilder().level(100).plugged(FALSE).build());
            sendBroadcast(new WifiBuilder().wifi(TRUE, 0).build());
            sendBroadcast(
                new NetworkBuilder().mobile(true, NetworkBuilder.Datatype.LTE_PLUS, 0, 4).build());
          }
        });
      }
    });
    exit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        grantDumpPermission(demoModeInitializer, new Runnable() {
          @Override public void run() {
            sendBroadcast(DemoMode.buildExit());
          }
        });
      }
    });
  }

  void grantWriteSettingsAndDumpPermissions(final DemoModeInitializer demoModeInitializer,
      final Runnable onSuccess) {
    new AsyncTask<Void, Void, GrantPermissionResult>() {
      @Override protected GrantPermissionResult doInBackground(Void... params) {
        GrantPermissionResult demoModeSettingResult =
            demoModeInitializer.setDemoModeSetting(ENABLED);
        GrantPermissionResult broadcastPermissionResult =
            demoModeInitializer.grantBroadcastPermission();
        if (demoModeSettingResult == SU_NOT_FOUND || broadcastPermissionResult == SU_NOT_FOUND) {
          return SU_NOT_FOUND;
        }
        if (demoModeSettingResult == FAILURE || broadcastPermissionResult == FAILURE) {
          return FAILURE;
        }
        return SUCCESS;
      }

      @Override protected void onPostExecute(GrantPermissionResult result) {
        if (result == SU_NOT_FOUND) {
          Toast.makeText(DemoModeActivity.this, R.string.su_not_found, Toast.LENGTH_LONG).show();
          return;
        }
        if (result == FAILURE) {
          Toast.makeText(DemoModeActivity.this, R.string.grant_permissions_failure,
              Toast.LENGTH_LONG).show();
          return;
        }
        onSuccess.run();
      }
    }.execute();
  }

  void grantDumpPermission(final DemoModeInitializer demoModeInitializer,
      final Runnable onSuccess) {
    new AsyncTask<Void, Void, GrantPermissionResult>() {
      @Override protected GrantPermissionResult doInBackground(Void... params) {
        return demoModeInitializer.grantBroadcastPermission();
      }

      @Override protected void onPostExecute(GrantPermissionResult result) {
        if (result == SU_NOT_FOUND) {
          Toast.makeText(DemoModeActivity.this, R.string.su_not_found, Toast.LENGTH_LONG).show();
          return;
        }
        if (result == FAILURE) {
          Toast.makeText(DemoModeActivity.this, R.string.grant_permissions_failure,
              Toast.LENGTH_LONG).show();
          return;
        }
        onSuccess.run();
      }
    }.execute();
  }
}
