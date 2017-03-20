package com.dishq.buzz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.io.IOException;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Response.VersionCheckResponse;
import server.api.ApiInterface;
import server.api.Config;

import com.dishq.buzz.ui.HomePageActivity;
import com.dishq.buzz.ui.SignUpActivity;
import com.dishq.buzz.util.Constants;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public String versionName;
    public int versionCode = 0;
    MixpanelAPI mixpanel = null;
    public String uniqueIdentifier;
    private static final String TAG = "MainActivity";
    private boolean networkFailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Google Analytics instantiation
        setContentView(R.layout.activity_main);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));

        uniqueIdentifier = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        YW8Application.getPrefs().edit().putString(Constants.UNIQUE_IDENTIFIER, uniqueIdentifier).apply();
        YW8Application.setUniqueId(uniqueIdentifier);
        Log.d(TAG, "Unique number: " + uniqueIdentifier);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;

            Log.d(TAG, pInfo.versionName + pInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        timer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(networkFailed) {
            checkNetwork();
        }

        try {
            final JSONObject properties = new JSONObject();
            properties.put("app_splash", "splash");
            mixpanel.track("app_splash", properties);
        } catch (final JSONException e) {
            throw new RuntimeException("Could not encode hour of the day in JSON");
        }

    }

    // To set the timer for the splash screen to be displayed
    Thread timer = new Thread() {
        public void run() {
            try {
                sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(networkFailed) {
                    checkNetwork();
                }
                else {
                    fetchVersion(versionName, versionCode);
                }
            }
        }
    };

    public void fetchVersion(final String versionName, final int versionCode) {
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<VersionCheckResponse> request = apiInterface.checkVersion(versionName, versionCode);
        request.enqueue(new Callback<VersionCheckResponse>() {
            @Override
            public void onResponse(Call<VersionCheckResponse> call, Response<VersionCheckResponse> response) {
                if (BuildConfig.DEBUG) {
                    Log.d("MainActivity", "Success");
                }
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
                if (BuildConfig.DEBUG) {
                    Log.d("MainActivity", "Failure");
                }
                if(!Util.checkAndShowNetworkPopup(MainActivity.this)) {
                    fetchVersion(versionName, versionCode);
                }
            }
        });

    }

    public void showAlert(String title, String message, boolean force) {
        if(!(MainActivity.this).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message).setCancelable(false)
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                    });
            if (!force) {
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

    private void checkNetwork(){
        if(!Util.checkAndShowNetworkPopup(this)){

            fetchVersion(versionName, versionCode);

        }else{
            networkFailed = true;
        }
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

}
