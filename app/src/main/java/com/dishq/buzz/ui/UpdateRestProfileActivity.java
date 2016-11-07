package com.dishq.buzz.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.services.GPSTrackerService;
import com.dishq.buzz.util.Util;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Finder.SignUpInfoFinder;
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

    private GPSTrackerService gps;
    private UpdateRestaurantFinder updateRestaurantFinder;
    public int rest_id = 0;
    private static String facebookOrGoogle = "";
    public static Boolean no_gps = false;
    public static Boolean yes_gps = false;
    private boolean noNetwork, didPause, locationOff;
    private ProgressDialog progressDialog;

    private double getLatitude, getLongitude;

    private String query = "";
    private SignUpInfoFinder signUpInfoFinder;
    private String restaurantName = "";
    private Boolean SHOW_BUZZ_TYPE_OPTIONS = false;
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
        setContentView(R.layout.activity_update_rest_profile);

        Intent intentFromSearch = getIntent();
        if (intentFromSearch != null) {
            query = intentFromSearch.getExtras().getString("restaurant_id");
            restaurantName = intentFromSearch.getExtras().getString("restaurant_name");
            facebookOrGoogle = intentFromSearch.getExtras().getString("signup_option");
        }
        setTags();
        fetchWaitTimeInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkAndShowNetworkPopup(UpdateRestProfileActivity.this)) {
            Log.e("sd", "4");
            if (noNetwork || (didPause && locationOff)) {
                Log.e("sd", "5");
                if (!no_gps && !yes_gps) {
                    checkGPS();
                }
            }
        }
    }

    void checkGPS() {
        if (!no_gps || (latitude == 17.77)) {
            gps = new GPSTrackerService(this);
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                getLatitude = latitude;
                longitude = gps.getLongitude();
                getLongitude = longitude;
            } else {
                locationOff = true;
                gps.showSettingsAlert();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        didPause = true;
    }

    private void setTags() {
        restToolbarName = (TextView) findViewById(R.id.toolbarTitle);
        updateRestName = (TextView) findViewById(R.id.cv_up_rest_name);
        restToolBarSearch = (ImageView) findViewById(R.id.tvMenuFinder);
        backButton = (ImageView) findViewById(R.id.back_button);
        waitTimeOne = (CardView) findViewById(R.id.cv_wait_one);
        waitTimeTwo = (CardView) findViewById(R.id.cv_wait_two);
        waitTimeThree = (CardView) findViewById(R.id.cv_wait_three);
        waitTimeFour = (CardView) findViewById(R.id.cv_wait_four);
        waitTimeFive = (CardView) findViewById(R.id.cv_wait_five);
        tvWaitTimeOne = (TextView) findViewById(R.id.cv_tv_wait_time_one);
        tvWaitTimeTwo = (TextView) findViewById(R.id.cv_tv_wait_time_two);
        tvWaitTimeThree = (TextView) findViewById(R.id.cv_tv_wait_time_three);
        tvWaitTimeFour = (TextView) findViewById(R.id.cv_tv_wait_time_four);
        tvWaitTimeFive = (TextView) findViewById(R.id.cv_tv_wait_time_five);
        tvMinOne = (TextView) findViewById(R.id.cv_tv_min_one);
        tvMinTwo = (TextView) findViewById(R.id.cv_tv_min_two);
        tvMinThree = (TextView) findViewById(R.id.cv_tv_min_three);
        tvMinFour = (TextView) findViewById(R.id.cv_tv_min_four);
        tvMinFive = (TextView) findViewById(R.id.cv_tv_min_five);
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
        if (restToolBarSearch != null) {
            restToolBarSearch.setVisibility(View.GONE);
        }
        if (restToolbarName != null) {
            restToolbarName.setText(getResources().getString(R.string.update));
        }
        if (restaurantName != null && updateRestName != null) {
            updateRestName.setText(restaurantName);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backButtonIntent = new Intent(UpdateRestProfileActivity.this, SearchActivity.class);
                backButtonIntent.putExtra("signup_option", facebookOrGoogle);
                finish();
                startActivity(backButtonIntent);
            }
        });

        if (waitTimeOne != null) {
            waitTimeOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    waitTimeOne.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                    waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.1"));
                    waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.3"));
                    waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.4"));
                    waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.6"));

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
                    waitTimeTwo.setAlpha(Float.parseFloat("1"));
                    waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.3"));
                    waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.4"));
                    waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.6"));

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
                    waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.1"));
                    waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                    waitTimeThree.setAlpha(Float.parseFloat("1"));
                    waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.4"));
                    waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.6"));

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
                    waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.1"));
                    waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.3"));
                    waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                    waitTimeFour.setAlpha(Float.parseFloat("1"));
                    waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.6"));

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
                    waitTimeTwo.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.15"));
                    waitTimeThree.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.3"));
                    waitTimeFour.setCardBackgroundColor(getResources().getColor(R.color.blueGreen));
                    waitTimeTwo.setAlpha(Float.parseFloat("0.45"));
                    waitTimeFive.setCardBackgroundColor(getResources().getColor(R.color.lightPurple));
                    waitTimeFive.setAlpha(Float.parseFloat("1"));

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
        checkGPS();
        rest_id= Integer.parseInt(query);
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(UpdateRestProfileActivity.this);
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                final UpdateRestaurantHelper updateRestaurantHelper = new UpdateRestaurantHelper(rest_id,
                        getLatitude, getLongitude, waitTimeId, buzzTypeId);
                ApiInterface apiInterface = Config.createService(ApiInterface.class);
                String tokenType = SignUpActivity.getTokenType();
                String access = SignUpActivity.getAccessToken();
                serverAccessToken = tokenType + " " + access;
                Call<UpdateRestaurantResponse> call = apiInterface.updateRestUserProf(serverAccessToken,
                        updateRestaurantHelper);
                call.enqueue(new Callback<UpdateRestaurantResponse>() {
                    @Override
                    public void onResponse(Call<UpdateRestaurantResponse> call, Response<UpdateRestaurantResponse> response) {
                        Log.d(TAG, "Success");
                        try {
                            if(response.isSuccessful()) {
                                UpdateRestaurantResponse.UserProfileUpdateInfo body = response.body().userProfileUpdateInfo;
                                if(body!=null) {
                                    updateRestaurantFinder = new UpdateRestaurantFinder(body.getHasBadgeUpgrade(), body.getNumPointsAdded(),
                                            body.currentBadgeInfo.getBadgeName(), body.currentBadgeInfo.getBadgeLevel());
                                    checkWhereToGo(updateRestaurantFinder);

                                }
                            }else {
                                String error = response.errorBody().string();
                                Log.d("UpdateRestaurant", error);
                                alertFarOff(UpdateRestProfileActivity.this);

                            }
                        } catch(IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<UpdateRestaurantResponse> call, Throwable t) {
                        Log.d(TAG, "Failure of connecting to the server");
                    }
                });
                return true;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                super.onPostExecute(b);
                progressDialog.dismiss();
            }
        };
        task.execute();

    }

    public void alertFarOff(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage("You can't give the waiting time unless you are at the place ")
                .setCancelable(false)
                .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backButtonIntent = new Intent(UpdateRestProfileActivity.this, HomePageActivity.class);
                        backButtonIntent.putExtra("signup_option", facebookOrGoogle);
                        finish();
                        startActivity(backButtonIntent);
                    }
                })
                .create();
        dialog.show();
    }

    public void checkWhereToGo(UpdateRestaurantFinder updateRestaurantFinder) {
        if(updateRestaurantFinder!=null) {
            Boolean badgeUpgrade = updateRestaurantFinder.getHasBadgeUpgrade();
            if(badgeUpgrade==true) {
                Intent intent = new Intent(UpdateRestProfileActivity.this, BigBadgeActivity.class);
                intent.putExtra("signup_option", facebookOrGoogle);
                intent.putExtra("badge_name", updateRestaurantFinder.getBadgeName());
                intent.putExtra("badge_level", updateRestaurantFinder.getBadgeLevel());
                finish();
                startActivity(intent);
            } else {
                createAlertDialog(UpdateRestProfileActivity.this);
                Intent goToHomePageIntent = new Intent(UpdateRestProfileActivity.this, HomePageActivity.class);
                goToHomePageIntent.putExtra("signup_option", facebookOrGoogle);
                finish();
                startActivity(goToHomePageIntent);
            }
        }else{
            Log.d(TAG, "fail");
        }

    }

    public void createAlertDialog(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage("Thanks! You just Earned 20 points ")
                .setCancelable(false)
                .create();
        dialog.show();

        //TextView message = (TextView) dialog.findViewById(android.R.id.message);
       // assert message != null;
    }

    public static boolean checkAndShowNetworkPopup(final Activity activity) {
        if (!Util.isOnline(false)) {

            AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("No Internet Detected")
                    .setMessage("Please try again when you're online. ")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            System.exit(0);
                        }
                    })
                    .create();
            dialog.show();

            TextView message = (TextView) dialog.findViewById(android.R.id.message);
            assert message != null;
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backButtonIntent = new Intent(UpdateRestProfileActivity.this, SearchActivity.class);
        backButtonIntent.putExtra("signup_option", facebookOrGoogle);
        finish();
        startActivity(backButtonIntent);
    }

}
