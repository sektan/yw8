package com.dishq.buzz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;

import java.util.Arrays;

import server.Finder.RestaurantInfoFinder;

/**
 * Created by dishq on 03-11-2016.
 */

public class SimilarRestaurantProfileActivity extends BaseActivity {
    public String similarRestCuisine = "";
    public String similarRestAddr = "";
    public String similarRestName = "";
    public String similarRestId = "";
    public Boolean similarRestIsOpenOn;
    public String similarRestType = "";
    private String TAG = "SimilarRestaurantProfileActivity";

    private TextView restToolbarName, foodTypeText, restaurantTypeText,
            restAddrText;
    private ImageView backButton, userProfFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_rest_prof);

        setTags();
        Intent intentFromRestProf = getIntent();
        if (intentFromRestProf != null) {
            similarRestCuisine = intentFromRestProf.getExtras().getString("similarRestCuisine");
            similarRestAddr = intentFromRestProf.getExtras().getString("similarRestAddr");
            similarRestName = intentFromRestProf.getExtras().getString("similarRestName");
            similarRestId = intentFromRestProf.getExtras().getString("restaurant_id");
            similarRestIsOpenOn = intentFromRestProf.getExtras().getBoolean("similarRestIsOpenOn");
            similarRestType = intentFromRestProf.getExtras().getString("similarRestType");

            fetchRestaurantInfo();
        }
    }

    private void setTags() {
        restToolbarName = (TextView) findViewById(R.id.toolbarTitle);
        foodTypeText = (TextView) findViewById(R.id.food_type_text);
        restaurantTypeText = (TextView) findViewById(R.id.restaurant_type_text);
        restAddrText = (TextView) findViewById(R.id.rest_addr_text);
        backButton = (ImageView) findViewById(R.id.back_button);
        userProfFinder = (ImageView) findViewById(R.id.tvMenuFinder);
    }

    private void setFunctionality() {

        if (foodTypeText != null) {
            foodTypeText.setText(similarRestCuisine);
        }

        if (restaurantTypeText != null) {
            restaurantTypeText.setText(similarRestType);
        }

        if (restAddrText != null) {
            restAddrText.setText(similarRestAddr);
        }

        if (restToolbarName != null) {
            restToolbarName.setText(similarRestName);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backButtonIntent = new Intent(SimilarRestaurantProfileActivity.this, HomePageActivity.class);
                finish();
                startActivity(backButtonIntent);
            }
        });

        userProfFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finderIntent = new Intent(SimilarRestaurantProfileActivity.this, SearchActivity.class);
                finish();
                startActivity(finderIntent);
            }
        });
    }

    private void fetchRestaurantInfo() {
        setFunctionality();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backButtonIntent = new Intent(SimilarRestaurantProfileActivity.this, SearchActivity.class);
        finish();
        startActivity(backButtonIntent);
    }
}
