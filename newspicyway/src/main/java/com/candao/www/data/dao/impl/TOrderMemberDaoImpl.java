package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TOrderMemberDao;
import com.candao.www.data.model.TOrderMember;

@Repository
public class TOrderMemberDaoImpl implements TOrderMemberDao {

	@Autowired
	private DaoSupport dao;
	 
	@Override
	public TOrderMember get(String orderId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", orderId);
		return dao.get(PREFIX + ".get", params);
	}

	@Override
	public int insert(TOrderMember tOrderMember) {
		return dao.insert(PREFIX + ".insert", tOrderMember);
	}

	@Override
	public int update(TOrderMember tOrderMember) {
		return dao.update(PREFIX + ".update", tOrderMember);
	}

	@Override
	public int updateValid(String orderId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", orderId);
		return dao.delete(PREFIX + ".updateValid", params);
	}

}
