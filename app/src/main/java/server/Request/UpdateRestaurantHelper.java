package server.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.UnsafeAllocator;

/**
 * Created by dishq on 02-11-2016.
 */

public class UpdateRestaurantHelper {
    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    @SerializedName("wait_time_id")
    @Expose
    private int waitTimeId;

    @SerializedName("buzz_type_id")
    @Expose
    private int buzzTypeId;

    public UpdateRestaurantHelper(String restaurantId, double latitude, double longitude, int waitTimeId,
                                  int buzzTypeId) {
        this.restaurantId = restaurantId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.waitTimeId = waitTimeId;
        this.buzzTypeId = buzzTypeId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public  void setLongitude(float longitude) {
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
