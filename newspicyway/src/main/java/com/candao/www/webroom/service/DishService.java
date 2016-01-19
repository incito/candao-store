package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Tdish;

public interface DishService {
	/**
	 * 分页查询数据
	 * @param params
	 * @param current
	 * @param pagesize
	 * @return
	 */
	 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
	 
	 public List<Tdish> getDishList();
	 /**
	  * 增
	  * @param tdish
	  * @return
	  */
	 public Tdish save(Tdish tdish);
	 /**
	  * 改
	  * @param tdish
	  * @return
	  */
	 public Tdish update(Tdish tdish);
	 /**
	  * 查
	  * @param id
	  * @return
	  */
	 public Tdish findById(String id);
	 /**
	  * 删
	  * @param id
	  * @return
	  */
	 public boolean deleteById(Map<String, Object> params);
	 
	 /**
	  * 根据套餐id 查询 关联单品
	  * @author zhao
	  * @param dish
	  * @return
	  */
	 public String getDishSet(Tdish dish);

	 /**
	  * 更新点餐的数量
	  * @author zhao
	  * @param dishids
	  */
	public int  updateDishComsumer(List<String> dishids);

	/**
	 * 多种计量单位使用这个方法
	 * @author zhao
	 * @param dishids
	 */
	public int updateDishSetComsumer(List<String> dishids);

	/**
	 * 根据id 查询菜品
	 * @author zhao
	 * @param dishids
	 * @return
	 */
	public List<Tdish> findAllByIds(List<String> dishids);

	/**
	 * 退菜 点击数减 1 
	 * @author zhao
	 * @param dishids
	 * @return
	 */
	public int  updateDishComsumerReduce(List<String> dishids);

	/**
	 * 多个单位退菜 点击数减 1 
	 * @author zhao
	 * @param dishids
	 */
	public int  updateDishSetComsumerReduce(List<String> dishids);

	/**
	 * 获取所有组合火锅信息
	 * @author zhao
	 * @return
	 */
	public String getAllDishSet();

	public List<Tdish> getDishLists();
	/**
	 *根据分类获取下面的所有菜品，多计量单位就是多个菜
	 * @param dishtype
	 * @return
	 * 	 * flag=0 没限制 查询所有
	 * flag=1 不包含火锅
	 * flag=2 不包含套餐
	 * flag=3 不包含火锅和套餐
	 */
	public List<Map<String,Object>> getDishListByType(String dishtype,String flag);
	/**
	 * 不包括多计量的数据
	 * @param dishtype
	 * @return
	 */
	public List<Map<String,Object>> getDishMapByType(String dishtype);
	/**
	 * 获取分类下的所有菜品，只从t_dish表中查询
	 * @param dishtype
	 * @return
	 */
	public List<Tdish> getDishesByDishType(String dishtype);
	
	public  List<Map<String,Object>> getdishCol(Map<String,Object> params);
	/**
	 * 添加菜品和分类的关系表
	 * @param list
	 * @return
	 */
	public int addTdishDishType(List<Map<String,Object>> list);

	public List<Map<String, Object>> find(Map<String, Object> params);

	public Tdish findAllById(String dishid);



	public List<Map<String, Object>> findDishes(Map<String, Object> params);

	public Page<Map<String, Object>> pageSearchService(
			Map<String, Object> params, int page, int rows);

	public List<Map<String, Object>> comfirmDelDish(Map<String, Object> map);

	public void updateDishNum(String orderId);

}
