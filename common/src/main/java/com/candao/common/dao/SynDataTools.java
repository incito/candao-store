package com.candao.common.dao;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
 

public class SynDataTools {

   Connection  connection = null;
	public SynDataTools( Connection  connection){
		this.connection = connection;
	}

	/**
	 * 根据id生成导出数据的SQL
	 * 
	 * @param id
	 * @return
	 */
	public   String generateSQLList(String tableName,String sqlCondition) {
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
				retStr = prifixSql + " modifytime   " + sufixSql;
				break;
			case "t_tablearea":
				retStr = prifixSql + " modifytime   " + sufixSql;
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
				retStr = "settledid";
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
		    	retStr = "id";
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