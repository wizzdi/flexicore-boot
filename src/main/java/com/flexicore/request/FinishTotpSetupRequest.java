package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;

public class FinishTotpSetupRequest {
    @JsonIgnore
    private User user;
    private String code;

    public String getCode() {
        return code;
    }

    public <T extends FinishTotpSetupRequest> T setCode(String code) {
        this.code = code;
        return (T) this;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public <T extends FinishTotpSetupRequest> T setUser(User user) {
        this.user = user;
        return (T) this;
    }
}
