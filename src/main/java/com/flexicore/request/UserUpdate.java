package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "used to update a user ,null/missing properties wont be updated")
public class UserUpdate extends UserCreate {
    private String id;
    @JsonIgnore
    private User user;

    @Schema(description = "id of the user to update",required = true)
    public String getId() {
        return id;
    }

    public <T extends UserUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public <T extends UserUpdate> T setUser(User user) {
        this.user = user;
        return (T) this;
    }
}
