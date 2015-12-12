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

import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayAdapter<Contact> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        db = new DatabaseHelper(getApplicationContext());
        List<Contact> contacts = db.getAllContacts();
        db.closeDB();

        listView = (ListView) findViewById(R.id.contactsList);
        if(contacts.size() != 0) {
            listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, contacts);

            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contact contact = (Contact) listView.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), ContactsDetails.class);
                    intent.putExtra("contact_id", contact.id);
                    startActivity(intent);
                }
            });
        } else {
            // remove list
            ViewGroup topView = ((ViewGroup)listView.getParent());
            topView.removeView(listView);

            // remove delete all button
            Button deleteAll = (Button)findViewById(R.id.deleteAllContactsButton);
            ((ViewGroup)deleteAll.getParent()).removeView(deleteAll);

            // add text about no prescriptions
            TextView text = new TextView(this);
            text.setText("No Contacts");
            text.setTextColor(Color.BLACK);
            text.setTextSize(15);
            topView.addView(text);
        }
    }

    public void deleteAllContacts(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete all your contacts?\nThis will also delete all prescriptions, appointments, reminders etc.")
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db = new DatabaseHelper(getApplicationContext());
                        db.deleteAllContacts(getApplicationContext());
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

    public void addContact(View v) {
        Intent intent = new Intent(this, NewContact.class);
        startActivity(intent);
    }

}