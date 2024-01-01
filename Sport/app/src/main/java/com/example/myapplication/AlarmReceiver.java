package com.example.myapplication;

import android.content.BroadcastReceiver;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class AlarmReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // 启动 NotificationService 发送通知
////        Intent serviceIntent = new Intent(context, NotificationService.class);
////        Log.d("ALARM_TEST","here2");
////        context.startService(serviceIntent);
//
//    }

    private static final int NOTIFICATION_FLAG = 1;

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("VIDEO_TIMER")) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
            // 通过Notification.Builder来创建通知，注意API Level
            // API16之后才支持

            Log.d("ALARM_TEST","here to send1");
            Notification notify = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("TickerText:" + "您有新短消息，请注意查收！")
                    .setContentTitle("Notification Title")
                    .setContentText("This is the notification message")
                    .setContentIntent(pendingIntent).setNumber(1).build(); // 需要注意build()是在API
            // level16及之后增加的，API11可以使用getNotificatin()来替代
            Log.d("ALARM_TEST","here to send2");
            notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
            // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d("ALARM_TEST","here to send3");

            manager.notify(NOTIFICATION_FLAG, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
            Log.d("ALARM_TEST","here to send4");
        }
    }

}
