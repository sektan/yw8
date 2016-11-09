package custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dishq.buzz.R;

import java.util.ArrayList;

import server.Finder.YearlyLeaderBoardFinder;

/**
 * Created by dishq on 08-11-2016.
 */

public class YearlyAdapter extends BaseAdapter{
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<YearlyLeaderBoardFinder> yearlyFinder;
    public YearlyAdapter(Context context, ArrayList<YearlyLeaderBoardFinder> yearlyFinder) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.yearlyFinder = yearlyFinder;
    }
    @Override
    public int getCount() {
        return yearlyFinder.size();
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
        if(view == null) {
            view = layoutInflater.inflate(R.layout.yearly_row_item, parent, false);
        }
        TextView yearlyRank = (TextView) view.findViewById(R.id.lv_yrank);
        TextView yearlyUserName = (TextView) view.findViewById(R.id.lv_yusername);
        TextView yearlyPoints = (TextView) view.findViewById(R.id.lv_ypoints);

        yearlyRank.setText(yearlyFinder.get(position).getYearRank());
        yearlyUserName.setText(yearlyFinder.get(position).getYearUserName());
        yearlyPoints.setText(yearlyFinder.get(position).getYearPoints());
        return view;
    }
}
