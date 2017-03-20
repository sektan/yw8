package com.dishq.buzz.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.dishq.buzz.Analytics.GoogleAnalyticsTrackers;
import com.dishq.buzz.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dishq on 03-11-2016.
 */

public final class YW8Application extends android.support.multidex.MultiDexApplication{

    private static YW8Application application;
    private static SharedPreferences prefs;
    private static String uniqueId;
    private Handler mHandler;
    private String accessToken;
    private String tokenType;
    private String facebookOrGoogle;
    private int numPointsAdded;
    private String goingToSearch;
    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    private static long appWentToBg = 0;
    public static boolean wasInBackground;
    MixpanelAPI mixpanel = null;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));
        mHandler = new Handler(Looper.getMainLooper());
        uniqueId = getPrefs().getString(Constants.UNIQUE_IDENTIFIER, null);
        accessToken = getPrefs().getString(Constants.ACCESS_TOKEN, null);
        tokenType = getPrefs().getString(Constants.TOKEN_TYPE, null);
        facebookOrGoogle = getPrefs().getString(Constants.FACEBOOK_OR_GOOGLE, null);
        numPointsAdded = getPrefs().getInt(Constants.NUM_POINTS_ADDED, 0);
        goingToSearch = getPrefs().getString(Constants.GOING_TO_SEARCH, null);
        appWentToBg = getPrefs().getLong(Constants.APP_WENT_TO_BACKGROUND, 0);
        Util.ACCESS_TOKEN = tokenType + " " + accessToken;
        GoogleAnalyticsTrackers.initialize(this);
        GoogleAnalyticsTrackers.getInstance().get(GoogleAnalyticsTrackers.Target.APP);
        registerActivityLifecycleCallbacks(activityCallbacks);
    }

    public static SharedPreferences getPrefs(){
        if(prefs == null){
            prefs = application.getSharedPreferences("yw8_app_prefs", MODE_PRIVATE);
        }
        return prefs;
    }


    public static synchronized YW8Application getInstance() {
        return application;
    }

    Application.ActivityLifecycleCallbacks activityCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            stopActivityTransitionTimer();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            startActivityTransitionTimer();
        }

        @Override
        public void onActivityStopped(Activity activity) {
            try {
                boolean foreground = new ForegroundCheckTask().execute(getApplicationContext()).get();
                if(!foreground) {
                    //App is in Background
                    long appWentToBgTime = System.currentTimeMillis();
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("app_minimise", "homescreen");
                        mixpanel.track("app_minimise", properties);
                        mixpanel.flush();
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    YW8Application.getPrefs().edit().putLong(Constants.APP_WENT_TO_BACKGROUND, appWentToBgTime).apply();
                    YW8Application.setAppWentToBg(appWentToBgTime);

                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    public void startActivityTransitionTimer() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                // Task is run when app is exited
                wasInBackground = true;
            }
        };

        long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;
        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    public void stopActivityTransitionTimer() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }

        wasInBackground = false;
    }

    public static long getAppWentToBg() {
        return YW8Application.appWentToBg;
    }

    public static void setAppWentToBg(long appWentToBg) {
        YW8Application.appWentToBg = appWentToBg;
    }

    public static void setUniqueId(String uniqueId) {
        YW8Application.uniqueId = uniqueId;
    }

    public static String getUniqueID() {
        return YW8Application.uniqueId;
    }

    public static int getNumPointsAdded() {
        return application.numPointsAdded;
    }

    public static void setNumPointsAdded( int numPointsAdded) {
        application.numPointsAdded = numPointsAdded;
    }

    public static void setFacebookOrGoogle(String facebookOrGoogle) {
        application.facebookOrGoogle = facebookOrGoogle;
    }

    public static String getFacebookOrGoogle() {
        return application.facebookOrGoogle;
    }

    public static void setAccessToken(String accessToken, String tokenType) {
        application.accessToken = accessToken;
        application.tokenType = tokenType;
        Util.ACCESS_TOKEN = tokenType + " " + accessToken;
    }

    public static String getAccessToken(){
        return Util.ACCESS_TOKEN;
    }

    public static String getGoingToSearch() {
        return application.goingToSearch;
    }

    public static void setGoingToSearch(String goingToSearch) {
        application.goingToSearch = goingToSearch;
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(application);
    }

    public static Context getContext() {
        return application;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        application = null;
    }

}
