package com.wizzdi.dynamic.annotations.service.cn.xdean.jex;

public interface ThrowingSupplier<T> {

	T supply() throws Throwable;
}
