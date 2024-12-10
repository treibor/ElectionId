package com.identity.entity;


import java.io.Serializable;

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
public class Candidate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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