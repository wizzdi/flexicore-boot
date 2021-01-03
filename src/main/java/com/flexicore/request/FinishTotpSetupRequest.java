package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;

public class FinishTotpSetupRequest {
    @JsonIgnore
    private User user;
    private String secret;

    public String getSecret() {
        return secret;
    }

    public <T extends FinishTotpSetupRequest> T setSecret(String secret) {
        this.secret = secret;
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
