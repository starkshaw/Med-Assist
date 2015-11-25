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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewPrescription extends AppCompatActivity {

    DatabaseHelper db;
    static Button dateOfIssue;
    static EditText notes, drug, quantity;
    static Spinner contactsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_prescription);
        contactsSpinner = (Spinner) findViewById(R.id.contactsSpinner);
        dateOfIssue = (Button)findViewById(R.id.date_of_issue);
        notes = (EditText)findViewById(R.id.prescription_notes);
        drug = (EditText)findViewById(R.id.drug);
        quantity = (EditText)findViewById(R.id.prescription_quantity);

        db = new DatabaseHelper(getApplicationContext());
        List<Contact> contacts = db.getAllContacts();
        db.close();
        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contacts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactsSpinner.setAdapter(adapter);
        contactsSpinner.setSelection(0);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateOfIssue.setText(day + "/" + month + "/" + year);
    }

    public void savePrescriptionToDB(View v) {
        String drugText = drug.getText().toString().trim();
        String quantityText = quantity.getText().toString().trim();
        if(drugText.isEmpty() || quantityText.isEmpty()) return;
        String date_of_issue = dateOfIssue.getText().toString().trim();
        String notesText = notes.getText().toString().trim();
        Contact contact = (Contact)contactsSpinner.getSelectedItem();
        if(contact == null) return;
        Prescription p = new Prescription(drugText, Integer.parseInt(quantityText), date_of_issue, notesText, contact);
        db = new DatabaseHelper(getApplicationContext());
        db.createPrescription(p);
        db.close();
        startActivity(new Intent(this, PrescriptionsActivity.class));
    }

    public void cancelSavePrescription(View v) {
        finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] date = dateOfIssue.getText().toString().split("/");
            int day = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int year = Integer.parseInt(date[2]);
            return new DatePickerDialog(getActivity(), this, year, month-1, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateOfIssue.setText(day + "/" + (month + 1) + "/" + year);
        }

    }

}


