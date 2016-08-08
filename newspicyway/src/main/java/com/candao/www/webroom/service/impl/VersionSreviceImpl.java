package com.candao.www.webroom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TbVersionDao;
import com.candao.www.data.model.TbVersion;
import com.candao.www.webroom.service.VersionService;
@Service
public class VersionSreviceImpl implements VersionService {
  @Autowired
  private TbVersionDao tbVersionDao;
	@Override
	public List<TbVersion> findAll() {
		
		return tbVersionDao.find(null);
	}

	@Override
	public TbVersion findOne(int type) {
		return tbVersionDao.get(type);
	}

	@Override
	public boolean update(TbVersion tbVersion) {
		return tbVersionDao.update(tbVersion)>0;
	}

}
