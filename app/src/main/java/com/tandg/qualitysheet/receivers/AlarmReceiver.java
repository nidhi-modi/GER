package com.tandg.qualitysheet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.tandg.qualitysheet.services.MyBackgroundService;


/**
 * Created by root on 8/6/20.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    public static final int REQUEST_CODE = 12345;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "AlarmReceiver onReceiver is attached");

            Intent background = new Intent(context, MyBackgroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(background);
        } else {
            context.startService(background);
        }




    }

}
