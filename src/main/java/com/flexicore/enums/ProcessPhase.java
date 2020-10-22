/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.enums;

public enum ProcessPhase {
	Waiting("waiting"),Reading("reading"),Read("read"),Processing("processing"),Processed("processed"),Writing("writing"),Written("written"),Done("done"),Error("error"),
	Postponed("postponed");

	private final String name;
	ProcessPhase(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
}
