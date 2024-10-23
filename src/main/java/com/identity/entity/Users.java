package com.identity.entity;

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
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_generator")
	@SequenceGenerator(name="users_generator", sequenceName = "users_seq", allocationSize=1)
	long userId;
	@NotEmpty
	private String userName;
	@NotEmpty
	private String password;
	@NotEmpty
	private String role;
	@ManyToOne
	@JoinColumn(name="districtId")
	@NotNull
	private District district;
	
	@ManyToOne
	@JoinColumn(name="stateId")
	@NotNull
	private State state;
	
	public Users() {
		
	}
	public Users(String user_name, String password, String role, District district, State state) {
		this.userName=user_name;
		this.password=password;
		this.role=role;
		this.district=district;
		this.state=state;
	}
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	@Override
	public String toString() {
		return "Users [userId=" + userId + ", userName=" + userName + ", password=" + password + ", role=" + role
				+ ", district=" + district + ", state=" + state + "]";
	}
	
	
}
