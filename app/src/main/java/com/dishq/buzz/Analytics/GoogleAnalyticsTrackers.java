package com.dishq.buzz.Analytics;

/**
 * Created by dishq on 8/27/16.
 */

        import android.content.Context;
        import com.dishq.buzz.R;
        import com.google.android.gms.analytics.GoogleAnalytics;
        import com.google.android.gms.analytics.Tracker;

        import java.util.HashMap;
        import java.util.Map;

public final class GoogleAnalyticsTrackers {

    public enum Target {
        APP,
        // Add more trackers here if you need, and update the code in #get(Target) below
    }

    private static GoogleAnalyticsTrackers sInstance;

    public static synchronized void initialize(Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("Extra call to initialize analytics trackers");
        }

        sInstance = new GoogleAnalyticsTrackers(context);
    }

    public static synchronized GoogleAnalyticsTrackers getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Call initialize() before getInstance()");
        }

        return sInstance;
    }

    private final Map<Target, Tracker> trackers = new HashMap<Target, Tracker>();
    private static Context mcontext;

    private GoogleAnalyticsTrackers(Context context) {
        mcontext = context.getApplicationContext();
    }

    public synchronized Tracker get(Target target) {
        if (!trackers.containsKey(target)) {
            Tracker tracker;
            switch (target) {
                case APP:
                    tracker = GoogleAnalytics.getInstance(mcontext).newTracker(R.xml.analytics_tracker);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + target);
            }
            trackers.put(target, tracker);
        }
        return trackers.get(target);
    }
}