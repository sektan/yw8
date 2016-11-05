package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dishq on 06-11-2016.
 */

public class MonthLeaderBoardResponse {

    @SerializedName("response")
    @Expose
    private String monthResponse;

    @SerializedName("data")
    @Expose
    public List<MonthPointsInfo> monthPointsInfo;

    public class MonthPointsInfo {
        @SerializedName("num_points")
        @Expose
        private int monthPoints;

        @SerializedName("user_details")
        @Expose
        private MonthUserDetails monthUserDetails;

        @SerializedName("rank")
        @Expose
        private int monthRank;

        public int getMonthPoints() {
            return monthPoints;
        }

        public void setMonthPoints(int monthPoints) {
            this.monthPoints = monthPoints;
        }

        public int getMonthRank() {
            return monthRank;
        }

        public void setMonthRank(int monthRank) {
            this.monthRank = monthRank;
        }

        public class MonthUserDetails {
            @SerializedName("username")
            @Expose
            private String monthUserName;

            @SerializedName("is_current_user")
            @Expose
            private Boolean monthIsCurrentUser;

            @SerializedName("user_id")
            @Expose
            private int monthUserId;

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
}
