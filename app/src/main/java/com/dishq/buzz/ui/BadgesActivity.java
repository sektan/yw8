package com.dishq.buzz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;

/**
 * Created by dishq on 16-11-2016.
 */

public class BadgesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);
        LinearLayout llbadge = (LinearLayout) findViewById(R.id.llbadge);
        llbadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BadgesActivity.this, UserProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
