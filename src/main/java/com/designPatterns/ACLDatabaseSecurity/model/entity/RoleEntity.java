package com.designPatterns.ACLDatabaseSecurity.model.entity;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

@Entity
public class RoleEntity {
	private long id;
	private String name;
	private Set<UserEntity> users;
	private Set<PrivilegeEntity> privileges;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	public Set<PrivilegeEntity> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<PrivilegeEntity> privileges) {
		this.privileges = privileges;
	}

}
