package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.GiftLogDao;
import com.candao.www.data.model.TGiftLog;

@Repository
public class TGiftDaoImpl implements GiftLogDao {
	
	@Autowired
	private DaoSupport daoSupport;

	/**
	 * 
	 * 保存礼物赠送信息到数据库
	 *
	 * @return
	 */
	@Override
	public int saveGiftLogInfo(TGiftLog giftLog){
		return daoSupport.insert(PREFIX + ".saveGiftLogInfo", giftLog);
	}
	/**
	 * 
	 * 保存礼物赠送信息到数据库
	 *
	 * @return
	 */
	@Override
	public TGiftLog getGiftLogInfo(String giftLogId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("id", giftLogId);
		List<TGiftLog> list = daoSupport.find(PREFIX + ".getGiftLogByid", paramMap);
		if(list==null||list.size()<=0){
			return null;
		}
		return list.get(0);
	}
	/**
	 * 
	 * 保存礼物赠送信息到数据库
	 *
	 * @return
	 */
	@Override
	public List<TGiftLog> getGiftLogInfo(Map<String,String> paramMap){
		List<TGiftLog> list = daoSupport.find(PREFIX + ".getGiftLog", paramMap);
		if(list==null||list.size()<=0){
			return null;
		}
		return list;
	}
	/**
	 * 
	 * 查询当前桌子赠送到目的桌的礼物列表
	 *
	 * @return
	 */
	@Override
	public List<TGiftLog> getGiftLogByOrder(String orderid){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("orderid", orderid);
		return daoSupport.find(PREFIX + ".getGiftLogByOrder", paramMap);
	}
	
	/**
	 * 
	 * 查询当前桌子赠送到目的桌的礼物列表
	 *
	 * @return
	 */
	@Override
	public List<TGiftLog> getGiftLogByRecOrder(String orderid){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("orderid", orderid);
		return daoSupport.find(PREFIX + ".getGiftLogByRecOrder", paramMap);
	}
	
	/**
	 * 
	 * 更新礼物赠送信息到数据库
	 *
	 * @return
	 */
	@Override
	public int updateGiftLogInfo(TGiftLog giftLog){
		return daoSupport.update(PREFIX + ".updateGiftLogInfo", giftLog);
	}
	@Override
	public Map<String, String> getGiftInfo(String giftId) {
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("id", giftId);
		List<Map<String,String>> list = daoSupport.find(PREFIX + ".getGiftInfo", paramMap);
		return list==null||list.size()<=0?null:list.get(0);
	}
	
	/**
	 * 查询礼物的状态
	 */
	@Override
	public Map<String, String> getOrderStatus(String orderid) {
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("orderid", orderid);
		List<Map<String,String>> list = daoSupport.find(PREFIX + ".getOrderStatus", paramMap);
		return list==null||list.size()<=0?null:list.get(0);
	}
	
	@Override
	public int updateOrderStatus(String orderid){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("orderid", orderid);
		return daoSupport.update(PREFIX + ".updateOrderStatus", paramMap);
	}
}
