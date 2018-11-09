package com.example.zxa01.backgroundtask.task;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.zxa01.backgroundtask.MainActivity;
import com.example.zxa01.backgroundtask.R;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_CUSTOM_BROADCAST = "com.example.zxa01.backgroundtask.ACTION_CUSTOM_BROADCAST";
    public static final String ACTION_CUSTOM_NOTIFICATION = "com.example.zxa01.backgroundtask.ACTION_CUSTOM_NOTIFICATION";
    public static final int NOTIFICATION_ID = 0;
    private Context context;


    public NotificationReceiver() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        if (intent.getAction().equals(ACTION_CUSTOM_BROADCAST)) {
            Toast.makeText(context, "查詢中...", Toast.LENGTH_SHORT).show();
        } else {
            sendBroadcast();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void sendBroadcast(){

        // 創建頻道
        NotificationChannel c = new NotificationChannel(String.valueOf(NOTIFICATION_ID),context.getString(R.string.title_notifications), NotificationManager.IMPORTANCE_DEFAULT);
        // 建立通知管理
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.createNotificationChannel(c);

        // 設定通知 intent
        Intent ci = new Intent(context, MainActivity.class);
        ci.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // 設定通知 pending intent
        PendingIntent pi = PendingIntent.getActivity(context, NOTIFICATION_ID, ci, PendingIntent.FLAG_UPDATE_CURRENT);
        // 建立通知
        NotificationCompat.Builder b = new NotificationCompat.Builder(context, String.valueOf(NOTIFICATION_ID))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(context.getString(R.string.title_notifications))
                .setContentText(context.getString(R.string.content_notifiaction))
                .setContentIntent(pi)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("詳細內容"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 共有五個優先順序級別 -2~2
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_dashboard_black_24dp, "More", pi); // 前往更多... intent 決定下一步
        nm.notify(NOTIFICATION_ID, b.build());
    }
}
