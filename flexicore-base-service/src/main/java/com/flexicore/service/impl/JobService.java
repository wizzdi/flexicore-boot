/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.service.impl;

import com.flexicore.enums.ProcessPhase;
import com.flexicore.interfaces.AnalyzerPlugin;
import com.flexicore.interfaces.ProccessPlugin;
import com.flexicore.model.Job;
import com.flexicore.model.JobInformation;
import com.flexicore.model.User;
import com.flexicore.request.RegisterForJobUpdates;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.websocket.Session;
import jakarta.ws.rs.BadRequestException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Component
@Primary
@Extension
public class JobService implements com.flexicore.service.JobService {
    private static Map<String, Map<String, Session>> jobListeners = new ConcurrentHashMap<>();

    @Autowired(required = false)
    @Lazy
    private JobLauncher jobLauncher;

    @Autowired(required = false)
    @Lazy
    private JobExplorer jobExplorer;

    @Autowired(required = false)
    @Lazy
    private org.springframework.batch.core.Job job;
    private static final Logger logger = LoggerFactory.getLogger(JobService.class);


    @Override
    public Job checkJobStatus(String id) {
        Job processJob = get(id);

        long jid = processJob.getBatchJobId();
        JobExecution jobExecution=jobExplorer.getJobExecution(jid);
        if(jobExecution!=null){
            processJob.setBatchStatus(jobExecution.getStatus());
        }
        return processJob;
    }

    @Override
    public boolean checkJobValidity(Job job) {
        return (job != null && job.getJobInformation() != null && job.getJobInformation().getHandler() != null);
    }

    @Override
    public void setJobDefualts(Job job) {
        job.setCurrentPhase(ProcessPhase.Waiting.getName());
        job.setCurrentPhasePrecentage(0);

    }

    @Override
    public void startJob(Job job, SecurityContext securityContext) {
        if (checkJobValidity(job)) {
            setJobDefualts(job);
            putFileProcessJob(job);

            job.setSecurityContext(securityContext);
            Properties prop = new Properties();
            prop.setProperty("fileProcessJobId", job.getId());
            try {
                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("jobId", job.getId(), true).toJobParameters();
                JobExecution jobExecution=jobLauncher.run(this.job, jobParameters);
                job.setBatchJobId(jobExecution.getJobId());
            } catch (Exception e) {
                logger.error("failed starting job", e);
            }

        }

    }

    @Override
    public Job startJob(Object content, Class<? extends ProccessPlugin> type,
                        Properties jobprops, HashMap<String, Object> requriments, SecurityContext securityContext) {
        Job job;
        User user = null;
        if (securityContext != null) {
            user = securityContext.getUser();
        }
        if (requriments == null) {
            requriments = new HashMap<>();
        }
        if (jobprops == null) {
            jobprops = new Properties();
        }
        job = new Job();

        JobInformation info = new JobInformation();
        info.setJobInfo(content);
        info.setHandle(true); // tells the PI system to read the next Cycle.
        info.setHandler(type); // the first PI to run (or
        // multiple of) will be an
        // Analyzer PI
        info.setJobProperties(jobprops);
        job.setCurrentPhase(ProcessPhase.Waiting.getName());
        job.setCurrentPhasePrecentage(0);
        job.setJobInformation(info);
        job.setSecurityContext(securityContext); // We need to know who has
        startJob(job, securityContext);
        return job;
    }

    @Override
    public void putFileProcessJob(Job job) {
        put(job);
    }
    @CachePut(value = "jobCache", key = "#job.id",cacheManager = "jobCacheManager",unless = "#result == null")
    public Job putInCache(Job job) {
        return job;
    }

    @Override
    public void put(Job job) {
        putInCache(job);
    }

    @Override
    @Cacheable(value = "jobCache", key = "#id",cacheManager = "jobCacheManager",unless = "#result==null")
    public Job get(String id) {
        return null;
    }



    @Override
    public Job getProcess(String id) {
        return get(id);
    }


    @Override
    public void updateJobProperty(String id, String key, String value) {
        Job processJob = get(id);
        Properties props = processJob.getJobInformation().getJobProperties();
        if (props == null) {
            props = new Properties();
            processJob.getJobInformation().setJobProperties(props);
        }
        props.setProperty(key, value);

    }

    @Override
    public void cancel(String id) {
        Job processJob = get(id);
        long jid = processJob.getBatchJobId();
        JobOperator jo = BatchRuntime.getJobOperator();
        jo.abandon(jid);
    }

    @Override
    public void updateJobPhase(String jobID, String phaseName) {
        Job job = get(jobID);
        job.setCurrentPhase(phaseName);
    }

    @Override
    public void registerForJobUpdates(RegisterForJobUpdates registerForJobUpdates, SecurityContext securityContext) {
        Job job = get(registerForJobUpdates.getJobId());
        if (job == null) {
            throw new BadRequestException("No Job with id " + registerForJobUpdates.getJobId());
        }
        Session session = sessionMap.get(registerForJobUpdates.getWebSocketSessionId());
        if (session == null) {
            throw new BadRequestException("No Session with id " + registerForJobUpdates.getWebSocketSessionId());
        }
        jobListeners.computeIfAbsent(job.getId(), f -> new ConcurrentHashMap<>()).put(session.getId(), session);

    }

    public Job test(SecurityContext securityContext) {


        return startJob(null, AnalyzerPlugin.class, null, null, securityContext);
    }
}
