package com.candao.www.webroom.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.model.SynSqlObject;
import com.candao.www.webroom.zookeeper.ZkDqQueuer;


public class BranchServiceThread  {
	
	private static final Logger logger = LoggerFactory.getLogger(BranchServiceThread.class);
	
	   
	   ZkDqQueuer  service;
	   SynSqlObject  synSqlObject;
	   
	   BranchServiceThread(){
		   	
	   }
	  public  BranchServiceThread(SynSqlObject  synSqlObject,int flag ){
		   this.synSqlObject = synSqlObject;
		   try {
			   String sql_file_path = PropertiesUtils.getValue("sql_file_path");
			   String branchId = PropertiesUtils.getValue("current_branch_id");
			   String dateStr =  DateUtils.toString(new Date());
			   String directoryPath  = sql_file_path + "/" +dateStr +"/";
			   File file = new File(directoryPath);
				 if(!file.exists()){
					 file.mkdirs();
				 }
				 file = new File(directoryPath+ "branchsql_"+branchId+".log");
				 if(!file.exists()){
					 file.createNewFile();
				 }
		        FileWriter fw = new FileWriter(file,true); //设置成true就是追加
		        fw.write(synSqlObject.getSqltext() + "\n");
 
		        fw.close();
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
	   }
	  
}