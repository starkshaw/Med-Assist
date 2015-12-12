package com.group16.medassist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
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
        int prescriptionId = Integer.parseInt(reminder.get("prescription_id"));
        int prescriptionQuantity = Integer.parseInt(reminder.get("prescriptionQuantity"));
        String instructions = reminder.get("instructions");
        int hour = Integer.parseInt(reminder.get("hour")), minute = Integer.parseInt(reminder.get("minute"));
        db.close();

        if(prescriptionQuantity < amount) {
            System.out.println("No medication reminder because there is less " +
                                "than " + amount + " left in the prescription " + drug);
            return;
        }

        SimpleDateFormat df = new SimpleDateFormat("dd/MM HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Dublin"));
        System.out.println("Reminder " + reminderId + " received at " + df.format(new Date()));

        Intent take = new Intent(context, MedicationReminderActionReceiver.Take.class);
        take.putExtra("reminderID", reminderId);
        take.putExtra("prescription_id", prescriptionId);
        take.putExtra("amount", amount);
        take.putExtra("drug", drug);
        PendingIntent takePI = PendingIntent.getBroadcast(context, reminderId, take, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent skip = new Intent(context, MedicationReminderActionReceiver.Skip.class);
        skip.putExtra("reminderID", reminderId);
        PendingIntent skipPI = PendingIntent.getBroadcast(context, reminderId, skip, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setWhen(0)
                .setAutoCancel(false)
                .addAction(0, "Take now!", takePI)
                .addAction(0, "Skip", skipPI)
                .setTicker("MedAssist Reminders")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSmallIcon(R.drawable.pill)
                .setContentTitle("Take " + amount + " " + drug + " at "+(hour>9?hour:"0"+hour)+":" + (minute > 9 ? minute : "0" + minute))
                .setContentText("Instructions: " + instructions)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(reminderId, notification);
    }

}
