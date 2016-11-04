package com.dishq.buzz.util;

import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by dishq on 03-11-2016.
 */

public final class YW8Application extends android.support.multidex.MultiDexApplication{

    private static YW8Application application;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
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
