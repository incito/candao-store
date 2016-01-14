package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TbInstrumentDao;
import com.candao.www.data.model.TbInstrument;
import com.candao.www.webroom.service.InstrumentService;
@Service
public class InstrumentServiceImpl implements InstrumentService{
	@Autowired
    private TbInstrumentDao tbInstrumentDao;
	@Override
	public boolean save(TbInstrument tbInstrument) {
		return tbInstrumentDao.insert(tbInstrument)>0;
	}

	@Override
	public boolean update(TbInstrument tbInstrument) {
		return tbInstrumentDao.update(tbInstrument)>0;
	}

	@Override
	public TbInstrument findByParams(Map<String, Object> params) {
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
	public int updateStatus(TbInstrument tbInstrument) {
		// TODO Auto-generated method stub
		return 0;
	}

}
