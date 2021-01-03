package com.flexicore.response;

public class SetupTotpResponse {
    private String secret;
    private String base64QRCode;

    public String getSecret() {
        return secret;
    }

    public <T extends SetupTotpResponse> T setSecret(String secret) {
        this.secret = secret;
        return (T) this;
    }

    public String getBase64QRCode() {
        return base64QRCode;
    }

    public <T extends SetupTotpResponse> T setBase64QRCode(String base64QRCode) {
        this.base64QRCode = base64QRCode;
        return (T) this;
    }
}
