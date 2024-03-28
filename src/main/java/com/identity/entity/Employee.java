package com.identity.entity;

import java.time.LocalDate;
import java.util.Arrays;
//import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;



@Entity
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_generator")
	@SequenceGenerator(name="employee_generator", sequenceName = "employee_seq", allocationSize=1)
	long eid;
	private long serialNo;
	@NotEmpty
	private String firstName;
	@NotEmpty
	private String lastName;
	@NotEmpty
	private String designation;
	private LocalDate enteredOn;
	private String enteredBy;
	@ManyToOne
	@JoinColumn(name="oid")
	@NotNull
	private Office office;
	//private Long office;
	
	@ManyToOne
	@JoinColumn(name="cid")
	@NotNull
	private Cell cell;
	//@Lob    Don't use lob for postgres
	private byte[] picture;
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="districtId", referencedColumnName="districtId"),
		//@JoinColumn(name="districtmasterId", referencedColumnName="districtId")
	})
	@NotNull
	private District district;
	@ManyToOne
	@JoinColumn(name="stateId")
	@NotNull
	private State state;
	@ManyToOne
	@JoinColumn(name="masterId")
	@NotNull
	private Districtmaster districtmaster;
	public Districtmaster getDistrictmaster() {
		return districtmaster;
	}

	public void setDistrictmaster(Districtmaster districtmaster) {
		this.districtmaster = districtmaster;
	}

	public State getState() {
		return state;
	}

	public long getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(long serialNo) {
		this.serialNo = serialNo;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Employee [eid=" + eid + ", serialNo=" + serialNo + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", designation=" + designation + ", enteredOn=" + enteredOn + ", enteredBy=" + enteredBy + ", office="
				+ office + ", cell=" + cell + ", picture=" + Arrays.toString(picture) + ", district=" + district
				+ ", state=" + state + ", districtmaster=" + districtmaster + "]";
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	

	public LocalDate getEnteredOn() {
		return enteredOn;
	}

	public void setEnteredOn(LocalDate enteredOn) {
		this.enteredOn = enteredOn;
	}

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

	public Employee() {

	}

	public Employee (String fname, String lname, String designation, Office office, Cell cell, byte[] pic, District district, Districtmaster districtmaster) {
		this.firstName=fname;
		this.lastName=lname;
		this.designation=designation;
		this.office=office;
		this.cell=cell;
		this.picture=pic;
		this.district=district;
		this.districtmaster=districtmaster;
	}

	/*
	@Override
	public String toString() {
		return "Employee [eid=" + eid + ", firstName=" + firstName + ", lastName=" + lastName + ", designation="
				+ designation + ", office=" + office + ", cell=" + cell + "]";
	}*/

	public long getEid() {
		return eid;
	}

	public void setEid(long eid) {
		this.eid = eid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String First_name) {
		this.firstName = First_name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	
	
}
