package com.candao.www.spring;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TbBranchDao;

/**
 * 容器启动完成后初始化门店ID
 * @author wly
 */
public class InitBranchId implements ApplicationListener<ContextRefreshedEvent>{
	
	private Logger logger = LoggerFactory.getLogger(InitBranchId.class);
	
	@Autowired
	private TbBranchDao tbBranchDao;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			if(PropertiesUtils.getBranchId() == null){
	    		Map<String, Object> map = tbBranchDao.getBranchInfo();
	    		String branchId = "";
	    		if(map == null || map.get("branchid") == null || map.get("branchid").toString().isEmpty()){
	    			logger.error("获取门店ID失败,请检查t_branch_info表(branchid字段)是否有数据");
	    			return;
	    		}
	    		branchId = (String) map.get("branchid");
	    		PropertiesUtils.setBranchId(branchId);
	    	}
		}
	}
}
