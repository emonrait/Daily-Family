package com.raihan.dailyfamily.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailMessage extends AsyncTask<Void, Void, Void> {

    //Declaring Variables
    private Context context;
    private Session session;
    private Activity activity;

    //Information to send email
    private String email;
    private String subject;
    private String message;


    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;
    //LoadingDialog loadingDialog = new LoadingDialog(activity);

    //Class Constructor
    public SendMailMessage(Context context, String email, String subject, String message) {
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false, false);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        progressDialog.dismiss();
        Toast.makeText(context, "Message Sent Successfully your provided email address", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            //mm.setFrom(new InternetAddress("xzy@outlook.com", "Naveed Qureshi"));

            mm.setFrom(new InternetAddress(Config.EMAIL, "POET"));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //mm.addRecipient(Message.RecipientType.CC, new InternetAddress("muzahidzoom@gmail.com"));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(message);

            //Sending email
            Transport.send(mm);


        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        //  Toast.makeText(context, "Please Enter Correct Email Address", Toast.LENGTH_LONG).show();
        return null;
    }


}
