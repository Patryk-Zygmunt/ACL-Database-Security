package com.designPatterns.ACLDatabaseSecurity.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.designPatterns.ACLDatabaseSecurity.model.entity.Salary;
import com.designPatterns.ACLDatabaseSecurity.repositories.SalaryRepository;

@Component
public class Init {
	private static final List<Double> SALARY_VALUES = Arrays.asList(1400.00, 14000.50, 1900.99, 5600.00, 2400.24);

	@Autowired
	SalaryRepository salaryRepository;

	@PostConstruct
	public void init() {
		System.out.println("INIT");

		for (double salaryValue : SALARY_VALUES) {
			Salary s = new Salary();
			s.setSalaryValue(salaryValue);
			salaryRepository.save(s);
		}
//		salaryRepository.findAll().forEach(s ->System.out.println(s.getSalaryId()+" | "+s.getSalaryValue()));

	}
}
