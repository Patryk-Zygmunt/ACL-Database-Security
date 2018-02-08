package com.designPatterns.ACLDatabaseSecurity.plugin.structures;

public class ProtectedEntityData {
    private Class<?> entityClass;
    private Class<?> setClass;
    private String setId;
    private String classId;
    private String privilegeId;

    public ProtectedEntityData(Class<?> entityClass, Class<?> setClass, String setId, String classId, String privilegeId) {
        this.entityClass = entityClass;
        this.setClass = setClass;
        this.setId = setId;
        this.classId = classId;
        this.privilegeId = privilegeId;
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

    public String getPrivilegeId() {
        return privilegeId;
    }
}
