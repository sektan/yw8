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
import com.dishq.buzz.adapters.YearAdapter;
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
import server.Response.YearLeaderBoardResponse;
import server.api.ApiInterface;
import server.api.Config;

/**
 * Created by dishq on 22-02-2017.
 * Package name version1.dishq.dishq.
 */

public class YearlyLeaderboardFragment extends Fragment {

    private static final String TAG = "YearlyFragment";
    String monthOrYear = "", monthOrYearText = "";
    MixpanelAPI mixpanel = null;
    ListView listView;

    //Empty Constructor
    public YearlyLeaderboardFragment() {
    }

    //Getting new instance of the Fragment
    public static YearlyLeaderboardFragment getInstance() {
        return new YearlyLeaderboardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.MIXPANEL_TOKEN));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_yearly, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.year_leaderboard_list);
        fetchYearlyDetails(Util.getYearNumber());
    }

    private void fetchYearlyDetails(final int yearNumber) {
        monthOrYearText = Integer.toString(Util.getYearNumber());
        Util.setMonthOrYear(monthOrYear);
        final String header = YW8Application.getAccessToken();
        ApiInterface apiInterface = Config.createService(ApiInterface.class);
        Call<YearLeaderBoardResponse> request = apiInterface.getYearLeaderBoardDetails(header, yearNumber);
        request.enqueue(new Callback<YearLeaderBoardResponse>() {
            @Override
            public void onResponse(Call<YearLeaderBoardResponse> call, Response<YearLeaderBoardResponse> response) {
                Log.d(TAG, "Success");
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("yearly", "yearly");
                    mixpanel.track("yearly", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                try {
                    if (response.isSuccessful()) {
                        ArrayList<YearLeaderBoardResponse.YearPointsInfo> body = response.body().yearPointsInfo;
                        if(body!=null){
                            for(int i=0; i<body.size(); i++){
                                Util.yearPointsInfos = body;
                            }
                            YearAdapter yearAdapter = new YearAdapter(getActivity());
                            listView.setAdapter(yearAdapter);
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
            public void onFailure(Call<YearLeaderBoardResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
            }
        });

    }
}
