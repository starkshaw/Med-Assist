package com.group16.medassist;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.NotificationCompat;

import junit.runner.Version;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class MedicationReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int reminderId = intent.getIntExtra("reminderID", -1);
        if(reminderId < 0) {
            System.out.println("Error failed to get reminderID");
            return;
        }

        DatabaseHelper db = new DatabaseHelper(context);
        Map<String, String> reminder = db.getReminder(reminderId);
        if(reminder.isEmpty()) {System.out.println("Shiiiiiiiit"); return;}
        String drug = reminder.get("drug");
        int amount = Integer.parseInt(reminder.get("amount"));
        String instructions = reminder.get("instructions");
        int hour = Integer.parseInt(reminder.get("hour")), minute = Integer.parseInt(reminder.get("minute"));
        db.close();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Dublin"));
        System.out.println("Reminder " + reminderId + " received at " + df.format(new Date()));

        Notification notification = new NotificationCompat.Builder(context)
                        .setTicker("MedAssist Reminders")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                        .setSmallIcon(R.drawable.pill)
                        .setContentTitle("Take " + amount + " " + drug + " at "+(hour>9?hour:"0"+hour)+":"+(minute>9?minute:"0"+minute))
                        .setContentText("Instructions: " + instructions)
                        .setAutoCancel(true)
                        .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(reminderId, notification);
    }

}
