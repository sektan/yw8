package com.dishq.buzz.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import server.Finder.MonthlyLeaderBoardFinder;
import server.Finder.YearlyLeaderBoardFinder;

/**
 * Created by dishq on 02-11-2016.
 */

public class Util {
    public static String ACCESS_TOKEN = "";
    public static double latitude = 17.77;
    public static double longitude;
    public static String monthOrYear = "";

    private static Util utilContent;

    public static boolean checkAndShowNetworkPopup(final Activity activity) {
        if (!isOnline(false)) {
            AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("No Internet Detected")
                    .setMessage("Please try again when you're online. ")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            System.exit(0);
                        }
                    })
                    .create();
            dialog.show();

            TextView message = (TextView) dialog.findViewById(android.R.id.message);
            assert message != null;

            return true;
        }
        return false;
    }

    public static boolean isOnline(boolean showToast) {
        ConnectivityManager conMgr = (ConnectivityManager) YW8Application.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            if (showToast)
                Toast.makeText(YW8Application.getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    public static String getMonthOrYear() {
        return utilContent.monthOrYear;
    }

    public static void setMonthOrYear(String monthOrYear) {
        utilContent.monthOrYear = monthOrYear;
    }
}
