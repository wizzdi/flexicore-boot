package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.UserToBaseclass;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "userToBaseclass", fieldType = UserToBaseclass.class, field = "id", groups = {Update.class}),
})
public class UserToBaseclassUpdate extends UserToBaseclassCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private UserToBaseclass userToBaseclass;

    public String getId() {
        return id;
    }

    public <T extends UserToBaseclassUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public UserToBaseclass getUserToBaseclass() {
        return userToBaseclass;
    }

    public <T extends UserToBaseclassUpdate> T setUserToBaseclass(UserToBaseclass userToBaseclass) {
        this.userToBaseclass = userToBaseclass;
        return (T) this;
    }
}
