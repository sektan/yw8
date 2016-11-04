package com.dishq.buzz.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;

/**
 * Created by dishq on 26-10-2016.
 */

public class UserProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.user_profile_toolbar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
