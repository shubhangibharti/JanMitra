package shubhangi.com.departmentapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shubhangi.departmentapp.R;

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
 * Created by ShubhangiBharti on 25-Mar-18.
 */

public class FeedbackResolve extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    String rec, subject, textMessage;
    String email = "janmitra_railways@outlook.com";
    TextView complaint;
    EditText remark;
    Button send;
    RadioGroup rg;
    Spinner departmentSpinner,zoneSpinner, divisionSpinner;
    LinearLayout notRes;
    String[] zones = {"Choose zone","Gorakhpur","New Delhi", "Howrah"};
    String[] divisions = {"Choose division", "Dhanbad",};
    String[] departments = {"Choose department", "Commercial","Security", "Operations", "Maintanance"};

    String _id, address, date, body;

    final String fromEmail = "shubhangibharti97@gmail.com";
    final String fromPassword = "ritu2297";

    int flag = 0;
    String dep,zone,division,rem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolved);
        context = this;
        notRes = (LinearLayout)findViewById(R.id.llNotRelevant);
        notRes.setVisibility(View.INVISIBLE);
        send = (Button)findViewById(R.id.bSend);
        complaint = (TextView)findViewById(R.id.tvComplaint);
        remark = (EditText)findViewById(R.id.etRemark);
        remark.setEnabled(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FeedbackResolve.this, android.R.layout.simple_spinner_dropdown_item,departments);
        departmentSpinner = (Spinner) findViewById(R.id.spinnerDept);
        departmentSpinner.setAdapter(adapter);
        departmentSpinner.setEnabled(false);


        ArrayAdapter<String> adapterZ = new ArrayAdapter<String>(FeedbackResolve.this, android.R.layout.simple_spinner_dropdown_item,zones);
        zoneSpinner = (Spinner) findViewById(R.id.spinnerZone);
        zoneSpinner.setAdapter(adapterZ);
        zoneSpinner.setEnabled(false);


        ArrayAdapter<String> adapterD = new ArrayAdapter<String>(FeedbackResolve.this, android.R.layout.simple_spinner_dropdown_item,divisions);
        divisionSpinner = (Spinner) findViewById(R.id.spinnerDiv);
        divisionSpinner.setAdapter(adapterD);
        divisionSpinner.setEnabled(false);

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dep = departmentSpinner.getSelectedItem().toString();
                send.setEnabled(true);
                remark.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                zone = zoneSpinner.getSelectedItem().toString();
                divisionSpinner.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                division = divisionSpinner.getSelectedItem().toString();
                departmentSpinner.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rg = (RadioGroup)findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(this);

        Bundle gotBasket = getIntent().getExtras();
        _id = gotBasket.getStringArray("key")[0];
        address = gotBasket.getStringArray("key")[1];
        date = gotBasket.getStringArray("key")[2];
        body = gotBasket.getStringArray("key")[3];
        complaint.setText(body);
        send.setOnClickListener(this);
        send.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        rem = remark.getText().toString();
        rec = email;
        subject = "";
        textMessage = "D-Feedback";
        switch (flag){
            case 1:
                subject += "0/";
                break;
            case 2:
                subject += "1/";
                break;
            case 3:
                subject += remark+"/";
                break;
        }
        String text = body;
        String[] line = text.split("\\r?\\n");
        subject += line[0];
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.rbResolved:
                flag = 1;
                notRes.setVisibility(View.INVISIBLE);
                send.setEnabled(true);
                break;
            case R.id.rbNotResolved:
                flag = 2;
                notRes.setVisibility(View.INVISIBLE);
                send.setEnabled(true);
                break;
            case R.id.rbNotRelevant:
                notRes.setVisibility(View.VISIBLE);
                remark.setEnabled(true);
                send.setEnabled(false);
                zoneSpinner.setEnabled(true);
                divisionSpinner.setEnabled(false);
                departmentSpinner.setEnabled(false);
                remark.setEnabled(false);

                break;
        }
    }


}
