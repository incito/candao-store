package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TbOpenBizLogDao;
import com.candao.www.data.model.TbOpenBizLog;

/**
 * 开业日志管理
 *  <pre>
 * 
 * Copyright : Copyright  Pandoranews 2014 ,Inc. All right
 * Company : 凯盈资讯科技有限公司
 * </pre>
 * @author  zhao
 * @version 1.0
 * @date 2014年12月16日 下午6:19:20
 * @history
 *
 */

@Repository
public class TbOpenBizLogDaoImpl implements TbOpenBizLogDao{

	
	 @Autowired
	 private DaoSupport dao;
	 
	@Override
	public TbOpenBizLog findOpenBizDate() {
		return dao.get(PREFIX + ".get",null);
	}

	@Override
	public int insertOpenBizLog(TbOpenBizLog bizLog) {
		return dao.insert(PREFIX + ".insert", bizLog);
	}

	@Override
	public int deleteOpenBizLog() {
		return dao.delete(PREFIX + ".insert", null);
	}

}
