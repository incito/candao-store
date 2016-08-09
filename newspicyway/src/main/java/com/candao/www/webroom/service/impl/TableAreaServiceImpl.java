package com.candao.www.webroom.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.data.dao.TbTableAreaDao;
import com.candao.www.data.model.TbTableArea;
import com.candao.www.webroom.service.TableAreaService;
@Service
public class TableAreaServiceImpl implements TableAreaService {
@Autowired
  private TbTableAreaDao tableAreaDao;
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
		return tableAreaDao.page(params, current, pagesize);
	}
	public List<Map<String,Object>> find(Map<String, Object> params){
		return tableAreaDao.find(params);
	}
	public List<Map<String,Object>> count(String id){
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id",  id);
		
		 return tableAreaDao.getCount(params);
	 }
	/*public List<Map<String,Object>> findAll(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id",  id);
		return tbasicDataDao.find( params);
	}*/
	@Override
	public boolean save(TbTableArea tbTableArea) {
		return tableAreaDao.insert(tbTableArea)>0;
	}
	@Override
	public TbTableArea findById(String id) {
		return tableAreaDao.get(id);
	}
	@Override
	public TbTableArea tableAvaliableStatus(String id) {
		return tableAreaDao.tableAvaliableStatus(id);
	}
	@Override
	public boolean update(TbTableArea tbTableArea) {
		return tableAreaDao.update(tbTableArea)>0;
	}
	@Override
	public boolean deleteById(String id) {
		return tableAreaDao.delete(id)>0;
	}

	@Override
	public List<Map<String, Object>> getTableAreaTag() {
		return tableAreaDao.getTableTag();
	}
	@Override
	public int updateListOrder(List<TbTableArea> tbtableArea) {
		// TODO Auto-generated method stub
		return tableAreaDao.updateListOrder( tbtableArea);
	}
	@Override
	public List<Map<String, Object>> findTableCountAndAreaname() {
		// TODO Auto-generated method stub
		return tableAreaDao.findTableCountAndAreaname();
	}
}

