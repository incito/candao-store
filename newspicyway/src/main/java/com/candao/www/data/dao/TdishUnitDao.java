package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TdishUnit;

public interface TdishUnitDao {
	public final static String PREFIX = TdishUnitDao.class.getName();
	/**
	 * 新增计量单位
	 * @return
	 */
	public int addDishUnit(List<TdishUnit> tdu);
	/**
	 * 根据菜品id删除计量单位
	 * @param dishid
	 * @return
	 */
	public boolean delDishUnit(String dishid);
	/**
	 * 根据菜品id获取计量单位
	 * @param dishId
	 * @return
	 */
	public List<TdishUnit> getUnitsBydishId(String dishId);
	/**
	 * 根据计量单位主键获取菜品详细信息
	 * @param unitId
	 * @return
	 */
	public Map<String,String> getDishDetail(String unitId);
	/**
	 * 获取菜品单位的历史记录
	 * @return
	 */
	public List<String> getUnitHistorylist();
}
