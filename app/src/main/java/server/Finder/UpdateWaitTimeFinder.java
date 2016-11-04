package server.Finder;

import java.util.List;

import server.Response.UpdateWaitTimeResponse;

/**
 * Created by dishq on 04-11-2016.
 */

public class UpdateWaitTimeFinder {
    Boolean showBuzzTypeOptions;
    String displayText;
    int waitTimeInMins;
    int waitTimeId;
    List<UpdateWaitTimeResponse.BuzzTypeDataInfo> buzzTypeDataInfo;

    public UpdateWaitTimeFinder(Boolean showBuzzTypeOptions, String displayText, int waitTimeInMins,
                                int waitTimeId, List<UpdateWaitTimeResponse.BuzzTypeDataInfo> buzzTypeDataInfo) {
        this.showBuzzTypeOptions = showBuzzTypeOptions;
        this.displayText = displayText;
        this.waitTimeInMins = waitTimeInMins;
        this.waitTimeId = waitTimeId;
        this.buzzTypeDataInfo = buzzTypeDataInfo;
    }

    public Boolean getShowBuzzTypeOptions() {
        return showBuzzTypeOptions;
    }

    public void setShowBuzzTypeOptions(Boolean showBuzzTypeOptions) {
        this.showBuzzTypeOptions = showBuzzTypeOptions;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public int getWaitTimeInMins() {
        return waitTimeInMins;
    }

    public void setWaitTimeInMins(int waitTimeInMins) {
        this.waitTimeInMins = waitTimeInMins;
    }

    public int getWaitTimeId() {
        return waitTimeId;
    }

    public void setWaitTimeId(int waitTimeId) {
        this.waitTimeId = waitTimeId;
    }
}
