package com.designPatterns.ACLDatabaseSecurity;

import com.designPatterns.ACLDatabaseSecurity.model.entity.PrivilegeEntity;
import com.designPatterns.ACLDatabaseSecurity.plugin.*;
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
import java.util.StringJoiner;
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
    @SuppressWarnings("unchecked")
    public SecurityInjections getInjections() {
        return new SecurityInjectionsBuilder(entityManager).
                setDefaultQueryInjection(data -> {
                    Subquery sq = data.queryData.getQuery().subquery(data.entityData.getSetClass());
                    Root from = sq.from(data.entityData.getSetClass());
                    sq.select(from.get(data.entityData.getSetId()));
                    sq.where(from.get(data.entityData.getPrivilegeId()).in(getPrivileges(data.principal)));

                    return data.queryData.getRoot().get(data.entityData.getClassId()).in(sq);
                })
                .setDefaultSqlInjection(data -> {

                    StringJoiner joiner = new StringJoiner(", ");
                    getPrivileges(data.principal)
                            .forEach(x -> joiner.add(x.toString()));

                    return data.sqlEntityData.alias + "." + data.entityData.getClassId() + " IN (SELECT "
                            + data.uniqueAlias + "." + data.entityData.getSetId() + " FROM "
                            + data.entityData.getSetClass().getSimpleName() + " " + data.uniqueAlias + " WHERE "
                            + data.uniqueAlias + "." +data.entityData.getPrivilegeId()
                            + " IN (" + joiner.toString() + "))";
                })
                .getQueryInjections();
    }

    private Set<Long> getPrivileges(Object principal) {
        return (((UserDetails) principal).getUser())
                .getRoles()
                .stream()
                .flatMap(roleEntity -> roleEntity.getPrivileges().stream())
                .map(PrivilegeEntity::getPrivilegeId)
                .collect(Collectors.toSet());
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
