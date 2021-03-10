package com.wizzdi.flexicore.boot.jpa.init.hibernate.cn.xdean.jex;

public interface ThrowingSupplier<T> {

	T supply() throws Throwable;
}
