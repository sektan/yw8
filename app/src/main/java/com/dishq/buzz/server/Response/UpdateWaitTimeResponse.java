package com.dishq.buzz.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tania on 03-11-2016.
 * property of dishq
 */

public class UpdateWaitTimeResponse {

    @SerializedName("response")
    @Expose
    private String updateWaitTimeResponse;

    @SerializedName("data")
    @Expose
    private List<UpdateWaitTimeInfo> updateWaitTimeInfo;

    private class UpdateWaitTimeInfo {
        @SerializedName("show_buzz_type_options")
        @Expose
        private Boolean showBuzzTypeOptions;

        @SerializedName("buzz_type_data")
        @Expose
        private List<BuzzTypeDataInfo> buzzTypeDataInfo;

        @SerializedName("display_text")
        @Expose
        private String displayText;

        @SerializedName("wait_time_in_mins")
        @Expose
        private int waitTimeInMins;

        @SerializedName("wait_time_id")
        @Expose
        private int waitTimeId;
    }

    public class BuzzTypeDataInfo {
        @SerializedName("buzz_type_id")
        @Expose
        private int buzzTypeId;

        @SerializedName("buzz_type_label")
        @Expose
        private String buzzTypeLabel;
    }
}
