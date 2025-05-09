package com.flexicore.annotations.rest;

import com.flexicore.annotations.IOperation;
import com.wizzdi.segmantix.model.Access;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@IOperation(access=Access.allow,Name="readOperation",Description="readOperation")
/**
 * syntactic sugar for IOperation 
 * @author Asaf
 *
 */
public @interface Read {

}
