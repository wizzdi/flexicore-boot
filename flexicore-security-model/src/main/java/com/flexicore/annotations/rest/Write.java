package com.flexicore.annotations.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.IOperation.Access;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@IOperation(access=Access.allow,Name="writeOperation",Description="writeOperation")
/**
 * syntactic sugar for IOperation 
 * @author Asaf
 *
 */
public @interface Write {

}
