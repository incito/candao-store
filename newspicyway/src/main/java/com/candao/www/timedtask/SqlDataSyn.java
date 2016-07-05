package com.candao.www.timedtask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.dao.SynDataTools;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.BranchDataSynDao;
import com.candao.www.data.dao.TSynSqlMapper;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.data.dao.TtemplateDishUnitlDao;
import com.candao.www.data.model.TtemplateDishUnit;
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
public class SqlDataSyn   {
 
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
    
	 @Autowired
	 private TtemplateDishUnitlDao ttemplateDishUnitlDao;
   
   public void sqlDataSyn(){
	   
	   synchronized(this){
		   int count = tSynSqlMapper.copyDataFromTemp();
		   if(count > 0){
//			       备份已经估清的菜品
			   List<TtemplateDishUnit> dishUnits = ttemplateDishUnitlDao.getTtemplateDishUnitByStatus();
			   
			   Map<String, Object> mapParam = new HashMap<String, Object>();
		       mapParam.put("id", null);
		       mapParam.put("result", null);
		       tSynSqlMapper.synData(mapParam);

//		                  把已经估清的菜品重新估清
		       StringBuilder dishBuilder = new StringBuilder();
		       for(TtemplateDishUnit dishUnit : dishUnits){
		        	String dishid = dishUnit.getDishid();
		        	dishBuilder.append("'").append(dishid).append("'").append(",");
		       }
		       if(dishBuilder.length() > 0){
		    	   String dishIds = dishBuilder.substring(0, dishBuilder.length()-1);
		    	   ttemplateDishUnitlDao.updateStatus(dishIds);
		       }
		   }
	   }
   }
   
}
