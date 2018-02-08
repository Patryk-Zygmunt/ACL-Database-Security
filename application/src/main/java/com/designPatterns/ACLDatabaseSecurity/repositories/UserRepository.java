package com.designPatterns.ACLDatabaseSecurity.repositories;

import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Linus on 21.10.2017.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

}