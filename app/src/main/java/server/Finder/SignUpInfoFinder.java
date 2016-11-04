package server.Finder;

import android.content.Context;

/**
 * Created by dishq on 04-11-2016.
 */

public class SignUpInfoFinder {

    private String accessToken;
    private String tokenType;
    private String expiresIn;
    private String refreshToken;
    private String responseScope;
    private Context context;

    //Constructor for class
    public SignUpInfoFinder(Context context, String accessToken, String tokenType, String expiresIn, String refreshToken, String responseScope) {
        this.context = context;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.responseScope = responseScope;
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
