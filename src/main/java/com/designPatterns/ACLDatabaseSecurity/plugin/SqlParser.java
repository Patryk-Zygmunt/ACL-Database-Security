package com.designPatterns.ACLDatabaseSecurity.plugin;


import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParser{
    private final static Pattern rootsPattern = Pattern.compile("(?:from|FROM) (\\w*(,\\s*)?)+");
    private final static Pattern joinsPattern = Pattern.compile("(?:join|JOIN)\\s+(\\w*)");

    public static List<String> getRoots(String sql){
       return parse(rootsPattern.matcher(sql));

    }

    public static List<String> getJoins(String sql){
        return parse(joinsPattern.matcher(sql));
    }

    private static List <String> parse(Matcher m){
        String roots;
        if (m.find())
            roots = m.group(1);
        else
            roots = "";
        return Arrays.asList(roots.split("\\s*,\\s*"));
    }

}
