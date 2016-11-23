package com.dishq.buzz.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.services.GPSTrackerService;
import com.dishq.buzz.util.Constants;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Finder.UpdateRestaurantFinder;
import server.Request.UpdateRestaurantHelper;
import server.Response.UpdateRestaurantResponse;
import server.Response.UpdateWaitTimeResponse;
import server.api.ApiInterface;
import server.api.Config;

import static com.dishq.buzz.util.Util.latitude;
import static com.dishq.buzz.util.Util.longitude;
import static com.dishq.buzz.util.Util.restaurantName;

/**
 * Created by dishq on 03-11-2016.
 */

public class UpdateRestProfileActivity extends BaseActivity {

    private final static int SETTINGS_RESULT_REQ_CODE = 100;
    private final static int NON_SETTINGS_RESULT_REQ_CODE = 200;

    private GPSTrackerService gps;
    private UpdateRestaurantFinder updateRestaurantFinder;
    public int rest_id = 0;
    public static Boolean no_gps = false;
    MixpanelAPI mixpanel = null;
    public static Boolean yes_gps = false;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private boolean noNetwork, didPause, locationOff;
    private ProgressDialog progressDialog, progressDialoglert, progressGpsAlert;

    private double getLatitude, getLongitude;
    AlertDialog closedialog = null;
    private String query = "";
    private String restaurantName = "", restAdd = "";
    private Boolean isUpdateClicked = false, isSettingsShowing = false;
    final int MY_PERMISSIONS_REQUEST_GPS_ACCESS = 0;
    String waitTimeUpdate, serverAccessToken, buzzTypeLabel;
    int waitTime, waitTimeId = 0, buzzTypeId = 0;
    private String TAG = "UpdateRestProfile";
    private TextView restToolbarName, updateRestName, updateRestAddr, textAmbiance, tvWaitTimeOne, tvWaitTimeTwo,
            tvWaitTimeThree, tvWaitTimeFour, tvWaitTimeFive, tvMinOne, tvMinTwo, tvMinThree, tvMinFour,
            tvMinFive, tvAmbianceOne, tvAmbianceTwo;
    private ImageView restToolBarSearch, backButton;
    private CardView waitTimeOne, waitTimeTwo, waitTimeThree, waitTimeFour, waitTimeFive,
            ambianceOne, ambianceTwo;
    //private LinearLayout llAmbiance;
    private Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));
        isUpdateClicked = false;

        query = Util.getRestId();
        restaurantName = Util.getRestaurantName();
        restAdd = Util.getRestAddr();
        setContentView(R.layout.activity_update_rest_profile);
        setTags();
        fetchWaitTimeInfo();
    }

    boolean checkGPS() {
        if (ContextCompat.checkSelfPermission(UpdateRestProfileActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // first check
            return getGPS();
        } else if (ContextCompat.checkSelfPermission(UpdateRestProfileActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            selfPermission();
        }
        return false;
    }

    public boolean getGPS() {
        if (!no_gps || (latitude == 17.77)) {
            gps = new GPSTrackerService(this);
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                getLatitude = latitude;
                longitude = gps.getLongitude();
                getLongitude = longitude;
                return true;
            } else {
                locationOff = true;
                progressDialog.dismiss();
                showSettingsAlert();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SETTINGS_RESULT_REQ_CODE:
                if (!(UpdateRestProfileActivity.this).isFinishing()) {
                    progressGpsAlert = new ProgressDialog(UpdateRestProfileActivity.this);
                    progressGpsAlert.show();
                }
                fetchUpdatedUserInfo();
                break;
            case NON_SETTINGS_RESULT_REQ_CODE:
                getGPS();
            fetchUpdatedUserInfo();
            default:
                if (!(UpdateRestProfileActivity.this).isFinishing()) {
                    progressGpsAlert = new ProgressDialog(UpdateRestProfileActivity.this);
                    progressGpsAlert.show();
                }
                getGPS();
                fetchUpdatedUserInfo();
                Log.w(TAG, "Request Code " + requestCode);
                break;
        }
    }

    public void showSettingsAlert() {
        displayLocationSettingsRequest(UpdateRestProfileActivity.this);
//        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(UpdateRestProfileActivity.this);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Your GPS Is Off");
//
//        // Setting Dialog Message
//        alertDialog.setMessage(" Turn on GPS to update restaurant wait time");
//
//        // On pressing Settings button
//        alertDialog.setPositiveButton("Turn on GPS", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivityForResult(intent, SETTINGS_RESULT_REQ_CODE);
//            }
//        });
//
//        // on pressing cancel button
//        alertDialog.setNegativeButton("No thanks ", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                alertNoForward(UpdateRestProfileActivity.this);
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
    }

    public void selfPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateRestProfileActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            progressDialog.dismiss();
            Log.e("accept", "accept");
            Toast.makeText(this, "Enable Location Permission to access this feature", Toast.LENGTH_SHORT).show(); // Something like this

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } else {
            //request the permission
            Log.e("accept", "not accept");
            progressDialog.dismiss();
            ActivityCompat.requestPermissions(UpdateRestProfileActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GPS_ACCESS);
        }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GPS_ACCESS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getGPS();
                } else {
                    showAlert("", "That permission is needed in order to update wait time. Tap Retry.");
                }
            }
        }
    }

    public void showAlert(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateRestProfileActivity.this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(UpdateRestProfileActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(UpdateRestProfileActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_GPS_ACCESS);
                        }
                    }
                });


        android.app.AlertDialog alert = builder.create();
        alert.show();
        TextView message1 = (TextView) alert.findViewById(android.R.id.message);
        //assert message != null;
        message1.setLineSpacing(0, 1.5f);

    }

    @Override
    protected void onPause() {
        super.onPause();
        didPause = true;
    }

    private void setTags() {
        restToolbarName = (TextView) findViewById(R.id.toolbarTitle);
        restToolbarName.setText(getResources().getString(R.string.update));
        restToolbarName.setTypeface(Util.getFaceMedium());
        updateRestName = (TextView) findViewById(R.id.cv_up_rest_name);
        updateRestName.setTypeface(null, Typeface.BOLD);
        updateRestName.setTypeface(Util.getFaceRoman());
        updateRestAddr = (TextView) findViewById(R.id.cv_up_rest_add);
        updateRestAddr.setTypeface(Util.getFaceRoman());
        updateRestAddr.setText(restAdd);
        restToolBarSearch = (ImageView) findViewById(R.id.tvMenuFinder);
        restToolBarSearch.setVisibility(View.GONE);
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backButtonIntent = new Intent(UpdateRestProfileActivity.this, SearchActivity.class);
                ;
                backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(backButtonIntent);
            }
        });
        TextView currentWaitTime = (TextView) findViewById(R.id.tv_current_wait);
        currentWaitTime.setTypeface(Util.getFaceRoman());
        waitTimeOne = (CardView) findViewById(R.id.cv_wait_one);
        waitTimeTwo = (CardView) findViewById(R.id.cv_wait_two);
        waitTimeThree = (CardView) findViewById(R.id.cv_wait_three);
        waitTimeFour = (CardView) findViewById(R.id.cv_wait_four);
        waitTimeFive = (CardView) findViewById(R.id.cv_wait_five);
        tvWaitTimeOne = (TextView) findViewById(R.id.cv_tv_wait_time_one);
        tvWaitTimeOne.setTypeface(Util.getFaceMedium());
        tvWaitTimeTwo = (TextView) findViewById(R.id.cv_tv_wait_time_two);
        tvWaitTimeTwo.setTypeface(Util.getFaceMedium());
        tvWaitTimeThree = (TextView) findViewById(R.id.cv_tv_wait_time_three);
        tvWaitTimeThree.setTypeface(Util.getFaceMedium());
        tvWaitTimeFour = (TextView) findViewById(R.id.cv_tv_wait_time_four);
        tvWaitTimeFour.setTypeface(Util.getFaceMedium());
        tvWaitTimeFive = (TextView) findViewById(R.id.cv_tv_wait_time_five);
        tvWaitTimeFive.setTypeface(Util.getFaceMedium());
        tvMinOne = (TextView) findViewById(R.id.cv_tv_min_one);
        tvMinOne.setTypeface(Util.getFaceMedium());
        tvMinTwo = (TextView) findViewById(R.id.cv_tv_min_two);
        tvMinTwo.setTypeface(Util.getFaceMedium());
        tvMinThree = (TextView) findViewById(R.id.cv_tv_min_three);
        tvMinThree.setTypeface(Util.getFaceMedium());
        tvMinFour = (TextView) findViewById(R.id.cv_tv_min_four);
        tvMinFour.setTypeface(Util.getFaceMedium());
        tvMinFive = (TextView) findViewById(R.id.cv_tv_min_five);
        tvMinFive.setTypeface(Util.getFaceMedium());
