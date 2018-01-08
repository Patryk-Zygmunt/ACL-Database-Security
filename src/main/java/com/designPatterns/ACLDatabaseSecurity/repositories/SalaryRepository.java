package com.designPatterns.ACLDatabaseSecurity.repositories;
import com.designPatterns.ACLDatabaseSecurity.model.entity.SalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<SalaryEntity, Long> {
    @Query("SELECT s FROM SalaryEntity AS s WHERE s.id < '7'")
    @Override
    List<SalaryEntity> findAll();
}
