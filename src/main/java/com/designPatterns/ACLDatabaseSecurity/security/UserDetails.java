package com.designPatterns.ACLDatabaseSecurity.security;

import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by Linus on 22.10.2017.
 */
@Component
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    UserEntity user;

    public UserDetails() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("USER");

    }

    public UserDetails(UserEntity user) {
        this.user = user;

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
