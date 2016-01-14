package com.candao.www.data.dao;

import java.util.Map;
import java.util.List;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbDataDictionary;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TdishUnit;

/**
 * 数据访问接口
 * 
 */
public interface TbDataDictionaryDao {
	public final static String PREFIX = TbDataDictionaryDao.class.getName();

	public TbDataDictionary get(java.lang.String id);
	
	public <K, V> Map<K, V> getDish(java.lang.String id);
	
	public <K, V> Map<K, V> getDishUnit(java.lang.String id);

	public <K, V> Map<K, V> findOne(java.lang.String id);

	public <T, K, V> List<T> find(Map<K, V> params);

	public int insert(TbDataDictionary tbDataDictionary);
	
	public void insertDish(Tdish dish);
	
	public void insertDishUnit(TdishUnit dishUnit);
	
    public void updatetDish(Tdish dish);
	
	public void updatetDishUnit(TdishUnit dishUnit);
	
	public int update(TbDataDictionary tbDataDictionary);

	public int delete(java.lang.String id);

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

	/**
	 * 取得数据分类
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDataDictionaryTag();

	public List<Map<String, Object>> getTypeandTypename();

	/**
	 * 获取某个分类下的所有字典数据
	 * 
	 * @param type 分类
	 * @return
	 */
	public List<Map<String, Object>> getDatasByType(String type);

	public int delDishTasteDao(String dishTasteId);


	public <T, K, V> List<T> getSystemList();
	
	/**
	 * 获取某个分类下的所有字典数据
	 * 
	 * @param type 分类
	 * @return
	 */
	public List<Map<String, Object>> getDicListByType(String type);
	
	/**
	 * 修改字典表数据
	 * 
	 * @param type 分类
	 * @return
	 */
	public int updataDicItemDesc(Map<String,Object> paramMap);
	
	/**
	 * 修改呼叫服务员时间设置
	 * 
	 * @param type 分类
	 * @return
	 */
	public int updataCallTimeSet(Map<String,Object> paramMap);

}
