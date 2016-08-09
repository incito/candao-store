package com.candao.www.interceptor;

//import groovy.transform.Synchronized;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TSynSqlMapper;
import com.candao.www.webroom.model.SynSqlObject;

/**
 * 接收分店的数据到总店
 *  <pre>
 * 
 * Copyright : Copyright  Pandoranews 2015 ,Inc. All right
 * Company : 上海餐道互联网金融服务有限公司
 * </pre>
 * @author  tom_zhao
 * @version 1.0
 * @date 2015年5月20日 下午8:12:05
 * @history
 *
 */
@Service
public class BranchDataListener {
	
	
    @Autowired
    TSynSqlMapper  tSynSqlMapper;

	/**
	 * 
	 * @param message
	 * @return
	 */
 
	public String receiveMessage(SynSqlObject  synSqlObject) {
		System.out.println("BranchDataListener receive message");

		 try{
			  String branchId = synSqlObject.getBranchid();
			  tSynSqlMapper.insert(synSqlObject);
			   String result = "1";
	   	       Map<String, Object> mapParam = new HashMap<String, Object>();
	   	       mapParam.put("branchid", branchId);
	   	       mapParam.put("result", result);
	   	       tSynSqlMapper.synData(mapParam);
	   	       result = String.valueOf(mapParam.get("result"));
	   	       
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
           
		}
		return null;
	}
 
   
}
