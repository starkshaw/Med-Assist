package com.group16.medassist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class ContactsDetails extends AppCompatActivity {

        static int contacts_id;
        DatabaseHelper db;
        Contact c;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contacts_details);

            Intent mIntent = getIntent();
            contacts_id = mIntent.getIntExtra("contact_id", -1);
            db = new DatabaseHelper(getApplicationContext());
            c = db.getContactById(contacts_id);
            db.close();
            if(c == null) {startActivity(new Intent(this, ContactsActivity.class)); return;}


            ((TextView)findViewById(R.id.contact_title)).setText(c.name);
            ((TextView)findViewById(R.id.contact_address)).setText(c.address);
            ((TextView)findViewById(R.id.contact_email)).setText(c.email);
            ((TextView)findViewById(R.id.contact_phone)).setText(c.phone);
            findViewById(R.id.contact_phone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phno = ((TextView) findViewById(R.id.contact_phone)).getText().toString();
                    if (phno.matches("[0-9]+")) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phno));
                        try {
                            startActivity(callIntent);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        public void deleteContact(View v) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete "+c.name+"?\nThis will also delete prescriptions, appointments, reminders etc.")
                    .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            db = new DatabaseHelper(getApplicationContext());
                            db.deleteContact(getApplicationContext(), contacts_id);
                            db.close();
                            startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                    })
                    .create().show();
        }

    }