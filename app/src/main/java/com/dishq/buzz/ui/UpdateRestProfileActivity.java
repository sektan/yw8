package com.dishq.buzz.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

/**
 * Created by dishq on 03-11-2016.
 */

public class UpdateRestProfileActivity extends BaseActivity {

    private final static int SETTINGS_RESULT_REQ_CODE = 100;

    private GPSTrackerService gps;
    private UpdateRestaurantFinder updateRestaurantFinder;
    public int rest_id = 0;
    public static Boolean no_gps = false;
    public static Boolean yes_gps = false;
    private boolean noNetwork, didPause, locationOff;
    private ProgressDialog progressDialog, progressDialoglert;

    private double getLatitude, getLongitude;
    AlertDialog closedialog = null;
    private String query = "";
    private String restaurantName = "";
    private Boolean SHOW_BUZZ_TYPE_OPTIONS = false;
    final int MY_PERMISSIONS_REQUEST_GPS_ACCESS = 0;
    String waitTimeUpdate, serverAccessToken, buzzTypeLabel;
    int waitTime, waitTimeId = 0, buzzTypeId = 0;
    private String TAG = "UpdateRestProfile";
    private TextView restToolbarName, updateRestName, textAmbiance, tvWaitTimeOne, tvWaitTimeTwo,
            tvWaitTimeThree, tvWaitTimeFour, tvWaitTimeFive, tvMinOne, tvMinTwo, tvMinThree, tvMinFour,
            tvMinFive, tvAmbianceOne, tvAmbianceTwo;
    private ImageView restToolBarSearch, backButton;
    private CardView waitTimeOne, waitTimeTwo, waitTimeThree, waitTimeFour, waitTimeFive,
            ambianceOne, ambianceTwo;
    private LinearLayout llAmbiance;
    private Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentFromSearch = getIntent();
        if (intentFromSearch != null) {
            query = intentFromSearch.getExtras().getString("restaurant_id");
            restaurantName = intentFromSearch.getExtras().getString("restaurant_name");
        }
        setContentView(R.layout.activity_update_rest_profile);
        setTags();
        fetchWaitTimeInfo();
    }

    boolean checkGPS() {
        if (ContextCompat.checkSelfPermission(UpdateRestProfileActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // first check okie?
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
                fetchUpdatedUserInfo();
                break;

            default:
                Log.w(TAG, "Request Code " + requestCode);
                break;
        }
    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(UpdateRestProfileActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Your GPS Is Off");

        // Setting Dialog Message
        alertDialog.setMessage("Want to see the drive time to restaurants? Turn on GPS.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Turn on GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, SETTINGS_RESULT_REQ_CODE);
//                getGPS();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No thanks ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                alertNoForward(UpdateRestProfileActivity.this);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    //TODO remove the checkSelfPermission (because it is repeated).
    public void selfPermission() {
         /*if (ContextCompat.checkSelfPermission(UpdateRestProfileActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {*/ // ??? Why again?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateRestProfileActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
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
        updateRestName.setTypeface(Util.getFaceRoman());
        restToolBarSearch = (ImageView) findViewById(R.id.tvMenuFinder);
        restToolBarSearch.setVisibility(View.GONE);
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backButtonIntent = new Intent(UpdateRestProfileActivity.this, SearchActivity.class);
                backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(backButtonIntent);
                finish();
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
        ambianceOne = (CardView) findViewById(R.id.cv_ambiance_one);
        ambianceTwo = (CardView) findViewById(R.id.cv_ambiance_two);
        tvAmbianceOne = (TextView) findViewById(R.id.cv_tv_ambiance_one);
        tvAmbianceTwo = (TextView) findViewById(R.id.cv_tv_ambiance_two);
        textAmbiance = (TextView) findViewById(R.id.tv_ambiance);
        llAmbiance = (LinearLayout) findViewById(R.id.ll_current_ambiance);
        buttonUpdate = (Button) findViewById(R.id.button_update);
        setFunctionality();
    }

    private void setFunctionality() {
        if (restaurantName != null && updateRestName != null) {
            updateRestName.setText(restaurantName);
        }

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

                    textAmbiance.setVisibility(View.VISIBLE);
                    llAmbiance.setVisibility(View.VISIBLE);

                    buttonUpdate.setEnabled(false);
                    buttonUpdate.setBackgroundColor(getResources().getColor(R.color.black));
                    buttonUpdate.setAlpha(Float.parseFloat("0.3"));
                    buttonUpdate.setTextColor(getResources().getColor(R.color.white));

                    SHOW_BUZZ_TYPE_OPTIONS = true;
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

                    textAmbiance.setVisibility(View.GONE);
                    llAmbiance.setVisibility(View.GONE);

                    buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                    buttonUpdate.setAlpha(Float.parseFloat("0.6"));
                    buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                    buttonUpdate.setEnabled(true);

                    SHOW_BUZZ_TYPE_OPTIONS = false;
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

                    textAmbiance.setVisibility(View.GONE);
                    llAmbiance.setVisibility(View.GONE);

                    buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                    buttonUpdate.setAlpha(Float.parseFloat("0.6"));
                    buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                    buttonUpdate.setEnabled(true);

                    SHOW_BUZZ_TYPE_OPTIONS = false;
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

                    textAmbiance.setVisibility(View.GONE);
                    llAmbiance.setVisibility(View.GONE);

                    buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                    buttonUpdate.setAlpha(Float.parseFloat("0.6"));
                    buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                    buttonUpdate.setEnabled(true);

                    SHOW_BUZZ_TYPE_OPTIONS = false;
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

                    textAmbiance.setVisibility(View.GONE);
                    llAmbiance.setVisibility(View.GONE);

                    buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                    buttonUpdate.setAlpha(Float.parseFloat("0.6"));
                    buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                    buttonUpdate.setEnabled(true);

                    SHOW_BUZZ_TYPE_OPTIONS = false;
                    waitTimeUpdate = "30+ mins";
                    waitTime = 40;
                    waitTimeId = 5;
                }
            });
        }

        if (ambianceOne != null) {
            ambianceOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ambianceOne.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                    ambianceOne.setAlpha(Float.parseFloat("1"));
                    ambianceTwo.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    ambianceTwo.setAlpha(Float.parseFloat("0.4"));

                    tvAmbianceOne.setTextColor(getResources().getColor(R.color.white));
                    tvAmbianceTwo.setTextColor(getResources().getColor(R.color.black));

                    buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                    buttonUpdate.setAlpha(Float.parseFloat("0.6"));
                    buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                    buttonUpdate.setEnabled(true);

                    buzzTypeId = 1;
                    buzzTypeLabel = "Quiet";
                }
            });
        }

        if (ambianceTwo != null) {
            ambianceTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ambianceOne.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    ambianceOne.setAlpha(Float.parseFloat("0.15"));
                    ambianceTwo.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                    ambianceTwo.setAlpha(Float.parseFloat("1"));


                    tvAmbianceOne.setTextColor(getResources().getColor(R.color.black));
                    tvAmbianceTwo.setTextColor(getResources().getColor(R.color.white));

                    buttonUpdate.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                    buttonUpdate.setAlpha(Float.parseFloat("0.6"));
                    buttonUpdate.setTextColor(getResources().getColor(R.color.white));
                    buttonUpdate.setEnabled(true);

                    buzzTypeId = 2;
                    buzzTypeLabel = "Lively";
                }
            });
        }

        if (buttonUpdate != null) {
            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            if (getLongitude == 0 | getLatitude == 0) {
//            fetchUpdatedUserInfo(); // TODO "TEMPORARY FIX" Can be optimized, bu structuring the whole implementation
            } else {
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
                        Log.d(TAG, "Success");
                        try {
                            if (response.isSuccessful()) {
                                UpdateRestaurantResponse.UserProfileUpdateInfo body = response.body().userProfileUpdateInfo;
                                if (body != null) {
                                    updateRestaurantFinder = new UpdateRestaurantFinder(body.getHasBadgeUpgrade(), body.getNumPointsAdded(),
                                            body.currentBadgeInfo.getBadgeName(), body.currentBadgeInfo.getBadgeLevel());

                                    YW8Application.getPrefs().edit().putInt(Constants.NUM_POINTS_ADDED, updateRestaurantFinder.getNumPointsAdded());
                                    YW8Application.setNumPointsAdded(updateRestaurantFinder.getNumPointsAdded());
                                    progressDialoglert = new ProgressDialog(UpdateRestProfileActivity.this);
                                    progressDialoglert.show();
                                    checkWhereToGo(updateRestaurantFinder);

                                }
                            } else {
                                String error = response.errorBody().string();
                                Log.d("UpdateRestaurant", error);
                                progressDialoglert = new ProgressDialog(UpdateRestProfileActivity.this);
                                progressDialoglert.show();
                                if (response.code() == 412) {
                                    alertTooFrequent(UpdateRestProfileActivity.this);
                                } else if (response.code() == 406) {
                                    alertFarOff(UpdateRestProfileActivity.this);
                                } else {

                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<UpdateRestaurantResponse> call, Throwable t) {
                        Log.d(TAG, "Failure of connecting to the server");
                    }
                });
            }
        }
    }

    public void alertNoForward(final Activity activity) {
        if (progressDialoglert != null) {
            progressDialoglert.dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Can't update without GPS");
        builder.setCancelable(true);

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
                .setMessage("You can't update buzz for same restaurant too often ")
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
    protected void onDestroy() {
        super.onDestroy();
        gps.stopUsingGPS();
    }

}
