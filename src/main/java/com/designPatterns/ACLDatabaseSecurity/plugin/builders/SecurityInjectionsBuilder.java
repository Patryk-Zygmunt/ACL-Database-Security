package com.designPatterns.ACLDatabaseSecurity.plugin.builders;

import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjections;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlQueryData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.injection.InjectionData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.QueryData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.injection.QueryInjectionData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.injection.SqlInjectionData;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


public class SecurityInjectionsBuilder {

    private CriteriaBuilder cb;

    public SecurityInjectionsBuilder(EntityManager em) {
        this.cb = em.getCriteriaBuilder();
    }

    private Map<Class<?>, Function<SqlQueryData, String>> sqlInjections = new HashMap<>();
    private Function <SqlQueryData, String> defaultSqlInjection = data -> "";

    private Map<Class<?>, BiConsumer<QueryData, ProtectedEntityData>> queryInjections = new HashMap<>();
    private BiConsumer<QueryData, ProtectedEntityData> defaultQueryInjection = (query, root) -> {
    };


    public SecurityInjectionsBuilder setDefaultQueryInjection(BiConsumer<QueryData, ProtectedEntityData> consumer) {
        defaultQueryInjection = consumer;
        return this;
    }

    public SecurityInjectionsBuilder setDefaultQueryInjection(Function<QueryInjectionData, Predicate> function) {
        setDefaultQueryInjection(makeConsumer(function));
        return this;
    }

    public SecurityInjectionsBuilder setDefaultSqlInjection(Function<SqlInjectionData, String> function){
        defaultSqlInjection = makeSqlFunction(function);
        return this;
    }


    public SecurityInjectionsBuilder addInjection(Class<?> entity, BiConsumer<QueryData, ProtectedEntityData> injection) {
        queryInjections.put(entity, injection);
        return this;
    }


//    public SecurityInjectionsBuilder addInjection(Class<?> entity, Function<InjectionData, Predicate> function) {
//        addInjection(entity, makeConsumer(function));
//        return this;
//    }

    private Function<SqlQueryData, String> makeSqlFunction(Function<SqlInjectionData,String> function){
        return data ->
                function.apply(
                        new SqlInjectionData(cb, data.entityData,
                                SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                                data.sqlData, "aop"+data.number)
                );
    }

    private BiConsumer<QueryData, ProtectedEntityData> makeConsumer(Function<QueryInjectionData, Predicate> function) {

        return (queryD, entity) -> Optional.ofNullable(SecurityContextHolder.getContext().
                getAuthentication()).
                ifPresent(auth -> {
                    Predicate p = function.apply(new QueryInjectionData(cb, entity, auth.getPrincipal(), queryD));
                    if (queryD.getQuery().getRestriction() != null)
                        queryD.getQuery().where(p, queryD.getQuery().getRestriction());
                    else
                        queryD.getQuery().where(p);
                });
    }

    public SecurityInjections getQueryInjections() {
        return new SecurityInjections(queryInjections, defaultQueryInjection, sqlInjections, defaultSqlInjection);
    }

}
