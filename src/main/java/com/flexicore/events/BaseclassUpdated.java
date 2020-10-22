package com.flexicore.events;

import com.flexicore.model.Baseclass;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class BaseclassUpdated<E extends Baseclass>  implements ResolvableTypeProvider {

    private Class<E> type;
    private E baseclass;

    public BaseclassUpdated(E baseclass) {
        this.baseclass = baseclass;
        this.type= (Class<E>) baseclass.getClass();
    }

    public BaseclassUpdated() {
    }

    public E getBaseclass() {
        return baseclass;
    }

    public <T extends BaseclassUpdated<E>> T setBaseclass(E baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    @Override
    public ResolvableType getResolvableType() {
        return type!=null?ResolvableType.forClassWithGenerics(BaseclassUpdated.class,type):ResolvableType.forClass(BaseclassUpdated.class);
    }
}
