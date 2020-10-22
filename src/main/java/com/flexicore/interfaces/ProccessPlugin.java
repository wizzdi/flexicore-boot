/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.interfaces;

import com.flexicore.model.Job;

import java.util.logging.Logger;

public interface ProccessPlugin extends Plugin {
	void process(Job job);

	
	String getVersion();
	void deactivate();
	void abort();
	boolean isActive();
	void setLogger(Logger logger);
	int getTTL();
	int getOrder(Job job);

}
