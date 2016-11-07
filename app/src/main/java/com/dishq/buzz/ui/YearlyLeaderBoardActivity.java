package com.dishq.buzz.ui;

import android.app.ListActivity;
import android.os.Bundle;

import com.dishq.buzz.R;

/**
 * Created by dishq on 07-11-2016.
 */

public class YearlyLeaderBoardActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_leaderboard_yearly);
    }
}
