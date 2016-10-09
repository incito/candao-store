package com.candao.common.dao;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.common.enums.ErrorMessage;
import com.candao.common.enums.Module;
import com.candao.common.exception.SysException;
 

public class SynDataTools {
	
    private Logger logger = LoggerFactory.getLogger(SynDataTools.class);

    Connection  connection = null;
	public SynDataTools( Connection  connection){
		this.connection = connection;
	}
	
	
	public List<Map<String,String>> generateData(String tableName,String sqlCondition)throws SysException{
		logger.info("tableName:-->"+tableName);
		logger.info("sqlCondition:-->"+sqlCondition);
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String,String>> result = null;
		try {
			stmt = connection.createStatement();
			DatabaseMetaData metadata = connection.getMetaData();
			// /////////////////////////////////////////////////////
			// 导出TESTt
			// /////////////////////////////////////////////////////
			String sqlExport = generateQuerySQL(metadata, tableName, sqlCondition);
			rs = stmt.executeQuery(sqlExport);
			//获取列名集合
			List<String> columns = getColumns(rs);
			//设置同步对象
			result = setData(rs,columns);
		} catch (SQLException e) {
			logger.error("sql报错",e);
			throw new SysException(ErrorMessage.SQLEXE_ERROR, Module.LOCAL_SHOP);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 
	 * @Description:设置对应字段和值的对象
	 * @create: 余城序
	 * @Modification:
	 * @param map 需要设置进去的对象
	 * @param columns 队列集合
	 * @return void
	 * @throws  
	 */
	private List<Map<String,String>> setData(ResultSet rs,List<String> columns) throws SysException{
		String colsValue = "";
		String value = "";
		Map<String,String> map = null;
		//数据集合
		List<Map<String,String>> list = new ArrayList<Map<String,String>>(64);
		try {
			while(rs.next()){
				map = new HashMap<String,String>();
				//循环获取每列的数据,第1列从1开始.所以获取列名,或列值,都是从1开始
				for (int i = 0; i < columns.size(); i++) {
					colsValue = rs.getString(columns.get(i));
					value = StringUtils.trimToEmpty(colsValue == null ? "" : new String(colsValue.getBytes(),"gbk"));
					map.put(columns.get(i), value);
				}
				list.add(map);
			}
			return list;
		} catch (SQLException e) {
			logger.error("数据库ResultSet操作失败",e);
			throw new SysException(ErrorMessage.DATASERVER_OPERATE_ERROR, Module.LOCAL_SHOP);
		}catch (UnsupportedEncodingException e) {
			logger.error("字符串格式错误",e);
			throw new SysException(ErrorMessage.ENCODE_ERROR, Module.LOCAL_SHOP);
		}
	}
	
	/**
	 * 
	 * @Description:根据某个表的ResultSet获取该表的字段
	 * @create: 余城序
	 * @Modification:
	 * @param result ResultSet
	 * @return List<String> 字段集合
	 */
	private List<String> getColumns(ResultSet rs) throws SysException{
		ResultSetMetaData rsm = null;
		int col;//列的个数
		try {
			rsm =rs.getMetaData();
			col = rsm.getColumnCount(); //获得列的个数
			//获得列集
			List<String> list = new ArrayList<String>(col);
			//第一列,从1开始.所以获取列名,或列值,都是从1开始
			for (int i = 1; i <= col; i++) {
				list.add(rsm.getColumnName(i));
			}
			return list;
		} catch (SQLException e) {
			logger.error("数据库ResultSet操作失败",e);
			throw new SysException(ErrorMessage.DATASERVER_OPERATE_ERROR, Module.LOCAL_SHOP);
		}
	}

	/**
	 * 根据id生成导出数据的SQL
	 * 
	 * @param id
	 * @return
	 */
	public String generateSQLList(String tableName,String sqlCondition) {
//		List<String> sqlList = new ArrayList<String>();

		Statement stmt = null;
		ResultSet rs = null;
		 StringBuilder sb = new StringBuilder();
		try {
			stmt = connection.createStatement();
			DatabaseMetaData metadata = connection.getMetaData();
			// /////////////////////////////////////////////////////
			// 导出TESTt
			// /////////////////////////////////////////////////////
			String sqlExport = generateQuerySQL(metadata, tableName, sqlCondition);
           
			rs = stmt.executeQuery(sqlExport);
			while (rs.next()) {
				String insertSQL = generateInsertSQL(rs, tableName);
				sb.append(insertSQL + "\n");
//				sqlList.add(insertSQL);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	/**
	 * 根据结果集和表名生成INSERT语句。
	 * 
	 * @param rs
	 * @param tableName
	 * @return
	 * @throws SQLException
	 * @throws UnsupportedEncodingException 
	 */
	private static String generateInsertSQL(ResultSet rs, String tableName) throws SQLException, UnsupportedEncodingException {
		if (rs == null)
			return "";
		
		String idName = getIdCondition(tableName);
		StringBuilder deleteSQLBuilder = new StringBuilder();
		deleteSQLBuilder.append("delete from " );
		deleteSQLBuilder.append(tableName);
		deleteSQLBuilder.append("  where ");
		deleteSQLBuilder.append(idName);
		deleteSQLBuilder.append(" in ('");
		deleteSQLBuilder.append(rs.getString(idName));
		deleteSQLBuilder.append("'); \n");
		
		StringBuilder insertSQLBuilder = new StringBuilder(); 
		insertSQLBuilder.append(deleteSQLBuilder.toString());
		
		insertSQLBuilder.append("insert ignore into ").append(tableName).append(" (");
		DatabaseMetaData metadata = rs.getStatement().getConnection().getMetaData();
		insertSQLBuilder.append(getColumnsString(metadata, tableName));
		insertSQLBuilder.append(") values (");

		String[] cols = getColumnsArray(metadata, tableName);
		int len = cols.length;
		for (int i = 0; i < len; i++) {
			String colsValue = rs.getString(cols[i]);
//			String idcard = new String(rs.getString("idcard").getBytes("ISO8859_1"), "utf8"); 
			String value = StringUtils.trimToEmpty(colsValue == null?"":new String(colsValue.getBytes(),"gbk"));
			if (i != len - 1) {
				if("".equals(value)){
					insertSQLBuilder.append("null, ");
				}else{
				   insertSQLBuilder.append("'").append(value).append("', ");
				}
			} else {
				if("".equals(value)){
					insertSQLBuilder.append("null");
				}else{
				   insertSQLBuilder.append("'").append(value).append("' ");
				}
			}
		}
		
		
		insertSQLBuilder.append(");");

		//System.out.println(insertSQLBuilder.toString());
		return insertSQLBuilder.toString();
	}

	/**
	 * 根据表名数据库元数据和条件生成查询语句
	 * 
	 * @param metadata
	 * @param tableName
	 * @param condition
	 *            查询条件 （eg：where ID=1）
	 * @return
	 * @throws SQLException
	 */
	private static String generateQuerySQL(DatabaseMetaData metadata, String tableName, String condition) throws SQLException {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select ");

		queryBuilder.append(getColumnsString(metadata, tableName));

		queryBuilder.append(" from ").append(tableName).append(" ").append(condition);

		return queryBuilder.toString();
	}

	private static String getColumnsString(DatabaseMetaData metadata, String tableName) throws SQLException {
		String[] cols = getColumnsArray(metadata, tableName);
		return StringUtils.join(cols, ", ");
	}

	/**
	 * 根据表名和数据库元数据获得所有列的List
	 * 
	 * @param metadata
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private static List<String> getColumnsList(DatabaseMetaData metadata, String tableName) throws SQLException {
		List<String> columns = new ArrayList<String>();

		ResultSet rs = metadata.getColumns(null, null, tableName, "%");
		while (rs.next()) {
			columns.add(rs.getString("COLUMN_NAME"));
		}

		return columns;
	}

	/**
	 * 根据表名和数据库元数据获得所有列的数组。
	 * 
	 * @param metadata
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private static String[] getColumnsArray(DatabaseMetaData metadata, String tableName) throws SQLException {
		List<String> list = getColumnsList(metadata, tableName);
		return list.toArray(new String[list.size()]);
	}
	

	 public static String getConditionSql(String tableName,String openDate,String endDate){
		   
		   if( tableName == null){
			    return "";   
		   }
		   tableName = tableName.toLowerCase(); 
		   String retStr = "";
		   
//		   String dateStr = DateUtils.toString(new Date());
		   String prifixSql = " where  ";
			StringBuffer sb = new StringBuffer("BETWEEN str_to_date('"); 
			sb.append(openDate + "','%Y-%m-%d %H:%i:%s')  AND str_to_date('");
			sb.append(endDate + "','%Y-%m-%d %H:%i:%s') ");
			String sufixSql = sb.toString();	   
				  
		   switch (tableName) {
		   case "t_table":
				//retStr = prifixSql + " modifytime   " + sufixSql;
				break;
			case "t_tablearea":
				//retStr = prifixSql + " modifytime   " + sufixSql;
				break;
			case "t_order":
				retStr = prifixSql + " begintime   " + sufixSql;
				break;
			case "t_order_detail":
				retStr = prifixSql + " begintime   " + sufixSql;
				break;
			case "t_order_detail_discard":
				retStr = prifixSql + " begintime   " + sufixSql;
				break;
			case "t_settlement":
				retStr = prifixSql + " inserttime   " + sufixSql;
				break;
			case "t_settlement_history":
				retStr = prifixSql + " inserttime   " + sufixSql;
				break;
		    case "t_settlement_detail":
		    	retStr = prifixSql + " inserttime   " + sufixSql;
				break;
		    case "t_settlement_detail_history":
		    	retStr = prifixSql + " inserttime   " + sufixSql;
				break;
		    case "t_biz_log":
		    	retStr = prifixSql + " inserttime   " + sufixSql;
				break;
		    case "t_order_member":	
		    	retStr = prifixSql + " ordertime   " + sufixSql;
		    	break;
		    case "t_branch_biz_log":
		    	retStr = prifixSql + " inserttime   " + sufixSql;
				break;
				
		    case "t_nodeclass":
		    	retStr = prifixSql + " pritertime   " + sufixSql;
				break;
		    case "t_order_history":
		    	retStr = prifixSql + " begintime   " + sufixSql;
				break;
		    case "t_order_detail_history":
		    	retStr = prifixSql + " begintime   " + sufixSql;
				break;
				
			 default:
				 retStr = "";
					break;
				}
		   return retStr;
	   }
	
	 
	 public static String getIdCondition(String tableName){
		   
		   if( tableName == null){
			    return "";   
		   }
		   tableName = tableName.toLowerCase(); 
		   String retStr = "";
				  
		   switch (tableName) {
		  case "t_table":
				retStr = "tableid";
				break;
			case "t_tablearea":
				retStr = "areaid";
					break;
			case "t_order":
				retStr = "orderid";
				break;
			case "t_order_detail":
				retStr = "orderdetailid";
				break;
			case "t_order_detail_discard":
				retStr = "orderdetailid";
				break;
			case "t_settlement":
				retStr = "settledId";
				break;
			case "t_settlement_history":
				retStr = "settledid";
				break;
		    case "t_settlement_detail":
		    	retStr = "sdetailid";
				break;
		    case "t_settlement_detail_history":
		    	retStr = "sdetailid";
				break;
		    case "t_biz_log":
		    	retStr = "id";
				break;
		    case "t_order_member":	
		    	retStr = "id";
		    	break;
		    case "t_branch_biz_log":	
		    	retStr = "id";
		    	break;
		    case "t_nodeclass":	
		    	retStr = "classNo";
		    	break;
		    case "t_order_history":	
		    	retStr = "orderid";
		    	break;
		    case "t_order_detail_history":	
		    	retStr = "orderdetailid";
		    	break;
			 default:
				 retStr = "";
					break;
				}
 
		   return retStr;
	   }

//	private static void loadDirver() {
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
	}