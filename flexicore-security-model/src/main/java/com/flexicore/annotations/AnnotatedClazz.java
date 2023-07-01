/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to create, on the source, default instances.
 * @author Avishay Ben Natan
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotatedClazz {

	String Name() default "";
	String Description() default "";
	String DisplayName() default "";
	String Category() default "General";





}

