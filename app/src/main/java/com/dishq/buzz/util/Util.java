package com.dishq.buzz.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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
    public static String monthName = "";
    public static String restaurantName = "", restId = "", restAddr = "";
    public static int yearNumber = 0, monthNumber = 0;
    public static Typeface faceRoman = Typeface.createFromAsset(YW8Application.getContext().getAssets(),
            "avenirltstdroman.ttf"),
            faceMedium = Typeface.createFromAsset(YW8Application.getContext().getAssets(),
                    "avenirltstdmedium.ttf");

    public static Typeface getFaceRoman() {
        return faceRoman;
    }

    public static Typeface getFaceMedium() {
        return faceMedium;
    }

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
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory( Intent.CATEGORY_HOME );
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(homeIntent);
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

    public static String getRestId() {
        return Util.restId;
    }

    public static void setRestId(String restId) {
        Util.restId = restId;
    }

    public static String getRestaurantName() {
        return Util.restaurantName;
    }

    public static void setRestaurantName(String restaurantName) {
        Util.restaurantName = restaurantName;
    }

    public static String getRestAddr() {
        return Util.restAddr;
    }

    public static void setRestAddr(String restAddr) {
        Util.restAddr = restAddr;
    }

    public static String getMonthOrYear() {
        return Util.monthOrYear;
    }

    public static void setMonthOrYear(String monthOrYear) {
        Util.monthOrYear = monthOrYear;
    }

    public static String getMonthName() {
        return Util.monthName;
    }

    public static void setMonthName(String monthName) {
        Util.monthName = monthName;
    }

    public static int getYearNumber() {
        return Util.yearNumber;
    }

    public static void setYearNumber(int yearNumber) {
        Util.yearNumber = yearNumber;
    }

    public static int getMonthNumber() {
        return Util.monthNumber;
    }

    public static void setMonthNumber(int monthNumber) {
        Util.monthNumber = monthNumber;
    }
}
