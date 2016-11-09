package server.Finder;

/**
 * Created by dishq on 09-11-2016.
 */

public class MonthlyLeaderBoardFinder {

    private int monthPoints;
    private int monthRank;
    private String monthUserName;
    private Boolean monthIsCurrentUser;
    private int monthUserId;

    public MonthlyLeaderBoardFinder(int monthPoints, int monthRank, String monthUserName,
                                    Boolean monthIsCurrentUser, int monthUserId) {
        this.monthPoints = monthPoints;
        this.monthRank = monthRank;
        this.monthUserName = monthUserName;
        this.monthIsCurrentUser = monthIsCurrentUser;
        this.monthUserId = monthUserId;
    }


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
