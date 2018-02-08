package com.designPatterns.ACLDatabaseSecurity.plugin.structures.injection;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.QueryData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.injection.InjectionData;

import javax.persistence.criteria.CriteriaBuilder;

public class QueryInjectionData extends InjectionData {
    public QueryData queryData;

    public QueryInjectionData(CriteriaBuilder criteriaBuilder, ProtectedEntityData entityData, Object principal, QueryData queryData) {
        super(criteriaBuilder, entityData, principal);
        this.queryData = queryData;
    }
}
