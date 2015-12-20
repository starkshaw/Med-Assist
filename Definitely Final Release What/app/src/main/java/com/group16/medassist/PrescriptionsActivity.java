package com.group16.medassist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PrescriptionsActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayAdapter<Prescription> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);
        db = new DatabaseHelper(getApplicationContext());
        List<Prescription> prescriptions = db.getAllPrescriptions();
        db.close();

        listView = (ListView) findViewById(R.id.prescriptionsList);
        if(prescriptions.size() != 0) {
            listAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, prescriptions);

            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Prescription prescription = (Prescription) listView.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), PrescriptionDetails.class);
                    intent.putExtra("prescription_id", prescription.id);
                    startActivity(intent);
                }
            });
        } else {
            // remove list
            ViewGroup topView = ((ViewGroup)listView.getParent());
            topView.removeView(listView);

            // remove delete all button
            Button deleteAll = (Button)findViewById(R.id.deleteAllPrescriptionsButton);
            ((ViewGroup)deleteAll.getParent()).removeView(deleteAll);

            // add text about no prescriptions
            TextView text = new TextView(this);
            text.setText("No Prescriptions");
            text.setTextColor(Color.BLACK);
            text.setTextSize(15);
            topView.addView(text);
        }
    }

    public void deleteAllPrescriptions(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete all your prescriptions?")
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db = new DatabaseHelper(getApplicationContext());
                        db.deleteAllPrescriptions(getApplicationContext());
                        db.close();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                })
                .create().show();
    }

    public void addPrescription(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if(db.getNumberOfContacts() == 0) {
            Toast.makeText(getApplicationContext(), "You must have contacts to add prescriptions", Toast.LENGTH_LONG).show();
            return;
        }
        db.closeDB();
        startActivity(new Intent(this, NewPrescription.class));
    }

}
