package com.group16.medassist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class NewPrescription extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_prescription);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        ((Button)findViewById(R.id.date_of_issue)).setText(day + "/" + month + "/" + year);
    }

    public void savePrescriptionToDB(View v) {
        String drug = ((EditText)findViewById(R.id.drug)).getText().toString();
        int quantity = Integer.parseInt(((EditText) findViewById(R.id.prescription_quantity)).getText().toString());
        String date_of_issue = ((Button)findViewById(R.id.date_of_issue)).getText().toString();

        Prescription p = new Prescription(drug, quantity, date_of_issue);
        db = new DatabaseHelper(getApplicationContext());
        db.createPrescription(p);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Great job you added a prescription!")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("MedAssist")
                .setContentText("Added " + drug + " prescription")
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

        startActivity(new Intent(this, PrescriptionsActivity.class));
    }

    public void cancelSavePrescription(View v) {
        startActivity(new Intent(this, PrescriptionsActivity.class));
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] date = ((Button)getActivity().findViewById(R.id.date_of_issue)).getText().toString().split("/");
            int day = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int year = Integer.parseInt(date[2]);
            return new DatePickerDialog(getActivity(), this, year, month-1, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            ((Button) getActivity().findViewById(R.id.date_of_issue)).setText(day + "/" + (month + 1) + "/" + year);
        }

    }

}


