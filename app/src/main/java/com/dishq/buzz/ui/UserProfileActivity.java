package com.dishq.buzz.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Response.FullUserDetailsResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 26-10-2016.
 */

public class UserProfileActivity extends BaseActivity {

    private static String serverAccessToken;
    private String TAG = "UserProfileActivity";
    private String monthOrYear = "";
    private ProgressDialog progressDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    ImageView userProfBack, userProfFinder, userProfBadge, progressImage;
    TextView userProfileHeader, userProfName, userProfBadgeName,
            userProfPointsAlloted, userProfInfoText, userProfMonth,
            userProfMonthRank, userProfMonthPoints, userProfYear,
            userProfYearRank, userProfYearPoints;
    ProgressBar userProfProgress;
    CardView monthCV, yearCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(UserProfileActivity.this);
        progressDialog.show();
        setContentView(R.layout.activity_user_profile);
        setTags();
        fetchFullUserDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setTags() {
        userProfBack = (ImageView) findViewById(R.id.back_button);
        userProfFinder = (ImageView) findViewById(R.id.tvMenuFinder);
        userProfBadge = (ImageView) findViewById(R.id.up_badge_image);
        progressImage = (ImageView) findViewById(R.id.progress_image);
        userProfileHeader = (TextView) findViewById(R.id.toolbarTitle);
        userProfName = (TextView) findViewById(R.id.up_user_name);
        userProfBadgeName = (TextView) findViewById(R.id.up_badge_name);
        userProfPointsAlloted = (TextView) findViewById(R.id.up_points_allotted);
        userProfInfoText = (TextView) findViewById(R.id.up_info_text);
        userProfMonth = (TextView) findViewById(R.id.up_month_name);
        userProfMonthRank = (TextView) findViewById(R.id.up_month_rank);
        userProfMonthPoints = (TextView) findViewById(R.id.up_month_points);
        userProfYear = (TextView) findViewById(R.id.up_year_no);
        userProfYearRank = (TextView) findViewById(R.id.up_year_rank);
        userProfYearPoints = (TextView) findViewById(R.id.up_year_points);
        monthCV = (CardView) findViewById(R.id.cv_month_leaderboard);
        yearCV = (CardView) findViewById(R.id.cv_year_leaderboard);
        userProfProgress = (ProgressBar) findViewById(R.id.up_progressBar);
        userProfBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
            }
        });
    }

    private void setFunctionality(final FullUserDetailsResponse.FullUserDetailsInfo body) {
        userProfFinder.setVisibility(View.GONE);

        userProfileHeader.setText(getResources().getString(R.string.profile));

        userProfName.setText(body.fullUserNameInfo.getfFullName());

        if (body.fullCurrBadgeInfo.getfCurrBadgeName() != null) {
            userProfBadgeName.setText(body.fullCurrBadgeInfo.getfCurrBadgeName());
        }
        if (body.fullCurrBadgeInfo.getfCurrBadgeLevel() != 0) {
            if (body.fullCurrBadgeInfo.getfCurrBadgeLevel() == 1) {
                userProfBadge.setImageResource(R.drawable.profile_points_rookie);
                progressImage.setImageResource(R.drawable.profile_points_soldier);
            } else if (body.fullCurrBadgeInfo.getfCurrBadgeLevel() == 2) {
                userProfBadge.setImageResource(R.drawable.profile_points_soldier);
                progressImage.setImageResource(R.drawable.profile_points_agent);
            } else if (body.fullCurrBadgeInfo.getfCurrBadgeLevel() == 3) {
                userProfBadge.setImageResource(R.drawable.profile_points_agent);
                progressImage.setImageResource(R.drawable.profile_points_captain);
            } else if (body.fullCurrBadgeInfo.getfCurrBadgeLevel() == 4) {
                userProfBadge.setImageResource(R.drawable.profile_points_captain);
                progressImage.setImageResource(R.drawable.profile_points_knight);
            } else if (body.fullCurrBadgeInfo.getfCurrBadgeLevel() == 5) {
                userProfBadge.setImageResource(R.drawable.profile_points_knight);
                progressImage.setImageResource(R.drawable.profile_points_general);
            } else {
                userProfBadge.setImageResource(R.drawable.profile_points_general);
                progressImage.setImageResource(R.drawable.profile_points_general);
            }
        }

        int thresholdPoints =body.nextBadgeInfo.getThresholdPoints()-1;
        int currPoints = body.getfLifeTimePoints();
        userProfPointsAlloted.setText(Integer.toString(currPoints));
        userProfProgress.setMax(thresholdPoints);
        userProfProgress.setProgress(currPoints);
        userProfProgress.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.lightPurple), android.graphics.PorterDuff.Mode.SRC_IN);

        userProfInfoText.setText(body.getfPointsToUgrade());

        userProfMonth.setText(body.monthBuzzPointsInfo.getmMonthName());

        if (body.monthBuzzPointsInfo.getmRank() != 0 && body.monthBuzzPointsInfo.getmRank() != -1) {
            String monthRankText = "# " + Integer.toString(body.monthBuzzPointsInfo.getmRank());
            userProfMonthRank.setText(monthRankText);
        }

        if (body.monthBuzzPointsInfo.getmNoOfPoints() != 0) {
            userProfMonthPoints.setText(Integer.toString(body.monthBuzzPointsInfo.getmNoOfPoints()));
        }

        if (body.yearBuzzPointsInfo.getyYear() != 0) {
            userProfYear.setText(Integer.toString(body.yearBuzzPointsInfo.getyYear()));
        }

        if (body.yearBuzzPointsInfo.getyRank() != 0 && body.yearBuzzPointsInfo.getyRank() != -1) {
            String yearRankText = "# " + Integer.toString(body.yearBuzzPointsInfo.getyRank());
            userProfYearRank.setText(yearRankText);
        }

        if (body.yearBuzzPointsInfo.getyNoOfPoints() != 0) {
            userProfYearPoints.setText(Integer.toString(body.yearBuzzPointsInfo.getyNoOfPoints()));
        }

        monthCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthOrYear = "month";
                Util.setMonthOrYear(monthOrYear);
                int monthNumber = body.monthBuzzPointsInfo.getmMonthNo();
                int yearNumber = body.yearBuzzPointsInfo.getyYear();
                Intent intent = new Intent(UserProfileActivity.this, LeaderBoardActivity.class);
                intent.putExtra("month_number", monthNumber);
                intent.putExtra("year_number", yearNumber);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        yearCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthOrYear = "year";
                Util.setMonthOrYear(monthOrYear);
                Intent intent = new Intent(UserProfileActivity.this, LeaderBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("year_number", body.yearBuzzPointsInfo.getyYear());
                startActivity(intent);
            }
        });

    }

    private void fetchFullUserDetails() {
        serverAccessToken = YW8Application.getAccessToken();

        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<FullUserDetailsResponse> request = apiInterface.getFullUserDetails(serverAccessToken);
        request.enqueue(new Callback<FullUserDetailsResponse>() {
            @Override
            public void onResponse(Call<FullUserDetailsResponse> call, Response<FullUserDetailsResponse> response) {
                Log.d(TAG, "success");
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        FullUserDetailsResponse.FullUserDetailsInfo body = response.body().fullUserDetailsInfo;
                        if (body != null) {
                            setFunctionality(body);
                        }
                    } else {
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FullUserDetailsResponse> call, Throwable t) {
                Log.d(TAG, "fail");
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }
}
