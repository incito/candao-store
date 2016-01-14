package com.candao.www.data.dao;


import java.util.List;
import java.util.Map;
import com.candao.common.page.Page;
import com.candao.www.data.model.Ttemplate;

/**
 * 数据访问接口
 *
 */
public interface TbTemplateDao {    
    public final static String PREFIX = TbTemplateDao.class.getName();
    
	public Ttemplate get(java.lang.String id);
	
	public <K, V> Map<K, V> findOne(java.lang.String id);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(Ttemplate tbTemplate);
	
	public int update(Ttemplate tbTemplate);
	
	public int updateTemplates(List<Map<String, Object>> list);
	
	public int delete(java.lang.String id );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	
	public List<Ttemplate> validateTemplate(Map<String, Object> params) ;
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	/**
	 * 添加菜谱模板
	 * @param list
	 * @return
	 */
	public int addTtemplate(List<Ttemplate> list);
	/**
	 * 删除菜谱和模板的关系
	 * @param menuid
	 * @return
	 */
	public int delTtemplate(String menuid);
	/**
	 * 获取该菜谱的所有模板
	 * @param menuid
	 * @return
	 */
	public <E, K, V> List<E> getTtemplateBymenuId(Map<K, V> params);
	/**
	 * pad端使用
	 * @author shen
	 * @date:2015年5月6日下午4:56:44
	 * @Description: TODO
	 */
	public <E, K, V> List<E> getTtemplateBymenuIdPad(Map<K, V> params);

}


