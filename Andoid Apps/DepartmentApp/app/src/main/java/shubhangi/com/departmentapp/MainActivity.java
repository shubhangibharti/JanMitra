package shubhangi.com.departmentapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shubhangi.departmentapp.R;

public class MainActivity extends AppCompatActivity {
    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    String rec, subject, textMessage;
    String address, body, date, _id;
    ListView lViewSMS;
    String email = "janmitra_railways@outlook.com";
    private String path = "http://janmitra.000webhostapp.com/insert.php";

    final String fromEmail = "shubhangibharti97@gmail.com";
    final String fromPassword = "ritu2297";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        //Button sync = (Button)findViewById(R.id.sync);
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            lViewSMS = (ListView) findViewById(R.id.listViewSMS);

            if (fetchInbox() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_row, R.id.listText, fetchInbox());
                lViewSMS.setAdapter(adapter);
                lViewSMS.setOnItemClickListener(new ListClickHandler());
            }

        }
    }

    public ArrayList<String> fetchInbox() {

        ArrayList<String> sms = new ArrayList<String>();

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            _id = cursor.getString(0);
            address = cursor.getString(1);
            date = cursor.getString(2);
            body = cursor.getString(3);

            System.out.println("======> Mobile number => " + address);
            System.out.println("=====> SMS Text => " + body);

            sms.add( address + "\n "+ body);

        }
        return sms;
    }


    public class ListClickHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            // TODO Auto-generated method stub
            TextView listText = (TextView) view.findViewById(R.id.listText);
            String text = listText.getText().toString();
            String[] lines = text.split("\\r?\\n");


            // email content

            rec = email;
            subject = "H-complaint";
            textMessage = text;


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

            pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

            RetreiveFeedTask task = new RetreiveFeedTask();
            task.execute();
        }
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
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }
}

