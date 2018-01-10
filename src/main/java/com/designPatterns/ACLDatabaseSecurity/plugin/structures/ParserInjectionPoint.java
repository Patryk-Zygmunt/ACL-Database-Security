package com.designPatterns.ACLDatabaseSecurity.plugin.structures;

public class ParserInjectionPoint {

    public String[] point;
    public boolean isWhereThere;

    public ParserInjectionPoint(String[] point, boolean isWhereThere) {
        this.point = point;
        this.isWhereThere = isWhereThere;
    }
}


