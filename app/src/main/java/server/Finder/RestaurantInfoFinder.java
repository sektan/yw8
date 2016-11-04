package server.Finder;

/**
 * Created by dishq on 03-11-2016.
 */

public class RestaurantInfoFinder {

    private String isOpenNow;
    private String displayAddress;
    private String[] cuisine;
    private String restName;
    private String restId;
    private String[] restaurantType;
    private Boolean showBuzzTypeText;
    private String waitTime;
    private String buzzTypeText;

    public RestaurantInfoFinder(String isOpenNow, String displayAddress, String[] cuisine,
                                String restName, String restId, String[] restaurantType,
                                Boolean showBuzzTypeText, String waitTime, String buzzTypeText) {
        this.isOpenNow = isOpenNow;
        this.displayAddress = displayAddress;
        this.cuisine = cuisine;
        this.restName = restName;
        this.restId = restId;
        this.restaurantType = restaurantType;
        this.showBuzzTypeText = showBuzzTypeText;
        this.waitTime = waitTime;
        this.buzzTypeText = buzzTypeText;
    }

    public String getIsOpenNow() {
        return isOpenNow;
    }

    public void setIsOpenNow(String isOpenNow) {
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
