package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid.List({
        @IdValid(targetField = "user", fieldType = SecurityUser.class, field = "userId", groups = {Create.class, Update.class}),

})
public class UserToBaseclassCreate extends SecurityLinkCreate {

    @JsonIgnore
    private SecurityUser user;
    private String userId;


    @JsonIgnore
    public SecurityUser getUser() {
        return user;
    }

    public <T extends UserToBaseclassCreate> T setUser(SecurityUser user) {
        this.user = user;
        return (T) this;
    }

    public String getUserId() {
        return userId;
    }

    public <T extends UserToBaseclassCreate> T setUserId(String userId) {
        this.userId = userId;
        return (T) this;
    }

}
