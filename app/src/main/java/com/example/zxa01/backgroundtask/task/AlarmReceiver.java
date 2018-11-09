package com.example.zxa01.backgroundtask.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_CUSTOM_ALARM = "com.example.zxa01.backgroundtask.ACTION_CUSTOM_ALARM";
    public static final int NOTIFICATION_ID = 1;

    public AlarmReceiver() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //  會收到 AlarmManager 的定時通知
        Toast.makeText(context,"鬧鐘通知",Toast.LENGTH_SHORT).show();
        context.sendBroadcast(new Intent(NotificationReceiver.ACTION_CUSTOM_NOTIFICATION));
    }

}


