package com.designPatterns.ACLDatabaseSecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.designPatterns.ACLDatabaseSecurity.model.entity.RoleEntity;
import com.designPatterns.ACLDatabaseSecurity.model.entity.SalaryEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

	RoleEntity findByName(String roleName);

}
