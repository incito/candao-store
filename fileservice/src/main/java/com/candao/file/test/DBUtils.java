package com.candao.file.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {

	
    static String driver = "com.mysql.jdbc.Driver";
    static String url = "jdbc:mysql://192.168.102.11:3306/ky?characterEncoding=utf-8";
    static String user = "root"; 
    static String password = "root";
    
	public  static void update(String filename){
		
		 Connection conn = getConn();
		try {
		   String sql = "{call updates(?)}";
           CallableStatement cs = (CallableStatement)conn.prepareCall(sql);
           cs.setString(1, filename);   
           cs.execute();
	       conn.close();
		 } catch(SQLException e) {
	          e.printStackTrace();
	         } catch(Exception e) {
	          e.printStackTrace();
	         } 
	}
	
	 
	
	 static Connection getConn(){
 
         try { 
          Class.forName(driver);
          Connection conn = DriverManager.getConnection(url,user,password);
          return conn;
         } catch(ClassNotFoundException e) {
          e.printStackTrace();
         } catch(SQLException e) {
          e.printStackTrace();
         } catch(Exception e) {
          e.printStackTrace();
         } 
         
         return null;
	 }
}
