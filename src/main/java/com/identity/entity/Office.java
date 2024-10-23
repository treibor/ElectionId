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
public class Office {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "office_generator")
	@SequenceGenerator(name="office_generator", sequenceName = "office_seq", allocationSize=1)
	@Column(name = "oid", updatable = false, nullable = false)
	private long oid;
	@NotEmpty
	private String officeName;
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
	public Office() {
		
	}
	public Office(String officeName) {
		//super();
		//this.oid = oid;
		this.officeName = officeName;
	}
	
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	@Override
	public String toString() {
		return "Office [oid=" + oid + ", officeName=" + officeName + ", district=" + district + ", state=" + state
				+ "]";
	}
	
}
