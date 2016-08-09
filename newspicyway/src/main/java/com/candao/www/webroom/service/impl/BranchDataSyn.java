//package com.candao.www.webroom.service.impl;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import javax.jms.Destination;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.stereotype.Service;
//import com.candao.common.utils.DateUtils;
//import com.candao.common.utils.PropertiesUtils;
//import com.candao.www.data.dao.BranchDataSynDao;
//import com.candao.www.webroom.model.SynSqlObject;
//
//
///**
// * 
// *  <pre>
// * 
// * Copyright : Copyright  Pandoranews 2015 ,Inc. All right
// * Company : 上海餐道互联网金融服务有限公司
// * </pre>
// * @author  tom_zhao
// * @version 1.0
// * @date 2015年6月1日 下午9:03:43
// * @history
// *
// */
//@Service
//public class BranchDataSyn   {
// 
//	 @Autowired
//	 BranchDataSynDao  branchDataSynDao;
//	 
//	 @Autowired
//	 private JmsTemplate jmsTemplate;
//	 
//	@Autowired
//	@Qualifier("branchDataQueue")
//	private Destination destination;
//	 
//   public void synBranchData(){
//	   int bizFlag = branchDataSynDao.checkBizData();
//	   //表示已经结业或未开业状态
//	   if(bizFlag == 0){
//		   //只有结业状态菜同步数据
//		   bizFlag = branchDataSynDao.checkSynDataFinish();
//		   //bizFlag = 1 表示 还没有同步
//		   SynSqlObject synSqlObject = null;
//		   String sql_file_path = PropertiesUtils.getValue("sql_file_path");
//		   String branchId = PropertiesUtils.getValue("current_branch_id");
//		   String dateStr =  DateUtils.toString(new Date());
//		   String directoryPath  = sql_file_path + "/" +dateStr +"/"+"branchsql_"+branchId+".log";
//		 
//		   if(bizFlag >= 1){
//				try {
//					  
//					   synSqlObject =  wrapSqlObjec(branchId,directoryPath);
//					   jmsTemplate.convertAndSend(destination, synSqlObject);
//					   
//					   branchDataSynDao.transferToHistory();
//					   Map<String, String> mapValue = new HashMap<String, String>();
//					   mapValue.put("branchid", branchId);
//					   mapValue.put("datapath",directoryPath);
//					   
//					   branchDataSynDao.insertSynRecord(mapValue);
//				} catch (Exception e) {
////					jmsTemplate.convertAndSend(destination, synSqlObject);
//				}
//			  
//		   }else{
//			   Map<String, String> mapValue = new HashMap<String, String>();
//			   mapValue.put("branchid", branchId);
//			   mapValue.put("datapath",directoryPath);
//			   branchDataSynDao.insertSynRecord(mapValue);
//		   }
//	   } 
//   }
//   
//   public SynSqlObject  wrapSqlObjec(String branchId,String directoryPath){
//	   SynSqlObject synSqlObject = new SynSqlObject();
//	   
//	   try {
//			 
//			   StringBuffer sb= new StringBuffer("");
//		       FileReader reader = new FileReader(directoryPath);
//		       BufferedReader br = new BufferedReader(reader);
//		       String str = null;
//		       while((str = br.readLine()) != null) {
//		             sb.append(str+"/n");
//	
//		       }
//		       synSqlObject.setBranchid(branchId);
//		       synSqlObject.setGenerattime(DateUtils.dateToString(new Date()));
//		       synSqlObject.setSqltext(sb.toString());
//		       synSqlObject.setStatus(0);
//		       
//		       
//		       br.close();
//		       reader.close();
//		       
//		       return synSqlObject;
//		       
//		} catch (Exception e) {
//			 e.printStackTrace();
//			 return null;
//		}
//   }
//}
