package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 03-11-2016.
 */

public class VersionCheckResponse {

    public String response;
    public VersionCheckData data;


    class VersionCheckData {
        @SerializedName("version_number")
        @Expose
        public String versionNumber;
        @SerializedName("show_update_popup")
        @Expose
        public boolean showUpdatePopup;
        @SerializedName("version_name")
        @Expose
        public String versionName;
        @SerializedName("do_force_update")
        @Expose
        public boolean doForceUpdate;
    }
}
