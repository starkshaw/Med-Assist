package com.group16.medassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMailActivity extends AppCompatActivity implements OnClickListener{

    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    EditText reciep, sub, msg;
    String rec, subject, textMessage;

    Spinner contactsSpinner;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        //spinner

        contactsSpinner = (Spinner) findViewById(R.id.emailSpinner);
        db = new DatabaseHelper(getApplicationContext());
        List<Contact> contacts = db.getAllContacts();
        db.close();
        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contacts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactsSpinner.setAdapter(adapter);
        contactsSpinner.setSelection(0);

        // end spinner

        context = this;





        Button login = (Button) findViewById(R.id.btn_submit);
        //reciep = (EditText) findViewById(R.id.et_to);
        sub = (EditText) findViewById(R.id.et_sub);
        //sub = ((EditText)findViewById(R.id.emailSpinner)).getText().toString();
        msg = (EditText) findViewById(R.id.et_text);
        //msg = (EditText)"hello".toString();

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //rec = reciep.getText().toString();

        rec = ((Contact)contactsSpinner.getSelectedItem()).email;
        System.out.println("Recipient " + rec);


        subject = sub.getText().toString();
        textMessage = msg.getText().toString();//

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("medassist353@gmail.com", "group_16");
            }
        });

        pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);
        RetrieveFeedTask task = new RetrieveFeedTask();
        task.execute();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                System.out.println("Sending mail.....");
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("medassist353@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            pdialog.dismiss();
           // reciep.setText("");
            msg.setText("");
            sub.setText("");
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            startActivity(intent);
        }
    }
}
