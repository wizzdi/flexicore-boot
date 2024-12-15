/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import com.wizzdi.segmantix.api.model.IRoleSecurity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;


@AnnotatedClazz(Category="Permissions", Name="RoleToBaseclass", Description="User Permission on Baseclass")
@Entity
public class RoleToBaseclass extends SecurityLink implements IRoleSecurity {


    @ManyToOne(targetEntity = Role.class)
    private Role role;

    @ManyToOne(targetEntity = Role.class)
    public Role getRole() {
        return role;
    }

    public <T extends RoleToBaseclass> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }

    @Transient
    @Override
    public SecurityEntity getSecurityEntity() {
        return role;
    }
}
