package com.designPatterns.ACLDatabaseSecurity.plugin.builders;

import com.designPatterns.ACLDatabaseSecurity.model.entity.UserEntity;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjections;
import com.designPatterns.ACLDatabaseSecurity.security.UserDetails;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


public class SecurityInjectionsBuilder {

    private CriteriaBuilder cb;

    public SecurityInjectionsBuilder(EntityManager em) {
        this.cb = em.getCriteriaBuilder();
    }

    private Map<Class<?>,BiConsumer<CriteriaQueryImpl, Root>> injections = new HashMap<>();
    private BiConsumer<CriteriaQueryImpl, Root> defaultInjection = (criteriaQuery, root) -> {};




    public SecurityInjectionsBuilder setDefaultInjection(BiFunction<CriteriaBuilder,UserEntity, BiFunction<CriteriaQueryImpl, Root, Predicate>> function){
        BiConsumer<CriteriaQueryImpl, Root> cons = (query, root) -> {
            Predicate p = function.apply(cb, ((UserDetails) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal()).getUser()).apply(query,root);
            if (query.getRestriction() != null)
                query.where(p, query.getRestriction());
            else
                query.where(p);
        };
        setDefaultInjection(cons);
        return this;
    }

    public SecurityInjectionsBuilder setDefaultInjection(BiConsumer<CriteriaQueryImpl, Root> consumer){
        defaultInjection = consumer;
        return this;
    }

    public SecurityInjectionsBuilder addInjection(Class<?> entity, BiConsumer<CriteriaQueryImpl, Root> injection){
        injections.put(entity,injection);
        return this;
    }

    public BiConsumer<CriteriaQueryImpl, Root> getDefaultInjection() {
        return defaultInjection;
    }

    public SecurityInjections getInjections(){
        return new SecurityInjections(injections,defaultInjection);
    }

}
