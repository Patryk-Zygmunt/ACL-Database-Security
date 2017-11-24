package com.designPatterns.ACLDatabaseSecurity;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecuredEntities;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjectionAspect;
import com.designPatterns.ACLDatabaseSecurity.plugin.SecurityInjections;
import com.designPatterns.ACLDatabaseSecurity.plugin.builders.SecuredEntitiesBuilder;
import com.designPatterns.ACLDatabaseSecurity.plugin.builders.SecurityInjectionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import javax.persistence.EntityManager;

@Configuration
public class PluginConfiguration {

    @Autowired
    EntityManager entityManager;

    @Bean
    @Scope("singleton")
    public SecurityInjections getInjections(){
        return new SecurityInjectionsBuilder(entityManager).
                setDefaultInjection((cb, user) -> (query, root) -> cb.equal(root.get("user"), user)).
                getInjections();
    }

    @Bean
    @Scope("singleton")
    public SecuredEntities getSecuredEntities(){
        return new SecuredEntitiesBuilder().
                addSearchPath("com.designPatterns.ACLDatabaseSecurity.model.entity").
                findAnnotatedEntities().
                getSecuredEntities();
    }

    @Bean
    public SecurityInjectionAspect getAspect(){
        return new SecurityInjectionAspect();
    }
}
