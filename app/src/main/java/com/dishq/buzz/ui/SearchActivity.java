package com.dishq.buzz.ui;


import android.app.AlertDialog;
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

import com.dishq.buzz.custom.AdapterClass;
import com.dishq.buzz.server.Finder.RestaurantSuggestFinder;

import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.dishq.buzz.server.Response.RestaurantSuggestResponse;
import com.dishq.buzz.server.api.ApiInterface;
import com.dishq.buzz.server.api.Config;

/**
 * Created by tania on 26-10-2016.
 * property of dishq
 */

public class SearchActivity extends BaseActivity {

    ListView listView;
    AdapterClass myAdapter;
    JSONObject prop;
    Boolean REST_IS_OPENED = false;
    private String sa = "";
    private String restName;
    ArrayList<RestaurantSuggestFinder> restaurantFinder = new ArrayList<>();
    EditText txtAutoComplete;
    LinearLayout norestaurant;
    ImageView backButton;
    ProgressBar progressBar;
    MixpanelAPI mixpanel = null;

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            //MixPanel Instantiation
            mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.MIXPANEL_TOKEN));
            setContentView(R.layout.activity_search);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            txtAutoComplete = (EditText) findViewById(R.id.restaurant_autosuggest);
            txtAutoComplete.setTypeface(Util.getFaceRoman());
            backButton = (ImageView) findViewById(R.id.autosuggest_back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("app_back", "app_back");
                        mixpanel.track("app_back", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    Intent searchBackIntent = new Intent(SearchActivity.this, HomePageActivity.class);
                    searchBackIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    finish();
                    startActivity(searchBackIntent);
                }
            });
            norestaurant = (LinearLayout) findViewById(R.id.noresult);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);

            prop = new JSONObject();
            txtAutoComplete.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT = 2;
                    if (txtAutoComplete.getText().length() > 1) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (txtAutoComplete.getRight() - txtAutoComplete.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                // your action here
                                txtAutoComplete.setText("");
                                restaurantFinder.clear();
                                if (!Util.checkAndShowNetworkPopup(SearchActivity.this)) {
                                    if (myAdapter != null) {
                                        myAdapter.notifyDataSetChanged();
                                    }
                                }
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });

            listView = (ListView) findViewById(R.id.list);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("search", "search");
                        properties.put("restaurant name", restaurantFinder.get(position).getRestaurantName());
                        mixpanel.track("search", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    REST_IS_OPENED = restaurantFinder.get(position).getIsOpenNowSearch();
                    Util.setRestId(restaurantFinder.get(position).getRestaurantId() + "");
                    Util.setRestaurantName(restaurantFinder.get(position).getRestaurantName());
                    Util.setRestAddr(restaurantFinder.get(position).getRestaurantAddress());
                    if (YW8Application.getGoingToSearch().equals("restaurant")) {
                        Intent i = new Intent(SearchActivity.this,
                                RestaurantProfileActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        finish();
                        startActivity(i);
                    } else if (YW8Application.getGoingToSearch().equals("update")) {
                        if (REST_IS_OPENED) {
                            Intent i = new Intent(SearchActivity.this,
                                    UpdateRestProfileActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            finish();
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SearchActivity.this,
                                    RestaurantProfileActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            finish();
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

                    if (s.length() > 1) {
                        txtAutoComplete.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cross_dark, 0);
                        if (!Util.checkAndShowNetworkPopup(SearchActivity.this)) {
                            try {
                                prop.put("Characters entered", s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressBar.setVisibility(View.VISIBLE);
                            fetchRestaurant(sa);
                        }
                    } else {
                        txtAutoComplete.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchRestaurant(String query) {
        Call<RestaurantSuggestResponse> request;
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        request = apiInterface.getRestaurantsuggestion(query, Util.getUserId(), Util.latitude, Util.longitude);
        request.enqueue(new Callback<RestaurantSuggestResponse>() {
            @Override
            public void onResponse(Call<RestaurantSuggestResponse> call, Response<RestaurantSuggestResponse> response) {
                progressBar.setVisibility(View.GONE);
                restaurantFinder.clear();
                if (response.body() != null) {
                    for (RestaurantSuggestResponse.restaurantSuggest data : response.body().ressuggest) {
                        restaurantFinder.add(new RestaurantSuggestFinder(data.restaurantId, data.restaurantName,
                                data.restaurantAddress, data.IS_OPEN_NOW));
                    }
                    myAdapter = new AdapterClass(SearchActivity.this, restaurantFinder);
                    listView.setEmptyView(findViewById(R.id.noresult));
                    listView.setAdapter(myAdapter);
                } else {
                    listView.setEmptyView(findViewById(R.id.noresult));
                }
            }

            @Override
            public void onFailure(Call<RestaurantSuggestResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("timeout", "timeout" + t.getMessage());
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

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
