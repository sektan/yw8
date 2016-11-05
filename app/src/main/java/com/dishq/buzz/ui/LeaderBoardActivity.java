package com.dishq.buzz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Response.MonthLeaderBoardResponse;
import server.Response.YearLeaderBoardResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 03-11-2016.
 */

public class LeaderBoardActivity extends BaseActivity {

    private static String serverAccessToken = "";
    private String TAG = "LeaderBoardActivity";
    int monthNumber = 0, yearNumber = 0;

    ImageView ldBack, ldFinder;
    TextView ldHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        setTags();

        Intent intent = getIntent();
        if(intent!=null) {
            monthNumber = intent.getExtras().getInt("month_number");
            yearNumber = intent.getExtras().getInt("year");
            yearNumber = 2016;
            if(monthNumber!=0) {
                fetchMonthlyDetails(monthNumber, yearNumber);
            }else {
                fetchYearlyDetails(yearNumber);
            }
        }
    }

    public void setTags() {
        ldBack = (ImageView) findViewById(R.id.back_button);
        ldFinder = (ImageView) findViewById(R.id.tvMenuFinder);
        ldHeader = (TextView) findViewById(R.id.toolbarTitle);

        setFunctionality();

    }

    public void setFunctionality() {
        ldBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaderBoardActivity.this, UserProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ldFinder.setVisibility(View.GONE);

        ldHeader.setText(getResources().getString(R.string.leaderboard));
    }

    private void fetchMonthlyDetails(int monthNumber, int yearNumber) {
        String header = fetchHeader();
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<MonthLeaderBoardResponse> request = apiInterface.getMonthLeaderBoardDetails(header, monthNumber, yearNumber);
        request.enqueue(new Callback<MonthLeaderBoardResponse>() {
            @Override
            public void onResponse(Call<MonthLeaderBoardResponse> call, Response<MonthLeaderBoardResponse> response) {
                Log.d(TAG, "Success");
            }

            @Override
            public void onFailure(Call<MonthLeaderBoardResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
            }
        });
    }

    private void fetchYearlyDetails(int yearNumber) {
        String header = fetchHeader();
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<YearLeaderBoardResponse> request = apiInterface.getYearLeaderBoardDetails(header, yearNumber);
        request.enqueue(new Callback<YearLeaderBoardResponse>() {
            @Override
            public void onResponse(Call<YearLeaderBoardResponse> call, Response<YearLeaderBoardResponse> response) {
                Log.d(TAG, "Success");
            }

            @Override
            public void onFailure(Call<YearLeaderBoardResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
            }
        });
    }

    private String fetchHeader() {
        String tokenType = SignUpActivity.getTokenType();
        String access = SignUpActivity.getAccessToken();
        serverAccessToken = tokenType + " " + access;
        return serverAccessToken;
    }
}
