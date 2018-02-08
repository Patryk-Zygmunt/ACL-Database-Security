package com.designPatterns.ACLDatabaseSecurity.plugin.structures.injection;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.QueryData;

import javax.persistence.criteria.CriteriaBuilder;

public class InjectionData {
    public CriteriaBuilder criteriaBuilder;
    public ProtectedEntityData entityData;
    public Object principal;

    public InjectionData(CriteriaBuilder criteriaBuilder, ProtectedEntityData entityData, Object principal) {
        this.criteriaBuilder = criteriaBuilder;
        this.entityData = entityData;
        this.principal = principal;
    }

}
