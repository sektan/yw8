package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 03-11-2016.
 */

public class SimilarRestaurantResponse {

    @SerializedName("response")
    @Expose
    private String similarRestResponse;

    @SerializedName("data")
    @Expose
    public SimilarRestaurant similarRestaurant;

    public class SimilarRestaurant {
        @SerializedName("cuisine")
        @Expose
        public String[] similarRestCuisine;

        @SerializedName("display_address")
        @Expose
        public String similarRestAddr;

        @SerializedName("name")
        @Expose
        public String similarRestName;

        @SerializedName("restaurant_id")
        @Expose
        public String similarRestId;

        @SerializedName("is_open_on")
        @Expose
        public String similarRestIsOpenOn;

        @SerializedName("restaurant_type")
        @Expose
        public String[] similarRestType;

        public String[] getSimilarRestCuisine() {
            return similarRestCuisine;
        }

        public void setSimilarRestCuisine(String[] similarRestCuisine) {
            this.similarRestCuisine = similarRestCuisine;
        }

        public String getSimilarRestAddr() {
            return similarRestAddr;
        }

        public void setSimilarRestAddr(String similarRestAddr) {
            this.similarRestAddr = similarRestAddr;
        }

        public String getSimilarRestName() {
            return similarRestName;
        }

        public void setSimilarRestName(String similarRestName) {
            this.similarRestName = similarRestName;
        }

        public String getSimilarRestId() {
            return similarRestId;
        }

        public void setSimilarRestId(String similarRestId) {
            this.similarRestId = similarRestId;
        }

        public String getSimilarRestIsOpenOn() {
            return similarRestIsOpenOn;
        }

        public void setSimilarRestIsOpenOn(String similarRestIsOpenOn) {
            this.similarRestIsOpenOn = similarRestIsOpenOn;
        }

        public String[] getSimilarRestType() {
            return similarRestType;
        }

        public void setSimilarRestType(String[] similarRestType) {
            this.similarRestType = similarRestType;
        }
    }
}
