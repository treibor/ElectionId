package com.identity.entity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;



@Entity
public class Political {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "political_generator")
	@SequenceGenerator(name="political_generator", sequenceName = "political_seq", allocationSize=1)
	long politicalId;
	private long serialNo;
	@NotEmpty
	private String firstName;
	@NotEmpty
	private String lastName;
	private LocalDate enteredOn;
	private String enteredBy;
	@ManyToOne
	@JoinColumn(name="partyId")
	@NotNull
	private Party party;
	@ManyToOne
	@JoinColumn(name="cid")
	@NotNull
	private Cell cell;
	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	//private Long office;
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

	@ManyToOne
	@JoinColumn(name="constituencyId")
	@NotNull
	private Constituency constituency;
	@ManyToOne
	@JoinColumn(name="candId")
	@NotNull
	private Candidate candidate;
	
	//@Lob    Don't use lob for postgres
	private byte[] picture;
	@ManyToOne
	@JoinColumn(name="districtId")
	@NotNull
	private District district;
	@ManyToOne
	@JoinColumn(name="stateId")
	@NotNull
	private State state;
	
	public Political() {
		
	}

	public long getPoliticalId() {
		return politicalId;
	}

	public void setPoliticalId(long politicalId) {
		this.politicalId = politicalId;
	}

	public long getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(long serialNo) {
		this.serialNo = serialNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public Constituency getConstituency() {
		return constituency;
	}

	public void setConstituency(Constituency constituency) {
		this.constituency = constituency;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
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
		return "Political [politicalId=" + politicalId + ", serialNo=" + serialNo + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", enteredOn=" + enteredOn + ", enteredBy=" + enteredBy + ", party="
				+ party + ", cell=" + cell + ", districtmaster=" + districtmaster + ", constituency=" + constituency
				+ ", candidate=" + candidate + ", picture=" + Arrays.toString(picture) + ", district=" + district
				+ ", state=" + state + "]";
	}
	
	
	
}

	