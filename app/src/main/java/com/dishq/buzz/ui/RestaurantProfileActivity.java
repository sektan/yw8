package com.dishq.buzz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

    private RestaurantInfoFinder restaurantInfoFinder;
    private SimilarRestInfoFinder similarRestInfoFinder;
    private TextView restToolbarName, noOfMins, foodTypeText, restaurantTypeText,
            restAddrText, restaurantSuggestion, suggestedRestName, suggestedRestAddr;
    private ImageView backButton, userProfFinder;
    private CardView cardViewSuggestRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);

        Intent intentFromSearch = getIntent();

        query = intentFromSearch.getExtras().getString("restaurant_id");
        restaurantName = intentFromSearch.getExtras().getString("restaurant_name");
        setTags();

        fetchRestaurantInfo(query);
    }

    private void setTags() {
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
            if (noOfMins != null) {
                String noOfmin = restaurantInfoFinder.getWaitTime();
                noOfMins.setText(noOfmin);
                if (noOfmin.equals("0")) {
                    restaurantSuggestion.setVisibility(View.GONE);
                    cardViewSuggestRes.setVisibility(View.GONE);

                } else {
                    //method to call SimilarRestaurant API
                    fetchSimilarRestInfo(query);
                    restaurantSuggestion.setVisibility(View.VISIBLE);
                    cardViewSuggestRes.setVisibility(View.VISIBLE);
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

            if (restaurantSuggestion != null) {
                restaurantSuggestion.setText("Similar restaurant nearby with no waiting time");
            }
        }
        if (restToolbarName != null) {
            restToolbarName.setText(restaurantName);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backButtonIntent = new Intent(RestaurantProfileActivity.this, HomePageActivity.class);
                finish();
                startActivity(backButtonIntent);
            }
        });

        userProfFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finderIntent = new Intent(RestaurantProfileActivity.this, SearchActivity.class);
                finish();
                startActivity(finderIntent);
            }
        });
    }

    private void fetchRestaurantInfo(final String query) {
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
    }

    private void fetchSimilarRestInfo(String query) {
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
                } else {
                    //show nothing
                    restaurantSuggestion.setVisibility(View.GONE);
                    cardViewSuggestRes.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SimilarRestaurantResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
            }
        });
    }

    public void startSimilarRestInfoActivity(SimilarRestInfoFinder similarRestInfoFinder) {
        final String similarRestCuisine = Arrays.toString(similarRestInfoFinder.getSimilarRestCuisine());
        final String similarRestAddr = similarRestInfoFinder.getSimilarRestAddr();
        final String similarRestName = similarRestInfoFinder.getSimilarRestName();
        final String similarRestId = similarRestInfoFinder.getSimilarRestId();
        final String similarRestIsOpenOn = similarRestInfoFinder.getSimilarRestIsOpenOn();
        final String similarRestType = Arrays.toString(similarRestInfoFinder.getSimilarRestType());

        suggestedRestName.setText(similarRestName);
        suggestedRestAddr.setText(similarRestAddr);

        cardViewSuggestRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentSuggestRest = new Intent(RestaurantProfileActivity.this, SimilarRestaurantProfileActivity.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backButtonIntent = new Intent(RestaurantProfileActivity.this, SearchActivity.class);
        finish();
        startActivity(backButtonIntent);
    }
}
