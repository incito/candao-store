package com.candao.www.timedtask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.candao.common.dao.DaoSupport;

/**
 * 启动version定时任务 
 * Created by QZM.
 * Date: 2016.11.3
 */
@Repository
public class VersionTaskService {
	
	public final static String PREFIX = VersionTaskService.class.getName();
	@Autowired
	private DaoSupport daoSupport;
	public String getVersion() {
		
		return daoSupport.findOne(PREFIX + ".getVersion");
	}
	
}
