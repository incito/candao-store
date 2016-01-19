package com.candao.www.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TworklogMapper;
import com.candao.www.data.model.Tworklog;

@Repository
public class TbWorkLogDaoImpl implements TworklogMapper{

    @Autowired
    private DaoSupport dao;
    
	@Override
	public int insert(Tworklog record) {
		return dao.insert(PREFIX + ".insert", record);
	}

}
