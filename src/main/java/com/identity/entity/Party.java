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
public class Party {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "party_generator")
	@SequenceGenerator(name="party_generator", sequenceName = "party_seq", allocationSize=1)
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