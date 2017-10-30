package com.designPatterns.ACLDatabaseSecurity.service;

import com.designPatterns.ACLDatabaseSecurity.model.entity.Salary;
import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import com.designPatterns.ACLDatabaseSecurity.repositories.SalaryRepository;
import com.designPatterns.ACLDatabaseSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

public class Init {
	private static final List<Double> SALARY_VALUES = Arrays.asList(1400.00, 14000.50, 1900.99, 5600.00, 2400.24);

    final
    SalaryRepository salaryRepository;
    private final UserRepository userRepository;

    @Autowired
    public Init(SalaryRepository salaryRepository, UserRepository userRepository) {
        this.salaryRepository = salaryRepository;
        this.userRepository = userRepository;
    }


    @PostConstruct
    public void init() {
		System.out.println("INIT");
        UserEntity user = new UserEntity();


		for (double salaryValue : SALARY_VALUES) {
			Salary s = new Salary();
            s.setValue(salaryValue);
            salaryRepository.save(s);
		}

	}
}
