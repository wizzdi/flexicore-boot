package com.flexicore.data.jsoncontainers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Used to start contact means verification")
public class VerifyMailStart {

    @JsonIgnore
    private User user;

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public VerifyMailStart setUser(User user) {
        this.user = user;
        return this;
    }
}
