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


        Intent intentFromRestProf = getIntent();
        if (intentFromRestProf != null) {
            similarRestCuisine = intentFromRestProf.getExtras().getString("similarRestCuisine");
            similarRestAddr = intentFromRestProf.getExtras().getString("similarRestAddr");
            similarRestName = intentFromRestProf.getExtras().getString("similarRestName");
            similarRestId = intentFromRestProf.getExtras().getString("restaurant_id");
            similarRestIsOpenOn = intentFromRestProf.getExtras().getBoolean("similarRestIsOpenOn");
            similarRestType = intentFromRestProf.getExtras().getString("similarRestType");
        }
        setContentView(R.layout.activity_similar_rest_prof);
        setTags();
        fetchRestaurantInfo();

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
            String type = similarRestCuisine.replaceAll("\\[", "").replaceAll("\\]", "");
            foodTypeText.setText(type);
        }

        if (restaurantTypeText != null) {
            String restType = similarRestType.replaceAll("\\[", "").replaceAll("\\]", "");
            restaurantTypeText.setText(restType);
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
                backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(backButtonIntent);
            }
        });

        userProfFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finderIntent = new Intent(SimilarRestaurantProfileActivity.this, SearchActivity.class);
                finderIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(finderIntent);
            }
        });
    }

    private void fetchRestaurantInfo() {
        setFunctionality();
    }
}
