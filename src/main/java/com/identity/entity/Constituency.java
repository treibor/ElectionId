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