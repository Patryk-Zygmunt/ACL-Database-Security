package com.designPatterns.ACLDatabaseSecurity.plugin;

import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import javax.persistence.criteria.Root;
import java.util.Map;
import java.util.function.BiConsumer;

public class SecurityInjections {


    private Map<Class<?>,BiConsumer<CriteriaQueryImpl, Root>> injections;
    private BiConsumer<CriteriaQueryImpl, Root> defaultInjection;

    public SecurityInjections(Map<Class<?>, BiConsumer<CriteriaQueryImpl, Root>> injections, BiConsumer<CriteriaQueryImpl, Root> defaultInjection) {
        this.injections = injections;
        this.defaultInjection = defaultInjection;
    }

    public void injectToQuery(CriteriaQueryImpl query, Root root) {
        injections.getOrDefault(root.getJavaType(), defaultInjection).accept(query, root);
    }

}
