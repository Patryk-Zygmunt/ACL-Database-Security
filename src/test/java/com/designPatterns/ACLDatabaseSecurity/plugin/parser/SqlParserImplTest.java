package com.designPatterns.ACLDatabaseSecurity.plugin.parser;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlEntityData;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class SqlParserImplTest {

    static final String statement = "SELECT x.OrderID, d.CustomerName, x.OrderDate, c.dupa FROM Orders x, Dudek c JOIN Customers d ON (c.x = d.a);";
    static final String statement2 = "SELECT x.OrderID, d.CustomerName, x.OrderDate, c.dupa FROM Orders x, Dudek c JOIN Customers d ON (c.x = d.a) WHERE c.xd = '12'";

    @Test
    public void getRoots() throws SqlParserException {
        SqlParserImpl parser = new SqlParserImpl(statement);
        Set<SqlEntityData> datas = parser.getRoots();

        assertEquals(true, datas.iterator().hasNext());
        SqlEntityData data = datas.iterator().next();
        assertEquals("Orders", data.root);
        assertEquals("x", data.alias);
    }

    @Test
    public void getJoins() throws SqlParserException {
        SqlParserImpl parser = new SqlParserImpl(statement);
        Set<SqlEntityData> datas = parser.getJoins();
        assertEquals(2, datas.size());
//        assertEquals(true, datas.iterator().hasNext());
//        SqlEntityData data = datas.iterator().next();
//        assertEquals("Orders", data.root);
//        assertEquals("x", data.alias);

    }

    @Test
    public void getRootsAndJoins() {
    }

    @Test
    public void addInjectionToWhereClause() throws SqlParserException {
        String injection = "x=y AND k=d AND j= (SELECT x.j from Car x WHERE x.id IN (1,2,3))";
        SqlParserImpl parser = new SqlParserImpl(statement);
        String result = parser.addInjectionToWhereClause(injection);
        assertEquals("SELECT x.OrderID, d.CustomerName, x.OrderDate, c.dupa FROM Orders x, Dudek c JOIN Customers d ON (c.x = d.a) WHERE x = y AND k = d AND j = (SELECT x.j FROM Car x WHERE x.id IN (1, 2, 3))", result);

        parser = new SqlParserImpl(statement2);
        result = parser.addInjectionToWhereClause(injection);
        assertEquals("SELECT x.OrderID, d.CustomerName, x.OrderDate, c.dupa FROM Orders x, Dudek c JOIN Customers d ON (c.x = d.a) WHERE c.xd = '12' AND x = y AND k = d AND j = (SELECT x.j FROM Car x WHERE x.id IN (1, 2, 3))", result);
    }
}