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
