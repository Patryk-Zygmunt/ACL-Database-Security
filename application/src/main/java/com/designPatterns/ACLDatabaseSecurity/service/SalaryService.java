package com.designPatterns.ACLDatabaseSecurity.service;

import com.designPatterns.ACLDatabaseSecurity.model.entity.SalaryEntity;
import com.designPatterns.ACLDatabaseSecurity.repositories.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Linus on 22.10.2017.
 */
@Service
public class SalaryService {

    @Autowired
    SalaryRepository salaryRepository;


    public List<SalaryEntity> getSalaries() {
        return salaryRepository.findAll();
    }


}
