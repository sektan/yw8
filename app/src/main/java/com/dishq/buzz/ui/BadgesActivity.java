package com.dishq.buzz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Util;

/**
 * Created by dishq on 16-11-2016.
 */

public class BadgesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);
        setTags();
    }

    public void setTags() {

        TextView cvGenName = (TextView) findViewById(R.id.cv_gen_name);
        cvGenName.setTypeface(Util.getFaceRoman());
        TextView cvGenLevel = (TextView) findViewById(R.id.cv_gen_level);
        cvGenLevel.setTypeface(Util.getFaceRoman());

        TextView cvKniName = (TextView) findViewById(R.id.cv_kni_name);
        cvKniName.setTypeface(Util.getFaceRoman());
        TextView cvKniLevel = (TextView) findViewById(R.id.cv_kni_level);
        cvKniLevel.setTypeface(Util.getFaceRoman());

        TextView cvCapName = (TextView) findViewById(R.id.cv_cap_name);
        cvCapName.setTypeface(Util.getFaceRoman());
        TextView cvCapLevel = (TextView) findViewById(R.id.cv_cap_level);
        cvCapLevel.setTypeface(Util.getFaceRoman());

        TextView cvAgeName = (TextView) findViewById(R.id.cv_age_name);
        cvAgeName.setTypeface(Util.getFaceRoman());
        TextView cvAgeLevel = (TextView) findViewById(R.id.cv_age_level);
        cvAgeLevel.setTypeface(Util.getFaceRoman());

        TextView cvSolName = (TextView) findViewById(R.id.cv_sol_name);
        cvSolName.setTypeface(Util.getFaceRoman());
        TextView cvSolLevel = (TextView) findViewById(R.id.cv_sol_level);
        cvSolLevel.setTypeface(Util.getFaceRoman());

        TextView cvRooName = (TextView) findViewById(R.id.cv_roo_name);
        cvRooName.setTypeface(Util.getFaceRoman());
        TextView cvRooLevel = (TextView) findViewById(R.id.cv_roo_level);
        cvRooLevel.setTypeface(Util.getFaceRoman());

        TextView badgesToCollect = (TextView) findViewById(R.id.badges_to_collect);
        badgesToCollect.setTypeface(Util.getFaceRoman());

        RelativeLayout rlbadge = (RelativeLayout) findViewById(R.id.rlbadge);
        rlbadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BadgesActivity.this, UserProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
