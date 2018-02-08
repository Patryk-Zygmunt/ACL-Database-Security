package com.designPatterns.ACLDatabaseSecurity.plugin.parser;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlEntityData;

import java.util.Set;

public interface SqlParser {

    Set<SqlEntityData> getRoots();

    Set<SqlEntityData> getJoins();

    Set<SqlEntityData> getRootsAndJoins();

    String addInjectionToWhereClause(String injection) throws SqlParserException;
}
