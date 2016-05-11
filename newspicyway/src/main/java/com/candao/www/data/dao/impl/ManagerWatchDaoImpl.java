package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.ManagerWatchDao;
import com.candao.www.data.model.AreaManager;

@Repository
public class ManagerWatchDaoImpl implements ManagerWatchDao {

	@Autowired
	private DaoSupport daoSupport;
	
	/**
	 * 根据工号删除经理区域信息
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param jobNumber
	 */
	public int deleteAreaManager(String jobNumber){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("jobNumber", jobNumber);
		return daoSupport.delete(PREFIX+".deleteAreaManager", params);
	}
	
	/**
	 * 批量保存经理区域
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param ams
	 * @return
	 */
	public int batchInsertAreaManager(List<AreaManager> ams){
		return daoSupport.insert(PREFIX + ".batchInsertAreaManager", ams);
	}
	
	/**
	 * 根据订单号服务员工号查询区域信息和经理工号
	 * @author weizhifang
	 * @since 2016-3-22
	 * @param orderid
	 * @return
	 */
	public List<Map<String,Object>> queryAreaAndManagerJobNumber(String orderid,String userid,String tableno){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderid", orderid);
		params.put("userid", userid);
		params.put("tableno", tableno);
		return daoSupport.find(PREFIX + ".queryAreaAndManagerJobNumber", params);
	}
	
	/**
	 * 查询其他区域的值班经理
	 * @author weizhifang
	 * @since 2016-3-28
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> queryOthersAreaManager(List<String> userids){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userid", userids);
		return daoSupport.find(PREFIX + ".queryOthersAreaManager",params);
	}
	
	/**
	 * 查询所有区域值班经理
	 * @author weizhifang
	 * @since 2016-4-21
	 * @return
	 */
	public List<Map<String,Object>> queryAllAreaManager(){
		return daoSupport.find(PREFIX + ".queryAllAreaManager",null);
	}
	
	/**
	 * 查询呼叫桌台所在区域
	 * @author weizhifang
	 * @since 2016-4-21
	 * @param tableno
	 * @return
	 */
	public String getTableName(String tableno){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("tableno", tableno);
		return daoSupport.get(PREFIX + ".getTableName",params);
	}
}
