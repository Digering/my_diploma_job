package com.example.english_4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Not", "Работаю_NotificationReceiver!!! \n\n\n\n\n");

        Intent serviceIntent = new Intent(context, CheckLogService.class);
        CheckLogService.enqueueWork(context, serviceIntent);
    }
}
