package com.group16.medassist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.prescriptionsButton).setOnClickListener(this);
        findViewById(R.id.appointmentsButton).setOnClickListener(this);
        findViewById(R.id.contactsButton).setOnClickListener(this);
        findViewById(R.id.mapsIcon).setOnClickListener(this);
        findViewById(R.id.mapsButton).setOnClickListener(this);
        findViewById(R.id.SendMail).setOnClickListener(this);
        findViewById(R.id.SendMailIcon).setOnClickListener(this);
        findViewById(R.id.contactsIcon).setOnClickListener(this);
        findViewById(R.id.prescriptionsIcon).setOnClickListener(this);
        findViewById(R.id.appointmentsIcon).setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.prescriptionsButton:
            case R.id.prescriptionsIcon:
                intent = new Intent(this, PrescriptionsActivity.class);  break;

            case R.id.appointmentsButton:
            case R.id.appointmentsIcon:
                intent = new Intent(this, AppointmentsActivity.class);  break;

            case R.id.contactsButton:
            case R.id.contactsIcon:
                intent = new Intent(this, ContactsActivity.class); break;

            case R.id.mapsButton:
            case R.id.mapsIcon:
                intent = new Intent(this, MapsActivity.class); break;

            case R.id.SendMail:
            case R.id.SendMailIcon:
                intent = new Intent(this, SendMailActivity.class); break;
        }
        if(intent != null)
            startActivity(intent);
    }

}
