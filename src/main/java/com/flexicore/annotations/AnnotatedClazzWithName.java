/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.annotations;

public class AnnotatedClazzWithName  {
	public AnnotatedClazzWithName(String classname, AnnotatedClazz annotatedclazz) {
		this.classname=classname;
		this.annotated=annotatedclazz;
				
	}
	public AnnotatedClazz getAnnotated() {
		return annotated;
	}
	public void setAnnotated(AnnotatedClazz annotated) {
		this.annotated = annotated;
	}
	public String getClassName() {
		return classname;
	}
	public void setClassName(String className) {
		classname = className;
	}
	AnnotatedClazz annotated;
	private String classname;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classname == null) ? 0 : classname.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		return obj instanceof AnnotatedClazzWithName && this.toString().equals(obj.toString());
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.classname;
	}

	
}
