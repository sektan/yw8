package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dishq on 03-11-2016.
 */

public class RestaurantSuggestResponse {

        @SerializedName("data")
        @Expose
        public List<restaurantSuggest> ressuggest;

    public class restaurantSuggest
    {
        @SerializedName("restaurant_id")
        @Expose
        public int restaurantId;

        @SerializedName("restaurant_address")
        @Expose
        public String restaurantAddress;

        @SerializedName("restaurant_name")
        @Expose
        public String restaurantName;

        @SerializedName("is_open_now")
        @Expose
        public Boolean IS_OPEN_NOW;
    }
}
