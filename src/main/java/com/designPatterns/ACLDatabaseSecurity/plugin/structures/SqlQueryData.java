package com.designPatterns.ACLDatabaseSecurity.plugin.structures;

public class SqlQueryData{
    public SqlEntityData sqlData;
    public int number;
    public ProtectedEntityData entityData;

    public SqlQueryData(SqlEntityData sqlData, int number, ProtectedEntityData entityData) {
        this.sqlData = sqlData;
        this.number = number;
        this.entityData = entityData;
    }
}
