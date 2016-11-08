package com.candao.www.webroom.service;

import java.util.List;

import com.candao.www.data.model.TdishUnit;

public interface DishUnitService {
	/**
	 * 新增计量单位
	 * @return
	 */
	public boolean addDishUnit(TdishUnit[] tdus);
	/**
	 * 根据菜品id获取计量单位
	 * @param dishId
	 * @return
	 */
	public List<TdishUnit> getUnitsBydishId(String dishId);
	/**
	 * 获取菜品单位的历史记录
	 * @return
	 */
	public List<String> getUnitHistorylist();

}
