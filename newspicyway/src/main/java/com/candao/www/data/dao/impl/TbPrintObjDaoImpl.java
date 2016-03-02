package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.www.data.dao.TbPrintObjDao;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbPrintObjDaoImpl implements TbPrintObjDao {
    @Autowired
    private DaoSupport dao;

	@Override
	public PrintObj  find(Map<String, Object> params) {
		   return dao.get(PREFIX + ".find", params);
	}

	@Override
	public <T, K, V> List<T> findDish(Map<K, V> params) {
		 return dao.find(PREFIX + ".findDish", params);
	}

	@Override
	public int update(PrintObj printObj) {
		return dao.update(PREFIX+".update",printObj);
	}

	@Override
	public int updateDish(PrintDish printDish) {
		return dao.update(PREFIX+".updateDish",printDish);
	}
	
	@Override
	public int  deleteDish(Map<String, Object> map){
		return dao.delete(PREFIX+".deleteDish", map);
	}
	
	@Override
	public int deletePrintObj(Map<String, Object> map2){
		return dao.delete(PREFIX+".deletePrintObj", map2);
	}
    
	@Override
	public List<PrintDish> findNowDish(Map<String, Object> map){
		 return dao.find(PREFIX + ".findNowDish", map);
	}
	
	@Override
	public List<PrintDish> findDishExceptNow(Map<String, Object> map){
		return dao.find(PREFIX + ".findDishExceptNow", map);
	}

	@Override
	public List<PrintDish> findDishType1(Map<String, Object> map) {
		 return dao.find(PREFIX + ".findDishType1", map);
	}

	@Override
	public List<PrintDish> findDishType(Map<String, Object> map) {
		 return dao.find(PREFIX + ".findDishType", map);
	}
	
	@Override
	public int updateDishCall(Map<String, Object> printObjMap){
		return dao.update(PREFIX+".updateDishCall",printObjMap);
	}

 
	@Override
	public List<PrintDish> findDishType3(Map<String, Object> map){
		 return dao.find(PREFIX + ".findDishType3", map);
	}

	@Override
	public int insertPrintDish(PrintDish printDish) {
		// TODO Auto-generated method stub
		return dao.update(PREFIX+".insertPrintDish",printDish);
	}

	@Override
	public List<PrintDish> findDishGroupByParentKey(Map<String, Object> map0) {
		 return dao.find(PREFIX + ".findDishGroupByParentKey", map0);
	}

	@Override
	public List<PrintDish> findDishNoPot(Map<String, Object> map0) {
		return dao.find(PREFIX + ".findDishNoPot", map0);
	}

	@Override
	public int updateDishWeight(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.update(PREFIX + ".updateDishWeight", map);
	}

 

	@Override
	public List<PrintDish> findPrinterByDishId(String dishid) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("dishid",dishid);
		return dao.find(PREFIX + ".findPrinterByDishId", map);
	}

	@Override
	public void deletefishpot(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 dao.update(PREFIX + ".deletefishpot", map);
		
	}

	@Override
	public List<PrintDish> findDishByPrimaryKey(Map<String, Object> map0) {
		return dao.find(PREFIX + ".findDishByPrimaryKey", map0);
	}

	@Override
	public void updateDishByPrimaryKey(Map<String, Object> map0) {
		 dao.update(PREFIX + ".updateDishByPrimaryKey", map0);
	}

	@Override
	public void updateDetailByPrimaryKey(Map<String, Object> map0) {
		 dao.update(PREFIX + ".updateDetailByPrimaryKey", map0);
	}

	@Override
	public List<PrintDish> findDishByObjId(Map<String, Object> map0) {
		return dao.find(PREFIX + ".findDishByObjId", map0);
	}

	@Override
	public void updateDishByObjId(Map<String, Object> map0) {
		 dao.update(PREFIX + ".updateDishByObjId", map0);
		
	}

	@Override
	public void updateDetailByObjId(Map<String, Object> map0) {
		 dao.update(PREFIX + ".updateDetailByObjId", map0);
		
	}

	@Override
	public List<PrintDish> findDishGroupBySuperKey(Map<String, Object> map) {
		 return dao.find(PREFIX + ".findDishGroupBySuperKey", map);
	}
	
	@Override
	public int findPrintTable(Map<String, Object> paramMap) {
		 return dao.get(PREFIX + ".findPrintTable", paramMap);
	}
}


