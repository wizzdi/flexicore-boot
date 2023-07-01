package com.wizzdi.security.bearer.jwt.testUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityUser;

import jakarta.persistence.Entity;

@Entity
public class TestUser extends SecurityUser {

    private String username;
    @JsonIgnore
    private String password;


    public String getUsername() {
        return username;
    }

    public <T extends TestUser> T setUsername(String username) {
        this.username = username;
        return (T) this;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public <T extends TestUser> T setPassword(String password) {
        this.password = password;
        return (T) this;
    }
}
