package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baselink;

public class BaselinkUpdate extends SecuredBasicCreate{

    private String id;
    @JsonIgnore
    private Baselink baselink;

    public String getId() {
        return id;
    }

    public <T extends SecuredBasicUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Baselink getBaselink() {
        return baselink;
    }

    public <T extends SecuredBasicUpdate> T setBaselink(Baselink baselink) {
        this.baselink = baselink;
        return (T) this;
    }
}
