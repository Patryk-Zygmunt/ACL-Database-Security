package com.designPatterns.ACLDatabaseSecurity.plugin.parser.jsql;

import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParserCreator;
import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParser;
import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParserException;

public class JSqlSqlParserCreator implements SqlParserCreator {

    @Override
    public SqlParser createParser(String sql) throws SqlParserException {
        return new JSqlParser(sql);
    }
}
