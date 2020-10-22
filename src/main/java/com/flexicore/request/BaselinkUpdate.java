package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baselink;

public class BaselinkUpdate extends BaselinkCreate{

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
