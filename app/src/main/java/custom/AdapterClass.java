package custom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dishq.buzz.R;

import java.util.ArrayList;

import server.Finder.RestaurantFinderResponse;

/**
 * Created by dishq on 03-11-2016.
 */

public class AdapterClass extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<RestaurantFinderResponse> menu_finders;
    public AdapterClass(Context context, ArrayList<RestaurantFinderResponse> menu_finders){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.menu_finders = menu_finders;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return menu_finders.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.acitivity_menu_finder_child, parent, false);
        }
        TextView restaurant_search_name = (TextView) view.findViewById(R.id.restaurant_search_name);
        TextView restaurant_search_drive_time = (TextView) view.findViewById(R.id.restaurant_search_address);

        restaurant_search_name.setText(menu_finders.get(position).getRestaurantName());
        Log.e("JSON Parser", "4");

        restaurant_search_drive_time.setText(menu_finders.get(position).getRestaurantAddress());
        return view;

    }


}