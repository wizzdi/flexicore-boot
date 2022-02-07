package com.wizzdi.flexicore.common.user.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "used to update a user ,null/missing properties wont be updated")
public class CommonUserUpdate extends CommonUserCreate {
    private String id;
    @JsonIgnore
    private User user;

    @Schema(description = "id of the user to update",required = true)
    public String getId() {
        return id;
    }

    public <T extends CommonUserUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public <T extends CommonUserUpdate> T setUser(User user) {
        this.user = user;
        return (T) this;
    }
}
