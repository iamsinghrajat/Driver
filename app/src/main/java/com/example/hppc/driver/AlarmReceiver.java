package com.example.hppc.driver;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by hp pc on 20-09-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e("Service started alarm","hh");
        Intent background = new Intent(context, LocationService.class);
        context.startService(background);
    }


}