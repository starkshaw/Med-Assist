package com.group16.medassist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PrescriptionDetails extends AppCompatActivity {

    static int prescription_id;
    DatabaseHelper db;
    EditText refill;
    Prescription p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);

        Intent mIntent = getIntent();
        prescription_id = mIntent.getIntExtra("prescription_id", -1);
        db = new DatabaseHelper(getApplicationContext());
        p = db.getPrescriptionById(prescription_id);
        db.close();
        if(p == null) {startActivity(new Intent(this, PrescriptionsActivity.class)); return;}

        refill = ((EditText)findViewById(R.id.refillAmount));
        ((TextView)findViewById(R.id.prescription_title)).setText(p.drug);
        ((TextView)findViewById(R.id.medication_quantity)).setText(String.valueOf(p.quantity));
        ((TextView)findViewById(R.id.contact_name)).setText(p.contact.name);
        ((TextView)findViewById(R.id.notes_content)).setText(p.notes);
        ((TextView)findViewById(R.id.contact_email)).setText(p.contact.email);
        ((TextView)findViewById(R.id.contact_phone)).setText(p.contact.phone);
        findViewById(R.id.contact_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phno = ((TextView) findViewById(R.id.contact_phone)).getText().toString();
                if (phno.matches("[0-9]+")) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phno));
                    try {startActivity(callIntent);}
                    catch (SecurityException e) {e.printStackTrace();}
                }
            }
        });
        findViewById(R.id.dosages_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DosagesActivity.class);
                i.putExtra("prescription_id", prescription_id);
                startActivity(i);
            }
        });

    }

    public void refillPrescription(View v) {
        db = new DatabaseHelper(getApplicationContext());
        String refillText = refill.getText().toString();
        if(refillText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must enter a number to refill", Toast.LENGTH_LONG).show();
            return;
        }
        db.refillPrescription(prescription_id, Integer.parseInt(refillText));
        db.close();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void deletePrescription(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete your "+p.drug+" prescription?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db = new DatabaseHelper(getApplicationContext());
                        db.deletePrescription(getApplicationContext(), prescription_id);
                        db.close();
                        startActivity(new Intent(getApplicationContext(), PrescriptionsActivity.class));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                })
                .create().show();
    }

}
