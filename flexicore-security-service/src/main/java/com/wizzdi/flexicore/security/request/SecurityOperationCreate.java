package com.wizzdi.flexicore.security.request;

import com.wizzdi.segmantix.model.Access;

import java.lang.reflect.Method;

public class SecurityOperationCreate extends BasicCreate {

    private Access defaultAccess;

    private String category;
    private Method method;
    private Class<?> clazz;

    public Access getDefaultAccess() {
        return defaultAccess;
    }

    public <T extends SecurityOperationCreate> T setDefaultAccess(Access defaultAccess) {
        this.defaultAccess = defaultAccess;
        return (T) this;
    }

    public String getCategory() {
        return category;
    }

    public <T extends SecurityOperationCreate> T setCategory(String category) {
        this.category = category;
        return (T) this;
    }

    public Method getMethod() {
        return method;
    }

    public <T extends SecurityOperationCreate> T setMethod(Method method) {
        this.method = method;
        return (T) this;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public <T extends SecurityOperationCreate> T setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return (T) this;
    }
}
