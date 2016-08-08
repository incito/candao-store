package com.candao.www.webroom.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.spring.SpringContext;
import com.candao.www.webroom.model.SynSqlObject;
import com.candao.www.webroom.zookeeper.ZkDqQueuer;


public class BranchServiceThread  {
	
	private static final Logger logger = LoggerFactory.getLogger(BranchServiceThread.class);
	
	   String  sql ;
//	   BranchProducerService  service;
	   
	   ZkDqQueuer  service;
	   Destination   branchQueue;
	   SynSqlObject  synSqlObject;
	   
	   BranchServiceThread(){
		   	
	   }
	   
//	    @Autowired
//	    BranchProducerService  producerService;
	//    
//	    @Autowired
//	    TSynSqlMapper  tSynSqlMapper;
	//    
//		@Qualifier("branchDataTopic")
//		private Destination destination;
	   
	  public  BranchServiceThread(SynSqlObject  synSqlObject ){
		   this.synSqlObject = synSqlObject;	
//		   tSynSqlMapper = SpringContext.getApplicationContext().getBean(TSynSqlMapper.class);
		   service = SpringContext.getApplicationContext().getBean(ZkDqQueuer.class);
		   new SingleProcess().start();
	   }
	 
	  public  BranchServiceThread(SynSqlObject  synSqlObject,int flag ){
		   this.synSqlObject = synSqlObject;
//		   tSynSqlMapper = SpringContext.getApplicationContext().getBean(TSynSqlMapper.class);
//		   
//		   service = SpringContext.getApplicationContext().getBean(ZkDqQueuer.class);
//		   branchQueue = (Destination)SpringContext.getApplicationContext().getBean("branchDataTopic");
		   
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
//			   tSynSqlMapper.insert(synSqlObject);
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
	   }
	  
	  class SingleProcess extends Thread{
		   @Override
		   public void run(){
			   try {
				service.sendMsg(synSqlObject);
			} catch (Exception e) {
				logger.error("-->",e);
				e.printStackTrace();
			}
		   }
	   }
}