package server.Finder;

/**
 * Created by dishq on 04-11-2016.
 */

public class UpdatedUserProfileFinder {
    private Boolean hasBadgeUpgrade;
    private int numPointsAdded;
    private String badgeName;
    private int badgeLevel;

    public UpdatedUserProfileFinder(Boolean hasBadgeUpgrade, int numPointsAdded, String badgeName,
                                int badgeLevel) {
        this.hasBadgeUpgrade = hasBadgeUpgrade;
        this.numPointsAdded = numPointsAdded;
        this.badgeName = badgeName;
        this.badgeLevel = badgeLevel;
    }

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
