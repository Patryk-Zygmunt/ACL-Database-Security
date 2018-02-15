package com.designPatterns.ACLDatabaseSecurity.service;

import com.app.COD;
import com.app.CODFactory;
import com.designPatterns.ACLDatabaseSecurity.model.entity.PrivilegeEntity;
import com.designPatterns.ACLDatabaseSecurity.model.entity.RoleEntity;
import com.designPatterns.ACLDatabaseSecurity.model.entity.SalariesSet;
import com.designPatterns.ACLDatabaseSecurity.model.entity.SalaryEntity;
import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import com.designPatterns.ACLDatabaseSecurity.repositories.PrivilegeRepository;
import com.designPatterns.ACLDatabaseSecurity.repositories.RoleRepository;
import com.designPatterns.ACLDatabaseSecurity.repositories.SalariesSetRepository;
import com.designPatterns.ACLDatabaseSecurity.repositories.SalaryRepository;
import com.designPatterns.ACLDatabaseSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class Init {
	private static final List<Double> SALARY_VALUES = Arrays.asList(1400.00, 14000.50, 1900.99, 5600.00, 2400.24);
	private static final COD cod = CODFactory.setLevelOfDepression(2);

	private final SalaryRepository salaryRepository;
	private final UserRepository userRepository;
	private final PrivilegeRepository privilegeRepository;
	private final RoleRepository roleRepository;
	private final SalariesSetRepository salariesSetRepository;

	@Autowired
	public Init(SalaryRepository salaryRepository, UserRepository userRepository,
			PrivilegeRepository privilegeRepository, RoleRepository roleRepository,
			SalariesSetRepository salariesSetRepository) {
		this.salaryRepository = salaryRepository;
		this.userRepository = userRepository;
		this.privilegeRepository = privilegeRepository;
		this.roleRepository = roleRepository;
		this.salariesSetRepository = salariesSetRepository;
	}

	@Transactional
	@PostConstruct
	public void init() {
		createExampleData();
		cod.i("USERS: ", userRepository.findAll());
		cod.i("SALARIES: ", salaryRepository.findAll());
	}

	private void createExampleData() {
		// USERS AND SALARIES
		UserEntity emp = createUser("emp", "emp");
		SalaryEntity empSalary = createSalary(5000.00, emp);
		UserEntity emp2 = createUser("emp2", "emp2");
		SalaryEntity emp2Salary = createSalary(5200.00, emp2);
		UserEntity emp3 = createUser("emp3", "emp3");
		SalaryEntity emp3Salary = createSalary(5400.00, emp3);

		UserEntity groupSupervisor = createUser("suv", "suv");
		SalaryEntity suvSalary = createSalary(7100.00, groupSupervisor);
		UserEntity admin = createUser("admin", "admin");
		SalaryEntity admSalary = createSalary(18000.00, admin);
		cod.i("INIT SALARIES: ", Arrays.asList(empSalary, emp2Salary, emp3Salary, suvSalary, admSalary));
		cod.i("INIT USERS: ", Arrays.asList(emp, emp2, emp3, groupSupervisor, admin));

		// EMPLOYEE
		PrivilegeEntity empPrivilege = createPrivilege("EMPLOYEE_PRIVILEGE");
		Set<SalariesSet> empSSs = createSalariesSets(empPrivilege, new HashSet<>(Arrays.asList(empSalary)));
		RoleEntity empRole = createRole(new HashSet<>(Arrays.asList(empPrivilege)), "EMPLOYEE");
		emp = setUserRole(emp, new HashSet<>(Arrays.asList(empRole)));

		// GROUP SUPERVISOR
		PrivilegeEntity suvPrivilegeEmp = createPrivilege("EMPLOYEE_PRIVILEGE");
		cod.i("SUV", suvPrivilegeEmp);
		Set<SalariesSet> suvSSs1 = createSalariesSets(suvPrivilegeEmp, new HashSet<>(Arrays.asList(suvSalary)));
		RoleEntity suvRole1 = createRole(new HashSet<>(Arrays.asList(suvPrivilegeEmp)), "EMPLOYEE");

		PrivilegeEntity suvPrivilegeSuv = createPrivilege("GROUP_SUPERVISOR_PRIVILEGE");
		Set<SalariesSet> suvSSs2 = createSalariesSets(suvPrivilegeSuv,
				new HashSet<>(Arrays.asList(empSalary, emp3Salary)));
		RoleEntity suvRole2 = createRole(new HashSet<>(Arrays.asList(suvPrivilegeSuv)), "GROUP_SUPERVISOR");

		groupSupervisor = setUserRole(groupSupervisor, new HashSet<>(Arrays.asList(suvRole1, suvRole2)));

		// ADMIN
		PrivilegeEntity admPrivilegeEmp = createPrivilege("EMPLOYEE_PRIVILEGE");
		Set<SalariesSet> admSSs1 = createSalariesSets(admPrivilegeEmp, new HashSet<>(Arrays.asList(admSalary)));
		RoleEntity admRole1 = createRole(new HashSet<>(Arrays.asList(admPrivilegeEmp)), "EMPLOYEE");

		PrivilegeEntity admPrivilegeAdm = createPrivilege("GROUP_SUPERVISOR_PRIVILEGE");
		Set<SalariesSet> admSSs2 = createSalariesSets(admPrivilegeAdm,
				new HashSet<>(Arrays.asList(empSalary, emp2Salary, emp3Salary, suvSalary, admSalary)));
		RoleEntity admRole2 = createRole(new HashSet<>(Arrays.asList(admPrivilegeAdm)), "ADMIN");
		cod.i("INIT PRIVILEGES: ",
				Arrays.asList(empPrivilege, suvPrivilegeEmp, suvPrivilegeSuv, admPrivilegeEmp, admPrivilegeAdm));

		admin = setUserRole(admin, new HashSet<>(Arrays.asList(admRole1, admRole2)));

	}

	private Set<SalariesSet> createSalariesSets(PrivilegeEntity privilege, Set<SalaryEntity> salaries) {
		Set<SalariesSet> sss = new HashSet<>();
		for (SalaryEntity s : salaries) {
			SalariesSet ss = new SalariesSet();
			ss.setPrivilegeId(privilege.getPrivilegeId());
			ss.setSalaryId(s.getId());
			ss = salariesSetRepository.save(ss);
			salariesSetRepository.flush();
			sss.add(ss);
		}
		return sss;
	}

	private UserEntity createUser(String username, String password) {
		UserEntity user = userRepository.findByUsername(username);
		if (user == null) {
			user = new UserEntity();
			user.setPassword(password);
			user.setUsername(username);
			user = userRepository.save(user);
			userRepository.flush();
		}
		return user;
	}

	private SalaryEntity createSalary(double value, UserEntity user) {
		SalaryEntity s = new SalaryEntity();
		s.setValue(value);
		s.setUser(user);
		salaryRepository.flush();
		return salaryRepository.save(s);
	}

	private UserEntity setUserRole(UserEntity u, Set<RoleEntity> roles) {
		UserEntity user = userRepository.findOne(u.getUserId());
		if (user != null) {
			user.setRoles(roles);
			user = userRepository.save(user);
			userRepository.flush();
		}
		return user;
	}

	private PrivilegeEntity createPrivilege(String privilegeName) {
		PrivilegeEntity privilege = new PrivilegeEntity();
		privilege.setName(privilegeName);
		privilege = privilegeRepository.save(privilege);
		privilegeRepository.flush();
		return privilege;
	}

	private RoleEntity createRole(Set<PrivilegeEntity> privileges, String roleName) {
		RoleEntity role = new RoleEntity();
		role.setName(roleName);
		role.setPrivileges(privileges);
		role = roleRepository.save(role);
		roleRepository.flush();
		return role;
	}
}
