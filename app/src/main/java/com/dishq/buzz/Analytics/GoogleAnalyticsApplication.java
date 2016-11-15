package com.dishq.buzz.Analytics;

import android.app.Application;

import com.dishq.buzz.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by dishq on 15-11-2016.
 */

public class GoogleAnalyticsApplication extends Application {
    private Tracker tracker;


    synchronized public Tracker getDefaultTracker() {
        if(tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.analytics_tracker);
        }
        return tracker;
    }
}
