package shubhangi.com.departmentapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by ShubhangiBharti on 25-Mar-18.
 */

public class Container extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost host = getTabHost();
        host.addTab(host.newTabSpec("one").setIndicator("Complaint").setContent(new Intent(this, MainActivity.class)));
        host.addTab(host.newTabSpec("two").setIndicator("Feedback").setContent(new Intent(this, Feedback.class)));
    }
}

