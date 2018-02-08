package com.designPatterns.ACLDatabaseSecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.designPatterns.ACLDatabaseSecurity.model.entity.SalariesSet;

@Repository
public interface SalariesSetRepository extends JpaRepository<SalariesSet, Long> {


}
