package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 05-11-2016.
 */

public class FullUserDetailsResponse {

    @SerializedName("response")
    @Expose
    private String fullUserResponse;

    @SerializedName("data")
    @Expose
    public FullUserDetailsInfo fullUserDetailsInfo;

    public class FullUserDetailsInfo {

        @SerializedName("life_time_points")
        @Expose
        private int fLifeTimePoints;

        @SerializedName("next_badge")
        @Expose
        public NextBadgeInfo nextBadgeInfo;

        @SerializedName("month_buzz_points")
        @Expose
        public MonthBuzzPointsInfo monthBuzzPointsInfo;

        @SerializedName("user_details")
        @Expose
        public FullUserNameInfo fullUserNameInfo;

        @SerializedName("year_buzz_points")
        @Expose
        public YearBuzzPointsInfo yearBuzzPointsInfo;

        @SerializedName("curr_badge")
        @Expose
        public FullCurrBadgeInfo fullCurrBadgeInfo;

        @SerializedName("points_to_upgrade_text")
        @Expose
        private String fPointsToUgrade;

        public int getfLifeTimePoints() {
            return fLifeTimePoints;
        }

        public void setfLifeTimePoints(int fLifeTimePoints) {
            this.fLifeTimePoints = fLifeTimePoints;
        }

        public String getfPointsToUgrade() {
            return fPointsToUgrade;
        }

        public void setfPointsToUgrade(String fPointsToUgrade) {
            this.fPointsToUgrade = fPointsToUgrade;
        }
    }

    public class NextBadgeInfo {
        @SerializedName("name")
        @Expose
        private String nextBadgeName;

        @SerializedName("badge_level")
        @Expose
        private int nextBadgeLevel;

        @SerializedName("threshold_points")
        @Expose
        private int thresholdPoints;

        public String getNextBadgeName() {
            return nextBadgeName;
        }

        public void setNextBadgeName(String nextBadgeName) {
            this.nextBadgeName = nextBadgeName;
        }

        public int getNextBadgeLevel() {
            return nextBadgeLevel;
        }

        public void setNextBadgeLevel(int nextBadgeLevel) {
            this.nextBadgeLevel = nextBadgeLevel;
        }

        public int getThresholdPoints() {
            return thresholdPoints;
        }

        public void setThresholdPoints(int thresholdPoints) {
            this.thresholdPoints = thresholdPoints;
        }
    }

    public class MonthBuzzPointsInfo {

        @SerializedName("num_points")
        @Expose
        private int mNoOfPoints;

        @SerializedName("month_text")
        @Expose
        private String mMonthName;

        @SerializedName("rank")
        @Expose
        private int mRank;

        @SerializedName("month")
        @Expose
        private int mMonthNo;

        public int getmNoOfPoints() {
            return mNoOfPoints;
        }

        public void setmNoOfPoints(int mNoOfPoints) {
            this.mNoOfPoints = mNoOfPoints;
        }

        public String getmMonthName() {
            return mMonthName;
        }

        public void setmMonthName(String mMonthName) {
            this.mMonthName = mMonthName;
        }

        public int getmRank() {
            return mRank;
        }

        public void setmRank(int mRank) {
            this.mRank = mRank;
        }

        public int getmMonthNo() {
            return mMonthNo;
        }

        public void setmMonthNo(int mMonthNo) {
            this.mMonthNo = mMonthNo;
        }
    }

    public class FullUserNameInfo {

        @SerializedName("fullname")
        @Expose
        private String fFullName;

        @SerializedName("displayname")
        @Expose
        private String fDisplayName;

        public String getfFullName() {
            return fFullName;
        }

        public void setfFullName(String fFullName) {
            this.fFullName = fFullName;
        }

        public String getfDisplayName() {
            return fDisplayName;
        }

        public void setfDisplayName(String fDisplayName) {
            this.fDisplayName = fDisplayName;
        }

    }

    public class YearBuzzPointsInfo {

        @SerializedName("num_points")
        @Expose
        private int yNoOfPoints;

        @SerializedName("rank")
        @Expose
        private int yRank;

        @SerializedName("year")
        @Expose
        private int yYear;

        public int getyNoOfPoints() {
            return yNoOfPoints;
        }

        public void setyNoOfPoints(int yNoOfPoints) {
            this.yNoOfPoints = yNoOfPoints;
        }

        public int getyRank() {
            return yRank;
        }

        public void setyRank(int yRank) {
            this.yRank = yRank;
        }

        public int getyYear() {
            return yYear;
        }

        public void setyYear(int yYear) {
            this.yYear = yYear;
        }

    }

    public class FullCurrBadgeInfo {

        @SerializedName("name")
        @Expose
        private String fCurrBadgeName;

        @SerializedName("badge_level")
        @Expose
        private int fCurrBadgeLevel;

        public String getfCurrBadgeName() {
            return fCurrBadgeName;
        }

        public void setfCurrBadgeName(String fCurrBadgeName) {
            this.fCurrBadgeName = fCurrBadgeName;
        }

        public int getfCurrBadgeLevel() {
            return fCurrBadgeLevel;
        }

        public void setfCurrBadgeLevel(int fCurrBadgeLevel) {
            this.fCurrBadgeLevel = fCurrBadgeLevel;
        }
    }
}
