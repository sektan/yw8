package com.dishq.buzz;

import android.content.Intent;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import com.dishq.buzz.ui.HomePageActivity;
import com.dishq.buzz.ui.SignUpActivity;
import com.dishq.buzz.util.YW8Application;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    MixpanelAPI mixpanel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));
        setContentView(R.layout.activity_main);

        timer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
            final long hourOfTheDay = hourOfTheDay();


        try {
            final JSONObject properties = new JSONObject();
            properties.put("first viewed on", hourOfTheDay);
            properties.put("user domain", "(unknown)");
            mixpanel.registerSuperProperties(properties);
        } catch (final JSONException e) {
            throw new RuntimeException("could not encode hour first viewed as JSON");
        }

        try {
            final JSONObject properties = new JSONObject();
            properties.put("hour of the day", hourOfTheDay);
            mixpanel.track("App resumed", properties);
        } catch (final JSONException e) {
            throw new RuntimeException("Could not encode hour of the day in JSON");
        }
    }

    // To set the timer for the splash screen to be displayed
    Thread timer = new Thread() {
        public void run() {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (!YW8Application.getAccessToken().equals("null null")) {
                    //Intent to start home page when access token
                    Intent startHomePageActivity = new Intent(MainActivity.this, HomePageActivity.class);
                    startHomePageActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(startHomePageActivity);
                } else {
                    //Intent to start the Signup Activity after the splash screen
                    Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(i);
                }
            }
        }
    };

    private int hourOfTheDay() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

}
