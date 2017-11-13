package com.designPatterns.ACLDatabaseSecurity.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtectedEntity {
    String mode() default "s";
}
