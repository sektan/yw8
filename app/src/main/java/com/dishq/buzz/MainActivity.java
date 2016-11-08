package com.dishq.buzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dishq.buzz.ui.HomePageActivity;
import com.dishq.buzz.ui.SignUpActivity;
import com.dishq.buzz.util.YW8Application;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Request.SignUpHelper;
import server.Response.SignUpResponse;
import server.api.ApiInterface;
import server.api.Config;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if (YW8Application.getAccessToken() != null) {
                    Intent startHomePageActivity = new Intent(MainActivity.this, HomePageActivity.class);
                    finish();
                    startActivity(startHomePageActivity);
                }else {
                    //Intent to start the Signup Activity after the splash screen
                    Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        }
    };

}
