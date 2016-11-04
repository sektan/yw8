package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 04-11-2016.
 */

public class ShortUserDetailsResponse {

    @SerializedName("response")
    @Expose
    private String shortUserResponse;

    @SerializedName("data")
    @Expose
    private ShortUserDetailsInfo shortUserDetailsInfo;

    public class ShortUserDetailsInfo {
        @SerializedName("life_time_points")
        @Expose
        private int lifeTimePoints;

        @SerializedName("user_details")
        @Expose
        private ShortUserDetails shortUserDetails;

        @SerializedName("curr_badge")
        @Expose
        private ShortUserCurrBadge shortUserCurrBadge;

        public int getLifeTimePoints() {
            return lifeTimePoints;
        }

        public void setLifeTimePoints(int lifeTimePoints) {
            this.lifeTimePoints = lifeTimePoints;
        }
    }

    public class ShortUserDetails {

        @SerializedName("fullname")
        @Expose
        private String fullName;

        @SerializedName("displayname")
        @Expose
        private String displayName;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    public class ShortUserCurrBadge {

        @SerializedName("name")
        @Expose
        private String shortUserName;

        @SerializedName("badge_level")
        @Expose
        private int badgeLevel;

        public String getShortUserName() {
            return shortUserName;
        }

        public void setShortUserName(String shortUserName) {
            this.shortUserName = shortUserName;
        }

        public int getBadgeLevel() {
            return badgeLevel;
        }

        public void setBadgeLevel(int badgeLevel) {
            this.badgeLevel = badgeLevel;
        }
    }
}
