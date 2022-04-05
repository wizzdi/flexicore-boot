package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Clazz;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import javax.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "Clazz", fieldType = Clazz.class, field = "id", groups = {Update.class})

})
public class ClazzUpdate extends ClazzCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private Clazz Clazz;

    public String getId() {
        return id;
    }

    public <T extends ClazzUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Clazz getClazz() {
        return Clazz;
    }

    public <T extends ClazzUpdate> T setClazz(Clazz Clazz) {
        this.Clazz = Clazz;
        return (T) this;
    }
}
