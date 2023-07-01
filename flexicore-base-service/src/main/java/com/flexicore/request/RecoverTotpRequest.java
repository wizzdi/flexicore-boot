package com.flexicore.request;

public class RecoverTotpRequest {
    private String recoveryCode;

    public String getRecoveryCode() {
        return recoveryCode;
    }

    public <T extends RecoverTotpRequest> T setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
        return (T) this;
    }
}
