/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import jakarta.persistence.Entity;


@AnnotatedClazz(Category="core", Name="Clazz", Description="Describes all other classes in the system")
@Entity
public class Clazz extends SecuredBasic {



}
