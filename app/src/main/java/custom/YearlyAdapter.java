package custom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dishq.buzz.R;
import com.dishq.buzz.ui.UserProfileActivity;

import java.util.ArrayList;
import server.Finder.YearlyLeaderBoardFinder;

/**
 * Created by dishq on 08-11-2016.
 */

public class YearlyAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<YearlyLeaderBoardFinder> yearlyFinder;

    public YearlyAdapter(Context context, ArrayList<YearlyLeaderBoardFinder> yearlyFinder){
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
    public View getView(int position, View ConverterView, ViewGroup viewGroup) {
        View view = null;
        if(view == null) {
            view = layoutInflater.inflate(R.layout.yearly_row_item, viewGroup, false);
        }
        TextView yearlyRank = (TextView) view.findViewById(R.id.lv_yrank);
        TextView yearlyUserName = (TextView) view.findViewById(R.id.lv_yusername);
        TextView yearlyPoints = (TextView) view.findViewById(R.id.lv_ypoints);
        LinearLayout llayout = (LinearLayout) view.findViewById(R.id.ll_year_row);

        if(llayout!=null) {
            if(yearlyFinder.get(position).getYearIsCurrentUser()){
                llayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        context.startActivity(intent);
                    }
                });
            }else {

            }
            if(position%2 == 1) {
                llayout.setBackgroundColor(context.getResources().getColor(R.color.rowPurple));
            }else {
                llayout.setBackgroundColor(context.getResources().getColor(R.color.rowGreen));
            }
        }

        if(yearlyRank != null) {
            if(Integer.toString(yearlyFinder.get(position).getYearRank())!=null) {
                if(yearlyFinder.get(position).getYearRank()!= -1) {
                    String rank = "#" + Integer.toString(yearlyFinder.get(position).getYearRank());
                    yearlyRank.setText(rank);
                }
            }
        }
        if(yearlyUserName != null) {
            yearlyUserName.setText(yearlyFinder.get(position).getYearUserName());
        }
        if(yearlyPoints != null) {
            if(Integer.toString(yearlyFinder.get(position).getYearPoints())!=null) {
                String points = Integer.toString(yearlyFinder.get(position).getYearPoints());
                yearlyPoints.setText(points);
            }
        }
        return view;
    }
}
