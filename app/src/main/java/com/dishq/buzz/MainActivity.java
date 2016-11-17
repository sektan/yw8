package com.dishq.buzz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.io.IOException;
import java.util.Calendar;

import android.content.pm.PackageInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Response.VersionCheckResponse;
import server.api.ApiInterface;
import server.api.Config;

import com.dishq.buzz.ui.HomePageActivity;
import com.dishq.buzz.ui.SignUpActivity;
import com.dishq.buzz.util.YW8Application;

public class MainActivity extends AppCompatActivity {
    public String versionName;
    public int versionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Crashlytics instantiation
        Fabric.with(this, new Crashlytics());
        //Google Analytics instantiation
        setContentView(R.layout.activity_main);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;

            Log.e("dfdd", pInfo.versionName + pInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        timer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // To set the timer for the splash screen to be displayed
    Thread timer = new Thread() {
        public void run() {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                fetchVersion(versionName, versionCode);
            }
        }
    };

    public void fetchVersion(String versionName, int versionCode) {
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<VersionCheckResponse> request = apiInterface.checkVersion(versionName, versionCode);
        request.enqueue(new Callback<VersionCheckResponse>() {
            @Override
            public void onResponse(Call<VersionCheckResponse> call, Response<VersionCheckResponse> response) {
                Log.d("MainActivity", "Success");
                try {
                    if(response.isSuccessful()) {
                        VersionCheckResponse.VersionCheckData body = response.body().versionCheckData;
                        if(body!=null) {
                            if(body.getShowUpdatePopup()) {
                                if(body.getDoForceUpdate()) {
                                    showAlert("Update YW8", "Update the app for best performance", true);
                                }else {

                                    showAlert("Update YW8", "Update the app for best performance", false);
                                }
                            }else {
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

                    } else {
                        String error = response.errorBody().string();
                        Log.d("VersionCheck", error);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<VersionCheckResponse> call, Throwable t) {
                Log.d("MainActivity", "Failure");
            }
        });

    }

    public void showAlert(String title, String message, boolean force) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                });
        if(!force){
            builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
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
            });
        }

        AlertDialog alert = builder.create();
        alert.show();

    }

}
