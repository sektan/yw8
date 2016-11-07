package com.dishq.buzz.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;

import retrofit2.http.Url;

/**
 * Created by dishq on 26-10-2016.
 */

public class GetPointsActivity extends BaseActivity {

    private WebView webviewPointsInfo, webViewTermsConditions ;
    private RelativeLayout termsConditions;
    ImageView GetPointsBack, GetPointsFinder;
    private static String facebookOrGoogle = "";
    TextView getPointsHeader;
    String url = "http://www.dishq.in/yw8/points";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_points);

        Intent intent = getIntent();
        if (intent != null) {
            facebookOrGoogle = intent.getExtras().getString("signup_option");
        }

        setTags();
    }

    public void setTags() {
        GetPointsBack = (ImageView) findViewById(R.id.back_button);
        GetPointsFinder = (ImageView) findViewById(R.id.tvMenuFinder);
        getPointsHeader = (TextView) findViewById(R.id.toolbarTitle);
        webviewPointsInfo = (WebView) findViewById(R.id.wv_get_points);
        termsConditions = (RelativeLayout) findViewById(R.id.rl_gp_terms);

        setFunctionality();
    }

    public void setFunctionality() {
        GetPointsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetPointsActivity.this, HomePageActivity.class);
                intent.putExtra("signup_option", facebookOrGoogle);
                finish();
                startActivity(intent);
            }
        });

        GetPointsFinder.setVisibility(View.GONE);

        getPointsHeader.setText(getResources().getString(R.string.get_points_heading));

        webviewPointsInfo.setWebViewClient(new webBrowser());
        webviewPointsInfo.getSettings().setLoadsImagesAutomatically(true);
        webviewPointsInfo.getSettings().setJavaScriptEnabled(true);
        webviewPointsInfo.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webviewPointsInfo.loadUrl(url);

        termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent internetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dishq.in/yw8/terms"));
                internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
                internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(internetIntent);

//                webViewTermsConditions = new WebView(GetPointsActivity.this);
//                webViewTermsConditions.getSettings().setJavaScriptEnabled(true);
//                final Activity mActivity = GetPointsActivity.this;
//                webViewTermsConditions.setWebViewClient(new WebViewClient() {
//                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                        Toast.makeText(mActivity, description, Toast.LENGTH_SHORT).show();
//                    }
//                });
//                webViewTermsConditions .loadUrl("http://www.dishq.in/yw8/terms");
//                setContentView(webViewTermsConditions );
            }
        });
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
        intent.putExtra("signup_option", facebookOrGoogle);
        finish();
        startActivity(intent);
    }
}
