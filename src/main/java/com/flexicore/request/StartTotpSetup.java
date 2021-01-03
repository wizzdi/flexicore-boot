package com.flexicore.request;

import com.flexicore.model.User;

public class StartTotpSetup {
    private User user;

    public User getUser() {
        return user;
    }

    public <T extends StartTotpSetup> T setUser(User user) {
        this.user = user;
        return (T) this;
    }
}
