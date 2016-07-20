package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TtemplateDishUnit;
public interface TtemplateDishUnitlDao {
	public final static String PREFIX = TtemplateDishUnitlDao.class.getName();
	/**
	 * 添加菜品单位详细
	 * @param list
	 * @return
	 */
	public int addTtemplateDishUnit(List<TtemplateDishUnit> list);
	/**
	 * 删除菜品单位详细
	 * @param menuid
	 * @return
	 */
	public int delTtemplateDishUnit(String menuid);
	/**
	 * 查询详细
	 * @param params
	 * @return
	 */
	public <E, K, V> List<E> getTtemplateDishUnitByparams(Map<K, V> params);
	/**
	 * 查询菜谱中鱼锅的明细
	 * @author shen
	 * @date:2015年5月20日下午3:47:16
	 * @Description: TODO
	 */
	public <E, K, V> List<E> getTtemplatefishpotUnitByparams(Map<K, V> params);
	/**
	 * pad端调用菜品详细
	 * @author shen
	 * @date:2015年5月6日下午10:32:47
	 * @Description: TODO
	 */
	public <E, K, V> List<E> getTtemplateDishUnitByparamsPad(Map<K, V> params);
	/**
	 * 更新菜谱中某个菜的估清
	 * @author shen
	 * @date:2015年5月11日下午8:12:25
	 * @Description: TODO
	 */
	public boolean updateDishStatus(Map<String,Object> params);
	public TtemplateDishUnit findOneTtd(Map<String, Object> paramsTtd);
	
	/**
	 * 获取所有已经估清的菜品
	 */
	List<TtemplateDishUnit> getTtemplateDishUnitByStatus();
	
	boolean updateStatus(String dishIds);
	public List<TtemplateDishUnit> findAllByDishIds(Map<String, Object> paramsTtd);
}
