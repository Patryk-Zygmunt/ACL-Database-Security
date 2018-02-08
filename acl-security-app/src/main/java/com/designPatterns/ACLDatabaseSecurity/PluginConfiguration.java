package com.designPatterns.ACLDatabaseSecurity;

import com.designPatterns.ACLDatabaseSecurity.model.entity.PrivilegeEntity;
import com.designPatterns.ACLDatabaseSecurity.plugin.*;
import com.designPatterns.ACLDatabaseSecurity.plugin.builders.SecuredEntitiesBuilder;
import com.designPatterns.ACLDatabaseSecurity.plugin.config.DefaultACLConfiguration;
import com.designPatterns.ACLDatabaseSecurity.security.UserDetails;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.stream.Collectors;


@Configuration
public class PluginConfiguration extends DefaultACLConfiguration {


    public PluginConfiguration(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Set<Long> getPrivileges(Object principal) {
        return (((UserDetails) principal).getUser())
                .getRoles()
                .stream()
                .flatMap(roleEntity -> roleEntity.getPrivileges().stream())
                .map(PrivilegeEntity::getPrivilegeId)
                .collect(Collectors.toSet());
    }

    @Override
    public SecuredEntities getSecuredEntities() {
        return new SecuredEntitiesBuilder().
                addSearchPath("com.designPatterns.ACLDatabaseSecurity.model.entity").
                findAnnotatedEntities().
                getSecuredEntities();
    }

}
