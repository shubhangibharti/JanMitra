package shubhangi.com.helplinenumber;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by ShubhangiBharti on 30-Mar-18.
 */
public class VoiceComplaint extends AppCompatActivity implements AdapterView.OnClickListener{
    String text;

    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    String rec, subject, textMessage;
    String email = "janmitra_@outlook.com";

    final String fromEmail = "shubhangibharti97@gmail.com";
    final String fromPassword = "ritu2297";

    ListView lv;
    Button b,s;
    static final int check = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.voice);
        lv = (ListView) findViewById(R.id.lvVoiceReturn);
        String[] a = {"Press Record"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_view_row,R.id.listText, a);
        lv.setAdapter(adapter);

        b = (Button)findViewById(R.id.bVoice);
        s = (Button)findViewById(R.id.bSend);
        b.setOnClickListener(this);
        s.setOnClickListener(this);
        s.setEnabled(false);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bVoice:
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak up daughter!");
                startActivityForResult(i, check);
                break;
            case R.id.bSend:


                //email

                rec = email;
                subject = "V-complaint/"+text;
                textMessage = "";


                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                session = Session.getDefaultInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, fromPassword);
                    }
                });

                pdialog = ProgressDialog.show(context, "", "Sending...", true);

                RetreiveFeedTask task = new VoiceComplaint.RetreiveFeedTask();
                task.execute();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (requestCode == check&&requestCode == RESULT_OK){
        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_view_row,R.id.listText, results));
        text = results.get(0);
        s.setEnabled(true);
        //}
        super.onActivityResult(requestCode, resultCode, data);


    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
        }
    }
}



