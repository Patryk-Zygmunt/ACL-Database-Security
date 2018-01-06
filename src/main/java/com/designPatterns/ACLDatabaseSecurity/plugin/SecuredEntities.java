package com.designPatterns.ACLDatabaseSecurity.plugin;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;

import javax.persistence.criteria.Root;
import java.util.Set;

public class SecuredEntities {
    private Set<ProtectedEntityData> selectProtectedEntities;

    public SecuredEntities(Set<ProtectedEntityData> selectProtectedEntities) {
        this.selectProtectedEntities = selectProtectedEntities;
    }

    boolean isRootProtected(Root root) {
        return selectProtectedEntities
                .stream()
                .anyMatch(x -> x.getEntityClass().getSimpleName().equals(root.getModel().getName()));
    }

    ProtectedEntityData getEntityData(Root root) {
        return getEntityData(root.getJavaType());
    }

    ProtectedEntityData getEntityData(Class<?> entity) {
        return selectProtectedEntities
                .stream()
                .filter(data -> data.getEntityClass().equals(entity))
                .findFirst()
                .get();
    }


}
