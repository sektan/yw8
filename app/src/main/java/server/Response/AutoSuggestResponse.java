package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dishq on 02-11-2016.
 */

public class AutoSuggestResponse {


        @SerializedName("data")
        @Expose
        public List<AutoSuggestResponseDataItem> data = new ArrayList<>();
        @SerializedName("response")
        @Expose
        public String response;

    class AutoSuggestResponseDataItem {

        @SerializedName("restaurant_id")
        @Expose
        public String restaurantId;
        @SerializedName("restaurant_address")
        @Expose
        public String restaurantAddress;
        @SerializedName("restaurant_name")
        @Expose
        public String restaurantName;

    }
}
