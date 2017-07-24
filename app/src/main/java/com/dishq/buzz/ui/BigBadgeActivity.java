package com.dishq.buzz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Util;

/**
 * Created by tania on 07-11-2016.
 * property of dishq
 */

public class BigBadgeActivity extends BaseActivity {

    private TextView badgeName;
    private ImageView badgeImage;
    private LinearLayout llBadge;
    private String nameBadge;
    int badgeLevel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent!= null) {
            nameBadge = intent.getExtras().getString("badge_name");
            badgeLevel = intent.getExtras().getInt("badge_level");
        }
        setContentView(R.layout.big_badge);
        setTags();
    }

    public void setTags() {
        TextView bigBadgeText = (TextView) findViewById(R.id.big_badge_text);
        bigBadgeText.setTypeface(Util.getFaceRoman());
        TextView youAreNowA = (TextView) findViewById(R.id.you_are_now_a);
        youAreNowA.setTypeface(Util.getFaceRoman());
        badgeImage = (ImageView) findViewById(R.id.big_badge_icon);
        badgeName = (TextView) findViewById(R.id.big_badge_name);
        badgeName.setTypeface(Util.getFaceMedium());
        llBadge = (LinearLayout) findViewById(R.id.ll_big_badge);
        setFunctionality();
    }

    public void setFunctionality() {

        if(nameBadge!=null) {
            badgeName.setText(nameBadge);
        }

        if(badgeLevel!=0) {
            if(badgeLevel == 1) {
                badgeImage.setImageResource(R.drawable.badge_big_rookie);
            }else if(badgeLevel == 2) {
                badgeImage.setImageResource(R.drawable.badge_big_soldier);
            }else if(badgeLevel == 3) {
                badgeImage.setImageResource(R.drawable.badge_big_agent);
            }else if(badgeLevel == 4) {
                badgeImage.setImageResource(R.drawable.badge_big_captain);
            }else if(badgeLevel == 5) {
                badgeImage.setImageResource(R.drawable.badge_big_knight);
            }else {
                badgeImage.setImageResource(R.drawable.badge_big_general);
            }
        }
        llBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BigBadgeActivity.this, UserProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
