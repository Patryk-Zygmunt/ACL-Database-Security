package com.designPatterns.ACLDatabaseSecurity.model.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class RoleEntity {
	private long roleId;
	private String name;
	private Set<UserEntity> users;
	private Set<PrivilegeEntity> privileges;

	@Id
	@GeneratedValue
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
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
	@JoinTable(
			name = "roles_privileges", 
//			joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId"), 
//			inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "privilegeId")
			joinColumns = @JoinColumn(name = "role_id"), 
			inverseJoinColumns = @JoinColumn(name = "privilege_id")
	)
	public Set<PrivilegeEntity> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<PrivilegeEntity> privileges) {
		this.privileges = privileges;
	}

}
