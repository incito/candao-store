package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

public interface TdishTypeDao {
	public final static String PREFIX = TdishTypeDao.class.getName();
	
	/**
	 * 新增菜品和分类的关联数据
	 * @return
	 */
	public int addDishType(List<Map<String,Object>> tdu);
	/**
	 * 根据菜品id删除菜品和分类的关联数据
	 * @param dishid
	 * @return
	 */
	public boolean delDishType(String dishid);

}
