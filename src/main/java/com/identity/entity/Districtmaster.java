package com.identity.entity;

import java.time.LocalDate;
import java.util.Arrays;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Districtmaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "master_generator")
	@SequenceGenerator(name="master_generator", sequenceName = "master_seq", allocationSize=1)
	long masterId;
	String masterLabel;
	private byte[] leftImage;
	private byte[] rightImage;
	private byte[] signatureImage;
	@NotEmpty
	private String headerFirstLine;
	private String headerSecondLine;
	private String headerThirdLine;
	private String footerFirstLine;
	private String footerSecondLine;
	private String footerThirdLine;
	private LocalDate validity;
	@ManyToOne(targetEntity = District.class)
	private District district;
	public Districtmaster() {
		//super();
	}
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	public long getMasterId() {
		return masterId;
	}
	public void setMasterId(long masterId) {
		this.masterId = masterId;
	}
	public String getMasterLabel() {
		return masterLabel;
	}
	public void setMasterLabel(String masterLabel) {
		this.masterLabel = masterLabel;
	}
	
	public byte[] getLeftImage() {
		return leftImage;
	}
	public void setLeftImage(byte[] leftImage) {
		this.leftImage = leftImage;
	}
	public byte[] getRightImage() {
		return rightImage;
	}
	public void setRightImage(byte[] rightImage) {
		this.rightImage = rightImage;
	}
	public byte[] getSignatureImage() {
		return signatureImage;
	}
	public void setSignatureImage(byte[] signatureImage) {
		this.signatureImage = signatureImage;
	}
	public String getHeaderFirstLine() {
		return headerFirstLine;
	}
	public void setHeaderFirstLine(String headerFirstLine) {
		this.headerFirstLine = headerFirstLine;
	}
	public String getHeaderSecondLine() {
		return headerSecondLine;
	}
	public void setHeaderSecondLine(String headerSecondLine) {
		this.headerSecondLine = headerSecondLine;
	}
	public String getHeaderThirdLine() {
		return headerThirdLine;
	}
	public void setHeaderThirdLine(String headerThirdLine) {
		this.headerThirdLine = headerThirdLine;
	}
	public String getFooterFirstLine() {
		return footerFirstLine;
	}
	public void setFooterFirstLine(String footerFirstLine) {
		this.footerFirstLine = footerFirstLine;
	}
	public String getFooterSecondLine() {
		return footerSecondLine;
	}
	public void setFooterSecondLine(String footerSecondLine) {
		this.footerSecondLine = footerSecondLine;
	}
	public String getFooterThirdLine() {
		return footerThirdLine;
	}
	public void setFooterThirdLine(String footerThirdLine) {
		this.footerThirdLine = footerThirdLine;
	}
	public LocalDate getValidity() {
		return validity;
	}
	public void setValidity(LocalDate validity) {
		this.validity = validity;
	}
	@Override
	public String toString() {
		return "Districtmaster [masterId=" + masterId + ", masterLabel=" + masterLabel + ", leftImage="
				+ Arrays.toString(leftImage) + ", rightImage=" + Arrays.toString(rightImage) + ", signatureImage="
				+ Arrays.toString(signatureImage) + ", headerFirstLine=" + headerFirstLine + ", headerSecondLine="
				+ headerSecondLine + ", headerThirdLine=" + headerThirdLine + ", footerFirstLine=" + footerFirstLine
				+ ", footerSecondLine=" + footerSecondLine + ", footerThirdLine=" + footerThirdLine + ", validity="
				+ validity + ", district=" + district + "]";
	}
	
	
}