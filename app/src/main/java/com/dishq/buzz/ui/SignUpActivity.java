package com.dishq.buzz.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.crashlytics.android.Crashlytics;
import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Constants;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Request.SignUpHelper;
import server.Response.SignUpResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 25-10-2016.
 */

public class SignUpActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignUpActivity";
    private static GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private String facebookAccessToken = "";
    private static String facebookOrGoogle = "";


    String ace = "";
    private TextView signupText;
    MixpanelAPI mixpanel = null;
    LoginButton loginButton;
    private Boolean GOOGLE_BUTTON_SELECTED, FACEBOOK_BUTTON_SELECTED;
    private ImageView facebookButton, googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));
        //Facebook SDK is initialized
        facebookSDKInitialize();

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

    @Override
    protected void onResume() {
        super.onResume();

        //Logs 'install' and 'app activate'App events
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //logs "app deactivate" app Event
        AppEventsLogger.deactivateApp(this);


    }

    //Intializing the facebook sdk
    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    //Method for mapping the various variables to their XML ids
    public void setTags(Context context) {
        signupText = (TextView) findViewById(R.id.signuptext);
        signupText.setTypeface(Util.getFaceRoman());
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookButton = (ImageView) findViewById(R.id.fb);
        googleButton = (ImageView) findViewById(R.id.google_sign_up);
        TextView restText = (TextView) findViewById(R.id.restaurants);
        restText.setTypeface(Util.getFaceRoman());
        TextView pubText = (TextView) findViewById(R.id.pubs);
        pubText.setTypeface(Util.getFaceRoman());
        TextView barText = (TextView) findViewById(R.id.bars);
        barText.setTypeface(Util.getFaceRoman());
        TextView connectWith = (TextView) findViewById(R.id.connect_with);
        connectWith.setTypeface(Util.getFaceRoman());
        setClickables(context);
    }

    //Setting up the clickables of the current activity
    public void setClickables(Context context) {
        if (loginButton != null) {
            loginButton.setReadPermissions("email");
            getLoginDetails(loginButton);
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
                    facebookOrGoogle = "facebook";
                    YW8Application.getPrefs().edit().putString(Constants.FACEBOOK_OR_GOOGLE, facebookOrGoogle).apply();
                    YW8Application.setFacebookOrGoogle(facebookOrGoogle);
                    if (!YW8Application.getAccessToken().equals("null null")) {
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        if (accessToken != null && accessToken.getToken() != null) {
                            FACEBOOK_BUTTON_SELECTED = true;
                            progressDialog = new ProgressDialog(SignUpActivity.this);
                            progressDialog.show();
                            fetchAccessToken(accessToken.getToken());
                        }
                    } else {
                        loginButton.performClick();
                    }
                }
            });
        }

        if (googleButton != null) {
            GOOGLE_BUTTON_SELECTED = true;
            googleButton.setOnClickListener(this);

        }

    }

    protected void getLoginDetails(LoginButton loginButton) {
        //Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookOrGoogle = "facebook";
                YW8Application.setFacebookOrGoogle(facebookOrGoogle);
                YW8Application.getPrefs().edit().putString(Constants.FACEBOOK_OR_GOOGLE, facebookOrGoogle).apply();
                facebookAccessToken = loginResult.getAccessToken().getToken();
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.show();
                fetchAccessToken(facebookAccessToken);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "OnCancel");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
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
                    facebookOrGoogle = "google";
                    YW8Application.getPrefs().edit().putString(Constants.FACEBOOK_OR_GOOGLE, facebookOrGoogle).apply();
                    YW8Application.setFacebookOrGoogle(facebookOrGoogle);
                    progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.show();
                    fetchAccessToken(ace);
                }

            };
            task.execute();

            Log.e("signin", acct.getDisplayName() + acct.getIdToken() + acct.getEmail());


        } else {
            Log.e("google", result + "");
        }
    }

    private void fetchAccessToken(final String accessToken) {
        String backend = "";
        if (FACEBOOK_BUTTON_SELECTED) {
            backend = getString(R.string.backend_facebook);
        } else if (GOOGLE_BUTTON_SELECTED) {
            backend = getString(R.string.backend_google);
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
                progressDialog.dismiss();
                Log.d("YW8", "success");
                if(FACEBOOK_BUTTON_SELECTED) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("facebook", "signup");
                        mixpanel.track("facebook", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                }else if(GOOGLE_BUTTON_SELECTED) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("google", "signup");
                        mixpanel.track("google", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                }
                SignUpResponse body = response.body();
                if (body != null) {
                    YW8Application.getPrefs().edit().putString(Constants.ACCESS_TOKEN, body.getAccessToken()).apply();
                    YW8Application.getPrefs().edit().putString(Constants.REFRESH_TOKEN, body.getRefreshToken()).apply();
                    YW8Application.getPrefs().edit().putString(Constants.TOKEN_TYPE, body.getTokenType()).apply();
                    YW8Application.setAccessToken(body.getAccessToken(), body.getTokenType());
                    startHomePageActivity();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("YW8", "failure");
            }
        });


    }

    public void startHomePageActivity() {
        Intent i = new Intent(SignUpActivity.this.getApplicationContext(), HomePageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    showAlert("", "That permission is needed to use Google Signup. Tap retry or use Facebook to Signup.");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void showAlert(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(SignUpActivity.this,
                                    new String[]{Manifest.permission.GET_ACCOUNTS},
                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    }
                });


        android.app.AlertDialog alert = builder.create();
        alert.show();
        TextView message1 = (TextView) alert.findViewById(android.R.id.message);
        assert message != null;
        message1.setLineSpacing(0, 1.5f);

    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