//        ambianceOne = (CardView) findViewById(R.id.cv_ambiance_one);
//        ambianceTwo = (CardView) findViewById(R.id.cv_ambiance_two);
//        tvAmbianceOne = (TextView) findViewById(R.id.cv_tv_ambiance_one);
//        tvAmbianceOne.setTypeface(Util.getFaceRoman());
//        tvAmbianceTwo = (TextView) findViewById(R.id.cv_tv_ambiance_two);
//        tvAmbianceTwo.setTypeface(Util.getFaceRoman());
//        textAmbiance = (TextView) findViewById(R.id.tv_ambiance);
//        textAmbiance.setTypeface(Util.getFaceRoman());
//        llAmbiance = (LinearLayout) findViewById(R.id.ll_current_ambiance);
        buttonUpdate = (Button) findViewById(R.id.button_update);
        buttonUpdate.setTypeface(Util.getFaceMedium());
        waitTimeOne.setCardBackgroundColor(getResources().getColor(R.color.white));
        waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.cardOne));
        waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.cardTwo));
        waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.cardThree));
        waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.cardFour));

        tvWaitTimeOne.setTextColor(getResources().getColor(R.color.black));
        tvWaitTimeTwo.setTextColor(getResources().getColor(R.color.black));
        tvWaitTimeThree.setTextColor(getResources().getColor(R.color.black));
        tvWaitTimeFour.setTextColor(getResources().getColor(R.color.black));
        tvWaitTimeFive.setTextColor(getResources().getColor(R.color.black));

        tvMinOne.setTextColor(getResources().getColor(R.color.black));
        tvMinTwo.setTextColor(getResources().getColor(R.color.black));
        tvMinThree.setTextColor(getResources().getColor(R.color.black));
        tvMinFour.setTextColor(getResources().getColor(R.color.black));
        tvMinFive.setTextColor(getResources().getColor(R.color.black));
        setFunctionality();
    }

    public void setOnclickables(Boolean onOrOff) {
        waitTimeOne.setClickable(onOrOff);
        waitTimeTwo.setClickable(onOrOff);
        waitTimeThree.setClickable(onOrOff);
        waitTimeFour.setClickable(onOrOff);
        waitTimeFive.setClickable(onOrOff);
    }

    private void setFunctionality() {
        if (restaurantName != null && updateRestName != null) {
            updateRestName.setText(restaurantName);
        }

        if (!isUpdateClicked) {
            setOnclickables(true);

            if (waitTimeOne != null) {
                waitTimeOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        waitTimeOne.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                        waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.cardOne));
                        waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.cardTwo));
                        waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.cardThree));
                        waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.cardFour));

                        tvWaitTimeOne.setTextColor(getResources().getColor(R.color.white));
                        tvWaitTimeTwo.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeThree.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeFour.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeFive.setTextColor(getResources().getColor(R.color.black));

                        tvMinOne.setTextColor(getResources().getColor(R.color.white));
                        tvMinTwo.setTextColor(getResources().getColor(R.color.black));
                        tvMinThree.setTextColor(getResources().getColor(R.color.black));
                        tvMinFour.setTextColor(getResources().getColor(R.color.black));
                        tvMinFive.setTextColor(getResources().getColor(R.color.black));

