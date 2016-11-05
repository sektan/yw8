package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dishq on 06-11-2016.
 */

public class YearLeaderBoardResponse {

    @SerializedName("response")
    @Expose
    private String yearResponse;

    @SerializedName("data")
    @Expose
    public List<YearPointsInfo> yearPointsInfo;

    public class YearPointsInfo {
        @SerializedName("num_points")
        @Expose
        private int yearPoints;

        @SerializedName("user_details")
        @Expose
        private YearUserDetails yearUserDetails;

        @SerializedName("rank")
        @Expose
        private int yearRank;

        public int getYearPoints() {
            return yearPoints;
        }

        public void setYearPoints(int yearPoints) {
            this.yearPoints = yearPoints;
        }

        public int getYearRank() {
            return yearRank;
        }

        public void setYearRank(int yearRank) {
            this.yearRank = yearRank;
        }

        public class YearUserDetails {
            @SerializedName("username")
            @Expose
            private String yearUserName;

            @SerializedName("is_current_user")
            @Expose
            private Boolean yearIsCurrentUser;

            @SerializedName("user_id")
            @Expose
            private int yearUserId;

            public String getYearUserName() {
                return yearUserName;
            }

            public void setYearUserName(String yearUserName) {
                this.yearUserName = yearUserName;
            }

            public Boolean getYearIsCurrentUser() {
                return yearIsCurrentUser;
            }

            public void setYearIsCurrentUser(Boolean yearIsCurrentUser) {
                this.yearIsCurrentUser = yearIsCurrentUser;
            }

            public int getYearUserId() {
                return yearUserId;
            }

            public void setYearUserId(int yearUserId) {
                this.yearUserId = yearUserId;
            }
        }
    }
}
