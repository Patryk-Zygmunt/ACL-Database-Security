package com.designPatterns.ACLDatabaseSecurity.plugin;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.QueryData;

import java.util.Map;
import java.util.function.BiConsumer;

public class SecurityInjections {


    private Map<Class<?>, BiConsumer<QueryData, ProtectedEntityData>> injections;
    private BiConsumer<QueryData, ProtectedEntityData> defaultInjection;

    public SecurityInjections(Map<Class<?>, BiConsumer<QueryData, ProtectedEntityData>> injections,
                              BiConsumer<QueryData, ProtectedEntityData> defaultInjection) {
        this.injections = injections;
        this.defaultInjection = defaultInjection;
    }

    public void injectToQuery(QueryData queryD, ProtectedEntityData entityData) {
        injections.getOrDefault(queryD.getRoot().getJavaType(), defaultInjection).accept(queryD, entityData);
    }

}
