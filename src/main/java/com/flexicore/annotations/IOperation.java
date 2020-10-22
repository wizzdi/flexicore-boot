/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.annotations;

import com.flexicore.model.Baseclass;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * defines an operation to be secured and list for Role association
 *
 * @author Avishay Ben Natan
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD,ANNOTATION_TYPE})
@Inherited
public @interface IOperation {
	String Name() default "";
	String Description() default "";
	String Category() default "General";
	boolean auditable() default false;
	Class<? extends Baseclass>[] relatedClazzes() default {};
	
	enum Access {
		deny, allow
		
		
	}
	Access access() default Access.allow;
}
