package com.identity.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class District {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "district_generator")
	@SequenceGenerator(name="district_generator", sequenceName = "district_seq", allocationSize=1)
	@JoinTable(name= "Districtmaster")
	private long districtId;
	@NotEmpty
	String districtName;
	@ManyToOne
	@JoinColumn(name="stateId")
	@NotNull
	private State state;
	
	
	
	public District() {
		//super();
	}
	public District(String district_name, State state) {
		this.districtName=district_name;
		this.state=state;
	}
	public long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "District [districtId=" + districtId + ", districtName=" + districtName + ", state=" + state
				+ "]";
	}
	
	
	
}