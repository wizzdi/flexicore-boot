/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * should annotate classes with restricted methods inside. This is solely for role definition.
 * Roles are created by dynamically deny or allow access to listed operations.
 * @author avishayb
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Inherited
public @interface OperationsInside {
}
