package com.dishq.buzz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;

import com.dishq.buzz.Analytics.GoogleAnalyticsTrackers;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by dishq on 03-11-2016.
 */

public final class YW8Application extends android.support.multidex.MultiDexApplication{

    private static YW8Application application;
    private static SharedPreferences prefs;
    private Handler mHandler;
    private String accessToken;
    private String tokenType;
    private String facebookOrGoogle;
    private int numPointsAdded;
    private String goingToSearch;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mHandler = new Handler(Looper.getMainLooper());
        accessToken = getPrefs().getString(Constants.ACCESS_TOKEN, null);
        tokenType = getPrefs().getString(Constants.TOKEN_TYPE, null);
        facebookOrGoogle = getPrefs().getString(Constants.FACEBOOK_OR_GOOGLE, null);
        numPointsAdded = getPrefs().getInt(Constants.NUM_POINTS_ADDED, 0);
        goingToSearch = getPrefs().getString(Constants.GOING_TO_SEARCH, null);
        Util.ACCESS_TOKEN = tokenType + " " + accessToken;
        GoogleAnalyticsTrackers.initialize(this);
        GoogleAnalyticsTrackers.getInstance().get(GoogleAnalyticsTrackers.Target.APP);
    }

    public static SharedPreferences getPrefs(){
        if(prefs == null){
            prefs = application.getSharedPreferences("yw8_app_prefs", MODE_PRIVATE);
        }
        return prefs;
    }

    //Method for invoking Google analytics
    public synchronized Tracker getGoogleAnalyticsTracker() {
        GoogleAnalyticsTrackers googleAnalyticsTrackers = GoogleAnalyticsTrackers.getInstance();
        return googleAnalyticsTrackers.get(GoogleAnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker mTracker = getGoogleAnalyticsTracker();
        // Set screen name.
        mTracker.setScreenName(screenName);
        // Send a screen view.
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    public static synchronized YW8Application getInstance() {
        return application;
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
