package com.designPatterns.ACLDatabaseSecurity.plugin.parser.ssql;

import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParser;
import com.designPatterns.ACLDatabaseSecurity.plugin.parser.SqlParserException;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ParserInjectionPoint;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlEntityData;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleSqlParser implements SqlParser {
    private final static Pattern rootsPattern = Pattern.compile("(?:from|FROM)\\s+((?:\\w+\\s+\\w+(?:,\\s*)?)+)");
    private final static Pattern joinsPattern = Pattern.compile("(?:join|JOIN)\\s+(\\w*\\s*\\w*)");
    String sql;

    public SimpleSqlParser(String sql) {
        this.sql = sql;
    }



    @Override
    public Set<SqlEntityData> getRoots() {
        return parse(rootsPattern.matcher(sql));
    }

    @Override
    public Set<SqlEntityData> getJoins() {
        return parse(joinsPattern.matcher(sql));
    }

    private Set<SqlEntityData> parse(Matcher m) {
        Set<SqlEntityData> result = new HashSet<>();
        while (m.find())
            result.addAll(
                    Arrays.stream(m.group(1).split("\\s*,\\s*"))
                            .map(x -> new SqlEntityData(x.split("\\s+")))
                            .collect(Collectors.toSet())
            );
        return result;
    }

    @Override
    public Set<SqlEntityData> getRootsAndJoins() {
        Set<SqlEntityData> result = getRoots();
        result.addAll(getJoins());
        return result;
    }

    @Override
    public String addInjectionToWhereClause(String injection) throws SqlParserException {
        String[] result = sql.split(" (WHERE|where)");
        if (result.length > 1)
            return result[0] + " WHERE " + injection + " AND" + result[1];

        return injectIfWhereClauseNotExist(injection);
    }

    private String injectIfWhereClauseNotExist(String injection) throws SqlParserException {
        Matcher m = rootsPattern.matcher(sql);
        if (m.find()) {
            String [] result = sql.split("(?<=" + m.group() + ")");
            if (result.length > 1)
                return result[0] + " WHERE " + injection + result[1];
            else
                return result[0] + " WHERE " + injection;
        }
        throw new SqlParserException("Cannot find place where to inject");
    }
}
