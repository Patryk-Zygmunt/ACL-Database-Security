package com.designPatterns.ACLDatabaseSecurity.plugin.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ProtectedEntity {
    Class<?> set();

    String setId();

    String classId() default "id";
}
