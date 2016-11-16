package com.dishq.buzz.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Util;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Finder.SimilarRestInfoFinder;
import server.Response.RestaurantInfoResponse;
import server.Response.SimilarRestaurantResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 02-11-2016.
 */

public class RestaurantProfileActivity extends BaseActivity {

    private String query = "";
    MixpanelAPI mixpanel = null;
    private String restaurantName = "";
    private String TAG = "RestaurantInfoResponse";
    private Boolean isOpenNow, similarOpenNow = null;
    private Toolbar restToolbar;
    private RelativeLayout rlRestWaitTime, rlRestClosed, rlRestWait, rlNoWait;
    private ProgressDialog progressDialog;

    private SimilarRestInfoFinder similarRestInfoFinder;
    private TextView restToolbarName, noOfMins, foodTypeText, restaurantTypeText,
            restAddrText, restaurantSuggestion, suggestedRestName, suggestedRestAddr,
            noWaitQuiet;
    private ImageView backButton, userProfFinder;
    private CardView cardViewSuggestRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(RestaurantProfileActivity.this);
        progressDialog.show();
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));

        Intent intentFromSearch = getIntent();

        query = intentFromSearch.getExtras().getString("restaurant_id");
        restaurantName = intentFromSearch.getExtras().getString("restaurant_name");
        setContentView(R.layout.activity_restaurant_profile);
        setTags();

        fetchRestaurantInfo(query);
    }

    private void setTags() {
        TextView noWaitTime = (TextView) findViewById(R.id.no_wait_time_text);
        noWaitTime.setTypeface(Util.getFaceRoman());
        TextView noWaitNoOfMin = (TextView) findViewById(R.id.nowait_no_of_mins);
        noWaitNoOfMin.setTypeface(Util.getFaceMedium());
        TextView noWaitNoOfMinText = (TextView) findViewById(R.id.nowait_no_min_text);
        noWaitNoOfMinText.setTypeface(Util.getFaceRoman());
        TextView noWaitPlaceIs = (TextView) findViewById(R.id.nowait_place_is);
        noWaitPlaceIs.setTypeface(Util.getFaceRoman());
        noWaitQuiet = (TextView) findViewById(R.id.nowait_quiet);
        noWaitQuiet.setTypeface(Util.getFaceMedium());
        TextView restClosed = (TextView) findViewById(R.id.rest_closed);
        restClosed.setTypeface(Util.getFaceMedium());
        TextView waitTimeText = (TextView) findViewById(R.id.wait_time_text);
        waitTimeText.setTypeface(Util.getFaceRoman());
        restToolbar = (Toolbar) findViewById(R.id.toolbar);
        rlNoWait = (RelativeLayout) findViewById(R.id.rest_no_wait_time);
        rlRestWaitTime = (RelativeLayout) findViewById(R.id.restaurant_wait_time_info);
        rlRestWait = (RelativeLayout) findViewById(R.id.restaurant_wait_info);
        rlRestClosed = (RelativeLayout) findViewById(R.id.rl_rest_closed);
        restToolbarName = (TextView) findViewById(R.id.toolbarTitle);
        restToolbarName.setTypeface(Util.getFaceMedium());
        noOfMins = (TextView) findViewById(R.id.no_of_mins);
        noOfMins.setTypeface(Util.getFaceMedium());
        TextView waitTimeMinText = (TextView) findViewById(R.id.wait_time_min_text);
        waitTimeMinText.setTypeface(Util.getFaceRoman());
        foodTypeText = (TextView) findViewById(R.id.food_type_text);
        foodTypeText.setTypeface(Util.getFaceRoman());
        restaurantTypeText = (TextView) findViewById(R.id.restaurant_type_text);
        restaurantTypeText.setTypeface(Util.getFaceRoman());
        restAddrText = (TextView) findViewById(R.id.rest_addr_text);
        restAddrText.setTypeface(Util.getFaceRoman());
        restaurantSuggestion = (TextView) findViewById(R.id.restaurant_suggestion);
        restaurantSuggestion.setTypeface(Util.getFaceRoman());
        suggestedRestName = (TextView) findViewById(R.id.cv_rest_name);
        suggestedRestName.setTypeface(Util.getFaceRoman());
        suggestedRestAddr = (TextView) findViewById(R.id.cv_rest_addr);
        suggestedRestAddr.setTypeface(Util.getFaceRoman());
        backButton = (ImageView) findViewById(R.id.back_button);
        cardViewSuggestRes = (CardView) findViewById(R.id.similar_restaurant);
        userProfFinder = (ImageView) findViewById(R.id.tvMenuFinder);
    }

    private void setFunctionality(RestaurantInfoResponse.RestaurantInfo body) {
        if (body != null) {
            isOpenNow = body.getIsOpenNow();
            if (isOpenNow) {
                noWaitQuiet.setText(body.waitTimeData.getBuzzTypeText());
                if (noOfMins != null) {
                    String noOfmin = body.waitTimeData.getWaitTimeDisplay().replace(" mins", "");
                    noOfMins.setText(noOfmin);
                    if (body.waitTimeData.getWaitTime().equals("0")) {
                        rlNoWait.setVisibility(View.VISIBLE);
                        rlRestWaitTime.setVisibility(View.GONE);
                        restaurantSuggestion.setVisibility(View.GONE);
                        cardViewSuggestRes.setVisibility(View.GONE);
                    } else {
                        //method to call SimilarRestaurant API
                        rlNoWait.setVisibility(View.GONE);
                        rlRestWaitTime.setVisibility(View.VISIBLE);
                        fetchSimilarRestInfo(query);
                    }
                }
            } else {
                restToolbar.setBackgroundColor(getResources().getColor(R.color.red));
                rlRestWaitTime.setVisibility(View.VISIBLE);
                rlNoWait.setVisibility(View.GONE);
                rlRestWaitTime.setBackgroundColor(getResources().getColor(R.color.red));
                rlRestWait.setVisibility(View.GONE);
                rlRestClosed.setVisibility(View.VISIBLE);
                fetchSimilarRestInfo(query);
            }
            if (foodTypeText != null) {
                String foodType = Arrays.toString(body.getCuisine());
                String type = foodType.replaceAll("\\[", "").replaceAll("\\]", "");
                foodTypeText.setText(type);
            }

            if (restaurantTypeText != null) {
                String restaurantType = Arrays.toString(body.getRestaurantType());
                String restType = restaurantType.replaceAll("\\[", "").replaceAll("\\]", "");
                restaurantTypeText.setText(restType);
            }

            if (restAddrText != null) {
                restAddrText.setText(body.getDisplayAddress());
            }

            if (restToolbarName != null) {
                restToolbarName.setText(restaurantName);
            }

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent backButtonIntent = new Intent(RestaurantProfileActivity.this, HomePageActivity.class);
                    backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("search", "rest info");
                        mixpanel.track("search", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    startActivity(backButtonIntent);
                }
            });

            userProfFinder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent finderIntent = new Intent(RestaurantProfileActivity.this, SearchActivity.class);
                    finish();
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("search", "search");
                        mixpanel.track("search", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    startActivity(finderIntent);
                }
            });
        }
    }

    private void fetchRestaurantInfo(final String query) {

        final Call<RestaurantInfoResponse> request;
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        request = apiInterface.getRestaurantInfo(query);
        request.enqueue(new Callback<RestaurantInfoResponse>() {

            @Override
            public void onResponse(Call<RestaurantInfoResponse> call, Response<RestaurantInfoResponse> response) {
                Log.d(TAG, "s");
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        RestaurantInfoResponse.RestaurantInfo body = response.body().restaurantInfo;
                        if (body != null) {
                            setFunctionality(body);
                        }
                    }else {
                        String error = response.errorBody().string();
                        Log.d("Restaurant Profile", error);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RestaurantInfoResponse> call, Throwable t) {
                Log.d(TAG, "f");
            }
        });
    }

    private void fetchSimilarRestInfo(final String query) {

        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        final Call<SimilarRestaurantResponse> request = apiInterface.getSimilarRestaurantInfo(query);
        request.enqueue(new Callback<SimilarRestaurantResponse>() {
            @Override
            public void onResponse(Call<SimilarRestaurantResponse> call, Response<SimilarRestaurantResponse> response) {
                Log.d(TAG, "Success");
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("alternative rest", "alternative rest");
                    mixpanel.track("alternative rest", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                try {
                    if(response.isSuccessful()) {
                        SimilarRestaurantResponse.SimilarRestaurant body = response.body().similarRestaurant;
                        if (body != null) {
                            similarRestInfoFinder = new SimilarRestInfoFinder(body.getSimilarRestCuisine(), body.getSimilarRestAddr(), body.getSimilarRestName(),
                                    body.getSimilarRestId(), body.getSimilarRestIsOpenOn(), body.getSimilarRestType());
                            startSimilarRestInfoActivity(similarRestInfoFinder);
                        }
                    }else {
                        String error = response.errorBody().string();
                        Log.d("Restaurant Profile", error);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SimilarRestaurantResponse> call, Throwable t) {
                similarOpenNow = false;
                Log.d(TAG, "Fail");
            }
        });
    }

    public void startSimilarRestInfoActivity(SimilarRestInfoFinder similarRestInfoFinder) {
        if(similarRestInfoFinder!=null) {
            final String similarRestAddr = similarRestInfoFinder.getSimilarRestAddr();
            final String similarRestName = similarRestInfoFinder.getSimilarRestName();
            final String similarRestId = similarRestInfoFinder.getSimilarRestId();
            similarOpenNow = similarRestInfoFinder.getSimilarRestIsOpenOn();

            if(similarOpenNow) {
                restaurantSuggestion.setVisibility(View.VISIBLE);
                cardViewSuggestRes.setVisibility(View.VISIBLE);
                suggestedRestName.setText(similarRestName);
                suggestedRestAddr.setText(similarRestAddr);
                if (restaurantSuggestion != null) {
                    restaurantSuggestion.setText(getResources().getString(R.string.similar_rest_text_wait));
                }
            } else {
                restaurantSuggestion.setVisibility(View.GONE);
                cardViewSuggestRes.setVisibility(View.GONE);
                if (restaurantSuggestion != null) {
                    restaurantSuggestion.setText(getResources().getString(R.string.similar_closed));
                }
            }
            cardViewSuggestRes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intentSuggestRest = new Intent(RestaurantProfileActivity.this, RestaurantProfileActivity.class);
                    finish();
                    intentSuggestRest.putExtra("restaurant_name", similarRestName);
                    intentSuggestRest.putExtra("restaurant_id", similarRestId);
                    startActivity(intentSuggestRest);
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
