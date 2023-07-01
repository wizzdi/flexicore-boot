package com.flexicore.response;

public class ImpersonateResponse {

    private String authenticationKey;


    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public <T extends ImpersonateResponse> T setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
        return (T) this;
    }
}
