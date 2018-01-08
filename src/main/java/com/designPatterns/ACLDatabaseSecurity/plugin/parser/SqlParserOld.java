package com.designPatterns.ACLDatabaseSecurity.plugin.parser;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ParserInjectionPoint;
import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlEntityData;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SqlParserOld {
    private final static Pattern rootsPattern = Pattern.compile("(?:from|FROM)\\s+((?:\\w+\\s+\\w+(?:,\\s*)?)+)");
    private final static Pattern joinsPattern = Pattern.compile("(?:join|JOIN)\\s+(\\w*\\s*\\w*)");

    public static Set<SqlEntityData> getRoots(String sql) {
        return parse(rootsPattern.matcher(sql));

    }

    public static Set<SqlEntityData> getJoins(String sql) {
        return parse(joinsPattern.matcher(sql));
    }

    public static Set<SqlEntityData> getAll(String sql) {
        Set<SqlEntityData> result = getRoots(sql);
        result.addAll(getJoins(sql));
        return result;
    }

    private static Set<SqlEntityData> parse(Matcher m) {
        Set<SqlEntityData> result = new HashSet<>();
        while (m.find())
            result.addAll(
                    Arrays.stream(m.group(1).split("\\s*,\\s*"))
                            .map(x -> new SqlEntityData(x.split("\\s+")))
                            .collect(Collectors.toSet())
            );
        return result;
    }

    public static ParserInjectionPoint getInjectionPoint(String sql) {
        String[] result = sql.split(" (WHERE|where)");
        if (result.length > 1)
            return new ParserInjectionPoint(result, true);
        Matcher m = rootsPattern.matcher(sql);
        if (m.find())
            return new ParserInjectionPoint(sql.split("(?<=" + m.group() + ")"), false);
        return null;
    }
}
