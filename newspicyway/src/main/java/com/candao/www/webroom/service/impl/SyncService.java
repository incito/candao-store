package com.candao.www.webroom.service.impl;

import com.candao.common.utils.HttpUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.spring.SpringContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店与总店数据同步功能
 *
 * @author zgt
 */
@Service
public class SyncService {
  @Autowired
  @Qualifier("sqlSessionFactory")
  private SqlSessionFactory sessionFactory;

  public static final Logger logger = Logger.getLogger(SyncService.class);

  /**
   * 同步云端数据
   *
   * @param type
   * @return
   */
  public Map syncDataFromCloud(String type) {
    String url = "http://" + PropertiesUtils.getValue("cloud.host") + "/" + PropertiesUtils.getValue("cloud.webroot") + "/sync/getJsonData";
    String queryString = "tenantId=" + PropertiesUtils.getValue("tenant_id") + "&branchId=" + PropertiesUtils.getValue("current_branch_id") + "&type=" + type;
    String result = HttpUtils.doGet(url, queryString);
    Assert.hasText(result);

    Map<String, Object> resp = new HashMap<>();

    if ("Read timed out".equalsIgnoreCase(result)) {
      logger.error("从总店同步数据超时：" + url + "?" + queryString);
      resp.put("statusCode", "500");
      resp.put("data", "从总店获取数据超时");
      return resp;
    }

    try {
      resp = JacksonJsonMapper.jsonToObject(result, Map.class);
      if ("200".equals(String.valueOf(resp.get("statusCode")))) {
        try {
          save((Map<String, List<Map<String, Object>>>) resp.get("data"));
        } catch (Exception e) {
          logger.error("数据入库失败\n" + result, e);
          resp.put("statusCode", "500");
          resp.put("data", "数据保存失败");
          return resp;
        }
      }
    } catch (Exception e) {
      logger.error("从总店同步数据失败\n" + result, e);
      resp.put("statusCode", "500");
      resp.put("data", "从总店获取数据失败");
      return resp;
    }

    return resp;
  }

  /**
   * 保存数据
   *
   * @param tables
   */
  public void save(Map<String, List<Map<String, Object>>> tables) {

    Assert.notEmpty(tables);

    DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) SpringContext.getBean(DataSourceTransactionManager.class);
    DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    TransactionStatus status = transactionManager.getTransaction(definition);
    try (
        Connection connection = sessionFactory.openSession().getConnection();
        Statement stmt = connection.createStatement()
    ) {

      connection.setAutoCommit(false);

      // 循环处理多张表
      for (Map.Entry<String, List<Map<String, Object>>> entry : tables.entrySet()) {
        if ("t_dictionary".equalsIgnoreCase(entry.getKey()))
          stmt.addBatch("DELETE FROM t_dictionary WHERE `type` IN ('SPECIAL','DISHSTATUS')");
        else
          stmt.addBatch("DELETE FROM " + entry.getKey());

        String dml = createSql(entry.getKey(), entry.getValue(), connection);
        if (StringUtils.hasText(dml))
          stmt.addBatch(dml);
      }

      // 持久化
      try {
        stmt.executeBatch();
        transactionManager.commit(status);
      } catch (SQLException e) {
        logger.error("同步数据入库失败：", e);
        transactionManager.rollback(status);
        throw e;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * 根据JSON数据拼装sql
   *
   * @param tableName
   * @param list
   * @param connection
   * @return
   */
  private String createSql(String tableName, List<Map<String, Object>> list, Connection connection) {
    Assert.notEmpty(list);

    StringBuffer sbf = new StringBuffer("insert into " + tableName + "(");

    try (ResultSet rs = connection.getMetaData().getColumns(null, null, tableName, "%")) {
      List<String> columns = new ArrayList<>();
      while (rs.next()) {
        columns.add(rs.getString("COLUMN_NAME"));
      }

      Assert.notEmpty(columns);

      for (String column : columns)
        sbf.append("`").append(column).append("`,");

      sbf.delete(sbf.length() - 1, sbf.length()).append(") values ");

      for (Map<String, Object> map : list) {
        sbf.append("(");
        for (String column : columns) {
          Object value = map.get(column);
          if (value == null) {
            sbf.append(value).append(",");
          } else if (value instanceof Number) {
            sbf.append(value).append(",");
          } else {
            sbf.append("'").append(value).append("',");
          }
        }
        sbf.delete(sbf.length() - 1, sbf.length()).append("),");
      }
      sbf.delete(sbf.length() - 1, sbf.length());

    } catch (SQLException e) {
      sbf = new StringBuffer();
      e.printStackTrace();
    }

    return sbf.toString();
  }

}
