package com.dishq.buzz.server.api;

import com.dishq.buzz.server.Request.SignUpHelper;
import com.dishq.buzz.server.Request.UpdateRestaurantHelper;
import com.dishq.buzz.server.Response.FullUserDetailsResponse;
import com.dishq.buzz.server.Response.RestaurantInfoResponse;
import com.dishq.buzz.server.Response.RestaurantSuggestResponse;
import com.dishq.buzz.server.Response.ShortUserDetailsResponse;
import com.dishq.buzz.server.Response.SignUpResponse;
import com.dishq.buzz.server.Response.UpdateRestaurantResponse;
import com.dishq.buzz.server.Response.UpdateWaitTimeResponse;
import com.dishq.buzz.server.Response.YearLeaderBoardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.dishq.buzz.server.Response.MonthLeaderBoardResponse;
import com.dishq.buzz.server.Response.SimilarRestaurantResponse;
import com.dishq.buzz.server.Response.VersionCheckResponse;

/**
 * Created by tania on 28-10-2016.
 * property of dishq
 */

public interface
ApiInterface {

    @GET("api/getandroidappdetails/")
    Call<VersionCheckResponse> checkVersion(@Query("version_name")String versionName, @Query("version_number")int versionCode);

    @POST("api/auth/signup/")
    Call<SignUpResponse> createNewUser(@Header("Authorization") String authorization,
                                       @Body SignUpHelper signUpHelper);

    @GET("api/restaurant/suggest/")
    Call<RestaurantSuggestResponse> getRestaurantsuggestion(@Query("query") String query,
                                                            @Query("user_id") String userId,
                                                            @Query("latitude")Double latitude,
                                                            @Query("longitude")Double longitude);

    @GET("api/restaurant/{restaurant_id}/details/")
    Call<RestaurantInfoResponse> getRestaurantInfo(@Path("restaurant_id") String restaurantId,
                                                   @Query("user_id") String userId);

    @GET("api/restaurant/{restaurant_id}/similar/")
    Call<SimilarRestaurantResponse> getSimilarRestaurantInfo(@Path("restaurant_id") String restaurantId,
                                                             @Query("user_id") String userId);

    @GET("api/restaurant/checkinform/")
    Call<UpdateWaitTimeResponse> getUpdateWaitTime();

    @POST("api/ugc/checkin/")
    Call<UpdateRestaurantResponse> updateRestUserProf(@Header("Authorization") String header,
                                                      @Body UpdateRestaurantHelper updateRestaurantHelper);

    @GET("api/userprofile/shortdetails/")
    Call<ShortUserDetailsResponse> getShortUserDetails(@Header("Authorization") String header);

    @GET("api/userprofile/fulldetails/")
    Call<FullUserDetailsResponse> getFullUserDetails(@Header("Authorization") String header);

    @GET("api/userprofile/monthleaderboard/")
    Call<MonthLeaderBoardResponse> getMonthLeaderBoardDetails(@Header("Authorization") String header,
                                                              @Query("month") int monthNumber,
                                                              @Query("year") int year);

    @GET("api/userprofile/yearleaderboard/")
    Call<YearLeaderBoardResponse> getYearLeaderBoardDetails(@Header("Authorization") String header,
                                                            @Query("year") int year);

}
