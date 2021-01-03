package com.flexicore.events;

import com.flexicore.model.nosql.BaseclassNoSQL;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class BaseclassNoSQLUpdated<E extends BaseclassNoSQL>  implements ResolvableTypeProvider {

    private Class<E> type;
    private E baseclass;

    public BaseclassNoSQLUpdated(E baseclass) {
        this.baseclass = baseclass;
        this.type= (Class<E>) baseclass.getClass();
    }

    public BaseclassNoSQLUpdated() {
    }

    public E getBaseclass() {
        return baseclass;
    }

    public <T extends BaseclassNoSQLUpdated<E>> T setBaseclass(E baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    @Override
    public ResolvableType getResolvableType() {
        return type!=null?ResolvableType.forClassWithGenerics(BaseclassNoSQLUpdated.class,type):ResolvableType.forClass(BaseclassNoSQLUpdated.class);
    }
}
