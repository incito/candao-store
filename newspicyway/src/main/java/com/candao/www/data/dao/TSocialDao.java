package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TbGift;

public interface TSocialDao {

	public final static String PREFIX = TSocialDao.class.getName();
	
	/**
	 * 保存礼物
	 * @author weizhifang
	 * @since 2015-11-11
	 * @param tbGift
	 * @return
	 */
	public int saveGift(TbGift tbGift);
	
	/**
	 * 删除全部礼物
	 * @author weizhifang
	 * @since 2015-11-12
	 * @return
	 */
	public int deleteGift();
	
	/**
	 * 查询桌子信息
	 * @author weizhifang
	 * @since 2015-11-12
	 * @param orderId
	 * @return
	 */
	public <T, K, V> List<T> queryTableInfo(String orderId);
	
	/**
	 * 系统设置-查询礼物列表
	 * @author weizhifang
	 * @since 2015-11-13
	 * @return
	 */
	public List<TbGift> getGiftList();
	
	/**
	 * 查询已送礼物列表
	 * @author weizhifang
	 * @since 2015-11-17
	 * @return
	 */
	public List<Map<String,Object>> sendGiftList(String orderId);
	
}
