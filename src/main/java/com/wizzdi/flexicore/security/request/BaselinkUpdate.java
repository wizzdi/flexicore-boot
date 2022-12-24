package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baselink;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "baselink", fieldType = Baselink.class, field = "id", groups = {Update.class})

})
public class BaselinkUpdate extends BaselinkCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private Baselink baselink;

    public String getId() {
        return id;
    }

    public <T extends BaselinkUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Baselink getBaselink() {
        return baselink;
    }

    public <T extends BaselinkUpdate> T setBaselink(Baselink baselink) {
        this.baselink = baselink;
        return (T) this;
    }
}
