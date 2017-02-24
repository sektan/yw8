package com.dishq.buzz.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dishq.buzz.R;
import com.dishq.buzz.adapters.MonthAdapter;
import com.dishq.buzz.util.Util;
import com.dishq.buzz.util.YW8Application;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.Response.MonthLeaderBoardResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 22-02-2017.
 * Package name version1.dishq.dishq.
 */

public class MonthlyLeaderboardFragment extends Fragment {

    private static final String TAG = "MonthlyFragment";
    String monthOrYear = "", monthOrYearText = "";
    MixpanelAPI mixpanel = null;
    ListView listView;

    //Empty Constructor
    public MonthlyLeaderboardFragment() {
    }

    //Getting new instance of the Fragment
    public static MonthlyLeaderboardFragment getInstance() {
        return new MonthlyLeaderboardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.MIXPANEL_TOKEN));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monthly, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.month_leaderboard_list);
        fetchMonthlyDetails(Util.getMonthNumber(), Util.getYearNumber());
    }

    private void fetchMonthlyDetails(final int monthNumber, final int yearNumber) {
        monthOrYearText = Util.getMonthName();
        final String header = YW8Application.getAccessToken();
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<MonthLeaderBoardResponse> request = apiInterface.getMonthLeaderBoardDetails(header, monthNumber, yearNumber);
        request.enqueue(new Callback<MonthLeaderBoardResponse>() {
            @Override
            public void onResponse(Call<MonthLeaderBoardResponse> call, Response<MonthLeaderBoardResponse> response) {
                Log.d(TAG, "Success");
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("monthly", "monthly");
                    mixpanel.track("monthly", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                try {
                    if (response.isSuccessful()) {
                        ArrayList<MonthLeaderBoardResponse.MonthPointsInfo> body = response.body().monthPointsInfo;
                        if(body!=null){
                            for(int i=0; i<body.size(); i++){
                                Util.monthPointsInfos = body;
                            }
                            MonthAdapter monthAdapter = new MonthAdapter(getActivity());
                            listView.setAdapter(monthAdapter);
                        }
                    } else {
                        String error = response.errorBody().string();
                        Log.d("LeaderBoard", error);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MonthLeaderBoardResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
            }
        });

    }
}
