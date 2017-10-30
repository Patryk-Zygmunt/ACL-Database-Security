package com.designPatterns.ACLDatabaseSecurity.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;


/**HOW IT WORKS
 * Implementacja interfejsu javax.persistence.EntityManager jest beansem, czyli jest zarządzana przez springa. Dzięki
 * temu możemy zrobić przecięcie (PointCut) na wywołaniu wybranej metody (JoinPoint) z tego interfejsu.
 *
 * Dodatkowo aspekt podczas przecięcia może wykonywać jakąś akcję (Advice). Mamy 4 typy akcji. Nas interesuje typ
 * Before advice (akcja wywoływana przed metodą) gdyż pozwala na modyfikację argumentów metody.
 *
 *
 */
@Aspect
@Component
public class SecurityInjectionAspect {

    @Autowired
    EntityManager entityManager;

    @Autowired
    SecuredEntitiesHolder entitiesHolder;


    /**
     * Definiowanie pointcut na joinpoincie javax.persistence.EntityManager.createQuery(..)
     * Poincut ten nie definiuje argumentów jakie przyjmuje createQuery, więc pasuje on dla każdej odmiany createQuery.
     */
    @Pointcut("execution(* javax.persistence.EntityManager.createQuery(..))")
    public void queryCreationJoin(){
    }


    /**
     * Advice, który jest wywoływany dla powyższego przecięcia z dodatkowym doprecyzowaniem jakie argumenty ma mieć
     * metoda podczas wywołania której chcemy przeciąć.
     *
     * Advice wywoływana przy selecie
     * @param criteriaQuery - tworzenie selecta
     *
     * Wewnątrz mamy prostą testową implementację, która sprawdza, czy któryś root (chyba to odpowiada tabeli z from)
     *                      jest chroniony (tutaj założenie jest, że będzie tylko jeden taki xdd) i jeśli tak to
     *                      dodawany jest warunek where.
     *
     */
    @Before("queryCreationJoin() && args(criteriaQuery)")
    @SuppressWarnings("unchecked")
    public void selectSecurityInjection(CriteriaQueryImpl criteriaQuery){
        criteriaQuery.getRoots().
                stream().
                filter(root -> entitiesHolder.isRootSelectProtected((Root) root)).
                findFirst().
                ifPresent(root -> addSecurityToQuery(criteriaQuery, (Root) root));
    }

    /**
     * Advice wywoływana przy updacie
     * @param updateQuery - tworzenie updatea
     */
    @Before("queryCreationJoin() && args(updateQuery)")
    public void updateSecurityInjection(CriteriaUpdate updateQuery){
        //TODO
    }

    /**
     * Advice wywoływana przy delecie
     * @param deleteQuery - tworzenie deletea
     */
    @Before("queryCreationJoin() && args(deleteQuery)")
    public void deleteSecurityInjection(CriteriaDelete deleteQuery){
        //TODO
    }

    /**
     * Testowe filtrowanie id większych od 3
     * @param query - query do modyfikacji
     * @param root - info o tabeli
     */
    @SuppressWarnings("unchecked")
    private void addSecurityToQuery(CriteriaQueryImpl query, Root root){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        query.where(cb.gt(root.get("id"), 3));
    }
}
