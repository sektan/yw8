package com.dishq.buzz.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
/**
 * Created by buzz on 24-10-2016.
 * Contains the Log In clickable details
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private CallbackManager callbackManager;
    private ProgressDialog pDialog;

    private TextView signUpText;
    LoginButton loginButton;
    private Boolean GOOGLE_BUTTON_SELECTED, FACEBOOK_BUTTON_SELECTED;
    private ImageView facebook_button, google_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook SDK is initialized
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signin);
        setTags(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    //Method for mapping the various variables to their XML ids
    public void setTags(Context context) {
        signUpText = (TextView) findViewById(R.id.signup_button_text);
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebook_button = (ImageView) findViewById(R.id.fb);
        google_button = (ImageView) findViewById(R.id.google_sign_in);
        setClickables(context);
    }

    //Setting up the clickables of the current activity
    public void setClickables(Context context) {
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                finish();
                startActivity(i);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GOOGLE_BUTTON_SELECTED = false;
            }
        });

        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GOOGLE_BUTTON_SELECTED = false;
                FACEBOOK_BUTTON_SELECTED = true;
                Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
                finish();
                startActivity(i);
                //loginButton.performClick();
            }
        });

        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FACEBOOK_BUTTON_SELECTED = false;
                Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
                finish();
                startActivity(i);
            }
        });
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(final LoginResult loginResult) {
//
//                DishApplication.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        signInUser(loginResult.getAccessToken().getToken());
//                    }
//                });
//            }
//
//            @Override
//            public void onCancel() {
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//            }
//        });

        }


    @Override
    public void onClick(View view) {

    }

}
