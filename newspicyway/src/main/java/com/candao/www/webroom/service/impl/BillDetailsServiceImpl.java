package com.candao.www.webroom.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.data.dao.TbBillDetailsDao;
import com.candao.www.webroom.service.BillDetailsService;
@Service
public class BillDetailsServiceImpl implements BillDetailsService {
	@Autowired
	private TbBillDetailsDao tbBillDetailsDao;
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params,
			int current, int pagesize) {
		// TODO Auto-generated method stub
		return  tbBillDetailsDao.page(params, current, pagesize);
	}

}
 