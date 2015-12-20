package com.group16.medassist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewContact extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
    }

    public void saveContactsToDB(View v) {
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String address = ((EditText) findViewById(R.id.address)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();

        if(name.isEmpty() || phone.isEmpty() || address.isEmpty() || email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must fill out all the fields", Toast.LENGTH_LONG).show();
            return;
        }

        Contact c = new Contact(name, email, phone, address);
        db = new DatabaseHelper(getApplicationContext());
        db.createContact(c);
        startActivity(new Intent(this, ContactsActivity.class));
    }

    public void cancelSaveContacts(View v) {
        finish();
    }

}