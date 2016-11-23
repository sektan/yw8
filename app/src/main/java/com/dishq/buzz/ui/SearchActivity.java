package com.dishq.buzz.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dishq.buzz.BaseActivity;
import com.dishq.buzz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import custom.AdapterClass;
import server.Finder.RestaurantSuggestFinder;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Response.RestaurantSuggestResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 26-10-2016.
 */

public class SearchActivity extends BaseActivity {

    ListView listView;
    AdapterClass myAdapter;
    JSONObject prop;
    Boolean REST_IS_OPENED = false;
    private String sa="";
    private String restaurnat_id,restaurant_name;
    ArrayList<RestaurantSuggestFinder> restaurantFinder = new ArrayList<RestaurantSuggestFinder>();
    EditText txtAutoComplete;
    LinearLayout norestaurant ;
    ImageView backButton;
    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            txtAutoComplete=(EditText)findViewById(R.id.restaurant_autosuggest);
            txtAutoComplete.setTypeface(Util.getFaceRoman());
            backButton=(ImageView) findViewById(R.id.autosuggest_back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent searchBackIntent = new Intent(SearchActivity.this, HomePageActivity.class);
                    searchBackIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    finish();
                    startActivity(searchBackIntent);
                }
            });
            norestaurant=(LinearLayout)findViewById(R.id.noresult);
            progressBar =(ProgressBar)findViewById(R.id.progress_bar);

            prop=new JSONObject();
            txtAutoComplete.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT = 2;
                    if (txtAutoComplete.getText().length()>1)
                    {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if(event.getRawX() >= (txtAutoComplete.getRight() - txtAutoComplete.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                // your action here
                                txtAutoComplete.setText("");
                                restaurantFinder.clear();
                                if(!Util.checkAndShowNetworkPopup(SearchActivity.this)) {
                                    myAdapter.notifyDataSetChanged();
                                }
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });

            listView=(ListView)findViewById(R.id.list);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    REST_IS_OPENED = restaurantFinder.get(position).getIsOpenNowSearch();
                    //restaurnat_id=  restaurantFinder.get(position).getRestaurantId()+"";
                    Util.setRestId(restaurantFinder.get(position).getRestaurantId()+"");
                    //restaurant_name=restaurantFinder.get(position).getRestaurantName();
                    Util.setRestaurantName(restaurantFinder.get(position).getRestaurantName());
                    //String restaurantAddress = restaurantFinder.get(position).getRestaurantAddress();
                    Util.setRestAddr(restaurantFinder.get(position).getRestaurantAddress());
                    try {
                        prop.put("Restaurant name",restaurant_name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (YW8Application.getGoingToSearch().equals("restaurant")){
                        Intent i = new Intent(SearchActivity.this,
                                RestaurantProfileActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                    }else if (YW8Application.getGoingToSearch().equals("update")){
                        if(REST_IS_OPENED) {
                            Intent i = new Intent(SearchActivity.this,
                                    UpdateRestProfileActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(i);
                        }else {
                            Intent i = new Intent(SearchActivity.this,
                                    RestaurantProfileActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(i);
                        }
                    }

                }
            });
            txtAutoComplete.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sa = s.toString();

                    if(s.length()>1)
                    {   txtAutoComplete.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cross_dark, 0);
                        if(!Util.checkAndShowNetworkPopup(SearchActivity.this)){
                            try {
                                prop.put("Characters entered",s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressBar.setVisibility(View.VISIBLE);
                            fetchRestaurant(sa);
                        }

                    }
                    else
                    {
                        txtAutoComplete.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void fetchRestaurant(String query)
    {
        Call<RestaurantSuggestResponse> request;
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        request = apiInterface.getRestaurantsuggestion(query, Util.latitude,Util.longitude);
        request.enqueue(new Callback<RestaurantSuggestResponse>() {
            @Override
            public void onResponse(Call<RestaurantSuggestResponse> call, Response<RestaurantSuggestResponse> response) {
                progressBar.setVisibility(View.GONE);
                restaurantFinder.clear();
                for (RestaurantSuggestResponse.restaurantSuggest data : response.body().ressuggest) {

                    restaurantFinder.add(new RestaurantSuggestFinder(data.restaurantId,  data.restaurantName,
                            data.restaurantAddress, data.IS_OPEN_NOW));
                }
                myAdapter = new AdapterClass(SearchActivity.this, restaurantFinder);
                Log.e("JSON Parser", "5");
                listView.setEmptyView(findViewById(R.id.noresult));
                listView.setAdapter(myAdapter);
                Log.e("JSON Parser", "6");

            }

            @Override
            public void onFailure(Call<RestaurantSuggestResponse> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    progressBar.setVisibility(View.GONE);
                    Log.e("timeout","timeout" +t.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setMessage("Your Internet connection is slow. Please find a better connection.").setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fetchRestaurant(sa);
                                }
                            });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                JSONObject prop=new JSONObject();
                try {
                    prop.put("Screen","menu finder");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent searchBackIntent = new Intent(SearchActivity.this, HomePageActivity.class);
        searchBackIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(searchBackIntent);
    }
}
