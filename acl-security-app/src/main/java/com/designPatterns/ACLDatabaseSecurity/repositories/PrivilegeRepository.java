package com.designPatterns.ACLDatabaseSecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.designPatterns.ACLDatabaseSecurity.model.entity.PrivilegeEntity;
import com.designPatterns.ACLDatabaseSecurity.model.entity.SalaryEntity;

@Repository
public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long> {

	PrivilegeEntity findByName(String privilegeEntityName);

}
