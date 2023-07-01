/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.interfaces.ProccessPlugin;
import com.wizzdi.flexicore.file.model.FileResource;

import java.util.Properties;

@AnnotatedClazz(Category = "JobInformation", Name = "JobInformation", Description = "holds jobs information")

/**
 * holds job information related to processing flow
 * @author Asaf
 *
 */
public class JobInformation {
    private Class<? extends ProccessPlugin> handler;
    private Object jobInfo;
    private boolean handle;
    private Result previousPhaseResult;
    private Result currrentPhaseResult;
    private boolean mergeWithOthers;
    private Properties jobProperties;


    public JobInformation() {
        // TODO Auto-generated constructor stub
    }


    @JsonIgnore
    public Class<? extends ProccessPlugin> getHandler() {
        return handler;
    }


    public void setHandler(Class<? extends ProccessPlugin> handler) {
        this.handler = handler;
    }


    @JsonIgnore
    public boolean isHandle() {
        return handle;
    }


    public void setHandle(boolean handle) {
        this.handle = handle;
    }


    @JsonIgnore
    public Result getPreviousPhaseResult() {
        return previousPhaseResult;
    }


    public void setPreviousPhaseResult(Result previousPhaseResult) {
        this.previousPhaseResult = previousPhaseResult;
    }


    @JsonIgnore
    public boolean isMergeWithOthers() {
        return mergeWithOthers;
    }


    public void setMergeWithOthers(boolean mergeWithOthers) {
        this.mergeWithOthers = mergeWithOthers;
    }


    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({@Type(value = FileResource.class)})
    @JsonIgnore
    public Object getJobInfo() {
        return jobInfo;
    }


    public void setJobInfo(Object jobInfo) {
        this.jobInfo = jobInfo;
    }



    public Result getCurrrentPhaseResult() {
        return currrentPhaseResult;
    }


    public void setCurrrentPhaseResult(Result currrentPhaseResult) {
        if (currrentPhaseResult != null) {
            this.currrentPhaseResult = currrentPhaseResult;
        }

    }


    @JsonIgnore
    public Properties getJobProperties() {
        return jobProperties;
    }


    public void setJobProperties(Properties jobProperties) {
        this.jobProperties = jobProperties;
    }


    @Override
    public String toString() {
        return "JobInformation [" + (handler != null ? "handler=" + handler + ", " : "")
                + (jobInfo != null ? "jobInfo=" + jobInfo + ", " : "") + "handle=" + handle + ", "
                + (previousPhaseResult != null ? "previousPhaseResult=" + previousPhaseResult + ", " : "")
                + (currrentPhaseResult != null ? "currrentPhaseResult=" + currrentPhaseResult + ", " : "")
                + "mergeWithOthers=" + mergeWithOthers + ", "
                + (jobProperties != null ? "jobProperties=" + jobProperties : "") + "]";
    }


}
