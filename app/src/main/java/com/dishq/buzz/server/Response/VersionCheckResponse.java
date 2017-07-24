package com.dishq.buzz.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tania on 03-11-2016.
 * property of dishq
 */

public class VersionCheckResponse {

    @SerializedName("response")
    @Expose
    public String response;

    @SerializedName("data")
    @Expose
    public VersionCheckData versionCheckData;

    public class VersionCheckData {
        @SerializedName("version_number")
        @Expose
        String lVersionNumber;
        @SerializedName("show_update_popup")
        @Expose
        boolean showUpdatePopup;
        @SerializedName("version_name")
        @Expose
        String lVersionName;
        @SerializedName("do_force_update")
        @Expose
        boolean doForceUpdate;

        public String getlVersionNumber() {
           return lVersionNumber;
        }

        public void setlVersionNumber(String lVersionNumber) {
            this.lVersionNumber = lVersionNumber;
        }

        public boolean getShowUpdatePopup() {
            return showUpdatePopup;
        }

        public void setShowUpdatePopup(Boolean showUpdatePopup) {
            this.showUpdatePopup = showUpdatePopup;
        }

        public String getlVersionName() {
            return lVersionName;
        }

        public void setlVersionName(String lVersionName) {
            this.lVersionName = lVersionName;
        }

        public Boolean getDoForceUpdate() {
            return doForceUpdate;
        }

        public void setDoForceUpdate(Boolean doForceUpdate) {
            this.doForceUpdate = doForceUpdate;
        }
    }
}
