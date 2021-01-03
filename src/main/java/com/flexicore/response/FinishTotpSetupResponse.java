package com.flexicore.response;

import java.util.List;

public class FinishTotpSetupResponse {

    private List<String> totpRecoveryCodes;

    public List<String> getTotpRecoveryCodes() {
        return totpRecoveryCodes;
    }

    public <T extends FinishTotpSetupResponse> T setTotpRecoveryCodes(List<String> totpRecoveryCodes) {
        this.totpRecoveryCodes = totpRecoveryCodes;
        return (T) this;
    }
}
