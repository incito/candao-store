package com.candao.common.dao;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import com.candao.common.page.Page;
import com.candao.common.page.PageContainer;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

public class SqlSessionDaoSupport implements DaoSupport {
	private SqlSessionTemplate sqlSession;
	
	 

	public SqlSessionDaoSupport(SqlSessionTemplate sqlSession) {   
		this.sqlSession = sqlSession;
	}
   
	@Override
	public int insertOnce(String statement, Object parameter){
		return sqlSession.insert(statement, parameter);
	}
	
	@Override
	public int insert(String statement, Object parameter) {
		return sqlSession.insert(statement, parameter);
	}

	@Override
	public int update(String statement, Object parameter) {
		return sqlSession.update(statement, parameter);
	}

	@Override
	public <K, V, T> T get(String statement, Map<K, V> parameter) {
		return sqlSession.selectOne(statement, parameter);
	}

	@Override
	public <K, V> Map<K, V> findOne(String statement, Map<K, V> parameter) {
		return sqlSession.selectOne(statement, parameter);
	}

	@Override
	public <K, V> int delete(String statement, Map<K, V> parameter) {
		return sqlSession.delete(statement, parameter);
	}

	@Override
	public <E, K, V> List<E> find(String statement, Map<K, V> parameter) {
		return sqlSession.selectList(statement, parameter);
	}
	
	
	@Override
	public <E, K, V> List<E> findAndFooter(String statement, Map<K, V> parameter) {
		
		List<E> find =  sqlSession.selectList(statement, parameter);
		PageList<E> footer = pageFooter(statement+"Footer",  parameter,  1,  10);
		find.addAll(footer);
		return find;
	}
	
	@Override
	public <E> List<E> find(String statement) {
		return sqlSession.selectList(statement);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E, K, V> Page<E> page(String pageStatement, Map<K, V> parameter, int current, int pagesize) {
		PageBounds pageBounds = new PageBounds(current, pagesize);
		PageList<E> result = (PageList<E>) sqlSession.selectList(pageStatement, parameter, pageBounds);
		Paginator paginator = result.getPaginator();
		return new PageContainer<E, K, V>(paginator.getTotalCount(), paginator.getLimit(), paginator.getPage(), result, parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E, K, V> Page<E> pageAndFooter(String pageStatement, Map<K, V> parameter, int current, int pagesize) {
		PageBounds pageBounds = new PageBounds(current, pagesize);
		PageList<E> result = (PageList<E>) sqlSession.selectList(pageStatement, parameter, pageBounds);
		PageList<E> pageFooter = pageFooter(pageStatement+"Footer",  parameter,  current,  pagesize);

		Paginator paginator = result.getPaginator();
		return new PageContainer<E, K, V>(paginator.getTotalCount(), paginator.getLimit(), paginator.getPage(), result, parameter,pageFooter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E, K, V> PageList<E> pageFooter(String pageStatement, Map<K, V> parameter, int current, int pagesize) {
		PageBounds pageBounds = new PageBounds(1, pagesize);
		PageList<E> pageFooter = (PageList<E>) sqlSession.selectList(pageStatement, parameter, pageBounds);
		
		
		return pageFooter;
	}
	
	@Override
	public Connection getConnection() {
		return sqlSession.getConnection();
	}

	@Override
	public Configuration getConfiguration() {
		return sqlSession.getConfiguration();
	}

	@Override
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSession;
	}

	@Override
	public <T> T findOne(String statement) {
		 return sqlSession.selectOne(statement);
	}

	@Override
	public  String  getSynSql(String tableName,String sqlCondition) {
		 
		SqlSessionFactory factory = sqlSession.getSqlSessionFactory();
		Connection conn = factory.openSession().getConnection();
		SynDataTools  syn = new SynDataTools(conn);
		 String sql = syn.generateSQLList(tableName,sqlCondition);
		 try {
			 if(!conn.isClosed()){
				 conn.close();
			 }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sql;
	}
	
	@Override
	public  String  executeSql(String sql) {
		 
		SqlSessionFactory factory = sqlSession.getSqlSessionFactory();
		Connection conn = factory.openSession().getConnection();
		 try {
			 executeSqlDelimiter(sql,conn);
			 if(!conn.isClosed()){
				 conn.close();
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sql;
	}
	
	private void executeSqlDelimiter(String sql, Connection conn){


		 String[] sqls = null;
	      if(sql != null){
	    	  sqls = sql.split(";");
	      }else{
	    	  return ;
	      }
	      try{
		    ScriptRunner runner = new ScriptRunner(conn);
		    runner.setDelimiter(";");
		    runner.setErrorLogWriter(null);
		    runner.setLogWriter(null);
		    runner.setErrorLogWriter(null);
		    for(String sql2 : sqls){
		    	  Reader reader =  new StringReader(sql2 + ";"); 
				   runner.runScript(reader);
		     }
	      }catch(Exception e){
			  e.printStackTrace();
		  }
	      
	}
 
}
