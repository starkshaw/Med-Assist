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

public class AppointmentsActivity extends AppCompatActivity{

    ListView listView;
    DatabaseHelper db;
    ArrayAdapter<Appointment> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        db = new DatabaseHelper(getApplicationContext());
        List<Appointment> appointments = db.getAllAppointments();
        db.closeDB();

        listView = (ListView) findViewById(R.id.appointmentsList);
        if(appointments.size() != 0) {
            listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, appointments);

            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Appointment appointment = (Appointment) listView.getItemAtPosition(position);

                    new AlertDialog.Builder(AppointmentsActivity.this)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete this appointment with "+appointment.contact.name+"?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    db = new DatabaseHelper(AppointmentsActivity.this);
                                    System.out.println(appointment.endTime);
                                    db.deleteAppointment(appointment);
                                    db.close();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    dialog.dismiss();

                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                            })
                            .create().show();
                    /*Intent intent = new Intent(getApplicationContext(), ContactsDetails.class);
                    intent.putExtra("appointment_id", appointment.id);
                    startActivity(intent);*/

                }
            });
        }  else {
            // remove list
            ViewGroup topView = ((ViewGroup)listView.getParent());
            topView.removeView(listView);

            // remove delete all button
            Button deleteAll = (Button)findViewById(R.id.deleteAllAppointmentsButton);
            ((ViewGroup)deleteAll.getParent()).removeView(deleteAll);

            // add text about no prescriptions
            TextView text = new TextView(this);
            text.setText("No Appointments");
            text.setTextColor(Color.BLACK);
            text.setTextSize(15);
            topView.addView(text);
        }
    }

    public void deleteAllAppointments(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete all your appointments?")
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db = new DatabaseHelper(getApplicationContext());
                        db.deleteAllAppointments();
                        dialog.dismiss();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                })
                .create().show();
    }

    public void addAppointment(View v)
    {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if(db.getNumberOfContacts() == 0)
        {
            Toast.makeText(getApplicationContext(), "You must have contacts to add appointments", Toast.LENGTH_LONG).show();
            return;
        }
        db.closeDB();
        Intent intent = new Intent(this, NewAppointment.class);
        startActivity(intent);
    }
    /*public void sendEmail(View v)
    {
        Intent intent = new Intent(this, SendMailActivity.class);
        startActivity(intent);
    }*/

}
