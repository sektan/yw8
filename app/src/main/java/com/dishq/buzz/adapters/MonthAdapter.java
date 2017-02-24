package com.dishq.buzz.adapters;

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

/**
 * Created by dishq on 24-02-2017.
 * Package name version1.dishq.dishq.
 */

public class MonthAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;

    public MonthAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Util.monthPointsInfos.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
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
            if(Util.monthPointsInfos.get(position).getMonthUserDetails().getMonthIsCurrentUser()) {
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
            }
            if(position%2 == 1) {
                lllayout.setBackgroundColor(context.getResources().getColor(R.color.rowPurple));
            }else {
                lllayout.setBackgroundColor(context.getResources().getColor(R.color.rowGreen));
            }
        }

        if(Integer.toString(Util.monthPointsInfos.get(position).getMonthRank())!=null) {
            if(Util.monthPointsInfos.get(position).getMonthRank()!= -1) {
                String rank = "#" + Integer.toString(Util.monthPointsInfos.get(position).getMonthRank());
                monthlyRank.setText(rank);
            }
        }
        monthlyUserName.setText(Util.monthPointsInfos.get(position).getMonthUserDetails().getMonthUserName());
        if(Integer.toString(Util.monthPointsInfos.get(position).getMonthPoints())!=null) {
            String points = Integer.toString(Util.monthPointsInfos.get(position).getMonthPoints());
            monthlyPoints.setText(points);
        }

        return view;
    }
}
