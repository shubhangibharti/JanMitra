package shubhangi.com.departmentapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.shubhangi.departmentapp.R;

/**
 * Created by ShubhangiBharti on 25-Mar-18.
 */

public class Feedback extends AppCompatActivity {
    String address, body, date, _id;
    ListView lViewSMS;
    private String path = "http://janmitra.000webhostapp.com/insertfeedback.php";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        //Button sync = (Button)findViewById(R.id.sync);
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            lViewSMS = (ListView) findViewById(R.id.listViewFeedback);

            if (fetchInbox(1) != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_row, R.id.listText, fetchInbox(1));
                lViewSMS.setAdapter(adapter);
                lViewSMS.setOnItemClickListener(new Feedback.ListClickHandler1());
            }

        }
    }

    public ArrayList<String> fetchInbox(int i) {
        if (i == 1) {
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

                sms.add(_id+ "\n" + address + "\n "+ date+ "\n" + body);

            }
            return sms;
        } else if (i == 2) {
            ArrayList<String> sms = new ArrayList<String>();
            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);
            cursor.moveToFirst();

            //_id = cursor.getString(0);
            //address = cursor.getString(1);
            //date = cursor.getString(2);
            //body = cursor.getString(3);
            //new PostDataTOServer().execute();

        }
        return null;
    }

    private class PostDataTOServer1 extends AsyncTask<String, Void, Void> {

        String response = "";
        //Create hashmap Object to send parameters to web service
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... para) {


            try {

                String link = path;
                String data = URLEncoder.encode("_id", "UTF-8") + "=" +
                        URLEncoder.encode(para[0], "UTF-8");
                data += "&" + URLEncoder.encode("address", "UTF-8") + "=" +
                        URLEncoder.encode(para[1], "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" +
                        URLEncoder.encode(para[2], "UTF-8");
                data += "&" + URLEncoder.encode("body", "UTF-8") + "=" +
                        URLEncoder.encode(para[3], "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return null;
            } catch (Exception e) {
                String s = new String("Exception: " + e.getMessage());
                System.out.print(s);
                return null;

            }
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            if (success == 1) {
//                Toast.makeText(getApplicationContext(), "Ordered successfully..!", Toast.LENGTH_LONG).show();
//            }
        }

    }

    public class ListClickHandler1 implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            // TODO Auto-generated method stub
            TextView listText = (TextView) view.findViewById(R.id.listText);
            String text = listText.getText().toString();
            String[] lines = text.split("\\r?\\n");

            Bundle basket = new Bundle();
            basket.putStringArray("key", lines);
            Intent a = new Intent(Feedback.this, FeedbackResolve.class);
            a.putExtras(basket);

            startActivity(a);

        }

    }
}
