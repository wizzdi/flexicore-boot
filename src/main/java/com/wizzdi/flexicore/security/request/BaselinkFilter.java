package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baselink;

import java.util.List;
import java.util.Set;

public class BaselinkFilter  extends BaseclassFilter{

	@JsonIgnore
	private List<Baseclass> leftside;
	@JsonIgnore
	private List<Baseclass> rightside;
	@JsonIgnore
	private List<Baseclass> values;
	private Set<String> simpleValues;

	@JsonIgnore
	public List<Baseclass> getLeftside() {
		return leftside;
	}

	public <T extends BaselinkFilter> T setLeftside(List<Baseclass> leftside) {
		this.leftside = leftside;
		return (T) this;
	}

	@JsonIgnore
	public List<Baseclass> getRightside() {
		return rightside;
	}

	public <T extends BaselinkFilter> T setRightside(List<Baseclass> rightside) {
		this.rightside = rightside;
		return (T) this;
	}

	@JsonIgnore
	public List<Baseclass> getValues() {
		return values;
	}

	public <T extends BaselinkFilter> T setValues(List<Baseclass> values) {
		this.values = values;
		return (T) this;
	}

	public Set<String> getSimpleValues() {
		return simpleValues;
	}

	public <T extends BaselinkFilter> T setSimpleValues(Set<String> simpleValues) {
		this.simpleValues = simpleValues;
		return (T) this;
	}
}
