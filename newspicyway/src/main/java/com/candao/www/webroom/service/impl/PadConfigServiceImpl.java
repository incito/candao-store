package com.candao.www.webroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.PadConfigDao;
import com.candao.www.webroom.model.PadConfig;
import com.candao.www.webroom.service.PadConfigService;

@Service
public class PadConfigServiceImpl  implements PadConfigService{

	@Autowired
	private PadConfigDao     padConfigDao;

	@Override
	public int saveorupdate(PadConfig padConfig) {
		int count =padConfigDao.selectisExsit();
		if(count>0){
			return padConfigDao.update(padConfig);
		}else{
			return padConfigDao.insert(padConfig);
		}
	}

	@Override
	public PadConfig getconfiginfos() {
		
		return padConfigDao.getconfiginfos();
	}
	
}
