package com.candao.www.utils;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

/**
 * Created by liaoy on 2016/11/1.
 */
public class FlywayCallback implements org.flywaydb.core.api.callback.FlywayCallback {
    private Flyway flyway;
    private String toDeleteVersions;

    public FlywayCallback(Flyway flyway) {
        this.flyway = flyway;
    }

    public String getToDeleteVersions() {
        return toDeleteVersions;
    }

    public void setToDeleteVersions(String toDeleteVersions) {
        this.toDeleteVersions = toDeleteVersions;
    }

    @Override
    public void beforeClean(Connection connection) {

    }

    @Override
    public void afterClean(Connection connection) {

    }

    @Override
    public void beforeMigrate(Connection connection) {

    }

    @Override
    public void afterMigrate(Connection connection) {
        if (null == toDeleteVersions || toDeleteVersions.isEmpty()) {
            return;
        }
        Statement statement = null;
        try {
            statement = connection.createStatement();
            StringBuilder sb = new StringBuilder("delete from ");
            sb.append(flyway.getTable()).append(" where version in (");
            StringTokenizer st = new StringTokenizer(toDeleteVersions,",");
            while (st.hasMoreElements()) {
                sb.append("'").append(st.nextElement()).append("',");
            }
            sb.deleteCharAt(sb.length() - 1).append(")");
            String sql = sb.toString();
            System.out.println("清除版本执行记录：" + sql);
            statement.execute(sql);
            System.out.println("清除版本执行记录成功！");
        } catch (SQLException e) {
            System.out.println("清除版本执行记录失败！");
            e.printStackTrace();
        }
    }

    @Override
    public void beforeEachMigrate(Connection connection, MigrationInfo info) {

    }

    @Override
    public void afterEachMigrate(Connection connection, MigrationInfo info) {

    }

    @Override
    public void beforeValidate(Connection connection) {

    }

    @Override
    public void afterValidate(Connection connection) {

    }

    @Override
    public void beforeBaseline(Connection connection) {

    }

    @Override
    public void afterBaseline(Connection connection) {

    }

    @Override
    public void beforeRepair(Connection connection) {

    }

    @Override
    public void afterRepair(Connection connection) {

    }

    @Override
    public void beforeInfo(Connection connection) {

    }

    @Override
    public void afterInfo(Connection connection) {

    }
}
