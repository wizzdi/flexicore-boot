package com.wizzdi.flexicore.security.events;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class BasicUpdated<E extends Basic>  implements ResolvableTypeProvider {

    private Class<E> type;
    private E baseclass;

    public BasicUpdated(E baseclass) {
        this.baseclass = baseclass;
        this.type= (Class<E>) baseclass.getClass();
    }

    public BasicUpdated() {
    }

    public E getBaseclass() {
        return baseclass;
    }

    public <T extends BasicUpdated<E>> T setBaseclass(E baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    @Override
    public ResolvableType getResolvableType() {
        return type!=null?ResolvableType.forClassWithGenerics(BasicUpdated.class,type):ResolvableType.forClass(BasicUpdated.class);
    }
}
