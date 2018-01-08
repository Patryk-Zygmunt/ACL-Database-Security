package com.designPatterns.ACLDatabaseSecurity.plugin.parser;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.ParserInjectionPoint;
import org.junit.Test;

import static org.junit.Assert.*;

public class SqlParserOldTest {

    @Test
    public void getRoots() {
        String sql1 = "SELECT * FROM dudek d, rpk r natural join xd";
        String sql2 = "SELECT * FROM dudek d natural join xd";
        String sql3 = "SELECT Pi";
        String sql4 = "SELECT * from dudek d natural join xd";

        assertEquals(2, SqlParserOld.getRoots(sql1).size());
        assertEquals(1, SqlParserOld.getRoots(sql2).size());
        assertEquals(0, SqlParserOld.getRoots(sql3).size());
        assertEquals(1, SqlParserOld.getRoots(sql4).size());
    }

    @Test
    public void getJoins() {
        String sql1 = "SELECT * FROM dudek natural join xd x natural join rpk r";
        String sql2 = "SELECT * FROM dudek natural JOIN xd x natural join rpk r";

        assertEquals(2, SqlParserOld.getJoins(sql1).size());
        assertEquals(2, SqlParserOld.getJoins(sql2).size());
    }

    @Test
    public void getInjectionPoint() {
        String sql1 = "SELECT x FROM ble x WHERE x.ble = x.ble AND x.ble =  x.ble";
        String sql2 = "SELECT x FROM ble x GROUP BY (x)";
        String sql3 = "SELECT x FROM ble x";

        ParserInjectionPoint ip = SqlParserOld.getInjectionPoint(sql1);
        ParserInjectionPoint ip2 = SqlParserOld.getInjectionPoint(sql2);
        ParserInjectionPoint ip3 = SqlParserOld.getInjectionPoint(sql3);

        assertEquals("sql1", true, ip.isWhereThere);
        assertEquals("sql1", 2, ip.point.length);
        assertEquals("sql2", false, ip2.isWhereThere);
        assertEquals("sql2", 2, ip2.point.length);
        assertEquals("sql3", false, ip3.isWhereThere);
        assertEquals("sql3", 1, ip3.point.length);


    }
}