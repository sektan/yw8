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
import com.dishq.buzz.util.Util;

import java.util.ArrayList;

import server.Finder.MonthlyLeaderBoardFinder;

/**
 * Created by dishq on 09-11-2016.
 */

public class MonthlyAdapter extends BaseAdapter{

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<MonthlyLeaderBoardFinder> monthlyFinder;

    public MonthlyAdapter(Context context, ArrayList<MonthlyLeaderBoardFinder> monthlyFinder){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.monthlyFinder = monthlyFinder;
    }

    @Override
    public int getCount() {
        return monthlyFinder.size();
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
            view = layoutInflater.inflate(R.layout.monthly_row_item, viewGroup, false);
        }
        TextView monthlyRank = (TextView) view.findViewById(R.id.lv_mrank);
        monthlyRank.setTypeface(Util.getFaceRoman());
        TextView monthlyUserName = (TextView) view.findViewById(R.id.lv_musername);
        monthlyUserName.setTypeface(Util.getFaceRoman());
        TextView monthlyPoints = (TextView) view.findViewById(R.id.lv_mpoints);
        monthlyPoints.setTypeface(Util.getFaceRoman());
        TextView monthlyPointsText = (TextView) view.findViewById(R.id.lv_mpoints_text);
        monthlyPointsText.setTypeface(Util.getFaceRoman());
        LinearLayout lllayout = (LinearLayout) view.findViewById(R.id.ll_monthly_row);

        if(lllayout!= null) {
            if(monthlyFinder.get(position).getMonthIsCurrentUser()) {
                monthlyRank.setTextColor(context.getResources().getColor(R.color.lightPurple));
                monthlyUserName.setTextColor(context.getResources().getColor(R.color.lightPurple));
                monthlyPoints.setTextColor(context.getResources().getColor(R.color.lightPurple));
                monthlyPointsText.setTextColor(context.getResources().getColor(R.color.lightPurple));
                lllayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        context.startActivity(intent);
                    }
                });
            }else {

            }
            if(position%2 == 1) {
                lllayout.setBackgroundColor(context.getResources().getColor(R.color.rowPurple));
            }else {
                lllayout.setBackgroundColor(context.getResources().getColor(R.color.rowGreen));
            }
        }

        if(monthlyRank != null) {
            if(Integer.toString(monthlyFinder.get(position).getMonthRank())!=null) {
                if(monthlyFinder.get(position).getMonthRank()!= -1) {
                    String rank = "#" + Integer.toString(monthlyFinder.get(position).getMonthRank());
                    monthlyRank.setText(rank);
                }
            }
        }
        if(monthlyUserName != null) {
            monthlyUserName.setText(monthlyFinder.get(position).getMonthUserName());
        }
        if(monthlyPoints != null) {
            if(Integer.toString(monthlyFinder.get(position).getMonthPoints())!=null) {
                String points = Integer.toString(monthlyFinder.get(position).getMonthPoints());
                monthlyPoints.setText(points);
            }

        }
        return view;
    }
}
