package com.dishq.buzz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import java.util.concurrent.Executor;

/**
 * Created by dishq on 03-11-2016.
 */

public final class YW8Application extends android.support.multidex.MultiDexApplication{

    private static YW8Application application;
    private static SharedPreferences prefs;
    private Handler mHandler;
    private String accessToken;
    private String tokenType;
    private Boolean hasBadgeUpgrade;
    private int currentBadgeLevel;
    private String currentBadgeName;
    private String facebookOrGoogle;
    private String currMonth;
    private String monthOrYear;
    private int currYear;
    private int numPointsAdded;
    private String goingToSearch;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mHandler = new Handler(Looper.getMainLooper());
        accessToken = getPrefs().getString(Constants.ACCESS_TOKEN, null);
        tokenType = getPrefs().getString(Constants.TOKEN_TYPE, null);
        hasBadgeUpgrade = getPrefs().getBoolean(Constants.HAS_BADGE_UPGRADE, false);
        currentBadgeLevel = getPrefs().getInt(Constants.CURRENT_BADGE_LEVEL, 0);
        currentBadgeName = getPrefs().getString(Constants.CURRENT_BADGE_NAME, null);
        facebookOrGoogle = getPrefs().getString(Constants.FACEBOOK_OR_GOOGLE, null);
        numPointsAdded = getPrefs().getInt(Constants.NUM_POINTS_ADDED, 0);
        goingToSearch = getPrefs().getString(Constants.GOING_TO_SEARCH, null);
        currMonth = getPrefs().getString(Constants.CURR_MONTH, null);
        currYear = getPrefs().getInt(Constants.CURR_YEAR, 0);
        monthOrYear = getPrefs().getString(Constants.MONTH_OR_YEAR, null);
        Util.ACCESS_TOKEN = tokenType + " " + accessToken;
    }

    public static SharedPreferences getPrefs(){
        if(prefs == null){
            prefs = application.getSharedPreferences("yw8_app_prefs", MODE_PRIVATE);
        }
        return prefs;
    }

    public static void runOnUiThread(Runnable runnable){
        application.mHandler.post(runnable);
    }

    class UiThreadExecutor implements Executor {
        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
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

    public static void setCurrentBadge(int currentBadgeLevel, String currentBadgeName) {
        application.currentBadgeLevel = currentBadgeLevel;
        application.currentBadgeName = currentBadgeName;
    }

    public static int getCurrentBadgeLevel() {
        return application.currentBadgeLevel;
    }

    public static String getCurrentBadgeName() {
        return application.currentBadgeName;
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

    public static String getMonthOrYear() {
        return application.monthOrYear;
    }

    public static void setMonthOrYear(String monthOrYear) {
        application.monthOrYear = monthOrYear;
    }

    public static synchronized YW8Application getInstance() {
        return application;
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
