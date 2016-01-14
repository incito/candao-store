package com.candao.www.webroom.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.IdentifierUtils;
import com.candao.www.data.dao.TdishUnitDao;
import com.candao.www.data.model.TdishUnit;
import com.candao.www.webroom.service.DishUnitService;
@Service
public class DishUnitServiceImpl implements DishUnitService {
	@Autowired
	private TdishUnitDao tdishUnitDao;
	

	@Override
	public boolean addDishUnit(TdishUnit[] tdus) {
		// TODO Auto-generated method stub
		String dishid="";
		 List<TdishUnit> list = Arrays.asList(tdus);
		 for(TdishUnit tdu:list){
			 dishid=tdu.getDishid();
			 tdu.setId(IdentifierUtils.getId().generate().toString());
		 }
		 if(!"".equals(dishid)||dishid!=null){
			 tdishUnitDao.delDishUnit(dishid);
		 }
		return tdishUnitDao.addDishUnit(list)>0;
	}

	@Override
	public boolean delDishUnit(String dishid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<TdishUnit> getUnitsBydishId(String dishId) {
		// TODO Auto-generated method stub
		return tdishUnitDao.getUnitsBydishId(dishId);
	}

	@Override
	public Map<String, String> getDishDetail(String unitId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getUnitHistorylist() {
		// TODO Auto-generated method stub
		return tdishUnitDao.getUnitHistorylist();
	}

}
