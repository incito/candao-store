package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TReturnDishDetailDao;
import com.candao.www.webroom.service.ReturnDishDetailService;

/**
 * 退菜明细表
 * @author Administrator
 *
 */
@Service
public class ReturnDishDetailServiceImpl implements ReturnDishDetailService {

	@Autowired
	private TReturnDishDetailDao treturnDishDetailDao;

	@Override
	public List<Map<String, Object>> returnDishList(Map<String, Object> params) {
		return treturnDishDetailDao.getReturnDishList(params);
	}
}
