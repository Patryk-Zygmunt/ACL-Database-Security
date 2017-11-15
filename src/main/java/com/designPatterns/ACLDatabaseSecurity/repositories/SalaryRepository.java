package com.designPatterns.ACLDatabaseSecurity.repositories;

import com.designPatterns.ACLDatabaseSecurity.aspect.MyAspect;
import com.designPatterns.ACLDatabaseSecurity.model.entity.SalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends JpaRepository<SalaryEntity, Long> {
    @Override
    @MyAspect
    SalaryEntity findOne(Long aLong);
}
