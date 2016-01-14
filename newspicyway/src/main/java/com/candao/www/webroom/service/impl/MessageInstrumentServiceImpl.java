package com.candao.www.webroom.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TbMessageInstrumentDao;
import com.candao.www.data.model.TbMessageInstrument;
import com.candao.www.webroom.service.MessageInstrumentService;
@Service
public class MessageInstrumentServiceImpl implements MessageInstrumentService{
	@Autowired
    private TbMessageInstrumentDao tbMessageInstrumentDao;
	@Override
	public boolean save(TbMessageInstrument tbMessageInstrument) {
		return tbMessageInstrumentDao.insert(tbMessageInstrument)>0;
	}

	@Override
	public boolean update(TbMessageInstrument tbMessageInstrument) {
		return tbMessageInstrumentDao.update(tbMessageInstrument)>0;
	}

	@Override
	public TbMessageInstrument findByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, Object>> find(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateStatus(TbMessageInstrument tbMessageInstrument) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TbMessageInstrument get(String id) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", id);
		return tbMessageInstrumentDao.get(map);
	}

}
