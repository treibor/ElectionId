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
public class Constituency {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "const_generator")
	@SequenceGenerator(name="const_generator", sequenceName = "const_seq", allocationSize=1)
	@Column(name = "constId", updatable = false, nullable = false)
	private Long constituencyId;
	@NotEmpty
	private String constituencyName;
	private String constituencyColour;
	@ManyToOne
	@JoinColumn(name="districtId")
	@NotNull
	private District district;
	@ManyToOne
	@JoinColumn(name="stateId")
	@NotNull
	private State state;
	public Constituency() {
		
	}
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	

	public Long getConstituencyId() {
		return constituencyId;
	}

	public void setConstituencyId(Long constituencyId) {
		this.constituencyId = constituencyId;
	}

	public String getConstituencyName() {
		return constituencyName;
	}

	public void setConstituencyName(String constituencyName) {
		this.constituencyName = constituencyName;
	}

	public String getConstituencyColour() {
		return constituencyColour;
	}

	public void setConstituencyColour(String constituencyColour) {
		this.constituencyColour = constituencyColour;
	}

	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}

	@Override
	public String toString() {
		return "Constituency [constId=" + constituencyId + ", constName=" + constituencyName + ", constColour=" + constituencyColour
				+ ", district=" + district + ", state=" + state + "]";
	}
	
}