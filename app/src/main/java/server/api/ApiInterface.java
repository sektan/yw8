package server.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import server.Request.SignUpHelper;
import server.Request.UpdateRestaurantHelper;
import server.Response.FullUserDetailsResponse;
import server.Response.MonthLeaderBoardResponse;
import server.Response.RestaurantInfoResponse;
import server.Response.RestaurantSuggestResponse;
import server.Response.ShortUserDetailsResponse;
import server.Response.SignUpResponse;
import server.Response.SimilarRestaurantResponse;
import server.Response.UpdateRestaurantResponse;
import server.Response.UpdateWaitTimeResponse;
import server.Response.VersionCheckResponse;
import server.Response.YearLeaderBoardResponse;

/**
 * Created by dishq on 28-10-2016.
 */

public interface
ApiInterface {

    @GET("api/getandroidappdetails/")
    Call<VersionCheckResponse> checkVersion(@Query("version_name")String versionName, @Query("version_number")int versionCode);

    @POST("api/auth/signup/")
    Call<SignUpResponse> createNewUser(@Body SignUpHelper signUpHelper);

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
