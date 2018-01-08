package com.designPatterns.ACLDatabaseSecurity.plugin;

import org.junit.Test;

import static org.junit.Assert.*;

public class SqlParserTest {

    @Test
    public void getRoots() {
        String sql1 = "SELECT * FROM dudek d, rpk r natural join xd";
        String sql2 = "SELECT * FROM dudek d natural join xd";
        String sql3 = "SELECT Pi";
        String sql4 = "SELECT * from dudek d natural join xd";

        assertEquals(2, SqlParser.getRoots(sql1).size());
        assertEquals(1, SqlParser.getRoots(sql2).size());
        assertEquals(0, SqlParser.getRoots(sql3).size());
        assertEquals(1, SqlParser.getRoots(sql4).size());
    }

    @Test
    public void getJoins() {
        String sql1 = "SELECT * FROM dudek natural join xd x natural join rpk r";
        String sql2 = "SELECT * FROM dudek natural JOIN xd x natural join rpk r";

        assertEquals(2, SqlParser.getJoins(sql1).size());
        assertEquals(2, SqlParser.getJoins(sql2).size());
    }
}