//                    textAmbiance.setVisibility(View.VISIBLE);
//                    llAmbiance.setVisibility(View.VISIBLE);

                        buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                        buttonUpdate.setAlpha(Float.parseFloat("1"));
                        buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                        buttonUpdate.setEnabled(true);

                        waitTimeUpdate = "0 mins";
                        waitTime = 0;
                        waitTimeId = 1;
                    }
                });
            }

            if (waitTimeTwo != null) {
                waitTimeTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        waitTimeOne.setCardBackgroundColor(getResources().getColor(R.color.white));
                        waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                        waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.cardTwo));
                        waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.cardThree));
                        waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.cardFour));

                        tvWaitTimeOne.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeTwo.setTextColor(getResources().getColor(R.color.white));
                        tvWaitTimeThree.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeFour.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeFive.setTextColor(getResources().getColor(R.color.black));

                        tvMinOne.setTextColor(getResources().getColor(R.color.black));
                        tvMinTwo.setTextColor(getResources().getColor(R.color.white));
                        tvMinThree.setTextColor(getResources().getColor(R.color.black));
                        tvMinFour.setTextColor(getResources().getColor(R.color.black));
                        tvMinFive.setTextColor(getResources().getColor(R.color.black));

//                    textAmbiance.setVisibility(View.GONE);
//                    llAmbiance.setVisibility(View.GONE);

                        buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                        buttonUpdate.setAlpha(Float.parseFloat("1"));
                        buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                        buttonUpdate.setEnabled(true);

                        waitTimeUpdate = "10 mins";
                        waitTime = 10;
                        waitTimeId = 2;
                    }
                });
            }

            if (waitTimeThree != null) {
                waitTimeThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        waitTimeOne.setCardBackgroundColor(getResources().getColor(R.color.white));
                        waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.cardOne));
                        waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                        waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.cardThree));
                        waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.cardFour));

                        tvWaitTimeOne.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeTwo.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeThree.setTextColor(getResources().getColor(R.color.white));
                        tvWaitTimeFour.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeFive.setTextColor(getResources().getColor(R.color.black));

                        tvMinOne.setTextColor(getResources().getColor(R.color.black));
                        tvMinTwo.setTextColor(getResources().getColor(R.color.black));
                        tvMinThree.setTextColor(getResources().getColor(R.color.white));
                        tvMinFour.setTextColor(getResources().getColor(R.color.black));
                        tvMinFive.setTextColor(getResources().getColor(R.color.black));

