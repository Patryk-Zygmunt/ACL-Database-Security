package com.designPatterns.ACLDatabaseSecurity;

import com.designPatterns.ACLDatabaseSecurity.model.entity.PrivilegeEntity;
import com.designPatterns.ACLDatabaseSecurity.model.entity.SalariesSet;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecuredEntities;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjectionAspect;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjections;
import com.designPatterns.ACLDatabaseSecurity.plugin.builders.SecuredEntitiesBuilder;
import com.designPatterns.ACLDatabaseSecurity.plugin.builders.SecurityInjectionsBuilder;
import com.designPatterns.ACLDatabaseSecurity.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Set;
import java.util.stream.Collectors;


@Configuration
public class PluginConfiguration {

    private final
    EntityManager entityManager;

    @Autowired
    public PluginConfiguration(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Bean
    @Scope("singleton")
    public SecurityInjections getInjections() {
        return new SecurityInjectionsBuilder(entityManager).
                setDefaultInjection(data -> {

                    Set<Long> ids = (((UserDetails) data.getPrincipal()).getUser())
                            .getRoles()
                            .stream()
                            .flatMap(roleEntity -> roleEntity.getPrivileges().stream())
                            .map(PrivilegeEntity::getPrivilegeId)
                            .collect(Collectors.toSet());

                    Subquery sq = data.getQueryData().getQuery().subquery(data.getEntityData().getSetClass());
                    Root from = sq.from(data.getEntityData().getSetClass());
                    sq.select(from.get(data.getEntityData().getSetId()));
                    sq.where(from.get("privilageId").in(ids));

                    return data.getQueryData().getRoot().get(data.getEntityData().getClassId()).in(sq);
                }).
                getInjections();
    }

    @Bean
    @Scope("singleton")
    public SecuredEntities getSecuredEntities() {
        return new SecuredEntitiesBuilder().
                addSearchPath("com.designPatterns.ACLDatabaseSecurity.model.entity").
                findAnnotatedEntities().
                getSecuredEntities();
    }

    @Bean
    public SecurityInjectionAspect getAspect() {
        return new SecurityInjectionAspect();
    }
}
