package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.AreaManager;

public interface ManagerWatchDao {
	
	public final static String PREFIX = ManagerWatchDao.class.getName();

	/**
	 * 根据工号删除经理区域信息
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param jobNumber
	 */
	public int deleteAreaManager(String jobNumber);
	
	/**
	 * 批量保存经理区域
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param ams
	 * @return
	 */
	public int batchInsertAreaManager(List<AreaManager> ams);
	
	/**
	 * 根据订单号服务员工号查询区域信息和经理工号
	 * @author weizhifang
	 * @since 2016-3-22
	 * @param orderid
	 * @return
	 */
	public List<Map<String,Object>> queryAreaAndManagerJobNumber(String orderid,String userid,String tableno);
	
	/**
	 * 查询其他区域的值班经理
	 * @author weizhifang
	 * @since 2016-3-28
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> queryOthersAreaManager(List<String> userids);
	
	/**
	 * 查询所有区域值班经理
	 * @author weizhifang
	 * @since 2016-4-21
	 * @return
	 */
	public List<Map<String,Object>> queryAllAreaManager();
	
	/**
	 * 查询呼叫桌台所在区域
	 * @author weizhifang
	 * @since 2016-4-21
	 * @param tableno
	 * @return
	 */
	public String getTableName(String tableno);
}
