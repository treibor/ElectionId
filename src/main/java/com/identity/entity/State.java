package com.identity.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class State {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "state_generator")
	@SequenceGenerator(name="state_generator", sequenceName = "state_seq", allocationSize=1)
	long stateId;
	@NotEmpty
	String stateName;
	
	public State() {
		
	}
	public State (String state_name) {
		this.stateName=state_name;
	}
	public long getStateId() {
		return stateId;
	}
	public void setStateId(long stateId) {
		this.stateId = stateId;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	@Override
	public String toString() {
		return "State [stateId=" + stateId + ", stateName=" + stateName + "]";
	}

	
	
}
