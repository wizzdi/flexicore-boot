/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.security.SecurityContext;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@SuppressWarnings("serial")

@AnnotatedClazz(Category = "Tenancy", Name = "TenantToUser", Description = "baseClass Tenancy")
@Entity
public class TenantToUser extends Baselink {

    private boolean defualtTennant;

    @ManyToOne(targetEntity = Tenant.class)

    @Override
    public Tenant getLeftside() {
        return (Tenant) super.getLeftside();
    }

    @Override
    public void setLeftside(Baseclass leftside) {
        // TODO Auto-generated method stub
        super.setLeftside(leftside);
    }

    public TenantToUser() {
    }

    public TenantToUser(String name, SecurityContext securityContext) {
        super(name, securityContext);
    }

    @ManyToOne(targetEntity = User.class)
    //@JoinColumn(name = "rightside", referencedColumnName = "id")

    @Override
    public User getRightside() {
        return (User) super.getRightside();
    }

    public void setUser(User baseclass) {
        this.rightside = baseclass;
    }

    public boolean isDefualtTennant() {
        return defualtTennant;
    }

    public void setDefualtTennant(boolean defualtTennant) {
        this.defualtTennant = defualtTennant;
    }




}
