package com.wizzdi.flexicore.boot.jpa.init.hibernate;

import java.lang.reflect.Method;

public abstract class EncryptionConfiguration {
	private final Class<?> clazz;
	private final String columnName;
	private final Method getter;

	public EncryptionConfiguration( String columnName, Method getter) {
		this.columnName = columnName;
		Method rootGetter=ReflectUtil.getRootMethod(getter);
		this.getter=rootGetter!=null?rootGetter: getter;
		this.clazz=this.getter.getDeclaringClass();

	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getColumnName() {
		return columnName;
	}

	public Method getGetter() {
		return getter;
	}

	public abstract String getRead();
	public abstract String getWrite();
	public abstract String getForColumn();

}