//                    textAmbiance.setVisibility(View.GONE);
//                    llAmbiance.setVisibility(View.GONE);

                        buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                        buttonUpdate.setAlpha(Float.parseFloat("1"));
                        buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                        buttonUpdate.setEnabled(true);

                        waitTimeUpdate = "20 mins";
                        waitTime = 20;
                        waitTimeId = 3;
                    }
                });
            }

            if (waitTimeFour != null) {
                waitTimeFour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        waitTimeOne.setCardBackgroundColor(getResources().getColor(R.color.white));
                        waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.cardOne));
                        waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.cardTwo));
                        waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                        waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.cardFour));

                        tvWaitTimeOne.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeTwo.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeThree.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeFour.setTextColor(getResources().getColor(R.color.white));
                        tvWaitTimeFive.setTextColor(getResources().getColor(R.color.black));

                        tvMinOne.setTextColor(getResources().getColor(R.color.black));
                        tvMinTwo.setTextColor(getResources().getColor(R.color.black));
                        tvMinThree.setTextColor(getResources().getColor(R.color.black));
                        tvMinFour.setTextColor(getResources().getColor(R.color.white));
                        tvMinFive.setTextColor(getResources().getColor(R.color.black));

//                    textAmbiance.setVisibility(View.GONE);
//                    llAmbiance.setVisibility(View.GONE);

                        buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                        buttonUpdate.setAlpha(Float.parseFloat("1"));
                        buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                        buttonUpdate.setEnabled(true);

                        waitTimeUpdate = "30 mins";
                        waitTime = 30;
                        waitTimeId = 4;
                    }
                });
            }

            if (waitTimeFive != null) {
                waitTimeFive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        waitTimeOne.setCardBackgroundColor(getResources().getColor(R.color.white));
                        waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.cardOne));
                        waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.cardTwo));
                        waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.cardThree));
                        waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));

                        tvWaitTimeOne.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeTwo.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeThree.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeFour.setTextColor(getResources().getColor(R.color.black));
                        tvWaitTimeFive.setTextColor(getResources().getColor(R.color.white));

                        tvMinOne.setTextColor(getResources().getColor(R.color.black));
                        tvMinTwo.setTextColor(getResources().getColor(R.color.black));
                        tvMinThree.setTextColor(getResources().getColor(R.color.black));
                        tvMinFour.setTextColor(getResources().getColor(R.color.black));
                        tvMinFive.setTextColor(getResources().getColor(R.color.white));

//                    textAmbiance.setVisibility(View.GONE);
//                    llAmbiance.setVisibility(View.GONE);

                        buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                        buttonUpdate.setAlpha(Float.parseFloat("1"));
                        buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                        buttonUpdate.setEnabled(true);

                        waitTimeUpdate = "30+ mins";
                        waitTime = 40;
                        waitTimeId = 5;
                    }
                });
            }
        } else {
            setOnclickables(false);
        }

