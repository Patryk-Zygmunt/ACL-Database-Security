package com.designPatterns.ACLDatabaseSecurity.aop;

import com.designPatterns.ACLDatabaseSecurity.model.entity.Salary;
import com.designPatterns.ACLDatabaseSecurity.repositories.SalaryRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringRunner;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
@EnableAspectJAutoProxy
public class SecurityInjectionAspectTest {


    @Autowired
    private EntityManager entityManager;

    @Resource
    private SalaryRepository salaryRepository;

    @Autowired
    private SecurityInjections securityInjections;

    CriteriaBuilder cb;

    @PostConstruct
    private void init(){
        cb = entityManager.getCriteriaBuilder();
    }


    @Before
    public void setUp() throws Exception {
        salaryRepository.save(new Salary(1, 100));
        salaryRepository.save(new Salary(2, 200));
        salaryRepository.save(new Salary(3, 300));
        salaryRepository.save(new Salary(4, 400));
        salaryRepository.save(new Salary(5, 500));
        salaryRepository.save(new Salary(6, 600));
        salaryRepository.save(new Salary(7, 700));
        salaryRepository.save(new Salary(8, 800));
        salaryRepository.save(new Salary(9, 900));
        salaryRepository.save(new Salary(10, 1000));
    }

    @After
    public void tearDown() throws Exception {
//        salaryRepository.deleteAll();
    }


    @Test
    public void testFindAll() throws Exception {
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.SELECT,
                (query, root) ->
                        ((CriteriaQueryImpl)query).
                                where(cb.gt(((Root)root).get("value"), 500)));

        List<Salary> salaries =  salaryRepository.findAll();
        assertEquals("result size",5,salaries.size());
    }

    @Test
    public void testFindById() throws Exception {
        //before
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.SELECT,
                (query, root) ->
                        ((CriteriaQueryImpl)query).
                                where(cb.gt(((Root)root).get("value"), 300), ((CriteriaQueryImpl) query).getRestriction()));
        //after
        List<Salary> salaries = salaryRepository.findByValueBefore(800);
        //then
        Set<Double> values = salaries.stream().map(salary -> salary.getValue()).collect(Collectors.toSet());
        Set<Double> expected = Sets.newHashSet(400.0,500.0,600.0,700.0);
        //assert
        assertEquals("result size",4, salaries.size());
        assertEquals("result content", expected, values);
    }

    @Test
    public void testFindByInRange() throws Exception {
        //before
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.SELECT,
                (query, root) ->
                        ((CriteriaQueryImpl)query).
                                where(cb.gt(((Root)root).get("value"), 500), ((CriteriaQueryImpl) query).getRestriction()));
        //after
        List<Double> range = Lists.newArrayList(400.0,500.0,600.0,700.0);
        List<Salary> salaries = salaryRepository.findMefedronToSzmataSukiNieChceZnacJebacByValueIn(range);
        //then
        Set<Double> values = salaries.stream().map(salary -> salary.getValue()).collect(Collectors.toSet());
        Set<Double> expected = Sets.newHashSet(600.0,700.0);
        //assert
        assertEquals("result size",2, salaries.size());
        assertEquals("result content", expected, values);

    }

    @Test
    public void testDeleteBy() throws Exception {
        //before
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.SELECT,
                (query, root) ->
                        ((CriteriaQueryImpl)query).
                                where(cb.gt(((Root)root).get("value"), 500), ((CriteriaQueryImpl) query).getRestriction()));
        //after
        List<Salary> salaries = salaryRepository.deleteByValueBefore(800);
        //then
        Set<Double> values = salaries.stream().map(salary -> salary.getValue()).collect(Collectors.toSet());
        Set<Double> expected = Sets.newHashSet(600.0,700.0);
        //assert
        assertEquals("result size", 2,salaries.size());
        assertEquals("result content", expected,values);
    }

    @Test
    public void testRemoveBy() throws Exception {
        //before
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.SELECT,
                (query, root) ->
                        ((CriteriaQueryImpl)query).
                                where(cb.gt(((Root)root).get("value"), 500), ((CriteriaQueryImpl) query).getRestriction()));
        //after
        Long actual = salaryRepository.removeByValueBefore(800);
        //assert
        assertEquals("result size", new Long(2),actual);
    }


    @Test
    public void testFindUsingQueryAnnotation() throws Exception {
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.SELECT, sql -> {
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add(sql);
            joiner.add("or s.value = '1000'");
            return joiner.toString();
        });


        List<Salary> salaries = salaryRepository.findByValue(600.0);
        assertEquals(2, salaries.size());


    }

    @Test
    public void testSelectUsingNamedQueryAnnotation() throws Exception {
        securityInjections.clearInjections();
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.SELECT, sql -> {
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add(sql);
            joiner.add("and s.value > 300");
            return joiner.toString();
        });
        List<Salary> salaries = salaryRepository.findUsingNamedQueryByValueBefore(800.0);

        assertEquals(4, salaries.size());
    }

    @Test
    public void testNativeQueryInjection() throws Exception {
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.SELECT, sql -> {
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add(sql);
            joiner.add("OR VALUE = 1000");
            return joiner.toString();
        });

        List<Salary> result = salaryRepository.findWywaloneJajacaByValue(300);
        assertEquals(2,result.size());
    }



    @Test
    public void testCriteriaDeleteInjection() throws Exception {
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.DELETE, (criteria, root) ->
                ((CriteriaDelete)criteria).where(cb.gt(((Root)root).get("value"), 500)));
        CriteriaDelete delete = cb.createCriteriaDelete(Salary.class);
        delete.from(Salary.class);
        int d = entityManager.createQuery(delete).executeUpdate();
        assertEquals(5,d);
    }

    @Test
    public void testCriteriaUpdateInjection() throws Exception{
        securityInjections.addInjection(Salary.class, SecurityInjections.Type.UPDATE, (criteria, root) ->
                ((CriteriaUpdate)criteria).where(cb.gt(((Root)root).get("value"), 500)));

        CriteriaUpdate update = cb.createCriteriaUpdate(Salary.class);
        update.from(Salary.class);
        Path value = update.getRoot().get("value");
        update.set(value, 69);
        int u = entityManager.createQuery(update).executeUpdate();
        assertEquals(5, u);
    }
}