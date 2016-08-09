package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.data.dao.TbPicturesDao;
import com.candao.www.data.model.TbPictures;
import com.candao.www.webroom.service.PicturesService;
@Service
public class PicturesServiceImpl implements PicturesService {
	
	@Autowired
    private TbPicturesDao picturesDao;
  
    
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
		return picturesDao.page(params, current, pagesize);
	}
	@Override
	public boolean save(TbPictures tbPictures) {
		return picturesDao.insert(tbPictures)>0;
	}
	@Override
	public TbPictures findById(String id) {
		return picturesDao.get(id);
	}
	@Override
	public boolean update(TbPictures tbPictures) {
		return picturesDao.update(tbPictures)>0;
	}
	@Override
	public boolean deleteById(String id) {
		return picturesDao.delete(id)>0;
	}
	
	@Override
	public List<Map<String, Object>>  find(Map<String, Object> params) {
		return picturesDao.find(params);
	}
}

