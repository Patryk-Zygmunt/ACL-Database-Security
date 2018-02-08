package com.designPatterns.ACLDatabaseSecurity.service;

import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import com.designPatterns.ACLDatabaseSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Linus on 21.10.2017.
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
