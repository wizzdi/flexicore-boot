package com.wizzdi.flexicore.boot.jpa.init.hibernate.cn.xdean.jex;

public interface ThrowingAction<T> {

	void apply() throws Throwable;
}
