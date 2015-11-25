package com.group16.medassist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DosageDetails extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayAdapter<DosageReminderToPrint> listAdapter;
    int dosageID;
    EditText instructions, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dosageID = getIntent().getIntExtra("dosage_id", -1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosage_details);
        instructions = (EditText)findViewById(R.id.dosage_instructions_update);
        amount = (EditText)findViewById(R.id.dosage_amount_update);

        db = new DatabaseHelper(getApplicationContext());
        List<DosageReminderToPrint> dosageReminders = db.getDosageReminders(dosageID);
        Dosage dosage = db.getDosageById(dosageID);
        db.close();
        instructions.setText(dosage.instructions);
        amount.setText(String.valueOf(dosage.dosageAmount));

        listView = (ListView) findViewById(R.id.remindersList);
        if (dosageReminders.size() != 0) {
            listAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, dosageReminders);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemPosition = position;
                    DosageReminderToPrint d = (DosageReminderToPrint) listView.getItemAtPosition(position);
                    System.out.println("Dosage ID: " + dosageID + " Reminder IDs: " + d.reminderRowIds);
                    /* Intent intent = new Intent(getApplicationContext(), DosageDetails.class);
                    intent.putExtra("dosage_id", dosage.dosageId);
                    startActivity(intent); */
                }
            });
        } else {
            noDosageReminders();
        }
    }

    public void noDosageReminders() {
        // remove list
        ViewGroup topView = ((ViewGroup)listView.getParent());
        topView.removeView(listView);

        // remove delete all button
        Button deleteAll = (Button)findViewById(R.id.deleteAllDosageReminders);
        ((ViewGroup)deleteAll.getParent()).removeView(deleteAll);

        // add text about no prescriptions
        TextView text = new TextView(this);
        text.setText("No Dosage Reminders");
        text.setTextColor(Color.BLACK);
        text.setTextSize(15);
        topView.addView(text);
    }

    public void addDosageReminder(View v) {
        Intent i = new Intent(this, NewDosageReminder.class);
        i.putExtra("dosage_id", dosageID);
        startActivity(i);
    }

    public void updateDosage(View v) {
        String amountText = amount.getText().toString().trim();
        String instructionsText = instructions.getText().toString().trim();
        if(amountText.isEmpty() || instructionsText.isEmpty()) return;
        db = new DatabaseHelper(this);
        db.updateDosage(dosageID, Integer.parseInt(amountText), instructionsText);
        db.close();
    }

    public void deleteDosage(View v) {
        db = new DatabaseHelper(this);
        db.deleteDosage(this, dosageID);
        db.close();
        Intent i = new Intent(this, DosagesActivity.class);
        i.putExtra("prescription_id", getIntent().getIntExtra("prescription_id", -1));
        startActivity(i);
    }

    public void deleteAllDosageReminders(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete all dosage reminders for this dosage?")
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(listAdapter == null) return;
                        listAdapter.clear();
                        listAdapter.notifyDataSetChanged();
                        noDosageReminders();
                        db = new DatabaseHelper(getApplicationContext());
                        db.deleteDosageReminders(getApplicationContext(), dosageID);
                        db.close();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                })
                .create().show();
    }

}