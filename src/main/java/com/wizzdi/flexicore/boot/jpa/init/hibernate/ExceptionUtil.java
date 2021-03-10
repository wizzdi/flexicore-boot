package com.wizzdi.flexicore.boot.jpa.init.hibernate;

public class ExceptionUtil {
	static <T> T uncheck(ThrowingSupplier<T> supplier) {
		try {
			return supplier.supply();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	static <T> void uncheck(ThrowingAction<T> action) {
		try {
			action.apply();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
