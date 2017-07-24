package com.dishq.buzz.server.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



/**
 * Created by tania on 02-11-2016.
 * property of dishq
 */

public class SignUpHelper {
    @SerializedName("grant_type")
    @Expose
    private String grantType;

    @SerializedName("backend")
    @Expose
    private String backendAuth;

    @SerializedName("client_id")
    @Expose
    private String clientId;

    @SerializedName("client_secret")
    @Expose
    private String clientSecret;

    @SerializedName("token")
    @Expose
    private String facebookAccess;

    //Constructor for the class
    public SignUpHelper(String grantType, String backendAuth, String clientId, String clientSecret, String facebookAccess){
        this.grantType = grantType;
        this.backendAuth = backendAuth;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.facebookAccess = facebookAccess;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getBackendAuth() {
        return backendAuth;
    }

    public void setBackendAuth(String backendAuth) {
        this.backendAuth = backendAuth;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getFacebookAccess() {
        return facebookAccess;
    }

    public void setFacebookAccess(String facebookAccess) {
        this.facebookAccess = facebookAccess;
    }
}