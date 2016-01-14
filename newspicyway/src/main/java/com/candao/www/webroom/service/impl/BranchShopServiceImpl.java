package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.data.dao.TbranchshopDao;
import com.candao.www.data.model.Tbranchshop;
import com.candao.www.webroom.service.BranchShopService;
@Service
public class BranchShopServiceImpl implements BranchShopService {

	@Autowired
	private TbranchshopDao tbranchshopDao;

	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params,
			int current, int pagesize) {
		// TODO Auto-generated method stub
		return tbranchshopDao.page(params, current, pagesize);
	}

	@Override
	public List<Tbranchshop> getTbParternerList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return tbranchshopDao.find(map);
	}

	@Override
	public boolean save(Tbranchshop tbranchshop) {
		// TODO Auto-generated method stub
		return tbranchshopDao.insert(tbranchshop)>0;
	}

	@Override
	public boolean update(Tbranchshop tbranchshop) {
		// TODO Auto-generated method stub
		return tbranchshopDao.update(tbranchshop)>0;
	}

	@Override
	public Tbranchshop findById(String id) {
		// TODO Auto-generated method stub
		return tbranchshopDao.get(id);
	}

	@Override
	public boolean deleteById(String id) {
		// TODO Auto-generated method stub
		return tbranchshopDao.delete(id)>0;
	}
	
	 public List<Tbranchshop> getAllListList(){
		 return tbranchshopDao.findAll();
	 }

}
