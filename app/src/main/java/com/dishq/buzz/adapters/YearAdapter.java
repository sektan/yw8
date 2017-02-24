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

public class YearAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;

    public YearAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Util.yearPointsInfos.size();
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
            view = layoutInflater.inflate(R.layout.yearly_row_item, viewGroup, false);
        }
        TextView yearlyRank = (TextView) view.findViewById(R.id.lv_yrank);
        yearlyRank.setTypeface(Util.getFaceRoman());
        TextView yearlyUserName = (TextView) view.findViewById(R.id.lv_yusername);
        yearlyUserName.setTypeface(Util.getFaceRoman());
        TextView yearlyPoints = (TextView) view.findViewById(R.id.lv_ypoints);
        yearlyPoints.setTypeface(Util.getFaceRoman());
        TextView yearlyPointsText = (TextView) view.findViewById(R.id.lv_ypoints_text);
        yearlyPointsText.setTypeface(Util.getFaceRoman());
        LinearLayout llayout = (LinearLayout) view.findViewById(R.id.ll_year_row);

        if(llayout!=null) {
            if(Util.yearPointsInfos.get(position).getYearUserDetails().getYearIsCurrentUser()){
                yearlyRank.setTextColor(context.getResources().getColor(R.color.lightPurple));
                yearlyUserName.setTextColor(context.getResources().getColor(R.color.lightPurple));
                yearlyPoints.setTextColor(context.getResources().getColor(R.color.lightPurple));
                yearlyPointsText.setTextColor(context.getResources().getColor(R.color.lightPurple));
                llayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
            if(position%2 == 1) {
                llayout.setBackgroundColor(context.getResources().getColor(R.color.rowPurple));
            }else {
                llayout.setBackgroundColor(context.getResources().getColor(R.color.rowGreen));
            }
        }

        if(Integer.toString(Util.yearPointsInfos.get(position).getYearRank())!=null) {
            if(Util.yearPointsInfos.get(position).getYearRank()!= -1) {
                String rank = "#" + Integer.toString(Util.yearPointsInfos.get(position).getYearRank());
                yearlyRank.setText(rank);
            }
        }
        yearlyUserName.setText(Util.yearPointsInfos.get(position).getYearUserDetails().getYearUserName());
        if(Integer.toString(Util.yearPointsInfos.get(position).getYearPoints())!=null) {
            String points = Integer.toString(Util.yearPointsInfos.get(position).getYearPoints());
            yearlyPoints.setText(points);
        }
        return view;
    }
}
