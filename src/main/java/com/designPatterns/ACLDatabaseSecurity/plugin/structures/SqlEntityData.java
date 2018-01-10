package com.designPatterns.ACLDatabaseSecurity.plugin.structures;

public class SqlEntityData {
    public String root;
    public String alias;

    public SqlEntityData(String root, String alias) {
        this.root = root;
        this.alias = alias;
    }

    public SqlEntityData(String [] args){
        this.root = args[0];
        this.alias = args[1];
    }
}
