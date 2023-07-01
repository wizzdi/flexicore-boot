package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.nosql.BaseclassNoSQL;

public class BaseclassNoSQLUpdate extends BaseclassNoSQLCreate{

    private String id;
    @JsonIgnore
    private BaseclassNoSQL baseclassNoSQL;

    public String getId() {
        return id;
    }

    public <T extends BaseclassNoSQLUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public BaseclassNoSQL getBaseclassNoSQL() {
        return baseclassNoSQL;
    }

    public <T extends BaseclassNoSQLUpdate> T setBaseclassNoSQL(BaseclassNoSQL baseclassNoSQL) {
        this.baseclassNoSQL = baseclassNoSQL;
        return (T) this;
    }
}
