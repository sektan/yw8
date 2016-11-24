package com.dishq.buzz.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Constants;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.firebase.auth.FirebaseAuth;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Response.ShortUserDetailsResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 24-10-2016.
 * Class contains the main page of the app
 */

public class HomePageActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ProgressDialog progressDialog;
    MixpanelAPI mixpanel = null;
    private static String serverAccessToken;
    private String goingToSearch = "";
    private GoogleApiClient mGoogleApiClient;
    private Button searchButton, updateButton;
    private CardView userProfileCard;
    private ImageView spBadgeImage;
    private TextView spBadgeName, spUserName, spUserPoints, spPointsInfo, tvCheckWaitTime, tvGiveWaitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));
        //Facebook SDK is initialized
        progressDialog = new ProgressDialog(HomePageActivity.this);
        progressDialog.show();

        String serverClientId = "54832716150-9d6pd2m4ttlcllelrpifbthke4t5eckb.apps.googleusercontent.com";
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(serverClientId)
                .requestIdToken(serverClientId)
                .build();

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addOnConnectionFailedListener(this).
                        addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Plus.API)
                .build();
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_page_toolbar);
        setSupportActionBar(toolbar);
        setTags();
        fetchShortUserProfile();

    }

    void setTags() {
        tvCheckWaitTime = (TextView) findViewById(R.id.check_waiting_time);
        tvCheckWaitTime.setTypeface(Util.getFaceRoman());
        tvGiveWaitTime = (TextView) findViewById(R.id.give_wait_time);
        tvGiveWaitTime.setTypeface(Util.getFaceRoman());
        searchButton = (Button) findViewById(R.id.waiting_time_search);
        searchButton.setTypeface(Util.getFaceMedium());
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goingToSearch = "restaurant";
                YW8Application.getPrefs().edit().putString(Constants.GOING_TO_SEARCH, goingToSearch).apply();
                YW8Application.setGoingToSearch(goingToSearch);
                Intent intentSearch = new Intent(HomePageActivity.this, SearchActivity.class);
                intentSearch.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("go2search", "home");
                    mixpanel.track("go2search", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                startActivity(intentSearch);
            }
        });
        updateButton = (Button) findViewById(R.id.give_time_update);
        updateButton.setTypeface(Util.getFaceMedium());
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goingToSearch = "update";
                YW8Application.getPrefs().edit().putString(Constants.GOING_TO_SEARCH, goingToSearch).apply();
                YW8Application.setGoingToSearch(goingToSearch);
                Intent intentSearch = new Intent(HomePageActivity.this, SearchActivity.class);
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("go2update", "home");
                    mixpanel.track("go2update", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                startActivity(intentSearch);
            }
        });
        userProfileCard = (CardView) findViewById(R.id.cv_user_profile);
        spBadgeImage = (ImageView) findViewById(R.id.cv_badge_image);
        spBadgeName = (TextView) findViewById(R.id.cv_badge_name);
        spBadgeName.setTypeface(Util.getFaceMedium());
        spUserName = (TextView) findViewById(R.id.cv_short_user_name);
        spUserName.setTypeface(Util.getFaceRoman());
        spUserPoints = (TextView) findViewById(R.id.cv_short_user_points);
        spUserPoints.setTypeface(Util.getFaceRoman());
        spPointsInfo = (TextView) findViewById(R.id.cv_short_user_points_info);
        spPointsInfo.setTypeface(Util.getFaceRoman());
    }

    void setFunctionality(ShortUserDetailsResponse.ShortUserDetailsInfo body) {
        userProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUserProf = new Intent(HomePageActivity.this, UserProfileActivity.class);
                intentUserProf.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("profile", "home");
                    mixpanel.track("profile", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                startActivity(intentUserProf);
            }
        });

        if (body != null) {
            if (spUserPoints != null && body.shortUserCurrBadge.getBadgeLevel() != 0) {
                spPointsInfo.setTextColor(ContextCompat.getColor(this, R.color.black));
                switch (body.shortUserCurrBadge.getBadgeLevel()) {
                    case 1:
                        spBadgeImage.setImageResource(R.drawable.homescreen_profile_rookie);
                        break;

                    case 2:
                        spBadgeImage.setImageResource(R.drawable.profile_points_soldier);
                        break;

                    case 3:
                        spBadgeImage.setImageResource(R.drawable.homescreen_profile_agent);
                        break;

                    case 4:
                        spBadgeImage.setImageResource(R.drawable.homescreen_profile_captain);
                        break;

                    case 5:
                        spBadgeImage.setImageResource(R.drawable.homescreen_profile_knight);
                        break;

                    case 6:
                        spBadgeImage.setImageResource(R.drawable.homescreen_profile_general);
                        break;

                    default:
                        break;
                }
                spUserPoints.setText(Integer.toString(body.getLifeTimePoints()));
                spPointsInfo.setText(getResources().getString(R.string.sp_lifetime_points));
            } else {
                spPointsInfo.setText(getResources().getString(R.string.give_time_win_prizes));
            }
            String displayName = body.shortUserDetails.getFullName();
            if (spUserName != null && displayName != null) {
                spUserName.setText(displayName);
            }
            String badgeName = body.shortUserCurrBadge.getShortUserBadgeName();
            spBadgeName.setText(badgeName);
        }
    }

    public void fetchShortUserProfile() {
        serverAccessToken = YW8Application.getAccessToken();
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<ShortUserDetailsResponse> request = apiInterface.getShortUserDetails(serverAccessToken);
        request.enqueue(new Callback<ShortUserDetailsResponse>() {
            @Override
            public void onResponse(Call<ShortUserDetailsResponse> call, Response<ShortUserDetailsResponse> response) {
                Log.d("YW8", "Success");
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ShortUserDetailsResponse.ShortUserDetailsInfo body = response.body().shortUserDetailsInfo;
                        if (body != null) {
                            mixpanel.identify(body.shortUserDetails.getUserId());
                            mixpanel.getPeople().identify(body.shortUserDetails.getUserId());
                            mixpanel.getPeople().set("Plan", "Premium");
                            setFunctionality(body);
                        }
                    } else {
                        String error = response.errorBody().string();
                        Log.d("HomePage", error);
                        if (!(HomePageActivity.this).isFinishing()) {
                            alertTryAgain(HomePageActivity.this);
                        }
                    }
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ShortUserDetailsResponse> call, Throwable t) {
                Log.d("YW8", "Fail");
                progressDialog.dismiss();
                if(!Util.checkAndShowNetworkPopup(HomePageActivity.this)) {
                    fetchShortUserProfile();
                }

            }
        });
    }

    public void alertServerConnectFailure(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage("Oops, something went wrong, please try again")
                .setCancelable(false)
                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent backButtonIntent = new Intent(HomePageActivity.this, HomePageActivity.class);
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
        dialog.show();
    }

    public void alertTryAgain(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage("Oops, something went wrong, please try again")
                .setCancelable(false)
                .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backButtonIntent = new Intent(HomePageActivity.this, HomePageActivity.class);
                        backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        finish();
                        startActivity(backButtonIntent);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_options:
                //TODO
                return true;
            case R.id.get_points:
                Intent intentGetPoints = new Intent(HomePageActivity.this, GetPointsActivity.class);
                intentGetPoints.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("win prizes", "nav");
                    mixpanel.track("win prizes", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                startActivity(intentGetPoints);
                return true;
            case R.id.log_out:
                String facebookOrGoogle = YW8Application.getFacebookOrGoogle();
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("log out", "nav");
                    mixpanel.track("log out", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                if (facebookOrGoogle.equals("facebook")) {
                    facebookSignOut();
                } else {
                    googleSignOut();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void facebookSignOut() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        userLogOut();
    }

    public void googleSignOut() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {

                    FirebaseAuth.getInstance().signOut();
                    if (mGoogleApiClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) {
                                    Log.d("HomePage", "User Logged out");
                                    userLogOut();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {
                    Log.d("HomePage", "Google API Client Connection Suspended");
                }
            });
        }
    }

    public void userLogOut() {
        YW8Application.getPrefs().edit().putString(Constants.ACCESS_TOKEN, "").apply();
        YW8Application.getPrefs().edit().putString(Constants.REFRESH_TOKEN, "").apply();
        YW8Application.getPrefs().edit().putString(Constants.TOKEN_TYPE, "").apply();
        YW8Application.setAccessToken(null, null);
        YW8Application.getPrefs().edit().clear().apply();
        getApplicationContext().getSharedPreferences("dish_app_prefs", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(HomePageActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
