package com.designPatterns.ACLDatabaseSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by Linus on 21.10.2017.
 */
@Aspect
@Component
public class MyAspectUser {

    public void perform(String name) {
    }

    @Pointcut("execution(* (@MyAspect *).*(..))")
    void methodOfAnnotatedClass() {
    }

    @After("@annotation(MyAspect) && methodOfAnnotatedClass()")
    public void after() {
        System.out.println("AFTER USING METHOD WITH CLASS ANNOTATION");

    }


    @Around("@annotation(MyAspect)")
    public void applause(ProceedingJoinPoint joinPoint) {
        System.out.println("My aspect started");
        try {
            Object proceed = joinPoint.proceed();
            System.out.println(">>executed" + joinPoint.getSignature() + "  " + joinPoint.toShortString());
        } catch (Throwable t) {
            System.out.println(" My aspect Failed" + joinPoint.getSignature());
        }

    }
}
