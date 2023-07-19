package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid.List({
        @IdValid(targetField = "user", fieldType = SecurityUser.class, field = "userId", groups = {Create.class, Update.class}),
        @IdValid(targetField = "baseclass", fieldType = Baseclass.class, field = "baseclassId", groups = {Create.class, Update.class})

})
public class UserToBaseclassCreate extends SecurityLinkCreate {

    @JsonIgnore
    private SecurityUser user;
    private String userId;
    @JsonIgnore
    private Baseclass baseclass;
    private String baseclassId;

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

    @JsonIgnore
    public Baseclass getBaseclass() {
        return baseclass;
    }

    public <T extends UserToBaseclassCreate> T setBaseclass(Baseclass baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    public String getBaseclassId() {
        return baseclassId;
    }

    public <T extends UserToBaseclassCreate> T setBaseclassId(String baseclassId) {
        this.baseclassId = baseclassId;
        return (T) this;
    }
}
