package com.tandg.qualitysheet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tandg.qualitysheet.services.MyBackgroundService;
import com.tandg.qualitysheet.utils.ApplicationUtils;


/**
 * Created by root on 8/1/18.
 */

public class SchedulerSetupReceiver extends BroadcastReceiver {


    private static final String TAG = SchedulerSetupReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, "SchedulerSetupReceiver onReceive() is called");

        ApplicationUtils.scheduleAlarm(context);

        Intent background = new Intent(context, MyBackgroundService.class);
        context.startService(background);


       /* AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class); // explicit
        // intent
        PendingIntent intentExecuted = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 1);
        now.set(Calendar.MINUTE, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                now.getTimeInMillis(), EXEC_INTERVAL, intentExecuted);*/
    }
}
