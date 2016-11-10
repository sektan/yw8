package com.dishq.buzz.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Finder.ShortUserDetailsFinder;
import server.Finder.SignUpInfoFinder;
import server.Response.ShortUserDetailsResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 24-10-2016.
 * Class contains the main page of the app
 */

public class HomePageActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ShortUserDetailsFinder shortUserDetailsFinder;
    private ProgressDialog progressDialog;

    private static String serverAccessToken;
    private String goingToSearch = "";
    private GoogleApiClient mGoogleApiClient;
    private Button searchButton, updateButton;
    private CardView userProfileCard;
    private ImageView spBadgeImage;
    private TextView spBadgeName, spUserName, spUserPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        searchButton = (Button) findViewById(R.id.waiting_time_search);
        updateButton = (Button) findViewById(R.id.give_time_update);
        userProfileCard = (CardView) findViewById(R.id.cv_user_profile);
        spBadgeImage = (ImageView) findViewById(R.id.cv_badge_image);
        spBadgeName = (TextView) findViewById(R.id.cv_badge_name);
        spUserName = (TextView) findViewById(R.id.cv_short_user_name);
        spUserPoints = (TextView) findViewById(R.id.cv_short_user_points);
    }

    void setFunctionality(ShortUserDetailsFinder shortUserDetailsFinder) {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goingToSearch = "restaurant";
                YW8Application.getPrefs().edit().putString(Constants.GOING_TO_SEARCH, goingToSearch).apply();
                YW8Application.setGoingToSearch(goingToSearch);
                Intent intentSearch = new Intent(HomePageActivity.this, SearchActivity.class);
                intentSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentSearch);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goingToSearch = "update";
                YW8Application.getPrefs().edit().putString(Constants.GOING_TO_SEARCH, goingToSearch).apply();
                YW8Application.setGoingToSearch(goingToSearch);
                Intent intentSearch = new Intent(HomePageActivity.this, SearchActivity.class);
                intentSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentSearch);
            }
        });

        userProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUserProf = new Intent(HomePageActivity.this, UserProfileActivity.class);
                intentUserProf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentUserProf);
            }
        });

        if (shortUserDetailsFinder != null) {
            int lifetimePoints = shortUserDetailsFinder.getLifeTimePoints();
            if (spUserPoints != null && lifetimePoints != 0) {
                if (lifetimePoints >0 && lifetimePoints <150){
                    spBadgeImage.setImageResource(R.drawable.homescreen_profile_rookie);
                } else if (lifetimePoints >=150 && lifetimePoints< 500) {
                    spBadgeImage.setImageResource(R.drawable.profile_points_soldier);
                } else if (lifetimePoints >=500 && lifetimePoints< 1000) {
                    spBadgeImage.setImageResource(R.drawable.homescreen_profile_agent);
                }else if (lifetimePoints >=1000 && lifetimePoints< 2000) {
                    spBadgeImage.setImageResource(R.drawable.homescreen_profile_captain);
                }else if (lifetimePoints >=2000 && lifetimePoints< 4000) {
                    spBadgeImage.setImageResource(R.drawable.homescreen_profile_knight);
                }else if (lifetimePoints >=4000) {
                    spBadgeImage.setImageResource(R.drawable.homescreen_profile_general);
                }
                spUserPoints.setText(Integer.toString(lifetimePoints));
            }
            String displayName = shortUserDetailsFinder.getFullName();
            if (spUserName != null && displayName != null) {
                spUserName.setText(displayName);
            }
            String badgeName = shortUserDetailsFinder.getShortUserName();
            spBadgeName.setText(badgeName);
        }
    }

    public void fetchShortUserProfile() {
        serverAccessToken = YW8Application.getAccessToken();

        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(HomePageActivity.this);
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                ApiInterface apiInterface = Config.createService(ApiInterface.class);
                Call<ShortUserDetailsResponse> request = apiInterface.getShortUserDetails(serverAccessToken);
                request.enqueue(new Callback<ShortUserDetailsResponse>() {
                    @Override
                    public void onResponse(Call<ShortUserDetailsResponse> call, Response<ShortUserDetailsResponse> response) {
                        Log.d("YW8", "Success");
                        try {
                            if (response.isSuccessful()) {
                                ShortUserDetailsResponse.ShortUserDetailsInfo body = response.body().shortUserDetailsInfo;
                                if (body != null) {
                                    shortUserDetailsFinder = new ShortUserDetailsFinder(body.getLifeTimePoints(), body.shortUserDetails.getFullName(),
                                            body.shortUserDetails.getDisplayName(), body.shortUserCurrBadge.getShortUserName(), body.shortUserCurrBadge.getBadgeLevel());
                                    setFunctionality(shortUserDetailsFinder);
                                }
                            }else {
                                String error = response.errorBody().string();
                                Log.d("HomePage", error);

                            }

                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ShortUserDetailsResponse> call, Throwable t) {
                        Log.d("YW8", "Fail");
                    }
                });
                return true;
            }
            @Override
            protected void onPostExecute (Boolean b) {
                super.onPostExecute(b);
                progressDialog.dismiss();
            }
        };
        task.execute();
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
                intentGetPoints.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentGetPoints);
                return true;
            case R.id.log_out:
                Intent intentLogOut = new Intent(HomePageActivity.this, LoginActivity.class);
                intentLogOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                String facebookOrGoogle = YW8Application.getFacebookOrGoogle();
                if (facebookOrGoogle.equals("facebook")) {
                    facebookSignOut();
                } else {
                    googleSignOut();
                }
                finish();
                startActivity(intentLogOut);
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

        if(mGoogleApiClient!=null) {
            mGoogleApiClient.connect();
            mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {

                    FirebaseAuth.getInstance().signOut();
                    if(mGoogleApiClient.isConnected()) {
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
        YW8Application.getPrefs().edit().putString(Constants.REFRESH_TOKEN,"").apply();
        YW8Application.getPrefs().edit().putString(Constants.TOKEN_TYPE, "").apply();
        YW8Application.setAccessToken(null, null);
        YW8Application.getPrefs().edit().clear().apply();
        getApplicationContext().getSharedPreferences("dish_app_prefs", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
