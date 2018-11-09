package com.example.zxa01.backgroundtask;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zxa01.backgroundtask.task.AlarmReceiver;
import com.example.zxa01.backgroundtask.task.NotificationReceiver;
import com.example.zxa01.backgroundtask.task.SimpleAdapter;
import com.example.zxa01.backgroundtask.task.SimpleAsyncTask;
import com.example.zxa01.backgroundtask.entity.Book;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SimpleAdapter mSimpleAdapter;
    private ArrayList<Book> books = new ArrayList();
    private TextView params;

    private boolean isChecked = true;
    private NotificationManager notificationManager;
    private NotificationReceiver notificationReceiver;
    private AlarmReceiver alarmReceiver;
    private AlarmManager alarmManager;
    private long triggerTime = SystemClock.elapsedRealtime() + 3 * 100; // 起始時間
    private long repeatInterval = 3 * 100; // 重複時間

    // Bottom Button
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_notifications:
                    // 送出 Notification
                    Intent intent = new Intent(NotificationReceiver.ACTION_CUSTOM_NOTIFICATION);
                    sendBroadcast(intent);
                    return true;
                case R.id.navigation_alarm:
                    // 建立 Alarm
                    Intent alarmIntent = new Intent(AlarmReceiver.ACTION_CUSTOM_ALARM);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, AlarmReceiver.NOTIFICATION_ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (isChecked) {
                        Toast.makeText(MainActivity.this, "新增鬧鐘", Toast.LENGTH_SHORT).show();
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, pendingIntent);
                        isChecked = false;
                    } else {
                        Toast.makeText(MainActivity.this, "取消鬧鐘", Toast.LENGTH_SHORT).show();
                        alarmManager.cancel(pendingIntent); // 取消 alarm 通知
                        notificationManager.cancelAll(); //取消通知
                        isChecked = true;
                    }
                    return true;
                case R.id.navigation_delete:
                    // 刪除所有通知
                    Toast.makeText(MainActivity.this, "cancel all notifications", Toast.LENGTH_SHORT).show();
                    notificationManager.cancelAll();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 建立 RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        books = new ArrayList<>();
        mSimpleAdapter = new SimpleAdapter(this, books);

        // 通知建立
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Notification
        notificationReceiver = new NotificationReceiver();
        IntentFilter notificationIntentFilter = new IntentFilter();
        notificationIntentFilter.addAction(notificationReceiver.ACTION_CUSTOM_BROADCAST);
        notificationIntentFilter.addAction(notificationReceiver.ACTION_CUSTOM_NOTIFICATION);
        registerReceiver(notificationReceiver, notificationIntentFilter);

        // Alarm
        alarmReceiver = new AlarmReceiver();
        IntentFilter alarmIntentFilter = new IntentFilter();
        alarmIntentFilter.addAction(alarmReceiver.ACTION_CUSTOM_ALARM);
        registerReceiver(alarmReceiver, alarmIntentFilter);

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(notificationReceiver);
        unregisterReceiver(alarmReceiver);
        super.onDestroy();
    }

    // 執行 Async Task
    @SuppressLint("ResourceAsColor")
    public void startTask(View view) {
        // 讀取參數 call api
        params = findViewById(R.id.params);
        SimpleAsyncTask simpleAsyncTask = new SimpleAsyncTask(params.getText().toString(), mRecyclerView, mSimpleAdapter, this);
        simpleAsyncTask.execute();
        // 送出 Toast 廣播
        Intent intent = new Intent(NotificationReceiver.ACTION_CUSTOM_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(new NotificationReceiver(), new IntentFilter(NotificationReceiver.ACTION_CUSTOM_BROADCAST));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
