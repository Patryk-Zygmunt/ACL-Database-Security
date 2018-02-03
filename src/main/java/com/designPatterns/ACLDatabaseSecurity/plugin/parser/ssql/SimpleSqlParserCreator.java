package com.designPatterns.ACLDatabaseSecurity.plugin.parser.ssql;

import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParser;
import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParserCreator;
import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParserException;

public class SimpleSqlParserCreator implements SqlParserCreator{
    @Override
    public SqlParser createParser(String sql) throws SqlParserException {
        return new SimpleSqlParser(sql);
    }
}
