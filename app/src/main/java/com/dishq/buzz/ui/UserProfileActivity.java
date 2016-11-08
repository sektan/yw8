package com.dishq.buzz.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.dishq.buzz.util.YW8Application;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Finder.FullUserDetailsFinder;
import server.Response.FullUserDetailsResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 26-10-2016.
 */

public class UserProfileActivity extends BaseActivity {

    private static String serverAccessToken;
    private String TAG = "UserProfileActivity";
    private FullUserDetailsFinder fullUserDetailsFinder;
    private ProgressDialog progressDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    ImageView userProfBack, userProfFinder, userProfBadge;
    TextView userProfileHeader, userProfName, userProfBadgeName,
            userProfPointsAlloted, userProfInfoText, userProfMonth,
            userProfMonthRank, userProfMonthPoints, userProfYear,
            userProfYearRank, userProfYearPoints;
    ProgressBar userProfProgress;
    CardView monthCV, yearCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

         setTags();
        fetchFullUserDetails();
    }

    private void setTags() {
        userProfBack = (ImageView) findViewById(R.id.back_button);
        userProfFinder = (ImageView) findViewById(R.id.tvMenuFinder);
        userProfBadge = (ImageView) findViewById(R.id.up_badge_image);
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
    }

    private void setFunctionality(final FullUserDetailsFinder fullUserDetailsFinder) {

        final int monthNumber = fullUserDetailsFinder.getmMonthNo();
        final int yearNumber = fullUserDetailsFinder.getyYear();
        final int currPoints = fullUserDetailsFinder.getfLifeTimePoints();
        userProfBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, HomePageActivity.class);
                finish();
                startActivity(intent);
            }
        });

        userProfFinder.setVisibility(View.GONE);

        userProfileHeader.setText(getResources().getString(R.string.profile));

        if(fullUserDetailsFinder!=null) {
            String userName = fullUserDetailsFinder.getfFullName();
            userProfName.setText(userName);

            String currBadgeName = fullUserDetailsFinder.getfCurrBadgeName();
            if(currBadgeName!=null && currPoints>0) {
                userProfBadgeName.setText(currBadgeName);
            }

            if(currPoints!=0) {
                if (currPoints >0 && currPoints <150){
                    userProfBadge.setImageResource(R.drawable.profile_points_rookie);
                } else if (currPoints >=150 && currPoints< 500) {
                    userProfBadge.setImageResource(R.drawable.profile_points_soldier);
                } else if (currPoints >=500 && currPoints< 1000) {
                    userProfBadge.setImageResource(R.drawable.profile_points_agent);
                }else if (currPoints >=1000 && currPoints< 2000) {
                    userProfBadge.setImageResource(R.drawable.profile_points_captain);
                }else if (currPoints >=2000 && currPoints< 4000) {
                    userProfBadge.setImageResource(R.drawable.profile_points_knight);
                }else if (currPoints >=4000) {
                    userProfBadge.setImageResource(R.drawable.profile_points_general);
                }
                userProfPointsAlloted.setText(Integer.toString(currPoints));
            }

            String pointsToUpgradeText = fullUserDetailsFinder.getfPointsToUgrade();
            userProfInfoText.setText(pointsToUpgradeText);

            String monthName = fullUserDetailsFinder.getmMonthName();
            userProfMonth.setText(monthName);

            int monthRank = fullUserDetailsFinder.getmRank();
            if(monthRank!=0) {
                String monthRankText = "# " + Integer.toString(monthRank);
                userProfMonthRank.setText(monthRankText);
            }

            int monthPoints = fullUserDetailsFinder.getmNoOfPoints();
            if(monthPoints!=0) {
                userProfMonthPoints.setText(Integer.toString(monthPoints));
            }

            int year = fullUserDetailsFinder.getyYear();
            if(year!=0) {
                userProfYear.setText(Integer.toString(year));
            }

            int yearRank = fullUserDetailsFinder.getyRank();
            if(yearRank!=0) {
                String yearRankText = "# " + Integer.toString(yearRank);
                userProfYearRank.setText(yearRankText);
            }

            int yearPoints = fullUserDetailsFinder.getyNoOfPoints();
            if(yearPoints!=0) {
                userProfYearPoints.setText(Integer.toString(yearPoints));
            }
        }

        monthCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserProfileActivity.this, LeaderBoardActivity.class);
                intent.putExtra("month_number", monthNumber);
                intent.putExtra("year_number", yearNumber);
                finish();
                startActivity(intent);
            }
        });

        yearCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = fullUserDetailsFinder.getyYear();
                Intent intent = new Intent(UserProfileActivity.this, LeaderBoardActivity.class);
                intent.putExtra("year_number", year);
                finish();
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus <200) {
                    progressStatus = currPoints;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            userProfProgress.setProgress(progressStatus);
                        }
                    });
                }
            }
        }).start();
    }

    private void fetchFullUserDetails() {
        serverAccessToken = YW8Application.getAccessToken();
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(UserProfileActivity.this);
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                ApiInterface apiInterface = Config.createService(ApiInterface.class);
                Call<FullUserDetailsResponse> request = apiInterface.getFullUserDetails(serverAccessToken);
                request.enqueue(new Callback<FullUserDetailsResponse>() {
                    @Override
                    public void onResponse(Call<FullUserDetailsResponse> call, Response<FullUserDetailsResponse> response) {
                        Log.d(TAG, "success");
                        try {
                            if (response.isSuccessful()) {
                                FullUserDetailsResponse.FullUserDetailsInfo body = response.body().fullUserDetailsInfo;
                                if(body!=null) {
                                    fullUserDetailsFinder = new FullUserDetailsFinder(body.getfLifeTimePoints(), body.getfPointsToUgrade(), body.nextBadgeInfo.getNextBadgeName(),
                                            body.nextBadgeInfo.getNextBadgeLevel(), body.monthBuzzPointsInfo.getmNoOfPoints(), body.monthBuzzPointsInfo.getmMonthName(),
                                            body.monthBuzzPointsInfo.getmRank(), body.monthBuzzPointsInfo.getmMonthNo(), body.fullUserNameInfo.getfFullName(),
                                            body.fullUserNameInfo.getfDisplayName(), body.yearBuzzPointsInfo.getyNoOfPoints(), body.yearBuzzPointsInfo.getyRank(),
                                            body.yearBuzzPointsInfo.getyYear(), body.fullCurrBadgeInfo.getfCurrBadgeName(), body.fullCurrBadgeInfo.getfCurrBadgeLevel());

                                    setFunctionality(fullUserDetailsFinder);
                                }
                            }else {
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
                    }
                });
                return true;
            }

            @Override
            protected void onPostExecute (Boolean b) {
                super.onPostExecute(b);
                progressDialog.dismiss();
            }
        };
        task.execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfileActivity.this, HomePageActivity.class);
        finish();
        startActivity(intent);
    }
}
