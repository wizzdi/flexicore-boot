package com.flexicore.request;

public class TotpAuthenticationRequest {

    private String code;


    public String getCode() {
        return code;
    }

    public <T extends TotpAuthenticationRequest> T setCode(String code) {
        this.code = code;
        return (T) this;
    }

}
