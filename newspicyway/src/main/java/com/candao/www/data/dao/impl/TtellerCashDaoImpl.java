package com.candao.www.data.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.www.data.dao.TtellerCashDao;
import com.candao.www.data.model.TtellerCash;
import com.candao.www.webroom.service.impl.ConvertVideoUtil;

@Repository
public class TtellerCashDaoImpl implements TtellerCashDao {

	LoggerHelper logger = LoggerFactory.getLogger(ConvertVideoUtil.class);
	@Autowired
	private DaoSupport daoSupport;
	
	@Override
	public List<TtellerCash> findUncleanPosList() {
		return daoSupport.find(PREFIX+".findUncleanPosList", null);
	}

}