//        if (ambianceOne != null) {
//            ambianceOne.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ambianceOne.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
//                    ambianceTwo.setCardBackgroundColor(getResources().getColor(R.color.cardThree));
//
//                    tvAmbianceOne.setTextColor(getResources().getColor(R.color.white));
//                    tvAmbianceTwo.setTextColor(getResources().getColor(R.color.black));
//
//                    buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
//                    buttonUpdate.setAlpha(Float.parseFloat("1"));
//                    buttonUpdate.setTextColor(getResources().getColor(R.color.white));
//                    buttonUpdate.setEnabled(true);
//
//                    buzzTypeId = 1;
//                    buzzTypeLabel = "Quiet";
//                }
//            });
//        }
//
//        if (ambianceTwo != null) {
//            ambianceTwo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ambianceOne.setCardBackgroundColor(getResources().getColor(R.color.cardOne));
//                    ambianceTwo.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
//
//
//                    tvAmbianceOne.setTextColor(getResources().getColor(R.color.black));
//                    tvAmbianceTwo.setTextColor(getResources().getColor(R.color.white));
//
//                    buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
//                    buttonUpdate.setAlpha(Float.parseFloat("1"));
//                    buttonUpdate.setTextColor(getResources().getColor(R.color.white));
//                    buttonUpdate.setEnabled(true);
//
//                    buzzTypeId = 2;
//                    buzzTypeLabel = "Lively";
//                }
//            });
//        }

        if (buttonUpdate != null) {
            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isUpdateClicked = true;
                    setOnclickables(false);
                    if (!(UpdateRestProfileActivity.this).isFinishing()) {
                        progressDialog = new ProgressDialog(UpdateRestProfileActivity.this);
                        progressDialog.show();
                    }
                    fetchUpdatedUserInfo();
                    buttonUpdate.setEnabled(false);
                }
            });
        }
    }

    private void fetchWaitTimeInfo() {
        Call<UpdateWaitTimeResponse> request;
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        request = apiInterface.getUpdateWaitTime();
        request.enqueue(new Callback<UpdateWaitTimeResponse>() {

            @Override
            public void onResponse(Call<UpdateWaitTimeResponse> call, Response<UpdateWaitTimeResponse> response) {
                Log.d(TAG, "Success");
            }

            @Override
            public void onFailure(Call<UpdateWaitTimeResponse> call, Throwable t) {
                Log.d(TAG, "Failure of connecting to the server");
            }
        });
    }

    private void fetchUpdatedUserInfo() {
        if (checkGPS()) {
            rest_id = Integer.parseInt(query);
            final UpdateRestaurantHelper updateRestaurantHelper = new UpdateRestaurantHelper(rest_id,
                    getLatitude, getLongitude, waitTimeId, buzzTypeId);
            ApiInterface apiInterface = Config.createService(ApiInterface.class);
            serverAccessToken = YW8Application.getAccessToken();
            Call<UpdateRestaurantResponse> call = apiInterface.updateRestUserProf(serverAccessToken,
                    updateRestaurantHelper);
            call.enqueue(new Callback<UpdateRestaurantResponse>() {
                @Override
                public void onResponse(Call<UpdateRestaurantResponse> call, Response<UpdateRestaurantResponse> response) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put(waitTimeUpdate, waitTimeUpdate);
                        mixpanel.track(waitTimeUpdate, properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }

                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put(buzzTypeLabel, buzzTypeLabel);
                        mixpanel.track(buzzTypeLabel, properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }

                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("update", "update");
                        mixpanel.track("update", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (progressGpsAlert != null) {
                        progressGpsAlert.dismiss();
                    }
                    Log.d(TAG, "Success");
                    try {
                        if (response.isSuccessful()) {
                            UpdateRestaurantResponse.UserProfileUpdateInfo body = response.body().userProfileUpdateInfo;
                            if (body != null) {
                                updateRestaurantFinder = new UpdateRestaurantFinder(body.getHasBadgeUpgrade(), body.getNumPointsAdded(),
                                        body.currentBadgeInfo.getBadgeName(), body.currentBadgeInfo.getBadgeLevel());

                                YW8Application.getPrefs().edit().putInt(Constants.NUM_POINTS_ADDED, updateRestaurantFinder.getNumPointsAdded()).apply();
                                YW8Application.setNumPointsAdded(updateRestaurantFinder.getNumPointsAdded());
                                if (!(UpdateRestProfileActivity.this).isFinishing()) {
                                    progressDialoglert = new ProgressDialog(UpdateRestProfileActivity.this);
                                    progressDialoglert.show();
                                }
                                checkWhereToGo(updateRestaurantFinder);

                            }
                        } else {
                            String error = response.errorBody().string();
                            Log.d("UpdateRestaurant", error);
                            if (!(UpdateRestProfileActivity.this).isFinishing()) {
                                progressDialoglert = new ProgressDialog(UpdateRestProfileActivity.this);
                                progressDialoglert.show();
                                if (response.code() == 412) {
                                    alertTooFrequent(UpdateRestProfileActivity.this);
                                } else if (response.code() == 406) {
                                    alertFarOff(UpdateRestProfileActivity.this);
                                } else if (response.code() == 400) {
                                    alertTryAgain(UpdateRestProfileActivity.this);
                                }
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<UpdateRestaurantResponse> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    if(!Util.checkAndShowNetworkPopup(UpdateRestProfileActivity.this)){
                        fetchUpdatedUserInfo();
                    }else {
                        alertServerConnectFailure(UpdateRestProfileActivity.this);
                    }

                    Log.d(TAG, "Failure of connecting to the server");
                }
            });

        }
    }

    public void alertServerConnectFailure(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage("Oops, something went wrong, please try again")
                .setCancelable(false)
                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent backButtonIntent = new Intent(UpdateRestProfileActivity.this, UpdateRestProfileActivity.class);
                        backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        finish();
                        startActivity(backButtonIntent);
                    }
                })
                .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                })
                .create();
        if (progressDialoglert != null) {
            progressDialoglert.dismiss();
        }
        dialog.show();
    }

    public void alertNoForward(final Activity activity) {
        if (progressDialoglert != null) {
            progressDialoglert.dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Can't update without GPS");
        builder.setCancelable(false);

        closedialog = builder.create();

        closedialog.show();

        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                closedialog.dismiss();
                timer2.cancel(); //this will cancel the timer of the system
                Intent goToHomePageIntent = new Intent(UpdateRestProfileActivity.this, HomePageActivity.class);
                finish();
                startActivity(goToHomePageIntent);
            }
        }, 3000); // the timer will count 3 seconds....
    }

    public void alertTooFrequent(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(getResources().getString(R.string.too_often_update))
                .setCancelable(false)
                .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backButtonIntent = new Intent(UpdateRestProfileActivity.this, HomePageActivity.class);
                        backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(backButtonIntent);
                    }
                })
                .create();
        if (progressDialoglert != null) {
            progressDialoglert.dismiss();
        }
        dialog.show();

    }

    public void alertFarOff(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage("You can't give the waiting time unless you are at the place ")
                .setCancelable(false)
                .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backButtonIntent = new Intent(UpdateRestProfileActivity.this, HomePageActivity.class);
                        backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(backButtonIntent);
                    }
                })
                .create();
        if (progressDialoglert != null) {
            progressDialoglert.dismiss();
        }
        dialog.show();

    }

    public void alertTryAgain(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage("Oops, something went wrong, please try again")
                .setCancelable(false)
                .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backButtonIntent = new Intent(UpdateRestProfileActivity.this, UpdateRestProfileActivity.class);
                        backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        finish();
                        startActivity(backButtonIntent);
                    }
                })
                .create();
        if (progressDialoglert != null) {
            progressDialoglert.dismiss();
        }
        dialog.show();
    }

    public void checkWhereToGo(UpdateRestaurantFinder updateRestaurantFinder) {
        if (updateRestaurantFinder != null) {
            Boolean badgeUpgrade = updateRestaurantFinder.getHasBadgeUpgrade();
            if (badgeUpgrade == true) {
                Intent intent = new Intent(UpdateRestProfileActivity.this, BigBadgeActivity.class);
                intent.putExtra("badge_name", updateRestaurantFinder.getBadgeName());
                intent.putExtra("badge_level", updateRestaurantFinder.getBadgeLevel());
                finish();
                startActivity(intent);
            } else {
                createAlertDialog(UpdateRestProfileActivity.this);
            }
        } else {
            Log.d(TAG, "fail");
        }

    }

    public void createAlertDialog(final Activity activity) {
        if (progressDialoglert != null) {
            progressDialoglert.dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(String.format(getResources().getString(R.string.num_of_points_added),
                Integer.toString(YW8Application.getNumPointsAdded())));
        builder.setCancelable(true);

        closedialog = builder.create();

        closedialog.show();

        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                closedialog.dismiss();
                timer2.cancel(); //this will cancel the timer of the system
                Intent goToHomePageIntent = new Intent(UpdateRestProfileActivity.this, UserProfileActivity.class);
                finish();
                startActivity(goToHomePageIntent);
            }
        }, 3000); // the timer will count 3 seconds....
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent searchBackIntent = new Intent(this, SearchActivity.class);
        searchBackIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(searchBackIntent);
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(UpdateRestProfileActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        alertNoForward(UpdateRestProfileActivity.this);
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

}
