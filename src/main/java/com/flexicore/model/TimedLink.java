/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.security.SecurityContext;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.OffsetDateTime;

@SuppressWarnings("serial")

@Entity

public class TimedLink extends Baselink {


	public TimedLink() {
	}

	public TimedLink(String name, SecurityContext securityContext) {
		super(name, securityContext);
	}
	@Column(columnDefinition = "timestamp with time zone")
	private OffsetDateTime startTime;
	@Column(columnDefinition = "timestamp with time zone")
	private OffsetDateTime endTime;

	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(OffsetDateTime startTime) {
		this.startTime = startTime;
	}

	public OffsetDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(OffsetDateTime endTime) {
		this.endTime = endTime;
	}
}
