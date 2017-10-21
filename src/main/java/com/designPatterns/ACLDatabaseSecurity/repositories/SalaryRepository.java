package com.designPatterns.ACLDatabaseSecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.designPatterns.ACLDatabaseSecurity.model.entity.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
	
	
	
	
}
