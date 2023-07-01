package com.flexicore.response;

public class TotpAuthenticationResponse {
    private String totpAuthenticationToken;


    public String getTotpAuthenticationToken() {
        return totpAuthenticationToken;
    }

    public <T extends TotpAuthenticationResponse> T setTotpAuthenticationToken(String totpAuthenticationToken) {
        this.totpAuthenticationToken = totpAuthenticationToken;
        return (T) this;
    }
}
