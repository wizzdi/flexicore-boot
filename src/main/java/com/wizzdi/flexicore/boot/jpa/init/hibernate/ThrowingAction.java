package com.wizzdi.flexicore.boot.jpa.init.hibernate;

public interface ThrowingAction<T> {

	void apply() throws Throwable;
}
