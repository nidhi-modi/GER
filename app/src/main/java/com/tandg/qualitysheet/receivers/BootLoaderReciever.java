package com.tandg.qualitysheet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tandg.qualitysheet.services.MyBackgroundService;
import com.tandg.qualitysheet.utils.CommonUtility;


/**
 * Created by root on 3/1/18.
 */

public class BootLoaderReciever extends BroadcastReceiver {

    private static final String TAG = BootLoaderReciever.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {



        context.startService( new Intent( context, MyBackgroundService.class ) );
        // context.startService( new Intent(context,ScreenBlockService.class) );
        CommonUtility.setAlarmAtPartucalarTime(context);


    }
}
