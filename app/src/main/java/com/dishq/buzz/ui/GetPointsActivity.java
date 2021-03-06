package com.dishq.buzz.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;
import com.dishq.buzz.util.Util;

import retrofit2.http.Url;

/**
 * Created by dishq on 26-10-2016.
 */

public class GetPointsActivity extends BaseActivity {

    private WebView webviewPointsInfo;
    ImageView GetPointsBack, GetPointsFinder;
    TextView getPointsHeader;
    private ProgressDialog progressDialog;
    String url = "http://www.yw8.in/x/points";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_points);
        progressDialog = new ProgressDialog(GetPointsActivity.this);
        progressDialog.show();
        setTags();
            }

    public void setTags() {
        GetPointsBack = (ImageView) findViewById(R.id.back_button);
        GetPointsFinder = (ImageView) findViewById(R.id.tvMenuFinder);
        getPointsHeader = (TextView) findViewById(R.id.toolbarTitle);
        webviewPointsInfo = (WebView) findViewById(R.id.wv_get_points);
        setFunctionality();
    }

    public void setFunctionality() {
        GetPointsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetPointsActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(intent);
            }
        });

        GetPointsFinder.setVisibility(View.GONE);
        getPointsHeader.setText(getResources().getString(R.string.get_points_heading));
        getPointsHeader.setTypeface(Util.getFaceMedium());

        webviewPointsInfo.setWebViewClient(new webBrowser());
        webviewPointsInfo.getSettings().setLoadsImagesAutomatically(true);
        webviewPointsInfo.getSettings().setJavaScriptEnabled(true);
        webviewPointsInfo.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        progressDialog.dismiss();
        webviewPointsInfo.loadUrl(url);
    }

    private class webBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GetPointsActivity.this, HomePageActivity.class);
        finish();
        startActivity(intent);
    }
}
