package com.designPatterns.ACLDatabaseSecurity.plugin;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlQueryData;

import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.HashSet;
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

    Set<SqlQueryData> getEntityData(Collection<SqlEntityData> entities) {

        Set<SqlQueryData> result = new HashSet<>();
        int counter = 0;
        for (ProtectedEntityData data : selectProtectedEntities)
            for (SqlEntityData d : entities) {
                if (d.root.equals(data.getEntityClass().getSimpleName())) {
                    result.add(new SqlQueryData(d, counter++, data));
                    break;
                }
            }
        return result;
    }


}
