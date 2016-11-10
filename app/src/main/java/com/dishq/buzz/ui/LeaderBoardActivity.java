package com.dishq.buzz.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;

import java.io.IOException;
import java.util.ArrayList;

import custom.MonthlyAdapter;
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
    int monthNumber = 0, yearNumber = 0;
    String monthOrYear = "", monthOrYearText = "";

    ImageView ldBack, ldFinder;
    TextView ldHeader, tvYear, tvMonth;
    ListView listView;
    Button yearOrMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(LeaderBoardActivity.this);
        progressDialog.show();

        monthOrYear = Util.getMonthOrYear();
        yearNumber = 2016;
        monthNumber = 11;

        if (monthOrYear.equals("month")) {
            fetchYearlyDetails(yearNumber);
            //fetchMonthlyDetails(monthNumber, yearNumber);
        } else if (monthOrYear.equals("year")) {
            fetchYearlyDetails(yearNumber);
        }
        setContentView(R.layout.activity_leaderboard);
        setTags();
    }

    public void setTags() {
        yearOrMonth = (Button) findViewById(R.id.leaderboard_button);
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
        tvYear = (TextView) findViewById(R.id.tv_year);
        tvMonth = (TextView) findViewById(R.id.tv_monthly);
    }

    public void setFunctionality() {

        if(monthOrYearText!=null) {
            yearOrMonth.setText(monthOrYearText);
        }
        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvMonth.setBackground(getResources().getDrawable(R.drawable.button_selector));
                monthNumber = 11;
                yearNumber = 2016;
                fetchMonthlyDetails(monthNumber, yearNumber);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvYear.setBackground(getResources().getDrawable(R.drawable.button_selector));
                yearNumber = 2016;
                fetchYearlyDetails(yearNumber);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }

    private void fetchMonthlyDetails(final int monthNumber, final int yearNumber) {
        monthOrYearText = "Nov 2016";
        final String header = YW8Application.getAccessToken();
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<MonthLeaderBoardResponse> request = apiInterface.getMonthLeaderBoardDetails(header, monthNumber, yearNumber);
        request.enqueue(new Callback<MonthLeaderBoardResponse>() {
            @Override
            public void onResponse(Call<MonthLeaderBoardResponse> call, Response<MonthLeaderBoardResponse> response) {
                Log.d(TAG, "Success");
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
        monthOrYearText = "2016";
        final String header = YW8Application.getAccessToken();
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<YearLeaderBoardResponse> request = apiInterface.getYearLeaderBoardDetails(header, yearNumber);
        request.enqueue(new Callback<YearLeaderBoardResponse>() {
            @Override
            public void onResponse(Call<YearLeaderBoardResponse> call, Response<YearLeaderBoardResponse> response) {
                Log.d(TAG, "Success");
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

}
