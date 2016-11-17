package com.dishq.buzz.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import custom.MonthlyAdapter;
import android.app.DatePickerDialog;
import custom.YearlyAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Finder.MonthlyLeaderBoardFinder;
import server.Finder.YearlyLeaderBoardFinder;
import server.Response.MonthLeaderBoardResponse;
import server.Response.YearLeaderBoardResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 03-11-2016.
 */

public class LeaderBoardActivity extends BaseActivity {

    ArrayList<YearlyLeaderBoardFinder> yearlyLeaderBoardFinder = new ArrayList<>();
    ArrayList<MonthlyLeaderBoardFinder> monthlyLeaderBoardFinder = new ArrayList<>();
    YearlyAdapter yearlyAdapter;
    MonthlyAdapter monthlyAdapter;
    private ProgressDialog progressDialog;
    private String TAG = "LeaderBoardActivity";
    private static int monthNumber = 0, yearNumber = 0;
    String monthOrYear = "", monthOrYearText = "";
    MixpanelAPI mixpanel = null;
    ImageView ldBack, ldFinder;
    CharSequence title = "Choose a month";
    TextView ldHeader;
    ListView listView;
    Button yearOrMonth, tvYear, tvMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(LeaderBoardActivity.this);
        progressDialog.show();
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));

        monthOrYear = Util.getMonthOrYear();
        yearNumber = Util.getYearNumber();
        monthNumber = Util.getMonthNumber();
        setContentView(R.layout.activity_leaderboard);
        setTags();
        if (monthOrYear.equals("month")) {
            fetchMonthlyDetails(monthNumber, yearNumber);
        } else if (monthOrYear.equals("year")) {
            fetchYearlyDetails(yearNumber);
        }
    }

    public void setTags() {
        yearOrMonth = (Button) findViewById(R.id.leaderboard_button);
        yearOrMonth.setTypeface(Util.getFaceRoman());
        listView = (ListView) findViewById(R.id.list_leaderBoard);
        listView.setVisibility(View.VISIBLE);
        ldBack = (ImageView) findViewById(R.id.back_button);
        ldBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ldFinder = (ImageView) findViewById(R.id.tvMenuFinder);
        ldFinder.setVisibility(View.GONE);
        ldHeader = (TextView) findViewById(R.id.toolbarTitle);
        ldHeader.setText(getResources().getString(R.string.leaderboard));
        ldHeader.setTypeface(Util.getFaceMedium());
        tvYear = (Button) findViewById(R.id.tv_year);
        tvYear.setTypeface(Util.getFaceRoman());
        tvMonth = (Button) findViewById(R.id.tv_monthly);
        tvMonth.setTypeface(Util.getFaceRoman());
    }

    public void setFunctionality() {

        if(monthOrYearText!=null) {
            yearOrMonth.setText(monthOrYearText);
            yearOrMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   createDialogWithoutDateField().show();
                }
            });
        }
        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tracking the screen view
                YW8Application.getInstance().trackScreenView("Monthly_leaderboard");
                fetchMonthlyDetails(monthNumber, yearNumber);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YW8Application.getInstance().trackScreenView("Year_leaderboard");
                fetchYearlyDetails(yearNumber);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }

    private void fetchMonthlyDetails(final int monthNumber, final int yearNumber) {
        monthOrYear = "month";
        Util.setMonthOrYear(monthOrYear);
        monthOrYearText = Util.getMonthName();
        if(tvMonth!=null) {
            tvMonth.setTextColor(getResources().getColor(R.color.white));
        }
        if(tvYear!=null) {
            tvYear.setTextColor(getResources().getColor(R.color.offWhite));
        }
        final String header = YW8Application.getAccessToken();
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<MonthLeaderBoardResponse> request = apiInterface.getMonthLeaderBoardDetails(header, monthNumber, yearNumber);
        request.enqueue(new Callback<MonthLeaderBoardResponse>() {
            @Override
            public void onResponse(Call<MonthLeaderBoardResponse> call, Response<MonthLeaderBoardResponse> response) {
                Log.d(TAG, "Success");
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("monthly", "monthly");
                    mixpanel.track("monthly", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        monthlyLeaderBoardFinder.clear();
                        for (MonthLeaderBoardResponse.MonthPointsInfo data : response.body().monthPointsInfo) {
                            monthlyLeaderBoardFinder.add(new MonthlyLeaderBoardFinder(data.monthPoints, data.monthRank, data.monthUserDetails.monthUserName,
                                    data.monthUserDetails.monthIsCurrentUser, data.monthUserDetails.monthUserId));
                        }
                        monthlyAdapter = new MonthlyAdapter(LeaderBoardActivity.this, monthlyLeaderBoardFinder);
                        listView.setAdapter(monthlyAdapter);
                        setFunctionality();
                    } else {
                        String error = response.errorBody().string();
                        Log.d("LeaderBoard", error);
                    }

                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MonthLeaderBoardResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
                progressDialog.dismiss();
            }
        });

    }

    private void fetchYearlyDetails(final int yearNumber) {
        monthOrYearText = Integer.toString(Util.getYearNumber());
        monthOrYear = "year";
        Util.setMonthOrYear(monthOrYear);
        if(tvMonth!=null) {
            tvMonth.setTextColor(getResources().getColor(R.color.offWhite));
        }
        if(tvYear!=null) {
            tvYear.setTextColor(getResources().getColor(R.color.white));
        }
        final String header = YW8Application.getAccessToken();
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<YearLeaderBoardResponse> request = apiInterface.getYearLeaderBoardDetails(header, yearNumber);
        request.enqueue(new Callback<YearLeaderBoardResponse>() {
            @Override
            public void onResponse(Call<YearLeaderBoardResponse> call, Response<YearLeaderBoardResponse> response) {
                Log.d(TAG, "Success");
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("yearly", "yearly");
                    mixpanel.track("yearly", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        yearlyLeaderBoardFinder.clear();
                        for (YearLeaderBoardResponse.YearPointsInfo data : response.body().yearPointsInfo) {
                            yearlyLeaderBoardFinder.add(new YearlyLeaderBoardFinder(data.yearPoints, data.yearRank, data.yearUserDetails.yearUserName,
                                    data.yearUserDetails.yearIsCurrentUser, data.yearUserDetails.yearUserId));
                        }
                        yearlyAdapter = new YearlyAdapter(LeaderBoardActivity.this, yearlyLeaderBoardFinder);
                        listView.setAdapter(yearlyAdapter);
                        setFunctionality();
                    } else {
                        String error = response.errorBody().string();
                        Log.d("LeaderBoard", error);
                    }

                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<YearLeaderBoardResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
    private DatePickerDialog createDialogWithoutDateField() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, null, year, month, 0);
        dpd.setTitle("Choose a month");
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            //Object monthPicker = datePickerField.get()
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
        }
        return dpd;
    }

}
