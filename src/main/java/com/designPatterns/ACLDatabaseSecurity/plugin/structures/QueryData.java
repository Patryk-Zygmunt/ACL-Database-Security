package com.designPatterns.ACLDatabaseSecurity.plugin.structures;


import org.hibernate.jpa.criteria.CriteriaQueryImpl;

import javax.persistence.criteria.Root;

public class QueryData {
    CriteriaQueryImpl query;
    Root root;

    public QueryData(CriteriaQueryImpl query, Root root) {
        this.query = query;
        this.root = root;
    }

    public CriteriaQueryImpl getQuery() {
        return query;
    }

    public Root getRoot() {
        return root;
    }
}
