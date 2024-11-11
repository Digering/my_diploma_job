package com.example.english_4;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CheckLogService extends JobIntentService {

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(3, createNotification());

        Log.e("Not", "Работаю!!! \n\n\n\n\n");
    }

    public CheckLogService() {
        super();
    }
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, CheckLogService.class, 1234, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        SQLiteDatabase db = new DatabaseHelper(this).open();

        // Текущее время
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        String currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now().format(formatter);
            Log.e("Not", "это время: \n\n\n\n\n"+currentTime);
        }
        // Запрос для проверки слов на повторение
        Cursor cursor = db.rawQuery("SELECT * FROM Log WHERE datatime <= ? AND repetition < 3", new String[]{currentTime});

        Log.e("Not", "эта служба: \n\n\n\n\n"+cursor.toString());

        if (cursor != null && cursor.moveToFirst()) {
//            createNotification();
            sendNotification(); // Отправляем уведомление
            Log.e("Not", "sendNotification \n\n\n\n\n");
        }

        if (cursor != null) {
            cursor.close();
            Log.e("Not", "cursor.close() \n\n\n\n\n");
        }
        db.close();
    }


    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1; // Уникальный ID уведомления

        // Проверяем, существует ли уведомление
        if (!isNotificationVisible(notificationManager, notificationId)) {
            Notification notification = createNotification();
            notificationManager.notify(notificationId, notification); // Отправляем уведомление
        } else {
            Log.e("Not", "Уведомление уже отображается на экране.");
        }
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Test")
                .setContentTitle("Время повтрорять слова")
                .setContentText("Для лучшего запоминания методикой приложения просьба не игнорировать повторения")
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        Log.e("Not", "createNotification!!!!!!!! \n\n\n\n\n");
        return builder.build();
    }
    private boolean isNotificationVisible(NotificationManager notificationManager, int notificationId) {
        // Список активных уведомлений
        StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
        for (StatusBarNotification notification : activeNotifications) {
            if (notification.getId() == notificationId) {
                return true; // Уведомление уже активно
            }
        }
        return false; // Уведомление не активно
    }
}
