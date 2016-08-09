package com.candao.www.data.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.CallWaiterDao;
import com.candao.www.data.model.TbMessage;
@Repository
public class CallWaiterDaoImpl implements CallWaiterDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public int saveCallInfo(TbMessage message) {
		return daoSupport.insert(PREFIX + ".saveMessage", message);
	}

	@Override
	public int updateCallInfo(TbMessage message) {
		return daoSupport.insert(PREFIX + ".updateMessage", message);
	}

	@Override
	public TbMessage queryCallInfo(Map<String, String> paramMap) {
		return daoSupport.get(PREFIX + ".queryMessageInfo", paramMap);
	}
	
	@Override
	public int updateCallInfoStatus(String orderid) {
		return daoSupport.insert(PREFIX + ".updateCallInfoStatus", orderid);
	}
}
