package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TSocialDao;
import com.candao.www.data.model.TbGift;

@Repository
public class TSocialDaoImpl implements TSocialDao {

	@Autowired
	private DaoSupport daoSupport;
	
	/**
	 * 保存礼物
	 * @author weizhifang
	 * @since 2015-11-11
	 * @param tbGift
	 * @return
	 */
	public int saveGift(TbGift tbGift){
		return daoSupport.insert(PREFIX+".saveGift", tbGift);
	}
	
	/**
	 * 删除全部礼物
	 * @author weizhifang
	 * @since 2015-11-12
	 * @return
	 */
	public int deleteGift(){
		return daoSupport.delete(PREFIX+".deleteGift", null);
	}
	
	/**
	 * 查询桌子信息
	 * @author weizhifang
	 * @since 2015-11-12
	 * @param orderId
	 * @return
	 */
	public <T, K, V> List<T> queryTableInfo(String orderid){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderid", orderid);
		return daoSupport.find(PREFIX+".queryAllOpenTable",params);
	}
	
	/**
	 * 系统设置-查询礼物列表
	 * @author weizhifang
	 * @since 2015-11-13
	 * @return
	 */
	public List<TbGift> getGiftList(){
		
		return daoSupport.find(PREFIX + ".getGiftList", null);
	}
	
	/**
	 * 查询已送礼物列表
	 * @author weizhifang
	 * @since 2015-11-17
	 * @return
	 */
	public List<Map<String,Object>> sendGiftList(String orderId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderId", orderId);
		return daoSupport.find(PREFIX + ".sendGiftList", params);
	}

}
