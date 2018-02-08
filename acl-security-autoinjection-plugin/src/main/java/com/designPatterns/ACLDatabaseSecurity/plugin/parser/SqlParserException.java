package com.designPatterns.ACLDatabaseSecurity.plugin.parser;

public class SqlParserException extends Exception {
    public SqlParserException() {
    }

    public SqlParserException(String s) {
        super(s);
    }

    public SqlParserException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SqlParserException(Throwable throwable) {
        super(throwable);
    }

    public SqlParserException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
