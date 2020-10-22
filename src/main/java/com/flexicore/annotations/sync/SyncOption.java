package com.flexicore.annotations.sync;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SyncOption {
    boolean continueSyncRecursionOnOneToMany() default false;
    boolean sync() default true;
    int version() default 0;

}
