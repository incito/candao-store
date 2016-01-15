package com.candao.www.timedtask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.dao.SynDataTools;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.BranchDataSynDao;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.webroom.model.SynSqlObject;
import com.candao.www.webroom.service.BranchProducerService;
import com.candao.www.webroom.service.BranchShopService;


/**
 * 
 *  <pre>
 * 
 * Copyright : Copyright  Pandoranews 2015 ,Inc. All right
 * Company : 上海餐道互联网金融服务有限公司
 * </pre>
 * @author  tom_zhao
 * @version 1.0
 * @date 2015年6月1日 下午9:03:43
 * @history
 *
 */
@Service
public class BranchDataSyn   {
 
	 @Autowired
	 BranchDataSynDao  branchDataSynDao;
	 
	 @Autowired
	 TbBranchDao  branchDao;
	 
//	 @Autowired
//	 ZkDqQueuer zkDqQueuer;
	 @Autowired
	 BranchShopService  branchShopService;
	 
	 @Autowired
	 BranchProducerService  service;
	 
   public void synBranchData(){
	   synData();
	   updateSynRecord();
   }
   
   public void reSynData(){
	   
	   int bizFlag = branchDataSynDao.checkSynDataFinish();
	   if(bizFlag > 0){
		   synData();
	   }
	   
   }
   
   public void synData(){
	   int bizFlag = branchDataSynDao.checkBizData();
	  // bizFlag = 0;
	   //表示已经结业或未开业状态
	   if(bizFlag == 0){
		   //只有结业状态菜同步数据
//		   bizFlag = branchDataSynDao.checkSynDataFinish();
		   //bizFlag = 1 表示 还没有同步
		   String branchId = PropertiesUtils.getValue("current_branch_id");
		   if(branchId != null){
				try {
					
					  Map<String, String> mapValue = new  HashMap<String, String>();
					  mapValue.put("branchid", PropertiesUtils.getValue("current_branch_id"));
					  mapValue.put("datapath", "暂时不用");
				      branchDataSynDao.insertSynRecord(mapValue);
				      
					String need_syn_tables = PropertiesUtils.getValue("need_syn_tables");
					String[] tables = need_syn_tables.split(",");
					String[] tablesCopy = new String[tables.length -1] ;
					String synTable = null;
					if(tables != null){
						System.arraycopy(tables, 0, tablesCopy, 0, tables.length -1);
						synTable = tables[tables.length -1 ];
					}
					Map  branchinfo = branchDao.getBranchInfo();
					//获取开业日期 和结业日期 
					Map<String,String> bizMap = branchDataSynDao.getBizDate();
					String openDate = bizMap.get("opendate");
					String endDate = bizMap.get("enddate");
					
					int sequenceNo = 0;
					for(String table : tables){
						String listsql = branchDataSynDao.getSynSql(table, SynDataTools.getConditionSql(table,openDate,endDate));
						if(listsql != null && !"".equals(listsql)){
							SynSqlObject synSqlObject = new SynSqlObject();
							synSqlObject.setId(UUID.randomUUID().toString());
							synSqlObject.setBranchid(branchId);
							synSqlObject.setTenantid((String)branchinfo.get("tenantid"));
							synSqlObject.setSql(listsql);
							synSqlObject.setFlag("1");
							synSqlObject.setSequenceNo(""+ sequenceNo ++ );
							service.sendMessage(synSqlObject);
						}
				 
					}  
					
//					 branchDataSynDao.updateBizLog();
					 
					 //同步分店 状态
					 String listsql = branchDataSynDao.getSynSql(synTable, SynDataTools.getConditionSql(synTable,openDate,endDate));
						
						if(listsql != null && !"".equals(listsql)){
							SynSqlObject synSqlObject = new SynSqlObject();
							synSqlObject.setId(UUID.randomUUID().toString());
							synSqlObject.setBranchid(branchId);
							synSqlObject.setTenantid((String)branchinfo.get("tenantid"));
							synSqlObject.setSql(listsql);
							synSqlObject.setFlag("1");
							synSqlObject.setSequenceNo(""+ sequenceNo ++ );
							service.sendMessage(synSqlObject);
						}
					
				//      branchDataSynDao.transferToHistory();
				      
				//      branchDataSynDao.deleteSynRecord();
				      
				      
 
				} catch (Exception e) {
					e.printStackTrace();
				}
			  
		   }else{
 
		   }
	   } 
   }
   private void updateSynRecord(){
	   branchDataSynDao.updateSynRecord(null);
   }
   
   public void deleteRecord(){
		      branchDataSynDao.transferToHistory();
		      branchDataSynDao.deleteSynRecord();
   }
   
  
   
   public SynSqlObject  wrapSqlObjec(String branchId,String directoryPath){
	   SynSqlObject synSqlObject = new SynSqlObject();
	   
	   try {
			 
			   StringBuffer sb= new StringBuffer("");
		       FileReader reader = new FileReader(directoryPath);
		       BufferedReader br = new BufferedReader(reader);
		       String str = null;
		       while((str = br.readLine()) != null) {
		             sb.append(str+"/n");
	
		       }
		       synSqlObject.setBranchid(branchId);
		       synSqlObject.setGenerattime(DateUtils.dateToString(new Date()));
		       synSqlObject.setSqltext(sb.toString());
		       synSqlObject.setStatus(0);
		       
		       
		       br.close();
		       reader.close();
		       
		       return synSqlObject;
		       
		} catch (Exception e) {
			 e.printStackTrace();
			 return null;
		}
   }
}
