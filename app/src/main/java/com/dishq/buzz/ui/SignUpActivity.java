package com.dishq.buzz.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.TokenData;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Finder.SignUpInfoFinder;
import server.Request.SignUpHelper;
import server.Response.SignUpResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 25-10-2016.
 */

public class SignUpActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignUpActivity";
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private SignUpInfoFinder signUpInfoFinder;
    private static String accessToken = "", tokenType = "";
    private static final int RC_SIGN_IN = 9001;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    String facebookAccessToken = "EAAHRn85eTYMBAPTpyd7R6Ha4ZBrGtlxtXri4vWPpCGKUDj6618JXizpDUJ4wPDFIdHw0tnmLejHSkPKV6mCHASaD2seYI9KwesBrSdAPI144Ka6uwJJhOFhxIVhlHN2NQOZCzdd0ZAaDSEUcHSBifyFUd2iJ8wZD";


    String ace = "";
    private TextView logInText;
    LoginButton loginButton;
    private Boolean GOOGLE_BUTTON_SELECTED, FACEBOOK_BUTTON_SELECTED;
    private ImageView facebookButton, googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook SDK is initialized
        FacebookSdk.sdkInitialize(getApplicationContext());

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
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
        // [END build_client]
        setContentView(R.layout.activity_signup);
        setTags(getApplicationContext());
    }

    //Method for mapping the various variables to their XML ids
    public void setTags(Context context) {
        logInText = (TextView) findViewById(R.id.login_button_text);
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookButton = (ImageView) findViewById(R.id.fb);
        googleButton = (ImageView) findViewById(R.id.google_sign_up);
        setClickables(context);
    }

    //Setting up the clickables of the current activity
    public void setClickables(Context context) {
        if (logInText != null) {
            logInText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                    finish();
                    startActivity(i);
                }
            });

        }
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GOOGLE_BUTTON_SELECTED = false;
                }
            });
        }

        if (facebookButton != null) {
            facebookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GOOGLE_BUTTON_SELECTED = false;
                    FACEBOOK_BUTTON_SELECTED = true;
                    Intent i = new Intent(SignUpActivity.this, HomePageActivity.class);
                    finish();
                    startActivity(i);
                    // loginButton.performClick();
                }
            });
        }

        if (googleButton != null) {
            googleButton.setOnClickListener(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!GOOGLE_BUTTON_SELECTED) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                try {
                    handleSignInResult(result);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.google_sign_up:
                GOOGLE_BUTTON_SELECTED = true;
                FACEBOOK_BUTTON_SELECTED = false;
                if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                    signIn();
                } else if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                    selfPermission();
                    // signIn();
                }

                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) throws IOException, GoogleAuthException {
        Log.e("handle", "1");
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            final String access = acct.getServerAuthCode();
            final String SCOPES = "https://www.googleapis.com/auth/userinfo.profile";

            AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    try {

                        ace = GoogleAuthUtil.getToken(getApplicationContext(),
                                Plus.AccountApi.getAccountName(mGoogleApiClient),
                                "oauth2:" + SCOPES);
                    } catch (IOException | GoogleAuthException | SecurityException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.i("", "mustafa olll " + access);
                    return ace;
                }

                @Override
                protected void onPostExecute(String token) {
                    Log.i(TAG, "Access token retrieved:" + ace);
                    fetchAccessToken(ace);
                }

            };
            task.execute();

            Log.e("signin", acct.getDisplayName() + acct.getIdToken() + acct.getEmail());
            // tv_username.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));

        } else {
            Log.e("google", result + "");
            //fetchAccessToken(accessToken);
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    private void fetchAccessToken(String accessToken) {

        if(accessToken==null) {
            accessToken = facebookAccessToken;
        }

        String backend;
        if (GOOGLE_BUTTON_SELECTED) {
            backend = getString(R.string.backend_google);
        } else {
            backend = getString(R.string.backend_facebook);
        }
        //Creating an APIRequest
        final SignUpHelper signUpHelper = new SignUpHelper("convert_token", backend, "bkdTGKU1Xe2B8gDgRPUVD5xsAGqlsajZUaHNGnW6",
                "aymffss0X4FP0k0A4A2qMJL5OdcTQckYxL9nlSA1M14DUXDGC5XuGfhUOjT7X888CQGd8XMbQONUpXTNj3wZd8cF0rFA9GsSj75jRWorPPGWTHSGi25rf45lMdZaEDAg",
                accessToken);
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<SignUpResponse> call = apiInterface.createNewUser(signUpHelper);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                Log.d("YW8", "success");
                SignUpResponse body = response.body();
                if(body!=null) {
                    signUpInfoFinder = new SignUpInfoFinder(SignUpActivity.this, body.getAccessToken(), body.getTokenType(),
                            body.getExpiresIn(), body.getRefreshToken(), body.getResponseScope());
                    setAccessToken(signUpInfoFinder.getAccessToken());
                    setTokenType(signUpInfoFinder.getTokenType());
                    startHomePageActivity(signUpInfoFinder);
                }
            }
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("YW8", "failure");
            }
        });
    }

    public void startHomePageActivity(SignUpInfoFinder signUpInfoFinder) {
        String tokenType = signUpInfoFinder.getTokenType();
        String accessToken = signUpInfoFinder.getAccessToken();
        String serverAccessToken = tokenType + " " + accessToken;
        Intent i = new Intent(SignUpActivity.this, HomePageActivity.class);
        //i.putExtra("SERVER_ACCESS_TOKEN", serverAccessToken);
        finish();
        startActivity(i);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    void selfPermission() {
        if (ContextCompat.checkSelfPermission(SignUpActivity.this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this,
                    Manifest.permission.GET_ACCOUNTS)) {
                Log.e("accept", "accept");
            } else {
                // we can request the permission.
                Log.e("accept", "not accept");
                ActivityCompat.requestPermissions(SignUpActivity.this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signIn();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e("yaay", "yaay");
                } else {
                    // showAlert("","That permission is needed to use Google Signup. Tap retry or use Facebook to Signup.");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static String getTokenType() {
        return tokenType;
    }

    public void  setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
