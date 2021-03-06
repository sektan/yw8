package server.Finder;

/**
 * Created by dishq on 03-11-2016.
 */

public class RestaurantSuggestFinder {

    int restaurantId ;
    String restaurantName;
    String restaurantAddress;
    Boolean IS_OPEN_NOW;

    public RestaurantSuggestFinder(int restaurantId, String restaurantName, String restaurantAddress,
                                   Boolean IS_OPEN_NOW) {
        super();
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.IS_OPEN_NOW = IS_OPEN_NOW;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public Boolean getIsOpenNowSearch() {
        return IS_OPEN_NOW;
    }

    public void setIsOpenNowSearch(Boolean IS_OPEN_NOW) {
        this.IS_OPEN_NOW = IS_OPEN_NOW;
    }
}
