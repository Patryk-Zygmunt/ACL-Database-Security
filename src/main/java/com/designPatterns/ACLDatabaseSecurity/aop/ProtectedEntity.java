package com.designPatterns.ACLDatabaseSecurity.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
public @interface ProtectedEntity {
    String mode() default "r";
}
