package com.group16.medassist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NewAppointment extends AppCompatActivity {

    DatabaseHelper db;
    static Spinner appointmentsSpinner;

    static SimpleDateFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        df = new SimpleDateFormat("HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Dublin"));
        Date d = new Date();
        ((Button)findViewById(R.id.startTimeButton)).setText(df.format(d));
        d.setTime(d.getTime()+60  * 60 * 1000);
        ((Button)findViewById(R.id.endTimeButton)).setText(df.format(d));

        appointmentsSpinner = (Spinner) findViewById(R.id.appointmentsSpinner);
        db = new DatabaseHelper(getApplicationContext());
        List<Contact> contacts = db.getAllContacts();
        db.close();
        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contacts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appointmentsSpinner.setAdapter(adapter);
        appointmentsSpinner.setSelection(0);
        //((Button)findViewById(R.id.date_of_issue)).setText(day + "/" + month + "/" + year);
    }

    public void saveAppointmentToDB(View v) {
        appointmentsSpinner = (Spinner) findViewById(R.id.appointmentsSpinner);
        String sTime = ((Button)findViewById(R.id.startTimeButton)).getText().toString();
        String sTimeHour = sTime.substring(0, 2);
        String sTimeMins = sTime.substring(3);
        String startTime = sTimeHour + ":" + sTimeMins;
        String eTime = ((Button)findViewById(R.id.endTimeButton)).getText().toString();
        String eTimeHour = eTime.substring(0, 2);
        String eTimeMins = eTime.substring(3);
        String endTime = eTimeHour + ":" + eTimeMins;
        //String date_of_issue = ((Button)findViewById(R.id.date_of_issue)).getText().toString();
        Contact contact = (Contact)appointmentsSpinner.getSelectedItem();
        Appointment a = new Appointment(contact, startTime, endTime);
        db = new DatabaseHelper(getApplicationContext());
        db.createAppointment(a);
        startActivity(new Intent(this, AppointmentsActivity.class));
    }

    public void cancelSaveAppointment(View v) {
        startActivity(new Intent(this, AppointmentsActivity.class));
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

    public void showEndTimePickerDialog(View v) {
        DialogFragment newFragment = new EndTimePickerFragment();
        newFragment.show(getFragmentManager(), "endTimePicker");
    }

    public void showStartTimePickerDialog(View v) {
        DialogFragment newFragment = new StartTimePickerFragment();
        newFragment.show(getFragmentManager(), "startTimePicker");
    }

    public static class StartTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String time = ((Button)getActivity().findViewById(R.id.startTimeButton)).getText().toString();
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3));
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            ((Button)getActivity().findViewById(R.id.startTimeButton)).setText(df.format(c.getTime()));

        }

    }

    public static class EndTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String time = ((Button)getActivity().findViewById(R.id.endTimeButton)).getText().toString();
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3));
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            ((Button)getActivity().findViewById(R.id.endTimeButton)).setText(df.format(c.getTime()));
        }

    }

}


