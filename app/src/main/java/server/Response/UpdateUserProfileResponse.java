package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 06-11-2016.
 */

public class UpdateUserProfileResponse {

    @SerializedName("message")
    @Expose
    private String updateUserMessage;

    @SerializedName("response")
    @Expose
    private String updateUserResponse;

    @SerializedName("data")
    @Expose
    private UpdateUserProfileInfo updateUserProfileInfo;

    public class UpdateUserProfileInfo {
        @SerializedName("curr_badge")
        @Expose
        private UpdateUserCurrBadgeInfo updateUserCurrBadgeInfo;

        @SerializedName("has_badge_upgrade")
        @Expose
        private Boolean updateUserHasBadgeUpgrade;

        @SerializedName("num_points_added")
        @Expose
        private int updateUserNumPointsAdded;
    }

    public class UpdateUserCurrBadgeInfo {

    }
}
