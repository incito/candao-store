package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
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
		return tbdatadictionaryDao.getDatasByType(type);
	}
	
	/**
	 * 获取通知消息类型
	 */
	@Override
	public List<Map<String, Object>> getNotificationDate(String type){
		List<Map<String, Object>> dictionaryMap = tbdatadictionaryDao.getDatasByType(type);
		List<Map<String, Object>> info = new ArrayList<>();
		for(Map<String, Object> map : dictionaryMap){
			Map<String, Object> dictionary = new HashMap<>();
			dictionary.put("dictid", map.get("id"));
			dictionary.put("itemid", map.get("itemid"));
			dictionary.put("itemDesc", map.get("itemDesc"));
			dictionary.put("type", map.get("type"));
			dictionary.put("item_value", map.get("itemValue"));
			info.add(dictionary);
		}
		return info;
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
	
	/**
	 * 查看开业结业时间
	 * @param type
	 * @return
	 */
	@Override
	public Map<String, Object> getOpenEndTime(String type) {
		List<Map<String, Object>> dataDictionaries = tbdatadictionaryDao.getDicListByType(type);
		Map<String, Object> timeMap = new HashMap<>();
		for(Map<String, Object> map : dataDictionaries){
			if("全天".equals(map.get("itemDesc")) || map.get("itemid").equals("2")){ //2表示全天
				timeMap.put("begintime", map.get("begintime"));
				timeMap.put("endtime", map.get("endtime"));
				timeMap.put("datetype", map.get("datetype"));
			}
		}
		return timeMap;
	}
}
