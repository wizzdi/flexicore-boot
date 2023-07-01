/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.security.SecurityContextBase;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@SuppressWarnings("serial")

@AnnotatedClazz(Category = "Tenancy", Name = "TenantToUser", Description = "baseClass Tenancy")
@Entity
public class TenantToUser extends Baselink {

    private boolean defualtTennant;

    @ManyToOne(targetEntity = SecurityTenant.class)

    @Override
    public SecurityTenant getLeftside() {
        return (SecurityTenant) super.getLeftside();
    }

    @Override
    public void setLeftside(Baseclass leftside) {
        // TODO Auto-generated method stub
        super.setLeftside(leftside);
    }

    public TenantToUser() {
    }

    public TenantToUser(String name, SecurityContextBase securityContext) {
        super(name, securityContext);
    }

    @ManyToOne(targetEntity = SecurityUser.class)
    //@JoinColumn(name = "rightside", referencedColumnName = "id")

    @Override
    public SecurityUser getRightside() {
        return (SecurityUser) super.getRightside();
    }

    public void setUser(SecurityUser baseclass) {
        this.rightside = baseclass;
    }

    public boolean isDefualtTennant() {
        return defualtTennant;
    }

    public void setDefualtTennant(boolean defualtTennant) {
        this.defualtTennant = defualtTennant;
    }




}
