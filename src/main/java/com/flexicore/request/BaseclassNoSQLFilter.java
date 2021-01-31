package com.flexicore.request;

import com.flexicore.model.FilteringInformationHolder;

import javax.persistence.Entity;

@Entity
public class BaseclassNoSQLFilter extends FilteringInformationHolder {

    private String baseclassNoSQLType;
    private boolean fromDateExclusive;
    private boolean toDateExclusive;

    public String getBaseclassNoSQLType() {
        return baseclassNoSQLType;
    }

    public <T extends BaseclassNoSQLFilter> T setBaseclassNoSQLType(String baseclassNoSQLType) {
        this.baseclassNoSQLType = baseclassNoSQLType;
        return (T) this;
    }

    public boolean isFromDateExclusive() {
        return fromDateExclusive;
    }

    public <T extends BaseclassNoSQLFilter> T setFromDateExclusive(boolean fromDateExclusive) {
        this.fromDateExclusive = fromDateExclusive;
        return (T) this;
    }

    public boolean isToDateExclusive() {
        return toDateExclusive;
    }

    public <T extends BaseclassNoSQLFilter> T setToDateExclusive(boolean toDateExclusive) {
        this.toDateExclusive = toDateExclusive;
        return (T) this;
    }
}
