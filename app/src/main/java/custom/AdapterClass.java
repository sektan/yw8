package custom;

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

import server.Finder.RestaurantSuggestFinder;

/**
 * Created by dishq on 03-11-2016.
 */

public class AdapterClass extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<RestaurantSuggestFinder> restfinders;
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
        TextView isRestaurantOpen = (TextView) view.findViewById(R.id.rest_closed);
        isRestaurantOpen.setTypeface(Util.getFaceRoman());
        TextView restaurantSearchAddress = (TextView) view.findViewById(R.id.restaurant_search_address);
        restaurantSearchAddress.setTypeface(Util.getFaceRoman());
        Boolean isRestOpened = restfinders.get(position).getIsOpenNowSearch();
        if(isRestOpened) {
            isRestaurantOpen.setVisibility(View.GONE);
        }else {
            isRestaurantOpen.setVisibility(View.VISIBLE);
        }

        restaurantSearchName.setText(restfinders.get(position).getRestaurantName());
        Log.e("JSON Parser", "4");

        restaurantSearchAddress.setText(restfinders.get(position).getRestaurantAddress());
        return view;

    }


}