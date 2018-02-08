package com.designPatterns.ACLDatabaseSecurity.plugin.config;

import com.designPatterns.ACLDatabaseSecurity.plugin.SecuredEntities;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjectionException;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjections;
import com.designPatterns.ACLDatabaseSecurity.plugin.builders.SecurityInjectionsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Set;
import java.util.StringJoiner;

public abstract class DefaultACLConfiguration extends ACLConfiguration {
    public DefaultACLConfiguration(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public abstract SecuredEntities getSecuredEntities();

    protected abstract Set<Long> getPrivileges(Object principal);

    @Override
    public SecurityInjections getInjections() throws SecurityInjectionException {
        return getInjectionsBuilder().getQueryInjections();

    }

    @SuppressWarnings("unchecked")
    protected SecurityInjectionsBuilder getInjectionsBuilder() {
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
                            + data.uniqueAlias + "." + data.entityData.getPrivilegeId()
                            + " IN (" + joiner.toString() + "))";
                });
    }
}
