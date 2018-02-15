package com.designPatterns.ACLDatabaseSecurity.plugin.structures;


import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class QueryData {
    private CriteriaQuery query;
    private Root root;

    public QueryData(CriteriaQuery query, Root root) {
        this.query = query;
        this.root = root;
    }

    public CriteriaQuery getQuery() {
        return query;
    }

    public Root getRoot() {
        return root;
    }
}
