package com.dishq.buzz.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

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

public class HomePageActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{

    private ShortUserDetailsFinder shortUserDetailsFinder;

    private static String serverAccessToken = "", facebookOrGoogle;
    private GoogleApiClient mGoogleApiClient;
    private Button searchButton, updateButton;
    private CardView userProfileCard;
    private ImageView spBadgeImage;
    private TextView spBadgeName, spUserName, spUserPoints;
    private static final String RESTAURANT_PROFILE = "RESTAURANT_PROFILE";
    private static final String RESTAURANT_UPDATE = "RESTAURANT_UPDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_page_toolbar);
        setSupportActionBar(toolbar);

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

        Intent intent = getIntent();
        facebookOrGoogle = intent.getExtras().getString("signup_option");

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
                Intent intentSearch = new Intent(HomePageActivity.this, SearchActivity.class);
                intentSearch.putExtra("SEARCH_ACTIVITY", RESTAURANT_PROFILE);
                startActivity(intentSearch);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentSearch = new Intent(HomePageActivity.this, SearchActivity.class);
                intentSearch.putExtra("SEARCH_ACTIVITY", RESTAURANT_UPDATE);
                startActivity(intentSearch);
            }
        });

        userProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUserProf = new Intent(HomePageActivity.this, UserProfileActivity.class);
                startActivity(intentUserProf);
            }
        });

        if(shortUserDetailsFinder!=null) {
            int lifetimePoints = shortUserDetailsFinder.getLifeTimePoints();
            if (spUserPoints != null && lifetimePoints != 0) {
                spUserPoints.setText(Integer.toString(lifetimePoints));
            }
            String displayName = shortUserDetailsFinder.getFullName();
            if (spUserName != null && displayName != null) {
                spUserName.setText(displayName);
            }
        }
    }

    public void fetchShortUserProfile() {
        String tokenType = SignUpActivity.getTokenType();
        String access = SignUpActivity.getAccessToken();
        serverAccessToken = tokenType + " " + access;
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<ShortUserDetailsResponse> request = apiInterface.getShortUserDetails(serverAccessToken);
        request.enqueue(new Callback<ShortUserDetailsResponse>() {
            @Override
            public void onResponse(Call<ShortUserDetailsResponse> call, Response<ShortUserDetailsResponse> response) {
                Log.d("YW8", "Success");
                if (call != null && response != null) {
                    ShortUserDetailsResponse.ShortUserDetailsInfo body = response.body().shortUserDetailsInfo;
                    if (body != null) {
                        shortUserDetailsFinder = new ShortUserDetailsFinder(body.getLifeTimePoints(), body.shortUserDetails.getFullName(),
                                body.shortUserDetails.getDisplayName(), body.shortUserCurrBadge.getShortUserName(), body.shortUserCurrBadge.getBadgeLevel());
                        setFunctionality(shortUserDetailsFinder);
                    }
                }
            }

            @Override
            public void onFailure(Call<ShortUserDetailsResponse> call, Throwable t) {
                Log.d("YW8", "Fail");
            }
        });
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
                startActivity(intentGetPoints);
                return true;
            case R.id.log_out:
                Intent intentLogOut = new Intent(HomePageActivity.this, SignUpActivity.class);
                if (facebookOrGoogle.equals("facebook")) {
                    facebookSignOut();
                }else {
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
        LoginManager.getInstance().logOut();
    }

    public void googleSignOut() {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                        }
                    });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
