package com.designPatterns.ACLDatabaseSecurity.plugin.parser;

public interface SqlParserCreator {
    SqlParser createParser(String sql) throws SqlParserException;
}
