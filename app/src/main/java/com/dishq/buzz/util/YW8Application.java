package com.dishq.buzz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

/**
 * Created by dishq on 03-11-2016.
 */

public final class YW8Application extends android.support.multidex.MultiDexApplication{

    private static YW8Application application;
    private static SharedPreferences prefs;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static SharedPreferences getPrefs(){
        if(prefs == null){
            prefs = application.getSharedPreferences("yw8_app_prefs", MODE_PRIVATE);
        }
        return prefs;
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
