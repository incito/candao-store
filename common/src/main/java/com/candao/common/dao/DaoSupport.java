package com.candao.common.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;

import com.candao.common.exception.SysException;
import com.candao.common.page.Page;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface DaoSupport {
	
	public int insert(String statement, Object parameter);
	
	public int insertOnce(String statement, Object parameter);

	public int update(String statement, Object parameter);

	public <K, V, T> T get(String statement, Map<K, V> parameter);

	public <K, V> Map<K, V> findOne(String statement, Map<K, V> parameter);
	
	public <T> T findOne(String statement);

	public <K, V> int delete(String statement, Map<K, V> parameter);

	public <E, K, V> List<E> find(String statement, Map<K, V> parameter);

	public <E> List<E> find(String statement);

	public <E, K, V> Page<E> page(String pageStatement, Map<K, V> parameter, int current, int pagesize);

	public Connection getConnection();

	public Configuration getConfiguration();

	public SqlSessionTemplate getSqlSessionTemplate();

	public <E, K, V> Page<E> pageAndFooter(String pageStatement, Map<K, V> parameter, int current, int pagesize);
	
	public <E, K, V> PageList<E> pageFooter(String pageStatement, Map<K, V> parameter, int current, int pagesize);

	public <E, K, V> List<E> findAndFooter(String statement, Map<K, V> parameter);
	
	public String getSynSql(String sqlCondition,String tableName);
	/**
	 * 
	 * @Description:获取需要同步的数据
	 * @create: 余城序
	 * @Modification:
	 * @param sqlCondition 需要执行的sql
	 * @param tableName 表名
	 * @return list 装有map集合,map的key为列名,value为值的map数据对象
	 */ 
	public List<Map<String,String>> getSynData(String sqlCondition,String tableName) throws SysException;
	
	public  String  executeSql(String sql) ;
}
