package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;

public class DisableTotpRequest {

    @JsonIgnore
    private User user;

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public <T extends DisableTotpRequest> T setUser(User user) {
        this.user = user;
        return (T) this;
    }
}
