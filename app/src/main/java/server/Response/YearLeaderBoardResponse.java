package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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

    public YearLeaderBoardResponse (ArrayList<YearPointsInfo> yearPointsInfo) {
        this.yearPointsInfo = yearPointsInfo;
    }

    public class YearPointsInfo {
        @SerializedName("num_points")
        @Expose
        public int yearPoints;

        @SerializedName("user_details")
        @Expose
        public YearUserDetails yearUserDetails;

        @SerializedName("rank")
        @Expose
        public int yearRank;

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
            public String yearUserName;

            @SerializedName("is_current_user")
            @Expose
            public Boolean yearIsCurrentUser;

            @SerializedName("user_id")
            @Expose
            public int yearUserId;

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
