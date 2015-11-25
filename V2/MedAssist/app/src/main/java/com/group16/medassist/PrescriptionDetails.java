package com.group16.medassist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PrescriptionDetails extends AppCompatActivity {

    static int prescription_id;
    DatabaseHelper db;
    EditText refill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);

        Intent mIntent = getIntent();
        prescription_id = mIntent.getIntExtra("prescription_id", -1);
        db = new DatabaseHelper(getApplicationContext());
        Prescription p = db.getPrescriptionById(prescription_id);
        db.close();
        if(p == null) startActivity(new Intent(this, PrescriptionsActivity.class));

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
        db.refillPrescription(prescription_id, Integer.parseInt(refill.getText().toString()));
        db.close();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void deletePrescription(View v) {
        db = new DatabaseHelper(this);
        db.deletePrescription(this, prescription_id);
        db.close();
        startActivity(new Intent(this, PrescriptionsActivity.class));
    }

}
