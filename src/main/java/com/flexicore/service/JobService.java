package com.flexicore.service;

import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.interfaces.ProccessPlugin;
import com.flexicore.model.Job;
import com.flexicore.request.RegisterForJobUpdates;
import com.flexicore.security.SecurityContext;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public interface JobService extends FlexiCoreService {
    Map<String, Session> sessionMap = new ConcurrentHashMap<>();


    static void registerSession(Session session) {
       sessionMap.put(session.getId(), session);
    }

    static void unregisterSession(Session session) {
        sessionMap.remove(session.getId());
    }
    @Deprecated
    Job startJob(Object content, Class<? extends ProccessPlugin> type,
                        Properties jobprops, HashMap<String, Object> requriments, SecurityContext securityContext);
    void startJob(Job job, SecurityContext securityContext);
    void setJobDefualts(Job job);
    boolean checkJobValidity(Job job);
    void putFileProcessJob(Job job);

    void put(Job job);
    Job get(String id);

    Job checkJobStatus(String id);

    Job getProcess(String id);

    void updateJobProperty(String id, String key, String value);

    void cancel(String id);

    void updateJobPhase(String jobID, String phaseName);

    void registerForJobUpdates(RegisterForJobUpdates registerForJobUpdates, SecurityContext securityContext);
}
