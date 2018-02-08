package com.designPatterns.ACLDatabaseSecurity.plugin.structures.injection;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlEntityData;

import javax.persistence.criteria.CriteriaBuilder;

public class SqlInjectionData extends InjectionData {
    public SqlEntityData sqlEntityData;
    public String uniqueAlias;

    public SqlInjectionData(CriteriaBuilder criteriaBuilder, ProtectedEntityData entityData, Object principal, SqlEntityData sqlEntityData, String uniqueAlias) {
        super(criteriaBuilder, entityData, principal);
        this.sqlEntityData = sqlEntityData;
        this.uniqueAlias = uniqueAlias;
    }
}
