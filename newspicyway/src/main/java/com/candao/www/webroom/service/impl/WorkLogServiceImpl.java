package com.candao.www.webroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TworklogMapper;
import com.candao.www.data.model.Tworklog;
import com.candao.www.webroom.service.WorkLogService;

@Service
public class WorkLogServiceImpl implements WorkLogService {
	
	@Autowired
	private TworklogMapper tworklogMapper;

	@Override
	public int saveLog(Tworklog workLog) {
		// TODO Auto-generated method stub
		return tworklogMapper.insert(workLog);
	}

}
