package com.identity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class MasterEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "masterelection_generator")
	@SequenceGenerator(name="masterelection_generator", sequenceName = "masterelection_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	@NotEmpty
	private String eventName;
	@ManyToOne
	@JoinColumn(name="districtId")
	@NotNull
	private District district;
	@ManyToOne
	@JoinColumn(name="stateId")
	@NotNull
	private State state;
	boolean isdefault;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public boolean isIsdefault() {
		return isdefault;
	}
	public void setIsdefault(boolean isdefault) {
		this.isdefault = isdefault;
	}
		
	
	
}
