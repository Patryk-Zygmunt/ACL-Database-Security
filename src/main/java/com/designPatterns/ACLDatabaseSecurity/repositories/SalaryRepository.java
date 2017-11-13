package com.designPatterns.ACLDatabaseSecurity.repositories;

import com.designPatterns.ACLDatabaseSecurity.model.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    List<Salary> findByValueBefore(double value);

    @Modifying
    @Transactional
    List<Salary> deleteByValueBefore(double value);

    @Modifying
    @Transactional
    Long removeByValueBefore(double value);

    List<Salary> findMefedronToSzmataSukiNieChceZnacJebacByValueIn(List<Double> values);

    @Query("select s from Salary s where s.value = ?1")
    List<Salary> findByValue(double value);

    List<Salary> findUsingNamedQueryByValueBefore(double value);

    @Query(value = "SELECT * FROM SALARY WHERE VALUE = ?1", nativeQuery = true)
    List<Salary> findWywaloneJajacaByValue(double value);

}
