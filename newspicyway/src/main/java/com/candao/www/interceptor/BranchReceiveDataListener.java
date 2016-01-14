package com.candao.www.interceptor;

//import groovy.transform.Synchronized;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TSynSqlMapper;
import com.candao.www.timedtask.BranchDataSyn;
import com.candao.www.webroom.model.SynSqlObject;

/**
 * 各个分店接收到总店的数据
 *  <pre>
 * 
 * Copyright : Copyright  Pandoranews 2015 ,Inc. All right
 * Company : 上海餐道互联网金融服务有限公司
 * </pre>
 * @author  tom_zhao
 * @version 1.0
 * @date 2015年5月20日 下午8:11:41
 * @history
 *
 */
@Service
public class BranchReceiveDataListener {
	
	
	   public static final Logger logger = Logger.getLogger(BranchReceiveDataListener.class);
	
    @Autowired
    TSynSqlMapper  tSynSqlMapper;
    
    @Autowired
    BranchDataSyn branchDataSyn;
    
//	@Autowired
//	JmsTemplate jmsTemplate;
	 
    
//	@Autowired
//	@Qualifier("branchDataQueue")
//	private Destination destination;
	
//	private SynSqlObject  synSqlObject;

	/**
	 * 
	 * @param message
	 * @return
	 */
 
	public String receiveMessage(SynSqlObject  synSqlObject) {
		logger.info("BranchReceiveDataListener receive message");
//        this.synSqlObject = synSqlObject;
		 try{
//			  String sqlText  = synSqlObject.getSql();
			  String branchId = synSqlObject.getBranchid();
//			  String generateTime = synSqlObject.getGeneratetime();
			  //insert into t_syn_sql
			   
			   if(synSqlObject != null ){
				    if("0".equals(synSqlObject.getFlag())){
				    	//总店下发的sql 同步文件
				    	 synSqlObject.setStatus(0);
						   tSynSqlMapper.insert(synSqlObject);
						   String result = "0";
//				   	       Map<String, Object> mapParam = new HashMap<String, Object>();
//				   	       mapParam.put("id", synSqlObject.getId());
//				   	       mapParam.put("result", result);
//				   	       tSynSqlMapper.synData(mapParam);
				    }else   if("2".equals(synSqlObject.getFlag())){
				    	//总店反馈的同步结果 每天结业数据的反馈结果
				    	int status = synSqlObject.getStatus();
				    	if(status == 1){
				    		//传送失败 需要把同步的表数据更新 失败次数，下次定时任务会再执行一次
				    		branchDataSyn.reSynData();
				    	}else{
				    		//把同步的记录表删除，表示当天该店已经同步完成
				    		branchDataSyn.deleteRecord();
				    	}
				    }
			   }
	   	       
		} catch (Exception e) {
//			jmsTemplate.convertAndSend(destination, synSqlObject);
			e.printStackTrace();
		} finally {

		}
		return null;
	}
 
   
}
