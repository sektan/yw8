package com.dishq.buzz.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tania on 04-11-2016.
 * property of dishq
 */

public class UpdateRestaurantResponse {

    @SerializedName("message")
    @Expose
    private String restUpdateMessage;

    @SerializedName("data")
    @Expose
    public UserProfileUpdateInfo userProfileUpdateInfo;

    @SerializedName("response")
    @Expose
    private String updateRestResponse;

    public class UserProfileUpdateInfo {
        @SerializedName("curr_badge")
        @Expose
        public CurrentBadgeInfo currentBadgeInfo;

        @SerializedName("has_badge_upgrade")
        @Expose
        private Boolean hasBadgeUpgrade;

        @SerializedName("num_points_added")
        @Expose
        private int numPointsAdded;

        public Boolean getHasBadgeUpgrade() {
            return hasBadgeUpgrade;
        }

        public void setHasBadgeUpgrade(Boolean hasBadgeUpgrade) {
            this.hasBadgeUpgrade = hasBadgeUpgrade;
        }

        public int getNumPointsAdded() {
            return numPointsAdded;
        }

        public void setNumPointsAdded(int numPointsAdded) {
            this.numPointsAdded = numPointsAdded;
        }
    }

    public class CurrentBadgeInfo {
        @SerializedName("name")
        @Expose
        private String badgeName;

        @SerializedName("badge_level")
        @Expose
        private int badgeLevel;

        public String getBadgeName() {
            return badgeName;
        }

        public void setBadgeName(String badgeName) {
            this.badgeName = badgeName;
        }

        public int getBadgeLevel() {
            return badgeLevel;
        }

        public void setBadgeLevel(int badgeLevel) {
            this.badgeLevel = badgeLevel;
        }
    }
}
