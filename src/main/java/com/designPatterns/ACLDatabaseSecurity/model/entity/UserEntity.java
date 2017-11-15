package com.designPatterns.ACLDatabaseSecurity.model.entity;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by Linus on 21.10.2017.
 */
@Entity
public class UserEntity {
	private long id;
	private String username;
	private String password;
	private Set<RoleEntity> roles;
	private SalaryEntity salary;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	@OneToOne(mappedBy = "user")
	//@JoinColumn(name = "salary_id")
//	@OneToOne(cascade = CascadeType.PERSIST, mappedBy = "user")
//	@OneToOne
//	@JoinColumn(name = "salary_id")
	public SalaryEntity getSalary() {
		return salary;
	}

	public void setSalary(SalaryEntity salary) {
		this.salary = salary;
	}

	

}
