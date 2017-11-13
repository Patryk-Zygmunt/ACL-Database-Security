package com.designPatterns.ACLDatabaseSecurity;

import com.designPatterns.ACLDatabaseSecurity.model.entity.Salary;
import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import com.designPatterns.ACLDatabaseSecurity.repositories.SalaryRepository;
import com.designPatterns.ACLDatabaseSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AclDatabaseSecurityApplication {
    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    UserRepository userRepository;


    public static void main(String[] args) {
        SpringApplication.run(AclDatabaseSecurityApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            userRepository.deleteAll();
            UserEntity user = new UserEntity();
            user.setPassword("admin");
            user.setUsername("admin");
            userRepository.save(user);
        };
    }
}




