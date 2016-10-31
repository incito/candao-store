package com.candao.www.webroom.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TdishTypeDao;
import com.candao.www.data.model.Tdish;
import com.candao.www.webroom.service.ComboDishService;
import com.candao.www.webroom.service.DishService;
@Service
public class DishServiceImpl implements DishService {
	@Autowired
	private TdishDao tdishDao;
	@Autowired
	private TdishTypeDao tdishTypeDao;
	@Autowired
	private ComboDishService comboDishService;
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params,
			int current, int pagesize) {
		// TODO Auto-generated method stub
		return  tdishDao.page(params, current, pagesize);
	}
	@Override
	public Tdish save(Tdish tdish) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for(String s:tdish.getColumnid().split(",")){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", IdentifierUtils.getId().generate().toString());
			map.put("dishid", tdish.getDishid());
			map.put("columnid", s);
			list.add(map);
		}
		 if(!"".equals(tdish.getDishid())||tdish.getDishid()!=null){
			 tdishTypeDao.delDishType(tdish.getDishid());
		 }
		tdishTypeDao.addDishType(list);
		tdish.setColumnid("");
		return tdishDao.save(tdish);
	}
	@Override
	public Tdish update(Tdish tdish) {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for(String s:tdish.getColumnid().split(",")){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", IdentifierUtils.getId().generate().toString());
			map.put("dishid", tdish.getDishid());
			map.put("columnid", s);
			list.add(map);
		}
		 if(!"".equals(tdish.getDishid())||tdish.getDishid()!=null){
			 tdishTypeDao.delDishType(tdish.getDishid());
		 }
		tdishTypeDao.addDishType(list);
		return tdishDao.update(tdish);
	}
	@Override
	public Tdish findById(String id) {
		// TODO Auto-generated method stub
		Tdish dish=tdishDao.get(id);
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("dishid", id);
		List<Map<String,Object>> list=tdishDao.getdishCol(params);
		String colStr="";
		if(list!=null&&list.size()>0){
			for(Map<String,Object> map:list){
				colStr+=","+String.valueOf(map.get("columnid"));
			}
		}
		dish.setColumnid(colStr.substring(1));
		return dish;
	}
	@Override
	public boolean deleteById(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tdishDao.deletepa(params)>0;
	}
	@Override
	public List<Tdish> getDishList() {
		// TODO Auto-generated method stub
		Map<String, Object> params=new HashMap<String, Object>();
		return tdishDao.find(params);
	}
