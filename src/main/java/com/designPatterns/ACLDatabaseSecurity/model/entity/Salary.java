package com.designPatterns.ACLDatabaseSecurity.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Salary {

	private long salaryId;
	private double salaryValue;

	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)@Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE,
	// generator="salaries_id_seq")
	// @SequenceGenerator(name="salaries_id_seq", sequenceName="salaries_id_seq",
	// allocationSize=1)
	// @Column(name = "SALARY_ID")
	// @Id
	// @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue
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
