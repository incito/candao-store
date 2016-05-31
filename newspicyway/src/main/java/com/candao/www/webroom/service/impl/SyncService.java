package com.candao.www.webroom.service.impl;

import com.candao.common.dao.DaoSupport;
import com.candao.www.spring.SpringContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 门店与总店数据同步功能
 *
 * @author zgt
 */
@Service
public class SyncService {
  @Autowired
  private DaoSupport dao;
  @Autowired
  @Qualifier("sqlSessionFactory")
  private SqlSessionFactory sessionFactory;

  public void saveDish(String sql, List<String> tableNames) {
    System.out.println(sql);

    DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) SpringContext.getBean(DataSourceTransactionManager.class);
    DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    TransactionStatus status = transactionManager.getTransaction(definition);
    try (
        Connection connection = sessionFactory.openSession().getConnection();
        Statement stmt = connection.createStatement()
    ) {

      connection.setAutoCommit(false);

      for (String tableName : tableNames) {
        if("t_dictionary".equalsIgnoreCase(tableName))
          stmt.addBatch("DELETE FROM t_dictionary WHERE `type` IN ('SPECIAL','DISHSTATUS')");
        else
          stmt.addBatch("TRUNCATE table " + tableName);
      }
      String[] sqls = sql.split(";");
      for (String s : sqls) {
        if (StringUtils.hasText(s))
          stmt.addBatch(s);
      }

      try {
        stmt.executeBatch();
        transactionManager.commit(status);
      } catch (SQLException e) {
        transactionManager.rollback(status);
        throw e;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

}
