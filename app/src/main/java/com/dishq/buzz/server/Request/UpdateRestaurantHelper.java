package com.dishq.buzz.server.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.UnsafeAllocator;

/**
 * Created by tania on 02-11-2016.
 * property of dishq
 */

public class UpdateRestaurantHelper {
    @SerializedName("restaurant_id")
    @Expose
    private int restaurantId;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("wait_time_id")
    @Expose
    private int waitTimeId;

    @SerializedName("buzz_type_id")
    @Expose
    private int buzzTypeId;

    public UpdateRestaurantHelper(int restaurantId, String latitude, String longitude, int waitTimeId,
                                  int buzzTypeId) {
        this.restaurantId = restaurantId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.waitTimeId = waitTimeId;
        this.buzzTypeId = buzzTypeId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude(){
        return longitude;
    }

    public  void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getWaitTimeId() {
        return waitTimeId;
    }

    public void setWaitTimeId(int waitTimeId) {
        this.waitTimeId = waitTimeId;
    }

    public int getBuzzTypeId() {
        return buzzTypeId;
    }

    public void setBuzzTypeId(int buzzTypeId) {
        this.buzzTypeId = buzzTypeId;
    }

}
