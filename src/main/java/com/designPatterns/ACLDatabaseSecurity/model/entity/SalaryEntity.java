package com.designPatterns.ACLDatabaseSecurity.model.entity;

import com.designPatterns.ACLDatabaseSecurity.aspect.MyAspect;
import org.springframework.stereotype.Component;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Component
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

	@MyAspect
	public void setValue(double value) {
		this.value = value;
	}

	// @OneToOne(cascade = CascadeType.MERGE)

//	@OneToOne//(mappedBy = "salary")
	@OneToOne//(mappedBy="salary")
    @JoinColumn(name = "user_id")
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "SalaryEntity [id=" + id + ", value=" + value + ", user=" + user + "]";
	}
	
	
}
