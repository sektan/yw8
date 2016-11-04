package server.Finder;

/**
 * Created by dishq on 03-11-2016.
 */

public class RestaurantFinderResponse {

    int restaurantId ;
    String restaurantName;
    String restaurantAddress ;

    public RestaurantFinderResponse(int restaurantId, String restaurantName, String restaurantAddress) {
        super();
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
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

}
