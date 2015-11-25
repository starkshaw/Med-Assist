package com.group16.medassist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.prescriptionsButton).setOnClickListener(this);
        findViewById(R.id.appointmentsButton).setOnClickListener(this);
        findViewById(R.id.contactsButton).setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.prescriptionsButton: intent = new Intent(this, PrescriptionsActivity.class); break;
            case R.id.appointmentsButton: break;
            case R.id.contactsButton: intent = new Intent(this, ContactsActivity.class); break;
        }
        if(intent != null)
            startActivity(intent);
    }

}
