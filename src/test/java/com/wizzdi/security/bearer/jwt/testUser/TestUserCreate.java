package com.wizzdi.security.bearer.jwt.testUser;

import com.wizzdi.flexicore.security.request.SecurityUserCreate;

public class TestUserCreate extends SecurityUserCreate {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public <T extends TestUserCreate> T setUsername(String username) {
        this.username = username;
        return (T) this;
    }

    public String getPassword() {
        return password;
    }

    public <T extends TestUserCreate> T setPassword(String password) {
        this.password = password;
        return (T) this;
    }
}
