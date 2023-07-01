package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Basic;

public class DeleteObjectRequest {

    private String id;
    private String type;
    @JsonIgnore
    private Basic basic;

    public String getId() {
        return id;
    }

    public <T extends DeleteObjectRequest> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public String getType() {
        return type;
    }

    public <T extends DeleteObjectRequest> T setType(String type) {
        this.type = type;
        return (T) this;
    }

    @JsonIgnore
    public Basic getBasic() {
        return basic;
    }

    public <T extends DeleteObjectRequest> T setBasic(Basic basic) {
        this.basic = basic;
        return (T) this;
    }
}
