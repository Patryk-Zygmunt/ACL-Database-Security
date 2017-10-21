package com.designPatterns.ACLDatabaseSecurity.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Salary {

	private long salaryId;
	private double salaryValue;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getSalaryId() {
		return salaryId;
	}

	public void setSalaryId(long salaryId) {
		this.salaryId = salaryId;
	}

	public double getSalaryValue() {
		return salaryValue;
	}

	public void setSalaryValue(double salaryValue) {
		this.salaryValue = salaryValue;
	}

}
