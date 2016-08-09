package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TbGift;

public interface SocialService {
	
	/**
	 * 保存礼物
	 * @author weizhifang
	 * @since 2015-11-11
	 * @param tbGift
	 * @return
	 */
	public void saveGift(String gifts);
	
	/**
	 * 查询桌子信息
	 * @author weizhifang
	 * @since 2015-11-12
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryTableInfo(String orderid);
	
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
