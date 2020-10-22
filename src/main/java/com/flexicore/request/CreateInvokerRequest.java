package com.flexicore.request;

public class CreateInvokerRequest {
    private String displayName;
    private String description;
    private String canonicalName;


    public String getDisplayName() {
        return displayName;
    }

    public CreateInvokerRequest setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateInvokerRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public CreateInvokerRequest setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
        return this;
    }
}
