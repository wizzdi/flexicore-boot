package com.wizzdi.flexicore.boot.jpa.init.hibernate;

public interface ThrowingSupplier<T> {

	T supply() throws Throwable;
}
