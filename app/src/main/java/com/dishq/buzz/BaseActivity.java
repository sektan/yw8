package com.dishq.buzz;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dishq.buzz.ui.SearchActivity;

/**
 * Created by dishq on 24-10-2016.
 */

public class BaseActivity extends AppCompatActivity {

    public static BaseActivity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = this;
        setTags(getApplicationContext());
    }

    public Context getContext() {
        return this;
    }

    private void setTags(Context context){
        setClickables(context);
    }

    private void setClickables(Context context){

    }

}
