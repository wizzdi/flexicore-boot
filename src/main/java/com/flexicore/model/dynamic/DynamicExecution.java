package com.flexicore.model.dynamic;

import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DynamicExecution extends Baseclass {



    public DynamicExecution() {
    }

    public DynamicExecution(String name, SecurityContext securityContext) {
        super(name, securityContext);
    }

    @OneToMany(targetEntity = ServiceCanonicalName.class,mappedBy = "dynamicExecution",fetch = FetchType.EAGER)

    private List<ServiceCanonicalName> serviceCanonicalNames=new ArrayList<>();
    private String methodName;

    @OneToOne(targetEntity = ExecutionParametersHolder.class)
    private ExecutionParametersHolder executionParametersHolder;
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastExecuted;


    @OneToMany(targetEntity = ServiceCanonicalName.class,mappedBy = "dynamicExecution",fetch = FetchType.EAGER)
    public List<ServiceCanonicalName> getServiceCanonicalNames() {
        return serviceCanonicalNames;
    }

    public DynamicExecution setServiceCanonicalNames(List<ServiceCanonicalName> serviceCanonicalNames) {
        this.serviceCanonicalNames = serviceCanonicalNames;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public DynamicExecution setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }


    @OneToOne(targetEntity = ExecutionParametersHolder.class)
    public ExecutionParametersHolder getExecutionParametersHolder() {
        return executionParametersHolder;
    }

    public DynamicExecution setExecutionParametersHolder(ExecutionParametersHolder executionParametersHolder) {
        this.executionParametersHolder = executionParametersHolder;
        return this;
    }

    public OffsetDateTime getLastExecuted() {
        return lastExecuted;
    }

    public <T extends DynamicExecution> T setLastExecuted(OffsetDateTime lastExecuted) {
        this.lastExecuted = lastExecuted;
        return (T) this;
    }
}
