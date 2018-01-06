package com.designPatterns.ACLDatabaseSecurity.plugin.structures;

public class ProtectedEntityData {
    private Class<?> entityClass;
    private Class<?> setClass;
    private String setId;
    private String classId;

    public ProtectedEntityData(Class<?> entityClass, Class<?> setClass, String setId, String classId) {
        this.entityClass = entityClass;
        this.setClass = setClass;
        this.setId = setId;
        this.classId = classId;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public Class<?> getSetClass() {
        return setClass;
    }

    public String getSetId() {
        return setId;
    }

    public String getClassId() {
        return classId;
    }
}
