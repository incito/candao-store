package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.AnimalDao;

@Repository
public class AnimalDaoImpl implements AnimalDao{

	
	@Autowired
	private DaoSupport dao;
	
	@Override
	public List<Map<String, Object>> allanimals() {
		
		 return dao.find(PREFIX+".allanimals", null);
	}

}
