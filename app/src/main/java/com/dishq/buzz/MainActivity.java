package com.dishq.buzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import com.dishq.buzz.ui.HomePageActivity;
import com.dishq.buzz.ui.SignUpActivity;
import com.dishq.buzz.util.YW8Application;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        timer.start();
    }


    // To set the timer for the splash screen to be displayed
    Thread timer=new Thread()
    {
        public void run() {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally
            {
                if (!YW8Application.getAccessToken().equals("null null")) {
                    //Intent to start home page when access token
                    Intent startHomePageActivity = new Intent(MainActivity.this, HomePageActivity.class);
                    startHomePageActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(startHomePageActivity);
                }else {
                    //Intent to start the Signup Activity after the splash screen
                    Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(i);
                }
            }
        }
    };

}
