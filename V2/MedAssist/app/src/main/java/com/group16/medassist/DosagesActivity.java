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
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class DosagesActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayAdapter<Dosage> listAdapter;
    int prescriptionId;
    List<Dosage> dosages;

    @Override
    protected void onResume() {
        super.onResume();
        if(listAdapter == null) return;
        db = new DatabaseHelper(getApplicationContext());
        dosages = db.getDosages(prescriptionId);
        db.closeDB();
        listAdapter.clear();
        listAdapter.addAll(dosages);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosages);
        prescriptionId = getIntent().getIntExtra("prescription_id", -1);

        db = new DatabaseHelper(getApplicationContext());
        dosages = db.getDosages(prescriptionId);
        db.closeDB();

        listView = (ListView) findViewById(R.id.dosages_list);
        if(dosages.size() != 0) {
            listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, android.R.id.text1, dosages);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemPosition = position;
                    Dosage dosage = (Dosage) listView.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), DosageDetails.class);
                    intent.putExtra("dosage_id", dosage.dosageId);
                    intent.putExtra("prescription_id", prescriptionId);
                    startActivity(intent);
                }
            }) ;
        } else { // remove list
            noDosages();
        }
    }

    public void noDosages() {
        ViewGroup topView = ((ViewGroup)listView.getParent());
        topView.removeView(listView);

        // remove delete all button
        Button deleteAll = (Button)findViewById(R.id.deleteAllDosagesButton);
        ((ViewGroup)deleteAll.getParent()).removeView(deleteAll);

        // add text about no prescriptions
        TextView text = new TextView(this);
        text.setText("No Dosages");
        text.setTextColor(Color.BLACK);
        text.setTextSize(15);
        topView.addView(text);
    }

    public void addDosage(View v) {
        Intent intent = new Intent(this, NewDosage.class);
        intent.putExtra("prescription_id", prescriptionId);
        startActivity(intent);
    }

    public void deleteAllDosages(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete all dosages for this prescription?")
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(listAdapter == null) return;
                        listAdapter.clear();
                        listAdapter.notifyDataSetChanged();
                        noDosages();
                        db = new DatabaseHelper(getApplicationContext());
                        db.deleteDosages(getApplicationContext(), prescriptionId);
                        db.close();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                })
                .create().show();
    }

}
