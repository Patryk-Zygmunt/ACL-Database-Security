package com.designPatterns.ACLDatabaseSecurity.plugin.builders;

import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjections;
import com.designPatterns.ACLDatabaseSecurity.security.UserDetails;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;


public class SecurityInjectionsBuilder {

    private CriteriaBuilder cb;

    public SecurityInjectionsBuilder(EntityManager em) {
        this.cb = em.getCriteriaBuilder();
    }

    private Map<Class<?>,BiConsumer<CriteriaQueryImpl, Root>> injections = new HashMap<>();
    private BiConsumer<CriteriaQueryImpl, Root> defaultInjection = (criteriaQuery, root) -> {};


    public SecurityInjectionsBuilder setDefaultInjection(BiConsumer<CriteriaQueryImpl, Root> consumer){
        defaultInjection = consumer;
        return this;
    }

    public SecurityInjectionsBuilder setDefaultInjection(BiFunction<CriteriaBuilder, UserEntity,
            BiFunction<CriteriaQueryImpl, Root, Predicate>> function){

        setDefaultInjection(makeConsumer(function));
        return this;
    }

    public SecurityInjectionsBuilder addInjection(Class<?> entity, BiConsumer<CriteriaQueryImpl, Root> injection){
        injections.put(entity,injection);
        return this;
    }

    public SecurityInjectionsBuilder addInjection(Class <?> entity, BiFunction<CriteriaBuilder, UserEntity,
            BiFunction<CriteriaQueryImpl, Root, Predicate>> function){

        addInjection(entity, makeConsumer(function));
        return this;
    }

    private BiConsumer<CriteriaQueryImpl, Root> makeConsumer(BiFunction<CriteriaBuilder,UserEntity,
            BiFunction<CriteriaQueryImpl, Root, Predicate>> function){

        return  (query, root) -> {
            Predicate p = function.apply(cb, ((UserDetails) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal()).getUser()).apply(query,root);
            if (query.getRestriction() != null)
                query.where(p, query.getRestriction());
            else
                query.where(p);
        };
    }

    public SecurityInjections getInjections(){
        return new SecurityInjections(injections,defaultInjection);
    }

}
