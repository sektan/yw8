package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dishq on 06-11-2016.
 * Package name version1.dishq.dishq.
 */

public class MonthLeaderBoardResponse {

    @SerializedName("response")
    @Expose
    private String monthResponse;

    @SerializedName("data")
    @Expose
    public ArrayList<MonthPointsInfo> monthPointsInfo;

    public class MonthPointsInfo {
        @SerializedName("num_points")
        @Expose
        public int monthPoints;

        @SerializedName("user_details")
        @Expose
        public MonthUserDetails monthUserDetails;

        @SerializedName("rank")
        @Expose
        public int monthRank;

        public int getMonthPoints() {
            return monthPoints;
        }

        public void setMonthPoints(int monthPoints) {
            this.monthPoints = monthPoints;
        }

        public MonthUserDetails getMonthUserDetails() {
            return monthUserDetails;
        }

        public void setMonthUserDetails(MonthUserDetails monthUserDetails) {
            this.monthUserDetails = monthUserDetails;
        }

        public int getMonthRank() {
            return monthRank;
        }

        public void setMonthRank(int monthRank) {
            this.monthRank = monthRank;
        }

    }

        public class MonthUserDetails {
            @SerializedName("username")
            @Expose
            String monthUserName;

            @SerializedName("is_current_user")
            @Expose
            Boolean monthIsCurrentUser;

            @SerializedName("user_id")
            @Expose
            int monthUserId;

            public String getMonthUserName() {
                return monthUserName;
            }

            public void setMonthUserName(String monthUserName) {
                this.monthUserName = monthUserName;
            }

            public Boolean getMonthIsCurrentUser() {
                return monthIsCurrentUser;
            }

            public void setMonthIsCurrentUser(Boolean monthIsCurrentUser) {
                this.monthIsCurrentUser = monthIsCurrentUser;
            }

            public int getMonthUserId() {
                return monthUserId;
            }

            public void setMonthUserId(int monthUserId) {
                this.monthUserId = monthUserId;
            }
    }
}
