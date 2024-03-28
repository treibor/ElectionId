package com.identity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Party {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "office_generator")
	@SequenceGenerator(name="office_generator", sequenceName = "office_seq", allocationSize=1)
	@Column(name = "partyId", updatable = false, nullable = false)
	private long partyId;
	@NotEmpty
	private String partyName;
	@ManyToOne
	@JoinColumn(name="districtId")
	@NotNull
	private District district;
	@ManyToOne
	@JoinColumn(name="stateId")
	@NotNull
	private State state;
	
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Party() {
		
	}
	public Party(String officeName) {
		//super();
		//this.oid = oid;
		this.partyName = officeName;
	}
	
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	public long getPartyId() {
		return partyId;
	}
	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	@Override
	public String toString() {
		return "Party [partyId=" + partyId + ", partyName=" + partyName + ", district=" + district + ", state=" + state
				+ "]";
	}
}