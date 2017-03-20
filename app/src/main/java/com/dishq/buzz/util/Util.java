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

import server.Response.MonthLeaderBoardResponse;
import server.Response.YearLeaderBoardResponse;

import static com.dishq.buzz.util.YW8Application.getContext;

/**
 * Created by dishq on 02-11-2016.
 * Package name version1.dishq.dishq.
 */

public class Util {

    public static ArrayList<MonthLeaderBoardResponse.MonthPointsInfo> monthPointsInfos = new ArrayList<>();
    public static ArrayList<YearLeaderBoardResponse.YearPointsInfo> yearPointsInfos = new ArrayList<>();
    public static String ACCESS_TOKEN = "";
    public static double latitude = 17.77;
    public static double longitude;
    public static String userId = "";
    public static String monthOrYear = "";
    public static String monthName = "";
    public static String restaurantName = "", restId = "", restAddr = "";
    public static int yearNumber = 0, monthNumber = 0;
    public static int tabSelected = -1;
    public static Typeface faceRoman = Typeface.createFromAsset(getContext().getAssets(),
            "avenirltstdroman.ttf"),
            faceMedium = Typeface.createFromAsset(getContext().getAssets(),
                    "avenirltstdmedium.ttf");

    public static Typeface getFaceRoman() {
        return faceRoman;
    }

    public static Typeface getFaceMedium() {
        return faceMedium;
    }

    public static boolean checkAndShowNetworkPopup(final Activity activity) {
        if(!(activity).isFinishing()){
        if (!isOnline()) {
            AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("No Internet Detected")
                    .setMessage("Please try again when you're online. ")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
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
        }else{
            return false;
        }

        }
        return false;
    }

    private static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                //Toast.makeText(getContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                //Toast.makeText(getContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            // not connected to the internet
            return false;
        }
        return false;
    }

    public static int getTabSelected() {
        return tabSelected;
    }

    public static void setTabSelected(int tabSelected) {
        Util.tabSelected = tabSelected;
    }

    public static String getRestId() {
        return Util.restId;
    }

    public static void setRestId(String restId) {
        Util.restId = restId;
    }

    public static String getUserId() {
        return Util.userId;
    }

    public static void setUserId(String userId) {
        Util.userId = userId;
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
