package com.designPatterns.ACLDatabaseSecurity.model.entity;

import com.designPatterns.ACLDatabaseSecurity.plugin.annotations.ProtectedEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@ProtectedEntity(set = SalariesSet.class, setId = "salaryId")
public class SalaryEntity {

	private long id;
	private double value;
	private UserEntity user;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@OneToOne
	@JoinColumn(name = "user_id")
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	
}
