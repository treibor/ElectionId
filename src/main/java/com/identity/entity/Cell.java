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
public class Cell {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cell_generator")
	@SequenceGenerator(name="cell_generator", sequenceName = "cell_seq", allocationSize=1)
	@Column(name = "cid", updatable = false, nullable = false)
	private Long cid;
	@NotEmpty
	private String cellName;
	private String cellColour;
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
	public Cell() {
		
	}
	public Cell(String cellName, String cellColour) {
		super();
		//this.cid = cid;
		this.cellName = cellName;
		this.cellColour=cellColour;
	}
	
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	
	public String getCellColour() {
		return cellColour;
	}
	public void setCellColour(String cellColour) {
		this.cellColour = cellColour;
	}
	@Override
	public String toString() {
		return "Cell [cid=" + cid + ", cellName=" + cellName + ", cellColour=" + cellColour + "]";
	}
	
}
