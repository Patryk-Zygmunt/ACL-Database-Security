package com.designPatterns.ACLDatabaseSecurity.plugin.structures;

import javax.persistence.criteria.CriteriaBuilder;

public class InjectionData {
    QueryData queryData;
    CriteriaBuilder criteriaBuilder;
    ProtectedEntityData entityData;
    Object principal;

    public InjectionData(QueryData queryData, CriteriaBuilder criteriaBuilder, ProtectedEntityData entityData, Object principal) {
        this.queryData = queryData;
        this.criteriaBuilder = criteriaBuilder;
        this.entityData = entityData;
        this.principal = principal;
    }

    public QueryData getQueryData() {
        return queryData;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }

    public ProtectedEntityData getEntityData() {
        return entityData;
    }

    public Object getPrincipal() {
        return principal;
    }
}
