package com.designPatterns.ACLDatabaseSecurity.plugin;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ProtectedEntityData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.QueryData;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlQueryData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    @Around("queryCreationJoin() && args(jpaqlString)")
    public Object sqlSecurityInjection(ProceedingJoinPoint jp, String jpaqlString) throws Throwable {
        if (ifUserSession()) return jp.proceed(new Object[]{jpaqlString});
        long startTime = System.currentTimeMillis();
        String result = doInjection(jpaqlString);

        displayTime(startTime);
        return jp.proceed(new Object[]{result});
    }

    private String doInjection(String sql) {
        String[] point = SqlParser.getInjectionPoint(sql);
        StringBuilder sb = new StringBuilder(point[0]);
        entities.getEntityData(SqlParser.getAll(sql))
                .forEach(data -> sb.append(" ").append(securityInjections.getSqlInjection(data)));

        if (point.length > 1)
            sb.append(" when").append(point[1]);
        return sb.toString();
    }

    private boolean ifUserSession() {
        return Objects.isNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private void displayTime(long startTime) {
        System.out.println("############# INJECTION TIME - " + (System.currentTimeMillis() - startTime));
    }

}
