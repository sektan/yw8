package com.dishq.buzz.custom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dishq.buzz.R;
import com.dishq.buzz.util.Util;

import java.util.ArrayList;

import com.dishq.buzz.server.Finder.RestaurantSuggestFinder;

/**
 * Created by tania on 03-11-2016.
 * property of dishq
 */

public class AdapterClass extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<RestaurantSuggestFinder> restfinders;
    public AdapterClass(Context context, ArrayList<RestaurantSuggestFinder> restfinders){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.restfinders = restfinders;
    }
    @Override
    public int getCount() {
        return restfinders.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.acitivity_menu_finder_child, parent, false);
        }
        TextView restaurantSearchName = (TextView) view.findViewById(R.id.restaurant_search_name);
        restaurantSearchName.setTypeface(Util.getFaceRoman());
        TextView restaurantSearchAddress = (TextView) view.findViewById(R.id.restaurant_search_address);
        restaurantSearchAddress.setTypeface(Util.getFaceRoman());

        restaurantSearchName.setText(restfinders.get(position).getRestaurantName());
        Log.e("JSON Parser", "4");

        restaurantSearchAddress.setText(restfinders.get(position).getRestaurantAddress());
        return view;

    }


}