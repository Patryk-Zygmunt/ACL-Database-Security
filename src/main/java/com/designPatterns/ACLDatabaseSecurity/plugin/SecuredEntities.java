package com.designPatterns.ACLDatabaseSecurity.plugin;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.criteria.Root;
import java.util.Set;


public class SecuredEntities {
    private Set<Class<?>> selectProtectedEntities = null;

    public SecuredEntities(Set<Class<?>> selectProtectedEntities) {
        this.selectProtectedEntities = selectProtectedEntities;
    }

    boolean isRootProtected(Root root){
        return selectProtectedEntities.stream().anyMatch(x -> x.getSimpleName().equals(root.getModel().getName()));
    }



}
