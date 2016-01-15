package com.candao.www.webroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TJsonRecordDao;
import com.candao.www.data.model.TJsonRecord;
import com.candao.www.webroom.service.JsonRecordService;

@Service
public class JsonRecordServiceImpl implements JsonRecordService {

	@Autowired
	private TJsonRecordDao tJsonRecordDao;
	
	public int insertJsonRecord(TJsonRecord jsonRecord) {
		return tJsonRecordDao.insertJsonRecord(jsonRecord);
	}

	
}
