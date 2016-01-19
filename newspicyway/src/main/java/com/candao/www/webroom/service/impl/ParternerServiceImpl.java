package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.data.dao.TbparternerDao;
import com.candao.www.data.model.TbParterner;
import com.candao.www.webroom.service.ParternerService;
@Service
public class ParternerServiceImpl implements ParternerService {
	@Autowired
	private TbparternerDao tbparternerDao;

	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params,
			int current, int pagesize) {
		// TODO Auto-generated method stub
		return tbparternerDao.page(params, current, pagesize);
	}

	@Override
	public List<TbParterner> getTbParternerList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return tbparternerDao.find(map);
	}

	@Override
	public boolean save(TbParterner tbParterner) {
		// TODO Auto-generated method stub
		return tbparternerDao.insert(tbParterner)>0;
	}

	@Override
	public boolean update(TbParterner tbParterner) {
		// TODO Auto-generated method stub
		return tbparternerDao.update(tbParterner)>0;
	}

	@Override
	public TbParterner findById(String id) {
		// TODO Auto-generated method stub
		return tbparternerDao.get(id);
	}

	@Override
	public boolean deleteById(String id) {
		// TODO Auto-generated method stub
		return tbparternerDao.delete(id)>0;
	}

}
