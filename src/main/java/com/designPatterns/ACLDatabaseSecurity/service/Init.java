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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Init {
	private static final List<Double> SALARY_VALUES = Arrays.asList(1400.00, 14000.50, 1900.99, 5600.00, 2400.24);
	// nie zmieniajcie głębokości na większą niż 2, bo hibernate się to nie podoba
	private static final COD cod = CODFactory.setLevelOfDepression(4);

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

		// PrivilegeEntity privilege = createReadPrivilege();
		// RoleEntity role = createAdminRole(privilege);
		// UserEntity admin = createAdmin(role);
		// createAdmin2(role);
		// createFewSalaries();
		// createSalaryWithUser(admin);

		cod.i("USERS: ", userRepository.findAll());
		cod.i("SALARIES: ", salaryRepository.findAll());
	}

	@Transactional
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

	@Transactional
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

	@Transactional
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

	@Transactional
	private SalaryEntity createSalary(double value, UserEntity user) {

		SalaryEntity s = new SalaryEntity();
		s.setValue(value);
		s.setUser(user);
		salaryRepository.flush();
		return salaryRepository.save(s);

	}

	@Transactional
	private UserEntity setUserRole(UserEntity u, Set<RoleEntity> roles) {
		UserEntity user = userRepository.findOne(u.getUserId());
		if (user != null) {
			user.setRoles(roles);
			user = userRepository.save(user);
			userRepository.flush();
		}
		return user;
	}

	@Transactional
	private PrivilegeEntity createPrivilege(String privilegeName) {
		// PrivilegeEntity privilege = privilegeRepository.findByName(privilegeName);
		// if (privilege == null) {
		PrivilegeEntity privilege = new PrivilegeEntity();
		privilege.setName(privilegeName);
		// privilege.setSalaries(authority);
		privilege = privilegeRepository.save(privilege);
		privilegeRepository.flush();
		// }

		return privilege;
	}

	@Transactional
	private RoleEntity createRole(Set<PrivilegeEntity> privileges, String roleName) {
		// RoleEntity role = roleRepository.findByName(roleName);
		// if (role == null) {
		RoleEntity role = new RoleEntity();
		role.setName(roleName);
		role.setPrivileges(privileges);
		role = roleRepository.save(role);
		roleRepository.flush();
		// }
		return role;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// @Transactional
	// private RoleEntity createAdminRole(PrivilegeEntity privilege) {
	// String roleName = "ADMIN";
	// RoleEntity role = roleRepository.findByName(roleName);
	// if (role == null) {
	// role = new RoleEntity();
	// role.setName(roleName);
	// role.setPrivileges(new HashSet<PrivilegeEntity>(Arrays.asList(privilege)));
	// roleRepository.save(role);
	// }
	// return role;
	// }
	//
	// @Transactional
	// private PrivilegeEntity createReadPrivilege() {
	// String privilegeName = "READ_PRIVILEGE";
	// PrivilegeEntity privilege = privilegeRepository.findByName(privilegeName);
	// if (privilege == null) {
	// privilege = new PrivilegeEntity();
	// privilege.setName(privilegeName);
	// privilegeRepository.save(privilege);
	// }
	// return privilege;
	// }
	//
	// @Transactional
	// private void createSalaryWithUser(UserEntity u) {
	// SalaryEntity salary = new SalaryEntity();
	// salary.setValue(66666.66);
	// // salary.setUser(u);
	// salaryRepository.save(salary);
	// }
	//
	// @Transactional
	// private void createFewSalaries() {
	// for (double salaryValue : SALARY_VALUES) {
	// SalaryEntity s = new SalaryEntity();
	// s.setValue(salaryValue);
	// salaryRepository.save(s);
	// }
	// // return salaryRepository.findAll();
	// }
	//
	// @Transactional
	// private UserEntity createAdmin(RoleEntity role) {
	// String username = "admin";
	// UserEntity user = userRepository.findByUsername(username);
	// if (user == null) {
	// user = new UserEntity();
	// user.setPassword("admin");
	// user.setUsername(username);
	// user.setRoles(new HashSet<RoleEntity>(Arrays.asList(role)));
	// userRepository.save(user);
	// }
	// return user;
	// }
	//
	// @Transactional
	// private UserEntity createAdmin2(RoleEntity role) {
	// String username = "admin2";
	// UserEntity user = userRepository.findByUsername(username);
	// if (user == null) {
	// user = new UserEntity();
	// user.setPassword("admin2");
	// user.setUsername(username);
	// user.setRoles(new HashSet<RoleEntity>(Arrays.asList(role)));
	// userRepository.save(user);
	// }
	// return user;
	// }
}
