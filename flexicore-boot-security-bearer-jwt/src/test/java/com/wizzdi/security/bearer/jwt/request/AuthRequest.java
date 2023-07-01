package com.wizzdi.security.bearer.jwt.request;

public class AuthRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public <T extends AuthRequest> T setUsername(String username) {
        this.username = username;
        return (T) this;
    }

    public String getPassword() {
        return password;
    }

    public <T extends AuthRequest> T setPassword(String password) {
        this.password = password;
        return (T) this;
    }
}
