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

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Finder.RestaurantInfoFinder;
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
    private String restaurantName = "";
    private String TAG = "RestaurantInfoResponse";
    private Boolean isOpenNow, similarOpenNow = false;
    private Toolbar restToolbar;
    private RelativeLayout rlRestWaitTime, rlRestClosed, rlRestWait;
    private ProgressDialog progressDialog;

    private RestaurantInfoFinder restaurantInfoFinder;
    private SimilarRestInfoFinder similarRestInfoFinder;
    private TextView restToolbarName, noOfMins, foodTypeText, restaurantTypeText,
            restAddrText, restaurantSuggestion, suggestedRestName, suggestedRestAddr;
    private ImageView backButton, userProfFinder;
    private CardView cardViewSuggestRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentFromSearch = getIntent();

        query = intentFromSearch.getExtras().getString("restaurant_id");
        restaurantName = intentFromSearch.getExtras().getString("restaurant_name");
        setContentView(R.layout.activity_restaurant_profile);
        setTags();

        fetchRestaurantInfo(query);
    }

    private void setTags() {
        restToolbar = (Toolbar) findViewById(R.id.toolbar);
        rlRestWaitTime = (RelativeLayout) findViewById(R.id.restaurant_wait_time_info);
        rlRestWait = (RelativeLayout) findViewById(R.id.restaurant_wait_info);
        rlRestClosed = (RelativeLayout) findViewById(R.id.rl_rest_closed);
        restToolbarName = (TextView) findViewById(R.id.toolbarTitle);
        noOfMins = (TextView) findViewById(R.id.no_of_mins);
        foodTypeText = (TextView) findViewById(R.id.food_type_text);
        restaurantTypeText = (TextView) findViewById(R.id.restaurant_type_text);
        restAddrText = (TextView) findViewById(R.id.rest_addr_text);
        restaurantSuggestion = (TextView) findViewById(R.id.restaurant_suggestion);
        suggestedRestName = (TextView) findViewById(R.id.cv_rest_name);
        suggestedRestAddr = (TextView) findViewById(R.id.cv_rest_addr);
        backButton = (ImageView) findViewById(R.id.back_button);
        cardViewSuggestRes = (CardView) findViewById(R.id.similar_restaurant);
        userProfFinder = (ImageView) findViewById(R.id.tvMenuFinder);
    }

    private void setFunctionality(RestaurantInfoFinder restaurantInfoFinder) {
        if (restaurantInfoFinder != null) {
            isOpenNow = restaurantInfoFinder.getIsOpenNow();
            if (isOpenNow) {
                if (noOfMins != null) {
                    String noOfmin = restaurantInfoFinder.getWaitTime();
                    noOfMins.setText(noOfmin);
                    if (noOfmin.equals("0")) {
                        restaurantSuggestion.setVisibility(View.GONE);
                        cardViewSuggestRes.setVisibility(View.GONE);

                    } else {
                        //method to call SimilarRestaurant API
                        if (restaurantSuggestion != null) {
                            restaurantSuggestion.setText(getResources().getString(R.string.similar_rest_text_wait));
                        }
                        fetchSimilarRestInfo(query);
                        restaurantSuggestion.setVisibility(View.VISIBLE);
                        cardViewSuggestRes.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                restToolbar.setBackgroundColor(getResources().getColor(R.color.red));
                rlRestWaitTime.setBackgroundColor(getResources().getColor(R.color.red));
                rlRestWait.setVisibility(View.GONE);
                rlRestClosed.setVisibility(View.VISIBLE);
                fetchSimilarRestInfo(query);
                if (similarOpenNow) {
                    if (restaurantSuggestion != null) {
                        restaurantSuggestion.setText(getResources().getString(R.string.similar_rest_closed));
                    }
                    restaurantSuggestion.setVisibility(View.VISIBLE);
                    cardViewSuggestRes.setVisibility(View.VISIBLE);
                } else {
                    if (restaurantSuggestion != null) {
                        restaurantSuggestion.setText(getResources().getString(R.string.similar_closed));
                    }
                    restaurantSuggestion.setVisibility(View.GONE);
                    cardViewSuggestRes.setVisibility(View.GONE);
                }

            }


            if (foodTypeText != null) {
                String foodType = Arrays.toString(restaurantInfoFinder.getCuisine());
                foodTypeText.setText(foodType);
            }

            if (restaurantTypeText != null) {
                String restaurantType = Arrays.toString(restaurantInfoFinder.getRestaurantType());
                restaurantTypeText.setText(restaurantType);
            }

            if (restAddrText != null) {
                String restAddr = restaurantInfoFinder.getDisplayAddress();
                restAddrText.setText(restAddr);
            }

            if (restToolbarName != null) {
                restToolbarName.setText(restaurantName);
            }

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent backButtonIntent = new Intent(RestaurantProfileActivity.this, HomePageActivity.class);
                    backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(backButtonIntent);
                }
            });

            userProfFinder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent finderIntent = new Intent(RestaurantProfileActivity.this, SearchActivity.class);
                    finderIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(finderIntent);
                }
            });
        }
    }

    private void fetchRestaurantInfo(final String query) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(RestaurantProfileActivity.this);
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                final Call<RestaurantInfoResponse> request;
                ApiInterface apiInterface = Config.createService(ApiInterface.class);
                request = apiInterface.getRestaurantInfo(query);
                request.enqueue(new Callback<RestaurantInfoResponse>() {

                    @Override
                    public void onResponse(Call<RestaurantInfoResponse> call, Response<RestaurantInfoResponse> response) {
                        Log.d(TAG, "s");
                        RestaurantInfoResponse.RestaurantInfo body = response.body().restaurantInfo;
                        if (body != null) {
                            restaurantInfoFinder = new RestaurantInfoFinder(body.getIsOpenNow(), body.getDisplayAddress(), body.getCuisine(),
                                    body.getRestName(), body.getRestId(), body.getRestaurantType(), body.waitTimeData.getShowBuzzTypeText(),
                                    body.waitTimeData.getWaitTime(), body.waitTimeData.getBuzzTypeText());
                            setFunctionality(restaurantInfoFinder);
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantInfoResponse> call, Throwable t) {
                        Log.d(TAG, "f");
                    }

                });
                return true;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                super.onPostExecute(b);
                progressDialog.dismiss();
            }
        };
        task.execute();
    }

    private void fetchSimilarRestInfo(final String query) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(RestaurantProfileActivity.this);
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                ApiInterface apiInterface = Config.createService(ApiInterface.class);
                final Call<SimilarRestaurantResponse> request = apiInterface.getSimilarRestaurantInfo(query);
                request.enqueue(new Callback<SimilarRestaurantResponse>() {
                    @Override
                    public void onResponse(Call<SimilarRestaurantResponse> call, Response<SimilarRestaurantResponse> response) {
                        Log.d(TAG, "Success");

                        SimilarRestaurantResponse.SimilarRestaurant body = response.body().similarRestaurant;
                        if (body != null) {
                            similarRestInfoFinder = new SimilarRestInfoFinder(body.getSimilarRestCuisine(), body.getSimilarRestAddr(), body.getSimilarRestName(),
                                    body.getSimilarRestId(), body.getSimilarRestIsOpenOn(), body.getSimilarRestType());
                            startSimilarRestInfoActivity(similarRestInfoFinder);
                            similarOpenNow = similarRestInfoFinder.getSimilarRestIsOpenOn();
                        } else {
                            //show nothing
                            similarOpenNow = false;
                            restaurantSuggestion.setVisibility(View.GONE);
                            cardViewSuggestRes.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<SimilarRestaurantResponse> call, Throwable t) {
                        Log.d(TAG, "Fail");
                    }
                });
                return true;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                super.onPostExecute(b);
                progressDialog.dismiss();
            }

        };
        task.execute();

    }

    public void startSimilarRestInfoActivity(SimilarRestInfoFinder similarRestInfoFinder) {
        final String similarRestCuisine = Arrays.toString(similarRestInfoFinder.getSimilarRestCuisine());
        final String similarRestAddr = similarRestInfoFinder.getSimilarRestAddr();
        final String similarRestName = similarRestInfoFinder.getSimilarRestName();
        final String similarRestId = similarRestInfoFinder.getSimilarRestId();
        final Boolean similarRestIsOpenOn = similarRestInfoFinder.getSimilarRestIsOpenOn();
        final String similarRestType = Arrays.toString(similarRestInfoFinder.getSimilarRestType());

        suggestedRestName.setText(similarRestName);
        suggestedRestAddr.setText(similarRestAddr);

        cardViewSuggestRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentSuggestRest = new Intent(RestaurantProfileActivity.this, SimilarRestaurantProfileActivity.class);
                intentSuggestRest.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                intentSuggestRest.putExtra("similarRestCuisine", similarRestCuisine);
                intentSuggestRest.putExtra("similarRestAddr", similarRestAddr);
                intentSuggestRest.putExtra("similarRestName", similarRestName);
                intentSuggestRest.putExtra("similarRestId", similarRestId);
                intentSuggestRest.putExtra("similarRestIsOpenOn", similarRestIsOpenOn);
                intentSuggestRest.putExtra("similarRestType", similarRestType);
                startActivity(intentSuggestRest);
            }
        });
    }
}
