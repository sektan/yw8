package com.dishq.buzz.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.fragments.MonthlyLeaderboardFragment;
import com.dishq.buzz.fragments.YearlyLeaderboardFragment;
import com.dishq.buzz.util.Util;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by dishq on 22-02-2017.
 * Package name version1.dishq.dishq.
 */

public class LeadBoardActivity extends BaseActivity{
    private static final String TAG = "LeaderBoardActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Fragment frag = null;
    MixpanelAPI mixpanel = null;
    ImageView ldBack, ldFinder;
    TextView ldHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));
        setContentView(R.layout.activity_leadboard);
        ldBack = (ImageView) findViewById(R.id.back_button);
        ldBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("app_back", "app_back");
                    mixpanel.track("app_back", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                finish();
            }
        });
        ldFinder = (ImageView) findViewById(R.id.tvMenuFinder);
        ldFinder.setVisibility(View.GONE);
        ldHeader = (TextView) findViewById(R.id.toolbarTitle);
        ldHeader.setText(getResources().getString(R.string.leaderboard));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (viewPager != null && tabLayout != null) {
            initViewPager();
        }
    }

    private void initViewPager() {
        FragmentManager manager = getSupportFragmentManager();
        LeaderboardAdapter adapter = new LeaderboardAdapter(manager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("MONTHLY");
        tabLayout.getTabAt(1).setText("YEAR");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab == tabLayout.getTabAt(0)) {

                }else {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                if (Util.getTabSelected()==0) {
                    TabLayout.Tab tab = tabLayout.getTabAt(0);
                    if (tab != null) {
                        tab.select();
                    }
                }else if(Util.getTabSelected()==1){
                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    if (tab != null) {
                        tab.select();
                    }
                }
            }
        });
    }

    /**
     * Setting up the adapter for the two fragments
     */
    private class LeaderboardAdapter extends FragmentPagerAdapter {

        LeaderboardAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    frag = MonthlyLeaderboardFragment.getInstance();
                    break;
                case 1:
                    frag = YearlyLeaderboardFragment.getInstance();
                    break;
            }
            return frag;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
