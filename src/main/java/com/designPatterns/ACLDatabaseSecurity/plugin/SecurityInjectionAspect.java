package com.designPatterns.ACLDatabaseSecurity.plugin;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.criteria.*;
import java.util.Objects;

@Aspect
public class SecurityInjectionAspect {


    @Autowired
    SecuredEntities entities;

    @Autowired
    SecurityInjections securityInjections;

    @Pointcut("execution(* javax.persistence.EntityManager.createQuery(..))")
    public void queryCreationJoin(){
    }


    /**
     * Used when repository method is findby, removeby or deleteby
     * @param criteriaQuery
     */
    @Before("queryCreationJoin() && args(criteriaQuery)")
    @SuppressWarnings("unchecked")
    public void selectSecurityInjection(CriteriaQueryImpl criteriaQuery){
        if(Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) return;
        long startTime = System.currentTimeMillis();
        criteriaQuery.getRoots().
                stream().
                filter(root -> entities.isRootProtected((Root) root)).
                forEach(root -> securityInjections.injectToQuery(criteriaQuery, (Root) root));

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
    }

}
