package com.dishq.buzz.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.dishq.buzz.R;

/**
 * Created by dishq on 07-11-2016.
 */

public class YearlyLeaderBoardActivity extends ListActivity {

    private ProgressDialog progressDialog;
    private ListView yearlyListView;
    private Context context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_leaderboard_yearly);
        yearlyListView = (ListView) findViewById(R.id.year_list);

    }

    public void setYearlyListView(ListView yearlyListView) {
        this.yearlyListView = yearlyListView;
    }

    public ListView getYearlyListView() {
        return yearlyListView;
    }
}
