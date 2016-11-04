package com.dishq.buzz.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;

/**
 * Created by dishq on 26-10-2016.
 */

public class GetPointsActivity extends BaseActivity {

    private WebView webviewPointsInfo, webViewTermsConditions ;
    private RelativeLayout termsConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_points);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.get_points_toolbar);
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        if(mActionBar!=null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        webviewPointsInfo = (WebView) findViewById(R.id.wv_get_points);
        webviewPointsInfo.loadUrl("http://www.dishq.in/yw8/points");

        termsConditions = (RelativeLayout) findViewById(R.id.rl_gp_terms);
        termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webViewTermsConditions = new WebView(GetPointsActivity.this);
                webViewTermsConditions.getSettings().setJavaScriptEnabled(true);
                final Activity mActivity = GetPointsActivity.this;
                webViewTermsConditions.setWebViewClient(new WebViewClient() {
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        Toast.makeText(mActivity, description, Toast.LENGTH_SHORT).show();
                    }
                });
                webViewTermsConditions .loadUrl("http://www.dishq.in/yw8/terms");
                setContentView(webViewTermsConditions );
            }
        });

    }
}
