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
                    int itemPosition = position;
                    Contact contact = (Contact) listView.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  name : " + contact.name, Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
    }

    public void deleteAllContacts(View v) {
        /*db = new DatabaseHelper(getApplicationContext());
        db.deleteAllContacts();
        if(listAdapter != null) {
            listAdapter.clear();
            listAdapter.notifyDataSetChanged();
        }*/
        System.out.println("Deleting all contacts..");
    }

    public void addContact(View v) {
        Intent intent = new Intent(this, NewContact.class);
        startActivity(intent);
    }

}