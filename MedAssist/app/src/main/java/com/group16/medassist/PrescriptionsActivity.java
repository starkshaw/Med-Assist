package com.group16.medassist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
        db.closeDB();

        listView = (ListView) findViewById(R.id.prescriptionsList);
        if(prescriptions.size() != 0) {
            listAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, prescriptions);

            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemPosition = position;
                    Prescription prescription = (Prescription) listView.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  Date of Issue : " + prescription.dateOfIssue, Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
    }

    public void deleteAllPrescriptions(View v) {
        db = new DatabaseHelper(getApplicationContext());
        db.deleteAllPrescriptions();
        if(listAdapter != null) {
            listAdapter.clear();
            listAdapter.notifyDataSetChanged();
        }
    }

    public void addPrescription(View v) {
        Intent intent = new Intent(this, NewPrescription.class);
        startActivity(intent);
    }

}
