package server.Response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dishq on 03-11-2016.
 */

public class RestaurantInfoResponse {
    @SerializedName("response")
    @Expose
    public String response;
    @SerializedName("data")
    @Expose
    public RestaurantInfo restaurantInfo;


    public class RestaurantInfo {
        @SerializedName("is_open_now")
        @Expose
        private Boolean isOpenNow;

        @SerializedName("display_address")
        @Expose
        private String displayAddress;

        @SerializedName("cuisine")
        @Expose
        private String[] cuisine;

        @SerializedName("name")
        @Expose
        private String restName;

        @SerializedName("restaurant_id")
        @Expose
        private String restId;

        @SerializedName("restaurant_type")
        @Expose
        private String[] restaurantType;

        @SerializedName("wait_time_data")
        @Expose
        public WaitTimeData waitTimeData;

        public Boolean getIsOpenNow() {
            return isOpenNow;
        }

        public void setIsOpenNow(Boolean isOpenNow) {
            this.isOpenNow = isOpenNow;
        }

        public String getDisplayAddress() {
            return displayAddress;
        }

        public void setDisplayAddress(String displayAddress) {
            this.displayAddress = displayAddress;
        }

        public String[] getCuisine() {
            return cuisine;
        }

        public void setCuisine(String[] cuisine) {
            this.cuisine = cuisine;
        }

        public String getRestName() {
            return restName;
        }

        public void setRestName(String restName) {
            this.restName = restName;
        }

        public String getRestId() {
            return restId;
        }

        public void setRestId(String restId) {
            this.restId = restId;
        }

        public String[] getRestaurantType() {
            return restaurantType;
        }

        public void setRestaurantType(String[] restaurantType) {
            this.restaurantType = restaurantType;
        }

    }

    public class WaitTimeData {
        @SerializedName("show_buzz_type_text")
        @Expose
        private Boolean showBuzzTypeText;

        @SerializedName("wait_time_in_mins")
        @Expose
        private String waitTime;

        @SerializedName("buzz_type_text")
        @Expose
        private String buzzTypeText;

        public Boolean getShowBuzzTypeText() {
            return showBuzzTypeText;
        }

        public void setShowBuzzTypeText(Boolean showBuzzTypeText) {
            this.showBuzzTypeText = showBuzzTypeText;
        }

        public String getWaitTime() {
            return waitTime;
        }

        public void setWaitTime(String waitTime) {
            this.waitTime = waitTime;
        }

        public String getBuzzTypeText() {
            return buzzTypeText;
        }

        public void setBuzzTypeText(String buzzTypeText) {
            this.buzzTypeText = buzzTypeText;
        }
    }


}