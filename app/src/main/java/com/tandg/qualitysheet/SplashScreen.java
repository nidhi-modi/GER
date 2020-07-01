package com.tandg.qualitysheet;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.tandg.qualitysheet.database.dataSource.QualityInfoDataSource;
import com.tandg.qualitysheet.qualitySheetActivity.QualitySheetActivity;
import com.tandg.qualitysheet.receivers.AlarmReceiver;
import com.tandg.qualitysheet.utils.AppContext;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = SplashScreen.class.getSimpleName();

    Handler                      handler;
    QualityInfoDataSource        qualityInfoDataSource;
    private static final int STORAGE_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initStartReceiver();

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                checkPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        STORAGE_PERMISSION_CODE);

                //navigateToQualitySheet();


            }
        },1000);
    }

    private void checkPermission(String writeExternalStorage, int storagePermissionCode) {

        if (ContextCompat.checkSelfPermission(SplashScreen.this, writeExternalStorage)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[] { writeExternalStorage },
                    storagePermissionCode);
        }
        else {
            navigateToQualitySheet();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

       if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                navigateToQualitySheet();

            }
            else {
                Log.e(TAG, "onRequestPermissionsResult: Denied " );
            }
        }
    }

    private void initStartReceiver() {

        qualityInfoDataSource = new QualityInfoDataSource(this);

        qualityInfoDataSource.open();

        int count = qualityInfoDataSource.isMasterEmpty();

        qualityInfoDataSource.close();

        if(count > 0) {

            scheduleAlarm();

            Log.e(TAG, "initResources: alarm started " );

        }

    }


    private void navigateToQualitySheet(){

        Intent intent=new Intent(getApplicationContext(), QualitySheetActivity.class);
        startActivity(intent);
        finish();

    }


    public void scheduleAlarm() {

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)!= null);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                30 * 1000, pIntent);

        //fire an alarm after every 30 sec

    }


}
