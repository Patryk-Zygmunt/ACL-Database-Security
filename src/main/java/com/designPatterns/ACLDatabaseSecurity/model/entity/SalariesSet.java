package com.designPatterns.ACLDatabaseSecurity.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SalariesSet {

	private long salariesSetId;
	private long salaryId;
	private long privilegeId;
	@Id
	@GeneratedValue
	public long getSalariesSetId() {
		return salariesSetId;
	}

	public void setSalariesSetId(long salariesSetId) {
		this.salariesSetId = salariesSetId;
	}

	public long getSalaryId() {
		return salaryId;
	}

	public void setSalaryId(long salaryId) {
		this.salaryId = salaryId;
	}

	public long getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(long privilegeId) {
		this.privilegeId = privilegeId;
	}

}
