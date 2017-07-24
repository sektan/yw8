package com.dishq.buzz.server.Finder;

/**
 * Created by tania on 05-11-2016.
 * property of dishq
 */

public class FullUserDetailsFinder {

    private int fLifeTimePoints;
    private String fPointsToUgrade;
    private String nextBadgeName;
    private int nextBadgeLevel;
    private int mNoOfPoints;
    private String mMonthName;
    private int mRank;
    private int mMonthNo;
    private String fFullName;
    private String fDisplayName;
    private int yNoOfPoints;
    private int yRank;
    private int yYear;
    private String fCurrBadgeName;
    private int fCurrBadgeLevel;

    public FullUserDetailsFinder(int fLifeTimePoints, String fPointsToUgrade, String nextBadgeName, int nextBadgeLevel,
                               int mNoOfPoints, String mMonthName, int mRank, int mMonthNo, String fFullName,
                               String fDisplayName, int yNoOfPoints, int yRank, int yYear, String fCurrBadgeName,
                               int fCurrBadgeLevel) {
        this.fLifeTimePoints = fLifeTimePoints;
        this.fPointsToUgrade = fPointsToUgrade;
        this.nextBadgeName = nextBadgeName;
        this.nextBadgeLevel = nextBadgeLevel;
        this.mNoOfPoints = mNoOfPoints;
        this.mMonthName = mMonthName;
        this.mRank = mRank;
        this.mMonthNo = mMonthNo;
        this.fFullName = fFullName;
        this.fDisplayName = fDisplayName;
        this.yNoOfPoints = yNoOfPoints;
        this.yRank = yRank;
        this.yYear = yYear;
        this.fCurrBadgeName = fCurrBadgeName;
        this.fCurrBadgeLevel = fCurrBadgeLevel;
    }

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
