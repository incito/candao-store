package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.AnimalDao;
import com.candao.www.webroom.service.AnimalService;


@Service
public class AnimalServiceImpl implements AnimalService{

	@Autowired
	private AnimalDao animalDao;
	
	
	@Override
	public List<Map<String, Object>> allanimals() {
		return animalDao.allanimals();
	}

}
