package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 品项销售明细表
 * @author Administrator
 *
 */
public interface TItemDetailDao {

	public final static String PREFIX = TItemDetailDao.class.getName();
	
	/**
	 * 查询品项销售明细存储过程
	 * @author weizhifang
	 * @since 2015-7-3
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemDetailProcedure(Map<String, Object> params);
	
	/**
	 * 查询品项销售明细子表存储过程
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemSubDetailProcedure(Map<String,Object> params);
	
	/**
	 * 查询品类列表
	 *
	 * @param itemid
	 * @return
	 * @author weizhifang
	 * @since 2015-5-16
	 */
	public List<Map<String,Object>> getItemDescList();
	
	/**
	 * 查询当前门店名称
	 * @author weizhifang
	 * @since 2015-7-29
	 * @param branchid
	 * @return
	 */
	public String getBranchName(String branchid);
}
