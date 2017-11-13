package com.designPatterns.ACLDatabaseSecurity.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Query;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;


@Aspect
@Component
public class SecurityInjectionAspect {


    @Autowired
    SecuredEntities entitiesHolder;

    @Autowired
    SecurityInjections securityInjections;

    @Autowired
    private EntityManager entityManager;

    @Pointcut("execution(* javax.persistence.EntityManager.createQuery(..))")
    public void queryCreationJoin(){
    }

    @Pointcut("execution(* javax.persistence.EntityManager.createNamedQuery(..))")
    public void namedQueryCreationJoin(){
    }

    @Pointcut("execution(* javax.persistence.EntityManager.createNativeQuery(..))")
    public void nativeQueryCreationJoin(){
    }


    /**
     * Used by @Query with nativeQuery=true
     * @param jp
     * @param sqlString
     * @param resultClass
     * @return
     * @throws Throwable
     */
    @Around("nativeQueryCreationJoin() && args(sqlString, resultClass)")
    public Object nativeQueryInjection(ProceedingJoinPoint jp, String sqlString, Class resultClass) throws Throwable {
        String name = resultClass.getSimpleName();
        String action = sqlString.substring(0, sqlString.indexOf(' ')).toLowerCase();
        if (entitiesHolder.isRootProtected(name, action))
            sqlString = securityInjections.injectToQuery(sqlString);
        return jp.proceed(new Object[]{sqlString, resultClass});
    }

    /**
     * Used by @NamedQuery
     * @param jp
     * @param name
     * @return
     * @throws Throwable
     */
    @Around("namedQueryCreationJoin() && args(name)")
    public Object namedQueryInjection(ProceedingJoinPoint jp, String name) throws Throwable {
        TypedQuery<?> query = (TypedQuery) jp.proceed(jp.getArgs());

        String unwrappedQuery = query.unwrap(Query.class).getQueryString();
        String className = name.substring(0,name.indexOf('.'));
        String action = unwrappedQuery.substring(0, unwrappedQuery.indexOf(' '));

        if (entitiesHolder.isRootProtected(className, action)){
            String injectedQuery = securityInjections.injectToQuery(unwrappedQuery);
            return entityManager.createQuery(injectedQuery);
        }
        return query;
    }

    /**
     * Used when repository method is findby, removeby or deleteby
     * @param criteriaQuery
     */
    @Before("queryCreationJoin() && args(criteriaQuery)")
    @SuppressWarnings("unchecked")
    public void selectSecurityInjection(CriteriaQueryImpl criteriaQuery){
        criteriaQuery.getRoots().
                stream().
                filter(root -> entitiesHolder.isRootSelectProtected((Root) root)).
                findFirst().//finds only one TODO it could sucks
                ifPresent(root -> securityInjections.injectToQuery(criteriaQuery, (Root) root));
    }

    /**
     * Used when repository method is @Query annotated.
     * For now it works only with selects from Salary.
     * @param jp
     * @param jpaqlString
     * @return
     * @throws Throwable
     */
    @Around("queryCreationJoin() && args(jpaqlString)")
    public Object sqlSecurityInjection(ProceedingJoinPoint jp, String jpaqlString) throws Throwable {
        //MOCK TODO it sucks
        if(jpaqlString.startsWith("select") && jpaqlString.contains("Salary"))
            jpaqlString = securityInjections.injectToQuery(jpaqlString);
        return jp.proceed(new Object[]{jpaqlString});
    }

    /**
     * I don't know if it is used somewhere in Spring repositories
     * Used when EntityManager.criteriaQuery parameter is CriteriaUpdate
     * @param updateQuery
     */
    @Before("queryCreationJoin() && args(updateQuery)")
    public void updateSecurityInjection(CriteriaUpdate updateQuery){
        Root root = updateQuery.getRoot();
        if (entitiesHolder.isRootUpdateProtected(root))
            securityInjections.injectToQuery(updateQuery);
    }

    /**
     * I don't know if it is used somewhere in Spring repositories
     * Used when EntityManager.createQuery parameter is CriteriaDelete
     * @param deleteQuery
     */
    @Before("queryCreationJoin() && args(deleteQuery)")
    public void deleteSecurityInjection(CriteriaDelete deleteQuery){
        Root root = deleteQuery.getRoot();
        if (entitiesHolder.isRootDeleteProtected(root))
            securityInjections.injectToQuery(deleteQuery);
    }

}
