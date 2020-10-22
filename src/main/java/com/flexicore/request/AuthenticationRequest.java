package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;

public class AuthenticationRequest {

    private String email;
    private String phoneNumber;
    @JsonIgnore
    private User user;
    private String password;
    private long secondsValid;


    public String getEmail() {
        return email;
    }

    public AuthenticationRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AuthenticationRequest setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthenticationRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public AuthenticationRequest setUser(User user) {
        this.user = user;
        return this;
    }

    public long getSecondsValid() {
        return secondsValid;
    }

    public AuthenticationRequest setSecondsValid(long secondsValid) {
        this.secondsValid = secondsValid;
        return this;
    }
}
