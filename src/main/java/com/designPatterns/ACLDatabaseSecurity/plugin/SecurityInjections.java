package com.designPatterns.ACLDatabaseSecurity.plugin;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.QueryData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlQueryData;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SecurityInjections {

    private Map<Class<?>, BiConsumer<QueryData, ProtectedEntityData>> queryInjections;
    private BiConsumer<QueryData, ProtectedEntityData> defaultQueryInjection;

    private Map<Class<?>, Function<SqlQueryData,String>> sqlInjections;
    private Function<SqlQueryData, String> defaultSqlInjection;

    public SecurityInjections(Map<Class<?>, BiConsumer<QueryData, ProtectedEntityData>> injections,
                              BiConsumer<QueryData, ProtectedEntityData> defaultInjection) {
        this.queryInjections = injections;
        this.defaultQueryInjection = defaultInjection;
    }

    public SecurityInjections(Map<Class<?>, BiConsumer<QueryData, ProtectedEntityData>> queryInjections, BiConsumer<QueryData, ProtectedEntityData> defaultQueryInjection, Map<Class<?>, Function<SqlQueryData, String>> sqlInjections, Function<SqlQueryData, String> defaultSqlInjection) {
        this.queryInjections = queryInjections;
        this.defaultQueryInjection = defaultQueryInjection;
        this.sqlInjections = sqlInjections;
        this.defaultSqlInjection = defaultSqlInjection;
    }

    public void injectToQuery(QueryData queryD, ProtectedEntityData entityData) {
        queryInjections.getOrDefault(queryD.getRoot().getJavaType(), defaultQueryInjection).accept(queryD, entityData);
    }

    public String getSqlInjection(SqlQueryData data) {
        return sqlInjections.getOrDefault(data.entityData.getEntityClass(), defaultSqlInjection).apply(data);
    }
}
