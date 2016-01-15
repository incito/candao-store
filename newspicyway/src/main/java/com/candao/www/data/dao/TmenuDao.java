package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.Tmenu;


public interface TmenuDao {
	public final static String PREFIX = TmenuDao.class.getName();
	/**
	 * 添加菜谱基本信息
	 * @param tmenu
	 * @return
	 */
	public int insert(Tmenu tmenu);
	/**
	 * 更新菜谱基本信息
	 * @param tmenu
	 * @return
	 */
	public int update(Tmenu tmenu);
	/**
	 * 获取单个菜谱的基本信息
	 * @param id
	 * @return
	 */
	public Tmenu get(java.lang.String id);
	/**
	 * 获取所有菜谱
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> find(Map<K, V> params);
	/**
	 * 根据门店获取菜谱列表
	 * @author shen
	 * @date:2015年5月29日下午3:27:46
	 * @Description: TODO
	 */
	public <T, K, V> List<T> findByBranchid(Map<K, V> params);
	/**
	 * 删除菜谱
	 * @param id
	 * @return
	 */
	public int delete(java.lang.String id );
	/**
	 * 获取该门店适用的菜谱
	 * @author shen
	 * @date:2015年5月6日下午2:18:26
	 * @Description: TODO
	 */
	public <T, K, V> List<T> findMenuByBrachid(Map<K, V> params);
	/**
	 * 根据菜谱id和分类id，获取该分类下的菜品信息
	 * @author shen
	 * @date:2015年6月2日下午6:28:30
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getBranchMenuDishByType(Map<String,Object> params);
	/**
	 * 获取菜谱中菜品的信息
	 * @author shen
	 * @date:2015年6月2日下午8:19:12
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getMenuDishDetailById(Map<String,Object> params);
	/**
	 * 获取本店到了该启用的时间菜谱列表
	 * @author shen
	 * @date:2015年5月13日下午7:58:01
	 * @Description: TODO
	 */
	public List<Tmenu> findEffectMenu(Map<String,Object> params);
	/**
	 * 获取菜谱人气菜品
	 * @author shen
	 * @date:2015年5月26日下午4:15:57
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getHeatDishList(Map<String,Object> params);
	/**
	 * 门店查看菜谱，获取当前菜谱的分类
	 * @author shen
	 * @date:2015年6月2日下午6:19:18
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getBranchMenuColumn(Map<String,Object> params);

}
