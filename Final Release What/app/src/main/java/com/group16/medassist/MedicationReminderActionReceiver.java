package com.group16.medassist;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;

public class MedicationReminderActionReceiver {


    public static class Skip extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra("reminderID", -1);
            System.out.println(id + " was skipped ");
            ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(id);
        }
    }

    public static class Take extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra("reminderID", -1);
            int amount = intent.getIntExtra("amount", -1);
            int prescriptionId = intent.getIntExtra("prescription_id", -1);
            String drug = intent.getStringExtra("drug");

            System.out.println(id + " was taken");
            ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(id);
            DatabaseHelper db = new DatabaseHelper(context);
            db.takeDoseFromPrescription(prescriptionId, amount);
            int disabledDosages = db.getNumberOfDisabledDosages(prescriptionId);
            if(disabledDosages > 0) {
                Notification notification = new NotificationCompat.Builder(context)
                        .setWhen(0)
                        .setAutoCancel(true)
                        .setTicker("Prescription needs refill")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSmallIcon(R.drawable.pill)
                        .setContentTitle("Prescription refill needed for " + drug)
                        .setContentText(disabledDosages + " dosages have been disabled")
                        .build();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(Integer.MIN_VALUE+prescriptionId, notification);
            }
            db.closeDB();
        }
    }

}
