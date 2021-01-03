package com.flexicore.events;

import com.flexicore.model.nosql.BaseclassNoSQL;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class BaseclassNoSQLCreated<E extends BaseclassNoSQL>  implements ResolvableTypeProvider {

    private Class<E> type;
    private E baseclass;

    public BaseclassNoSQLCreated(E baseclass) {
        this.baseclass = baseclass;
        this.type= (Class<E>) baseclass.getClass();
    }

    public BaseclassNoSQLCreated() {
    }

    public E getBaseclass() {
        return baseclass;
    }

    public <T extends BaseclassNoSQLCreated<E>> T setBaseclass(E baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    @Override
    public ResolvableType getResolvableType() {
        return type!=null?ResolvableType.forClassWithGenerics(BaseclassNoSQLCreated.class,type):ResolvableType.forClass(BaseclassNoSQLCreated.class);
    }
}
