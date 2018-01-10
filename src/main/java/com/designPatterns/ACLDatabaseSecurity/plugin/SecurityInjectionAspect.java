package com.designPatterns.ACLDatabaseSecurity.plugin;

import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParserImpl;
import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParser;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.QueryData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.criteria.*;
import java.util.Objects;
import java.util.StringJoiner;

@Aspect
public class SecurityInjectionAspect {


    @Autowired
    SecuredEntities entities;

    @Autowired
    SecurityInjections securityInjections;

    /**
     * Pointcut used by @Query and repository methods
     */
    @Pointcut("execution(* javax.persistence.EntityManager.createQuery(..))")
    public void queryCreationJoin() {
    }


    /**
     * Used when repository method is findby, removeby or deleteby
     *
     * @param criteriaQuery
     */
    @Before("queryCreationJoin() && args(criteriaQuery)")
    @SuppressWarnings("unchecked")
    public void selectSecurityInjection(CriteriaQueryImpl criteriaQuery) {
        if (ifUserSession()) return;
        long startTime = System.currentTimeMillis();

        criteriaQuery.getRoots().
                stream().
                filter(root -> entities.isRootProtected((Root) root)).
                forEach(root -> securityInjections.injectToQuery(new QueryData(criteriaQuery, (Root) root),
                        entities.getEntityData((Root) root)));

        displayTime(startTime);
    }

    /**
     * Answer used by @Query
     *
     * @param jp
     * @param jpaqlString
     */
    @Around("queryCreationJoin() && args(jpaqlString)")   //TODO exception handling?
    public Object sqlSecurityInjection(ProceedingJoinPoint jp, String jpaqlString) throws Throwable {
        if (ifUserSession()) return jp.proceed(new Object[]{jpaqlString});
        long startTime = System.currentTimeMillis();

        SqlParser parser = new SqlParserImpl(jpaqlString);
        StringJoiner sj = new StringJoiner(" AND ");
        entities.getEntityData(parser.getRootsAndJoins())
                .forEach(data -> sj.add(securityInjections.getSqlInjection(data)));
        String injectedSql = parser.addInjectionToWhereClause(sj.toString());

        displayTime(startTime);
        return jp.proceed(new Object[]{injectedSql});
    }


    private boolean ifUserSession() {
        return Objects.isNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private void displayTime(long startTime) {
        System.out.println("############# INJECTION TIME - " + (System.currentTimeMillis() - startTime));
    }

}
