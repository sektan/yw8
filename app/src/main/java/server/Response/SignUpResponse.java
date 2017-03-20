package server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 02-11-2016.
 * Package name version1.dishq.dishq.
 */

public class SignUpResponse {
    @SerializedName("data")
    @Expose
    public SignUpData signUpData;

    @SerializedName("message")
    @Expose
    private String signUpMessage;

    @SerializedName("response")
    @Expose
    private String signUpResponse;

    public class SignUpData {

        @SerializedName("access_token")
        @Expose
        private String accessToken;

        @SerializedName("token_type")
        @Expose
        private String tokenType;

        @SerializedName("expires_in")
        @Expose
        private String expiresIn;

        @SerializedName("refresh_token")
        @Expose
        private String refreshToken;

        @SerializedName("scope")
        @Expose
        private String responseScope;

        @SerializedName("user_data")
        @Expose
        private UserData userData;

        public UserData getUserData() {
            return userData;
        }

        public void setUserData(UserData userData) {
            this.userData = userData;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(String expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getResponseScope() {
            return responseScope;
        }

        public void setResponseScope(String responseScope) {
            this.responseScope = responseScope;
        }

    }

    public class UserData {
        @SerializedName("username")
        @Expose
        private String userName;

        @SerializedName("user_id")
        @Expose
        private int userId;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}

