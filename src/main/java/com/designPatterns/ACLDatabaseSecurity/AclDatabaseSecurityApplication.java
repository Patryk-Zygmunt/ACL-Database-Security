package com.designPatterns.ACLDatabaseSecurity;

import com.app.COD;
import com.app.CODFactory;
import com.designPatterns.ACLDatabaseSecurity.model.entity.SalaryEntity;
import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import com.designPatterns.ACLDatabaseSecurity.repositories.SalaryRepository;
import com.designPatterns.ACLDatabaseSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class AclDatabaseSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(AclDatabaseSecurityApplication.class, args);
	}

}
