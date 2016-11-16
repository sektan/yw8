package com.dishq.buzz.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

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
    MixpanelAPI mixpanel = null;
    private String monthOrYear = "";
    private ProgressDialog progressDialog;

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
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));
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
        userProfileHeader.setTypeface(Util.getFaceMedium());
        userProfName = (TextView) findViewById(R.id.up_user_name);
        userProfName.setTypeface(Util.getFaceRoman());
        userProfBadgeName = (TextView) findViewById(R.id.up_badge_name);
        userProfBadgeName.setTypeface(Util.getFaceRoman());
        userProfPointsAlloted = (TextView) findViewById(R.id.up_points_allotted);
        userProfPointsAlloted.setTypeface(Util.getFaceRoman());
        TextView userProfLifetimeText = (TextView) findViewById(R.id.up_lifetime_points);
        userProfLifetimeText.setTypeface(Util.getFaceRoman());
        userProfInfoText = (TextView) findViewById(R.id.up_info_text);
        userProfInfoText.setTypeface(Util.getFaceRoman());
        userProfMonth = (TextView) findViewById(R.id.up_month_name);
        userProfMonth.setTypeface(Util.getFaceMedium());
        userProfMonthRank = (TextView) findViewById(R.id.up_month_rank);
        userProfMonthRank.setTypeface(Util.getFaceRoman());
        userProfMonthPoints = (TextView) findViewById(R.id.up_month_points);
        userProfMonthPoints.setTypeface(Util.getFaceRoman());
        TextView mPtText = (TextView) findViewById(R.id.up_month_pt_text);
        mPtText.setTypeface(Util.getFaceRoman());
        userProfYear = (TextView) findViewById(R.id.up_year_no);
        userProfYear.setTypeface(Util.getFaceMedium());
        userProfYearRank = (TextView) findViewById(R.id.up_year_rank);
        userProfYearRank.setTypeface(Util.getFaceRoman());
        userProfYearPoints = (TextView) findViewById(R.id.up_year_points);
        userProfYearPoints.setTypeface(Util.getFaceRoman());
        TextView yPtText = (TextView) findViewById(R.id.up_ypts_text);
        yPtText.setTypeface(Util.getFaceRoman());
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
        RelativeLayout userGreenInfo = (RelativeLayout) findViewById(R.id.rl_up_user_info);
        userGreenInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, BadgesActivity.class);
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
        Util.setMonthName(body.monthBuzzPointsInfo.getmMonthName());

        if (body.monthBuzzPointsInfo.getmRank() != 0 && body.monthBuzzPointsInfo.getmRank() != -1) {
            String monthRankText = "#" + Integer.toString(body.monthBuzzPointsInfo.getmRank());
            userProfMonthRank.setText(monthRankText);
        }

        if (body.monthBuzzPointsInfo.getmNoOfPoints() != 0) {
            userProfMonthPoints.setText(Integer.toString(body.monthBuzzPointsInfo.getmNoOfPoints()));
        }

        if (body.yearBuzzPointsInfo.getyYear() != 0) {
            userProfYear.setText(Integer.toString(body.yearBuzzPointsInfo.getyYear()));
            Util.setYearNumber(body.yearBuzzPointsInfo.getyYear());
        }

        if (body.yearBuzzPointsInfo.getyRank() != 0 && body.yearBuzzPointsInfo.getyRank() != -1) {
            String yearRankText = "#" + Integer.toString(body.yearBuzzPointsInfo.getyRank());
            userProfYearRank.setText(yearRankText);
        }

        if (body.yearBuzzPointsInfo.getyNoOfPoints() != 0) {
            userProfYearPoints.setText(Integer.toString(body.yearBuzzPointsInfo.getyNoOfPoints()));
        }

        Util.setMonthNumber(body.monthBuzzPointsInfo.getmMonthNo());

        monthCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthOrYear = "month";
                Util.setMonthOrYear(monthOrYear);
                Intent intent = new Intent(UserProfileActivity.this, LeaderBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("monthly leaderboard", "monthly leaderboard");
                    mixpanel.track("monthly leaderboard", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
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
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("yearly leaderboard", "yearly leaderboard");
                    mixpanel.track("yearly leaderboard", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfileActivity.this, HomePageActivity.class);
        finish();
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
        this.finish();
    }
}
