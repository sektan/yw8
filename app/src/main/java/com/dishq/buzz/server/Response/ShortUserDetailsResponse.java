package com.dishq.buzz.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tania on 04-11-2016.
 * property of dishq
 */

public class ShortUserDetailsResponse {

    @SerializedName("response")
    @Expose
    private String shortUserResponse;

    @SerializedName("data")
    @Expose
    public ShortUserDetailsInfo shortUserDetailsInfo;

    public class ShortUserDetailsInfo {
        @SerializedName("life_time_points")
        @Expose
        private int lifeTimePoints;

        @SerializedName("user_details")
        @Expose
        public ShortUserDetails shortUserDetails;

        @SerializedName("curr_badge")
        @Expose
        public ShortUserCurrBadge shortUserCurrBadge;

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

        @SerializedName("user_id")
        @Expose
        private String userId;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public class ShortUserCurrBadge {

        @SerializedName("name")
        @Expose
        private String shortUserBadgeName;

        @SerializedName("badge_level")
        @Expose
        private int badgeLevel;

        public String getShortUserBadgeName() {
            return shortUserBadgeName;
        }

        public void setShortUserBadgeName(String shortUserBadgeName) {
            this.shortUserBadgeName = shortUserBadgeName;
        }

        public int getBadgeLevel() {
            return badgeLevel;
        }

        public void setBadgeLevel(int badgeLevel) {
            this.badgeLevel = badgeLevel;
        }
    }
}
