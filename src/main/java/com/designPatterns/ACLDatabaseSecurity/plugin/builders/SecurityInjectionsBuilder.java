package com.designPatterns.ACLDatabaseSecurity.plugin.builders;

import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjections;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.InjectionData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.QueryData;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class SecurityInjectionsBuilder {

    private CriteriaBuilder cb;

    public SecurityInjectionsBuilder(EntityManager em) {
        this.cb = em.getCriteriaBuilder();
    }

    private Map<Class<?>, BiConsumer<QueryData, ProtectedEntityData>> injections = new HashMap<>();
    private BiConsumer<QueryData, ProtectedEntityData> defaultInjection = (query, root) -> {
    };


    public SecurityInjectionsBuilder setDefaultInjection(BiConsumer<QueryData, ProtectedEntityData> consumer) {
        defaultInjection = consumer;
        return this;
    }

    public SecurityInjectionsBuilder setDefaultInjection(Function<InjectionData, Predicate> function) {

        setDefaultInjection(makeConsumer(function));
        return this;
    }

    public SecurityInjectionsBuilder addInjection(Class<?> entity, BiConsumer<QueryData, ProtectedEntityData> injection) {
        injections.put(entity, injection);
        return this;
    }

    public SecurityInjectionsBuilder addInjection(Class<?> entity, Function<InjectionData, Predicate> function) {

        addInjection(entity, makeConsumer(function));
        return this;
    }

    private BiConsumer<QueryData, ProtectedEntityData> makeConsumer(Function<InjectionData, Predicate> function) {

        return (queryD, entity) -> Optional.ofNullable(SecurityContextHolder.getContext().
                getAuthentication()).
                ifPresent(auth -> {
                    Predicate p = function.apply(new InjectionData(queryD, cb, entity, auth.getPrincipal()));
                    if (queryD.getQuery().getRestriction() != null)
                        queryD.getQuery().where(p, queryD.getQuery().getRestriction());
                    else
                        queryD.getQuery().where(p);
                });
    }

    public SecurityInjections getInjections() {
        return new SecurityInjections(injections, defaultInjection);
    }

}
