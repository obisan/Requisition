package com.dubinets.requisition.mail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by dubinets on 04.09.2016.
 */
public class sender_mail_async extends AsyncTask<Object, String, Boolean> {
    protected Context           context;
    protected GmailSender       gmailSender;
    protected String            subject;
    protected String            body;
    protected String            sender;
    protected String            recipients;

    protected ProgressDialog    WaitingDialog;

    public sender_mail_async(Context context, GmailSender gmailSender, String subject, String body, String sender, String recipients) {
        this.context = context;
        this.gmailSender = gmailSender;
        this.subject = subject;
        this.body = body;
        this.sender = sender;
        this.recipients = recipients;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        WaitingDialog = ProgressDialog.show(context, "Отправка данных", "Отправляем сообщение...", true);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        WaitingDialog.dismiss();
        Toast.makeText(context, "Отправка завершена!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        try {
            gmailSender.sendMail(subject, body, sender, recipients);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
