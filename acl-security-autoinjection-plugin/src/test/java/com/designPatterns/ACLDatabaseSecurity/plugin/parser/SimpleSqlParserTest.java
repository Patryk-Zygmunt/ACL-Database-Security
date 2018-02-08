package com.designPatterns.ACLDatabaseSecurity.plugin.parser;

import com.designPatterns.ACLDatabaseSecurity.plugin.parser.ssql.SimpleSqlParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleSqlParserTest {

    @Test
    public void getRoots() {
        String sql1 = "SELECT * FROM dudek d, rpk r natural join xd";
        String sql2 = "SELECT * FROM dudek d natural join xd";
        String sql3 = "SELECT Pi";
        String sql4 = "SELECT * from dudek d natural join xd";

        assertEquals(2, new SimpleSqlParser(sql1).getRoots().size());
        assertEquals(1, new SimpleSqlParser(sql2).getRoots().size());
        assertEquals(0, new SimpleSqlParser(sql3).getRoots().size());
        assertEquals(1, new SimpleSqlParser(sql4).getRoots().size());
    }

    @Test
    public void getJoins() {
        String sql1 = "SELECT * FROM dudek natural join xd x natural join rpk r";
        String sql2 = "SELECT * FROM dudek natural JOIN xd x natural join rpk r";

        assertEquals(2, new SimpleSqlParser(sql1).getJoins().size());
        assertEquals(2, new SimpleSqlParser(sql2).getJoins().size());
    }

    @Test
    public void addInjectionToWhereClause() throws SqlParserException {
        String sql1 = "SELECT x FROM ble x WHERE x.ble = x.ble AND x.ble =  x.ble";
        String sql2 = "SELECT x FROM ble x GROUP BY (x)";
        String sql3 = "SELECT x FROM ble x";

        String result1 = "SELECT x FROM ble x WHERE XDD AND x.ble = x.ble AND x.ble =  x.ble";
        String result2 = "SELECT x FROM ble x WHERE jd = kurwe GROUP BY (x)";
        String result3 = "SELECT x FROM ble x WHERE dudek = rpk";

        assertEquals("sql1", result1, new SimpleSqlParser(sql1).addInjectionToWhereClause("XDD"));
        assertEquals("sql2", result2, new SimpleSqlParser(sql2).addInjectionToWhereClause("jd = kurwe"));
        assertEquals("sql3", result3, new SimpleSqlParser(sql3).addInjectionToWhereClause("dudek = rpk"));


    }
}