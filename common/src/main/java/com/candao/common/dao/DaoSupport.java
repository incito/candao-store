package com.candao.common.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;

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
	 
	public  String  executeSql(String sql) ;
}
