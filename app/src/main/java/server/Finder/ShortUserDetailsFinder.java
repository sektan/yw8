package server.Finder;

/**
 * Created by dishq on 04-11-2016.
 */

public class ShortUserDetailsFinder {

    private int lifeTimePoints;
    private String fullName;
    private String displayName;
    private String shortUserName;
    private int badgeLevel;

    public ShortUserDetailsFinder(int lifeTimePoints, String fullName, String displayName, String shortUserName, int badgeLevel) {
        this.lifeTimePoints = lifeTimePoints;
        this.fullName = fullName;
        this.displayName = displayName;
        this.shortUserName = shortUserName;
        this.badgeLevel = badgeLevel;
    }

    public int getLifeTimePoints() {
        return lifeTimePoints;
    }

    public void setLifeTimePoints(int lifeTimePoints) {
        this.lifeTimePoints = lifeTimePoints;
    }

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

    public String getShortUserName() {
        return shortUserName;
    }

    public void setShortUserName(String shortUserName) {
        this.shortUserName = shortUserName;
    }

    public int getBadgeLevel() {
        return badgeLevel;
    }

    public void setBadgeLevel(int badgeLevel) {
        this.badgeLevel = badgeLevel;
    }
}
