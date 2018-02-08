package com.designPatterns.ACLDatabaseSecurity.plugin.config;

import com.designPatterns.ACLDatabaseSecurity.plugin.SecuredEntities;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjectionAspect;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjectionException;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjections;
import com.designPatterns.ACLDatabaseSecurity.plugin.parser.jsql.JSqlSqlParserCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.persistence.EntityManager;

public abstract class ACLConfiguration {
    protected final
    EntityManager entityManager;

    @Autowired
    public ACLConfiguration(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Bean
    @Scope("singleton")
    public abstract SecurityInjections getInjections() throws SecurityInjectionException;


    @Bean
    @Scope("singleton")
    public abstract SecuredEntities getSecuredEntities();

    @Bean
    public SecurityInjectionAspect getAspect() {
        return new SecurityInjectionAspect(new JSqlSqlParserCreator());
    }
}
