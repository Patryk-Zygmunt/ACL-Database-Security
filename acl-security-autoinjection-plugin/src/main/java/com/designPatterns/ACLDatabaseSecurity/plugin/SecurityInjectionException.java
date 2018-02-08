package com.designPatterns.ACLDatabaseSecurity.plugin;

public class SecurityInjectionException extends Exception {
    public SecurityInjectionException() {
        super();
    }

    public SecurityInjectionException(String s) {
        super(s);
    }

    public SecurityInjectionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SecurityInjectionException(Throwable throwable) {
        super(throwable);
    }

    protected SecurityInjectionException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
