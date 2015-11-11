package com.group16.medassist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.prescriptionsButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.appointmentsButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.contactsButton)).setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.prescriptionsButton: intent = new Intent(this, PrescriptionsActivity.class); break;
            case R.id.appointmentsButton: break;
            case R.id.contactsButton: break;
        }
        if(intent != null)
            startActivity(intent);
    }

}
