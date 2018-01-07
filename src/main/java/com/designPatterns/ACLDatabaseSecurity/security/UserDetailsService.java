package com.designPatterns.ACLDatabaseSecurity.security;

import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import com.designPatterns.ACLDatabaseSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Linus on 21.10.2017.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {


    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.getUserByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username + " not found");
        return new UserDetails(user);
    }
}
