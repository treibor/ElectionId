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
public class Candidate {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cand_generator")
	@SequenceGenerator(name="cand_generator", sequenceName = "cand_seq", allocationSize=1)
	@Column(name = "candId", updatable = false, nullable = false)
	private long candId;
	@NotEmpty
	private String candidateName;
	@ManyToOne
	@JoinColumn(name="constId")
	@NotNull
	private Constituency constituency;
	@ManyToOne
	@JoinColumn(name="partyId")
	@NotNull
	private Party party;
	
	@ManyToOne
	@JoinColumn(name="districtId")
	@NotNull
	private District district;
	@ManyToOne
	@JoinColumn(name="stateId")
	@NotNull
	private State state;
	
	
	public Candidate() {
		
	}


	public long getCandId() {
		return candId;
	}


	public void setCandId(long candId) {
		this.candId = candId;
	}


	

	public String getCandidateName() {
		return candidateName;
	}


	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}


	public Constituency getConstituency() {
		return constituency;
	}


	public void setConstituency(Constituency constituency) {
		this.constituency = constituency;
	}


	public Party getParty() {
		return party;
	}


	public void setParty(Party party) {
		this.party = party;
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


	@Override
	public String toString() {
		return "Candidate [candId=" + candId + ", candName=" + candidateName + ", constituency=" + constituency + ", party="
				+ party + ", district=" + district + ", state=" + state + "]";
	}
	
}