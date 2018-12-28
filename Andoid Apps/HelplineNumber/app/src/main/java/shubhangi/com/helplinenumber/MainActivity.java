package shubhangi.com.helplinenumber;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    String rec, subject, textMessage;

    String address, body, date, _id;
    ListView lViewSMS;
    String email = "janmitra_@outlook.com";

    final String fromEmail = "shubhangibharti97@gmail.com";
    final String fromPassword = "ritu2297";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for email
        context = this;
        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        {
            lViewSMS = (ListView) findViewById(R.id.listViewSMS);
            if (fetchInbox() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_row, R.id.listText, fetchInbox());
                lViewSMS.setAdapter(adapter);
                lViewSMS.setOnItemClickListener(new MainActivity.ListClickHandler());
            }
        }

    }

    public ArrayList<String> fetchInbox() {

        ArrayList<String> sms = new ArrayList<String>();

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);

        cursor.moveToLast();
        while (cursor.moveToPrevious()) {
            _id = cursor.getString(0);
            address = cursor.getString(1);
            date = cursor.getString(2);
            body = cursor.getString(3);

            System.out.println("======> Mobile number => " + address);
            System.out.println("=====> SMS Text => " + body);
            if(address.equals("+919425630336"))
            sms.add(address + "\n " + body);

        }
        return sms;
    }

    public class ListClickHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            // TODO Auto-generated method stub
            TextView listText = (TextView) view.findViewById(R.id.listText);
            String text = listText.getText().toString();



            //email

            rec = email;
            subject = "H-complaint/"+text;
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
            Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
        }
    }
}


