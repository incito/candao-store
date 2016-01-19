package com.candao.www.webroom.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.common.utils.MD5;
import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.data.model.TbDataDictionary;
import com.candao.www.webroom.service.DataDictionaryService;
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {
@Autowired
  private TbDataDictionaryDao tbdatadictionaryDao;
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
		return tbdatadictionaryDao.page(params, current, pagesize);
	}
	@Override
	public boolean save(TbDataDictionary tbDataDictionary) {
		return tbdatadictionaryDao.insert(tbDataDictionary)>0;
	}
	@Override
	public TbDataDictionary findById(String id) {
		return tbdatadictionaryDao.get(id);
	}
	@Override
	public boolean update(TbDataDictionary tbDataDictionary) {
		return tbdatadictionaryDao.update(tbDataDictionary)>0;
	}
	@Override
	public boolean deleteById(String id) {
		return tbdatadictionaryDao.delete(id)>0;
	}
	@Override
	public List<Map<String, Object>> getDataDictionaryTag() {
		return tbdatadictionaryDao.getDataDictionaryTag();
	}
	@Override
	public List<Map<String, Object>> getTypeandTypename() {
		// TODO Auto-generated method stub
		return tbdatadictionaryDao.getTypeandTypename(); 
	}
	@Override
	public List<Map<String, Object>> getDatasByType(String type) {
		// TODO Auto-generated method stub
		return tbdatadictionaryDao.getDatasByType(type);
	}
	
	@Override
	  public String find(String dictKey){
		  TbDataDictionary dataDictionary = tbdatadictionaryDao.get(dictKey);
		  if(dataDictionary == null ){
			  return "";
		  }
		  return MD5.md5(dataDictionary.getItemDesc());
	  }
	@Override
	public boolean delDishTasteService(String dishTasteId) {
		// TODO Auto-generated method stub
		return tbdatadictionaryDao.delDishTasteDao(dishTasteId)>0;
	}
	@Override
	public List<Map<String, Object>> findByParams(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return tbdatadictionaryDao.find(map);
	}

}
