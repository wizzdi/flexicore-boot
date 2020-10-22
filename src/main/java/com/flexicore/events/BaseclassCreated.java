package com.flexicore.events;

import com.flexicore.model.Baseclass;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class BaseclassCreated<E extends Baseclass>  implements ResolvableTypeProvider {

    private Class<E> type;
    private E baseclass;

    public BaseclassCreated(E baseclass) {
        this.baseclass = baseclass;
        this.type= (Class<E>) baseclass.getClass();
    }

    public BaseclassCreated() {
    }

    public E getBaseclass() {
        return baseclass;
    }

    public <T extends BaseclassCreated<E>> T setBaseclass(E baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    @Override
    public ResolvableType getResolvableType() {
        return type!=null?ResolvableType.forClassWithGenerics(BaseclassCreated.class,type):ResolvableType.forClass(BaseclassCreated.class);
    }
}
