package com.candao.www.timedtask;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.BranchDataSynDao;
import com.candao.www.data.dao.TSynSqlMapper;
import com.candao.www.data.dao.TbBranchDao;
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
public class SqlDataSyn   {
 
	public static final Logger logger = LoggerFactory.getLogger(SqlDataSyn.class);
	
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
	 
	    @Autowired
     TSynSqlMapper  tSynSqlMapper;
    
   
   public void sqlDataSyn(){
	   
	   synchronized(this){
		   int count = tSynSqlMapper.copyDataFromTemp();
		   if(count > 0){
			   
			   Map<String, Object> mapParam = new HashMap<String, Object>();
		       mapParam.put("id", null);
		       mapParam.put("result", null);
		       tSynSqlMapper.synData(mapParam);
		       logger.info("---->同步数据结束");

		   }
	   }
   }
   
}
