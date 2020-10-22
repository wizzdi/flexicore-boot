package com.flexicore.data.jsoncontainers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public class ResetUserPasswordRequest {

    private String email;
    private String password;

    @JsonIgnore
    private User user;

    @Schema(description = "user email to reset password for",required = true)

    public String getEmail() {
        return email;
    }

    public ResetUserPasswordRequest setEmail(String email) {
        this.email = email;
        return this;
    }
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public ResetUserPasswordRequest setUser(User user) {
        this.user = user;
        return this;
    }

    @Schema(description = "password to use when resetting , " +
            "will be used only when the user is being reset not via mail (for example if an admin is resetting it)")
    public String getPassword() {
        return password;
    }

    public ResetUserPasswordRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