//	@Override
//	 public String getDishSet(Tdish dish){
//		 //TODO 要一次性取出 优化
//		 List<Tdish> list = new ArrayList<Tdish>();
//		 Map<String, Object> map = new HashMap<String, Object>();
//		 
//		 Tdish mainDishTdish = tdishDao.get(dish.getDishid());
//		 if(mainDishTdish != null && mainDishTdish.getContent() != null){
//			 String [] ids = mainDishTdish.getContent().split(",");
//			 for(String id : ids ){
//				 Tdish childDish = tdishDao.get(id);
//				 list.add(childDish);
//			 }
//			 map.put("result", "0");
//			 map.put("dishes", list);
//			 return JacksonJsonMapper.objectToJson(map);
//		 }
//		 return  Constant.FAILUREMSG;
//	 }
	@Override
	public String getDishSet(Tdish dish){
		//TODO 要一次性取出 优化
		List<Tdish> listDish = new ArrayList<Tdish>();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("dishid", dish.getDishid());
		List<Map<String,Object>> list=comboDishService.getFishPotDetailList(params);
		//给鱼锅的详细数据，第一个是锅，下面的是鱼
		if(list != null && list.size()>0){
			Tdish potDish = tdishDao.get(String.valueOf(list.get(list.size()-1).get("contactdishid")));
			listDish.add(potDish);
			for(int i=0;i<list.size()-1;i++){
				Tdish childDish = tdishDao.get(String.valueOf(list.get(i).get("contactdishid")));
				listDish.add(childDish);
			}
			map.put("result", "0");
			map.put("dishes", listDish);
			return JacksonJsonMapper.objectToJson(map);
		}
		return  Constant.FAILUREMSG;
	}
	@Override
	public int updateDishComsumer(List<String> dishids) {
		return tdishDao.updateDishComsumer(dishids);
		
	}
	
	@Override
	public int updateDishSetComsumer(List<String> dishids){
		return tdishDao.updateDishSetComsumer(dishids);
	}
	
	@Override
	public List<Tdish> findAllByIds(List<String> dishids){
		return tdishDao.findAllByIds(dishids);
	}
	
	/**
	 * 退菜 点击数减 1 
	 * @author zhao
	 * @param dishids
	 * @return
	 */
	public int  updateDishComsumerReduce(List<String> dishids){
		return tdishDao.updateDishComsumerReduce(dishids);
	}

	/**
	 * 多个单位退菜 点击数减 1 
	 * @author zhao
	 * @param dishids
	 */
	public int  updateDishSetComsumerReduce(List<String> dishids){
		return tdishDao.updateDishSetComsumerReduce(dishids);
	}

	public String getAllDishSet(){
			 List<Map<String, Object>> listRows = new ArrayList<Map<String, Object>>();
			 Map<String, Object> map = new HashMap<String, Object>();
			 List<Tdish> dishList = tdishDao.getAllDishSet();
			 if(dishList == null || dishList.size() == 0){
				 return  Constant.FAILUREMSG;
			 }
			 for(Tdish tdish : dishList){
				 List<Tdish> list = new ArrayList<Tdish>();
				 Map<String, Object> mapDishSet = new HashMap<String, Object>();
				 if(tdish != null && tdish.getContent() != null){
					 String [] ids = tdish.getContent().split(",");
					 for(String id : ids ){
						 Tdish childDish = tdishDao.get(id);
						 list.add(childDish);
						 
					 }
					 mapDishSet.put("dishid", tdish.getDishid());
					 mapDishSet.put("dishes", list);
				 }
				
				 listRows.add(mapDishSet);
			 }
			 map.put("result", "0");
			 map.put("rows", listRows);
			 return JacksonJsonMapper.objectToJson(map);
	}
	@Override
	public List<Tdish> getDishLists() {
		Map<String, Object> params=new HashMap<String, Object>();
		return tdishDao.findD(params);
	}
	@Override
	public List<Map<String, Object>> getDishListByType(String dishtype,String flag) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("columnid", dishtype);
		params.put("flag", flag);
		return tdishDao.getDishListByType(params);
	}
	@Override
	public List<Tdish> getDishesByDishType(String dishtype) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("columnid", dishtype);
		return tdishDao.getDishesByDishType(params);
	}
	@Override
	public List<Map<String, Object>> getdishCol(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tdishDao.getdishCol(params);
	}
	@Override
	public List<Map<String, Object>> getDishMapByType(String dishtype) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("columnid", dishtype);
		return tdishDao.getDishMapByType(params);
	}
	@Override
	public int addTdishDishType(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		for(Map<String,Object> map:list){
			map.put("id", IdentifierUtils.getId().generate().toString());
		}
		return tdishTypeDao.addDishType(list);
	}
	@Override
	public List<Map<String, Object>> find(Map<String, Object> params) {
		return tdishDao.find(params);
	}
	@Override
	public Tdish findAllById(String dishid) {
		// TODO Auto-generated method stub
		return tdishDao.get(dishid);
	}


	@Override
	public List<Map<String, Object>> findDishes(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tdishDao.findDishes(params);
	}
	@Override
	public Page<Map<String, Object>> pageSearchService(
			Map<String, Object> params, int page, int rows) {
		// TODO Auto-generated method stub
		return  tdishDao.pageDao(params, page, rows);
	}
	@Override
	public List<Map<String, Object>> comfirmDelDish(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return tdishDao.comfirmDelDish(map);
	}
	@Override
	public void updateDishNum(String orderId) {
		tdishDao.updateDishNum(orderId);
	}

	@Override
	public void dishSellOut(Map<String, Object> params) {
		tdishDao.dishSellOut(params);
	}
	@Override
	public void dishRevertSellOut(Map<String, Object> params) {
		tdishDao.dishRevertSellOut(params);
	}
	
	
}
 