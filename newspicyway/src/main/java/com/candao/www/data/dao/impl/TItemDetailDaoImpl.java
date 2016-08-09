package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TItemDetailDao;

/**
 * 品项销售明细表
 * @author Administrator
 *
 */
@Repository
public class TItemDetailDaoImpl implements TItemDetailDao {

	@Autowired
	private DaoSupport daoSupport;
	
	/**
	 * 查询品项销售明细存储过程
	 * @author weizhifang
	 * @since 2015-7-3
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemDetailProcedure(Map<String, Object> params){
		return daoSupport.find(PREFIX + ".itemDetailProcedure", params);
	}
	
	/**
	 * 查询品项销售明细子表存储过程
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemSubDetailProcedure(Map<String,Object> params){
		return daoSupport.find(PREFIX + ".itemSubDetailProcedure", params);
	}
	
	/**
	  * 查询品类列表
	  * @author weizhifang
	  * @since 2015-5-16
	  * @param itemid
	  * @return
	  */
	 public List<Map<String,Object>> getItemDescList(){
		 return daoSupport.find(PREFIX + ".getItemDescList");
	 }
	 
	 /**
	  * 查询当前门店名称
	  * @author weizhifang
	  * @since 2015-7-29
	  * @param branchid
	  * @return
	  */
	 public String getBranchName(String branchid){
		 return daoSupport.getSqlSessionTemplate().selectOne(PREFIX + ".getBranchName",branchid);
	 }
}
