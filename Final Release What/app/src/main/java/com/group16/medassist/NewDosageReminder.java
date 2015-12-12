package com.group16.medassist;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class NewDosageReminder extends AppCompatActivity {

    DatabaseHelper db;
    int dosageId;
    static SimpleDateFormat df;
    CheckBox[] dayCheckBoxes = new CheckBox[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dosageId = getIntent().getIntExtra("dosage_id", -1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dosage_reminder);
        dayCheckBoxes[0] = (CheckBox)findViewById(R.id.sunday);
        dayCheckBoxes[1] = (CheckBox)findViewById(R.id.monday);
        dayCheckBoxes[2] = (CheckBox)findViewById(R.id.tuesday);
        dayCheckBoxes[3] = (CheckBox)findViewById(R.id.wednesday);
        dayCheckBoxes[4] = (CheckBox)findViewById(R.id.thursday);
        dayCheckBoxes[5] = (CheckBox)findViewById(R.id.friday);
        dayCheckBoxes[6] = (CheckBox)findViewById(R.id.saturday);

        df = new SimpleDateFormat("HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Dublin"));
        ((Button)findViewById(R.id.reminderTime)).setText(df.format(new Date()));
    }

    public void saveDosageReminderToDB(View v) {
        boolean[] days = new boolean[dayCheckBoxes.length];
        boolean noDays = true;
        for(int i = 0; i < dayCheckBoxes.length; ++i) {
            boolean checked = dayCheckBoxes[i].isChecked();
            days[i] = checked;
            if(checked & noDays) noDays = false;
        }
        if(noDays) {
            Toast.makeText(getApplicationContext(), "You must select at least one day", Toast.LENGTH_LONG).show();
            return;
        }

        String time = ((Button)findViewById(R.id.reminderTime)).getText().toString();
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3));

        db = new DatabaseHelper(getApplicationContext());
        for(int day = 1; day < 8; ++day)
            if(days[day-1]) {
                int id = (int)db.createDosageReminder(dosageId, day, hour, minute);
                if(id != -1) {
                    System.out.println("Added dosage reminder to database with id " + id);
                    scheduleDosageReminder(id);
                }
            }
        db.close();
        Intent intent = new Intent(this, DosageDetails.class);
        intent.putExtra("dosage_id", dosageId);
        startActivity(intent);
    }

    public void scheduleDosageReminder(int reminderId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MedicationReminderReceiver.class);
        intent.putExtra("reminderID", reminderId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reminderId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        db = new DatabaseHelper(this);
        Dosage.Reminder reminder = db.getDosageReminderById(reminderId);
        db.close();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, reminder.day);
        calendar.set(Calendar.HOUR_OF_DAY, reminder.hour);
        calendar.set(Calendar.MINUTE, reminder.minute);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis() + 60000)
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Dublin"));
        System.out.println("Setting a weekly alarm with id " + reminderId + " starting at: " + df.format(calendar.getTime()));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    public void cancel(View v) {finish();}

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String time = ((Button)getActivity().findViewById(R.id.reminderTime)).getText().toString();
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3));
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            ((Button)getActivity().findViewById(R.id.reminderTime)).setText(df.format(c.getTime()));
        }

    }

}




