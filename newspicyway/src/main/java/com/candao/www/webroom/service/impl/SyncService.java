package com.candao.www.webroom.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.candao.common.utils.HttpUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.model.Tdish;
import com.candao.www.spring.SpringContext;

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

    public static final Logger logger = LoggerFactory.getLogger(SyncService.class);
    
    @Autowired
	private TdishDao tdishDao;

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
//         			同步数据
                    doSave((Map<String, List<Map<String, Object>>>) resp.get("data"));
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
    public void doSave(Map<String, List<Map<String, Object>>> tables) {

        Assert.notEmpty(tables);

        DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) SpringContext.getBean(DataSourceTransactionManager.class);
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(definition);
        Connection connection = sessionFactory.openSession().getConnection();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();


			// 循环处理多张表
			for (Map.Entry<String, List<Map<String, Object>>> entry : tables.entrySet()) {
				//字典表不需要从总店同步
				if(!"t_dictionary".equalsIgnoreCase(entry.getKey())){
					if ("t_template_dishunit".equalsIgnoreCase(entry.getKey())) {
						//套餐表不去除餐具信息
						stmt.addBatch("DELETE FROM t_template_dishunit WHERE id NOT IN('DISHES_98')");
					} else if ("t_dish".equalsIgnoreCase(entry.getKey())) {
						//不去除餐具信息
						stmt.addBatch("DELETE FROM t_dish WHERE dishid NOT IN('DISHES_98')");
					} 
					else {
						stmt.addBatch("DELETE FROM " + entry.getKey());
					}
					String dml = createSql(entry.getKey(), entry.getValue(), connection);
					if (StringUtils.hasText(dml)){
						stmt.addBatch(dml);
					}
					//菜品的人气是门店本地数据，需要备份及恢复
					if("t_dish".equalsIgnoreCase(entry.getKey())){
						List<Tdish> findAllDish = tdishDao.findAllDish();
						for (Tdish tdish : findAllDish) {
							if(tdish.getOrderNum() != null){
								String sql = "update t_dish set orderNum = " + Integer.parseInt(tdish.getOrderNum()) + " where dishid = '" + tdish.getDishid() + "';";
								stmt.addBatch(sql);
							}
						}
					}
				}else{
                    stmt.addBatch("DELETE FROM t_dictionary WHERE type ='PAYWAY'");
                    stmt.addBatch("TRUNCATE TABLE t_payway_set ");
                    List<Map<String, Object>> value = entry.getValue();
                    List<Map<String, Object>> payways = new ArrayList<>();
                    for(Map<String,Object> dictionary:value){
                            if("payway".equalsIgnoreCase(dictionary.get("type").toString())){
                                payways.add(dictionary);
                            }
                    }
                    String dml = createSql(entry.getKey(), payways, connection);
                    if (StringUtils.hasText(dml)){
                        stmt.addBatch(dml);
                    }
                }
			}
            // 持久化
            stmt.executeBatch();
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("同步数据失败：", e);
            transactionManager.rollback(status);
            throw new RuntimeException(e);
        } finally {
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
        //数据表为空的情况则忽略
    	if(list == null  || list.isEmpty()){
    		return null;
    	}

        StringBuffer sbf = new StringBuffer("insert ignore  into " + tableName + "(");

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
                        // 增加单引号转义
                        String vstr = value.toString().replaceAll("'", "\\\\'");
                        sbf.append("'").append(vstr).append("',");
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
