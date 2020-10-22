/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.enums.ProcessPhase;
import com.flexicore.exceptions.PhaseErrorDetected;
import com.flexicore.interfaces.Phaser;
import com.flexicore.security.SecurityContext;
import org.springframework.batch.core.BatchStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

@AnnotatedClazz(Category="Job", Name="Job", Description="Used to track file processing")

public class Job implements Phaser{
	private String id;
	private ConcurrentLinkedDeque<String> phaseHistory= new ConcurrentLinkedDeque<>();
	private float currentPhasePrecentage=0;
	private int numberOfProcessors=0;
	
	private JobInformation jobInformation;
	private HashMap<Class<?>,List<Object>> toPresist;
	private long batchJobId;
	private BatchStatus batchStatus;
	private ArrayList<String> history = new ArrayList<>();
	private boolean retry=false;
	private int retryTimes;
	private long retrySleepPeriod;
	private SecurityContext securityContext;
	private String entityIdToReturnToClient;
	private String typeOfReturned;
	

	
	
	public Job() {
		super();
		id=Baseclass.getBase64ID();
		setCurrentPhase(ProcessPhase.Waiting.getName());
	}

	@Override
	public String getCurrentPhase() {
		return phaseHistory.peekLast();
	}
	@Override
	public synchronized void setCurrentPhase(String currentPhase) {
		phaseHistory.add(currentPhase);
		this.notifyAll();
	}

	public float getCurrentPhasePrecentage() {
		return currentPhasePrecentage;
	}
	public void setCurrentPhasePrecentage(float currentPhasePrecentage) {
		this.currentPhasePrecentage = currentPhasePrecentage;
	}

	public int getNumberOfProcessors() {
		return numberOfProcessors;
	}

	public void setNumberOfProcessors(int numberOfProcessors) {
		this.numberOfProcessors = numberOfProcessors;
	}

	


	public long getBatchJobId() {
		return batchJobId;
	}

	public void setBatchJobId(long batchJobId) {
		this.batchJobId = batchJobId;
	}

	public BatchStatus getBatchStatus() {
		return batchStatus;
	}

	public void setBatchStatus(BatchStatus batchStatus) {
		this.batchStatus = batchStatus;
	}

	
	public JobInformation getJobInformation() {
		return jobInformation;
	}
	public void setJobInformation(JobInformation jobInformation) {
		this.jobInformation = jobInformation;
	}
	

	public Object getObjectToMergeWith(Object o){
		if(toPresist==null){
			toPresist= new HashMap<>();
		}
		List<Object> list = toPresist.computeIfAbsent(o.getClass(), k -> new ArrayList<>());
		if(list.size()>0){
			return list.get(0);
		}
		else{
			return null;
		}
	}
	
	public void addObjectToPresist(Object o){
		if(toPresist==null){
			toPresist= new HashMap<>();
		}
        List<Object> list = toPresist.computeIfAbsent(o.getClass(), k -> new ArrayList<>());
        list.add(o);
	}
	
	public void addObjectsToPersist(List<?> list){
		for (Object object : list) {
			addObjectToPresist(object);
		}
	}
	@JsonIgnore
	public HashMap<Class<?>, List<Object>> getToPresist() {
		return toPresist;
	}
	
	public void log(String s){
		history.add(s);
	}
	
	public void logHistoryAndLog(String s,Logger logger){
		history.add(s);
		if(logger!=null){
			logger.info(s);

		}
	}
	public ArrayList<String> getHistory() {
		return history;
	}
	public void setHistory(ArrayList<String> history) {
		this.history = history;
	}
	public long getRetrySleepPeriod() {
		return retrySleepPeriod;
	}
	public void setRetrySleepPeriod(long retrySleepPeriod) {
		this.retrySleepPeriod = retrySleepPeriod;
	}
	public int getRetryTimes() {
		return retryTimes;
	}
	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}
	public boolean isRetry() {
		return retry;
	}
	public void setRetry(boolean retry) {
		this.retry = retry;
	}


	
	@Override
	public String toString() {
		return "Job [" + (phaseHistory.peek() != null ? "currentPhase=" + phaseHistory.peek() + ", " : "") + "currentPhasePrecentage="
				+ currentPhasePrecentage + ", numberOfProcessors=" + numberOfProcessors + ", "
				+ (jobInformation != null ? "jobInformation=" + jobInformation + ", " : "")
				+ (toPresist != null ? "toPresist=" + toPresist + ", " : "") + "batchJobId=" + batchJobId + ", "
				+ (batchStatus != null ? "batchStatus=" + batchStatus + ", " : "")
				+ (history != null ? "history=" + history + ", " : "") + "retry=" + retry + ", retryTimes=" + retryTimes
				+ ", retrySleepPeriod=" + retrySleepPeriod + ", "
				+ (securityContext != null ? "securityContext=" + securityContext : "") + "]";
	}
	@JsonIgnore
	public SecurityContext getSecurityContext() {
		return securityContext;
	}
	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}
	
	public String getEntityIdToReturnToClient() {
		return entityIdToReturnToClient;
	}
	public void setEntityIdToReturnToClient(String entityIdToReturnToClient) {
		this.entityIdToReturnToClient = entityIdToReturnToClient;
	}
	public String getTypeOfReturned() {
		return typeOfReturned;
	}
	public void setTypeOfReturned(String typeOfReturned) {
		this.typeOfReturned = typeOfReturned;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public synchronized void waitForPhase(String phase) throws InterruptedException, PhaseErrorDetected {
		while(!this.phaseHistory.contains(phase)){
			if(this.phaseHistory.contains(ProcessPhase.Error.name())){
				throw new PhaseErrorDetected("error phase detected, history: "+history);
			}
			this.wait();

		}

	}

	@Override
	public synchronized void waitForOneOfPhase(Set<String> phases) throws InterruptedException, PhaseErrorDetected {
		while(!containPhases(phases)){
			if(this.phaseHistory.contains(ProcessPhase.Error.name())){
				throw new PhaseErrorDetected("error phase detected, history: "+history);
			}
			this.wait();

		}

	}

	private boolean containPhase(String phase) {
		return this.phaseHistory.contains(phase);
	}

	private boolean containPhases(Set<String> phase) {
		for (String s : phase) {
			if(containPhase(s)){
				return true;
			}
		}
		return false;
	}


}
