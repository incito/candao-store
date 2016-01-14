package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Tdish;

public interface TdishDao {
	public final static String PREFIX = TdishDao.class.getName();

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

	public Tdish get(java.lang.String id);
	
	public <K, V> Map<K, V> findOne(java.lang.String id);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public Tdish save(Tdish tdish);
	
	public Tdish update(Tdish tdish);
	
	public int delete(java.lang.String id );
	public <K, V> int deletepa(Map<K, V> params);
	
    /**
     * 批量更改是否显示状态
     * @param stauts
     * @param articleids
     */
    public int updateArticleStatus(int isselect,String[] articleids);
  
    /**
     * 更新菜品数量
     * @author zhao
     * @param dishids
     * @return
     */
	public int updateDishComsumer(List<String> dishids);
	
	
    /**
     * 更新菜品数量 对于多种计量的菜品使用这个方法
     * @author zhao
     * @param dishids
     * @return
     */
	public int updateDishSetComsumer(List<String> dishids);

	/**
	 * 批处理查询
	 * @author zhao
	 * @param dishids
	 * @return
	 */
	public List<Tdish> findAllByIds(List<String> dishids);

	public int updateDishComsumerReduce(List<String> dishids);

	public int updateDishSetComsumerReduce(List<String> dishids);

	public List<Tdish> findAllDish();

	public List<Tdish> getAllDishSet();

	public int updateOrderNum(Tdish dish);
	
	public <T, K, V> List<T> findD(Map<K, V> params);
	
	
	public <T, K, V> List<T> getDishListByType(Map<K, V> params);
	public <T, K, V> List<T> getDishMapByType(Map<K, V> params);
	
	public <T, K, V> List<T> getDishesByDishType(Map<K, V> params);
	public <T, K, V> List<T> getdishCol(Map<K, V> params);


	public List<Map<String, Object>> findDishes(Map<String, Object> params);

	public Page<Map<String, Object>> pageDao(Map<String, Object> params,
			int page, int rows);

	public List<Map<String, Object>> comfirmDelDish(Map<String, Object> map);

	public void updateDishNum(String orderId);
	
}