package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;
import com.candao.www.webroom.model.TcomboDishGroupList;
public interface ComboDishService {
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
	 * 获取火锅的明细
	 * @param 
	 * @return
	 */
	public List<Map<String,Object>> getFishPotDetailList(Map<String,Object> map);
	/**
	 * Pad获取套餐的数据
	 * @param dishid
	 * @return
	 */
	public String getComboDishJson(Map<String,Object> map);
	/**
	 * 保存套餐数据
	 * @author tom_zhao
	 * @param tcomboDishGroupList
	 */
	public void save(TcomboDishGroupList tcomboDishGroupList);
	
	/**
	 * 修改套餐数据
	 * @author tom_zhao
	 * @param tcomboDishGroupList
	 */
	public void update(TcomboDishGroupList tcomboDishGroupList);
	public List<Map<String, Object>> ifDishesDetail(Map<String, Object> map);
	

}
