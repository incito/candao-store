package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TGiftLog;

/**
 * 
 * 呼叫服务员
 * 
 * 
 * @author YANGZHONGLI
 *
 */
public interface GiftLogService {
	/**
	 * 
	 * 保存礼物赠送信息到数据库
	 *
	 * @return
	 */
	public TGiftLog saveGiftLogInfo(TGiftLog giftLog);
	
	/**
	 * 
	 * 保存礼物赠送信息到数据库
	 *
	 * @return
	 */
	public TGiftLog getGiftLogInfo(String giftLogId);
	
	/**
	 * 
	 * 更新礼物赠送信息到数据库
	 *
	 * @return
	 */
	public int updateGiftLogInfo(TGiftLog giftLog,String primarykey);
	/**
	 * 
	 * 获取礼物信息
	 *
	 * @return
	 */
	public Map<String,String> getGiftInfo(String giftId);
	/**
	 * 
	 * 获取礼物信息
	 *
	 * @return
	 */
	public Map<String,String> getOrderStatus(String orderid);
	/**
	 * 
	 * 查询当前桌子赠送到目的桌的礼物列表
	 *
	 * @return
	 */
	public List<TGiftLog> getGiftLogByRecOrder(String orderid);
	/**
	 * 
	 * 更新订单收礼状态
	 * @param orderid
	 * @return
	 */
	public int updateOrderStatus(String orderid);

	public int deleteById(String logId);
		
}
