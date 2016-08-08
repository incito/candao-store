package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbasicData;
import com.candao.www.data.model.TdishUnit;

public interface DishTypeService {
	/**
	 * 分页查询数据
	 * @param params
	 * @param current
	 * @param pagesize
	 * @return
	 */
	 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize,String itemtype);
	 
	 public List<Map<String,Object>> findAll(String id);
	 /**
	  * 保存数据
	  * @param tbDataDictionary
	  * @return
	  */
	 public boolean save(TbasicData tbasicData);
	 /**
	  * 更改数据
	  * @param tbDataDictionary
	  * @return
	  */
	 public boolean update(TbasicData tbasicData);
	 /**
	  * 查询单个数据
	  * @param id
	  * @return
	  */
	 public TbasicData findById(String id);
	 /**
	  * 删除单个数据
	  * @param id
	  * @return
	  */
	 public boolean deleteById(String id);
	 /**
	  * 取得数据字典
	  * @return
	  */
	  public List<Map<String,Object>> getDataDictionaryTag(String itemtype);
	  
	  /** 
		  * 关联菜品分类数据字典和菜品得到所有模板数据
		  * id=0 表示父节点
		  * @return
      */
	 public List<Map<String,Object>> getAllDishAndDishType(String id);
	 
	 
	 /**
	  * 获取所有菜单分类
	  * @author zhao
	  * @param id
	  * @return
	  */
	 public List<Map<String,Object>> findAllCategory(String id) ;
	 /**
	  * 根据条件获取结果
	  * @param params
	  * @return
	  */
	 public List<Map<String,Object>> getListByparams(Map<String, Object> params);
	 /**
	  * 拖拽分类的时候，更新分类的顺序
	  * @param tdus
	  * @return
	  */
     public int updateListOrder(List<TbasicData> tdus);

	public int updateDishTagListOrder(List<Map<String, Object>> dishTag);

}
