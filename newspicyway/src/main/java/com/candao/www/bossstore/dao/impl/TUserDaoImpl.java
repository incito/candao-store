package com.candao.www.bossstore.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.bossstore.dao.TUserDao;
@Repository
public class TUserDaoImpl implements TUserDao{
	@Autowired
	private DaoSupport daoSupport;
	@Override
	public Map<String,String> getOpenUser(Map<String,String> params) {
		List<Map<String,String>> list = daoSupport.find(PREFIX+".getOpenUser",params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
