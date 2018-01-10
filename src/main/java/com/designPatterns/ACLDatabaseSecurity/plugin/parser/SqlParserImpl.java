package com.designPatterns.ACLDatabaseSecurity.plugin.parser;

import com.designPatterns.ACLDatabaseSecurity.plugin.structures.SqlEntityData;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.util.*;
import java.util.stream.Collectors;

public class SqlParserImpl implements SqlParser {

    private String sql;
    private Select select;
    private PlainSelect plainSelect;

    public SqlParserImpl(String sql) throws SqlParserException {
        this.sql = sql;
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Select) {
                this.select = (Select) statement;
                this.plainSelect = (PlainSelect) select.getSelectBody();
            } else
                throw new SqlParserException("Cannot parse sql as Select");

        } catch (JSQLParserException e) {
            throw new SqlParserException("Cannot parse sql", e);
        }
    }

    @Override
    public Set<SqlEntityData> getRoots() {
        Set<SqlEntityData> result = new HashSet();
        String[] sp = plainSelect.getFromItem().toString().split("(\\sAS\\s)|(\\sas\\s)|\\s");
        result.add(new SqlEntityData(sp[0], sp[1]));
        return result;
    }

    @Override
    public Set<SqlEntityData> getJoins() {
        List<Join> joins = plainSelect.getJoins();
        if (Objects.nonNull(joins))
            return joins.stream()
                    .map(join -> join.getRightItem().toString().split("(\\sAS\\s)|(\\sas\\s)|\\s"))
                    .map(arr -> new SqlEntityData(arr[0], arr[1]))
                    .collect(Collectors.toSet());
        return new HashSet<>();
    }

    @Override
    public Set<SqlEntityData> getRootsAndJoins() {
        Set<SqlEntityData> result = getRoots();
        result.addAll(getJoins());
        return result;
    }

    @Override
    public String addInjectionToWhereClause(String injection) throws SqlParserException {
        try {
            Expression ex = CCJSqlParserUtil.parseCondExpression(injection);
            if (Objects.isNull(plainSelect.getWhere()))
                plainSelect.setWhere(ex);
            else
                plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), ex));

        } catch (JSQLParserException e) {
            throw new SqlParserException("Cannot parse injection", e);
        }
        return select.toString();
    }
}
