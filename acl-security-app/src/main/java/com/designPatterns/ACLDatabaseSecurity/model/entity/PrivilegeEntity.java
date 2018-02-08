package com.designPatterns.ACLDatabaseSecurity.model.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

@Entity
public class PrivilegeEntity {
	private long privilegeId;
	private String name;
	private Set<RoleEntity> roles;
//	private Set<SalaryEntity> salaries;

	@Id
	@GeneratedValue
	public long getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(long privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(mappedBy = "privileges",fetch = FetchType.EAGER)
	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(
//			name = "salaries_set", 
//			joinColumns = @JoinColumn(name = "privilege_id"), 
//			inverseJoinColumns = @JoinColumn(name = "salary_id")
//	)
//	public Set<SalaryEntity> getSalaries() {
//		return salaries;
//	}
//
//	public void setSalaries(Set<SalaryEntity> salaries) {
//		this.salaries = salaries;
//	}

}
