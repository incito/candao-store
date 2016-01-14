package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TcomboDishGroup;
import com.candao.www.data.model.TgroupDetail;

public interface ComboDishDao {
	public final static String PREFIX = ComboDishDao.class.getName();
	/**
	 * 获取套餐组合规则的基本信息
	 * dishid 套餐的id
	 * @return
	 */
	public List<Map<String,Object>> getTdishGroupList(String dishid);
	/**
	 * 获取套餐组合下的菜
	 * @param tdishGroupid 组合的主键
	 * @return
	 */
	public List<Map<String,Object>> getTgroupDetailList(String tdishGroupid);
	
	/**
	 * 保存套餐的数据到t_dish_group
	 * @author tom_zhao
	 * @param group
	 */
	public int saveGroup(TcomboDishGroup group);
	/**
	 * 保存套餐的明细数据到 t_group_detail
	 * @author tom_zhao
	 * @param detail
	 */
	public int saveGroupDetail(TgroupDetail detail);
	
	/**
	 * 删除套餐的数据 t_dish_group
	 * @author tom_zhao
	 * @param group
	 */
	public int deleteGroup(String dishId);
	/**
	 * 删除套餐的数据 t_group_detail
	 * @author tom_zhao
	 * @param detail
	 */
	public int deleteGroupDetail(String dishId);
	/**
	 * 获取火锅的明细
	 * @param 
	 * @return
	 */
	public List<Map<String,Object>> getFishPotDetailList(Map<String,Object> map);
	/**
	 * pad端获取鱼锅信息
	 * @author shen
	 * @date:2015年5月7日下午7:55:59
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getFishPotDetailPad(Map<String,Object> map);
	public List<Map<String, Object>> ifDishesDetail(Map<String, Object> map);

}
