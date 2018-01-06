package com.designPatterns.ACLDatabaseSecurity.plugin.builders;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.annotations.ProtectedEntity;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecuredEntities;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SecuredEntitiesBuilder {


    private Set<ProtectedEntityData> annotatedEntities = new HashSet<>();
    private Set<String> paths = new HashSet<>();

    public SecuredEntitiesBuilder addSearchPath(String path) {
        paths.add(path);
        return this;
    }

    public SecuredEntitiesBuilder findAnnotatedEntities() {
        Reflections reflections = new Reflections(paths);
        annotatedEntities.addAll(
                reflections.getTypesAnnotatedWith(ProtectedEntity.class)
                        .stream()
                        .map(cl -> {
                            ProtectedEntity ann = cl.getAnnotation(ProtectedEntity.class);
                            return new ProtectedEntityData(cl, ann.set(), ann.setId(), ann.classId());
                        })
                        .collect(Collectors.toSet())
        );
        return this;
    }

    public SecuredEntities getSecuredEntities() {
        return new SecuredEntities(annotatedEntities);
    }

}
