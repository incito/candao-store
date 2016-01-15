package com.candao.www.data.dao;

import java.util.Map;

import com.candao.www.data.model.TbMessage;

/**
 * 
 * 呼叫服务员
 * 
 * 
 * @author YANGZHONGLI
 *
 */
public interface CallWaiterDao {
	
	public final static String PREFIX = CallWaiterDao.class.getName();
	/**
	 * 
	 * 保存呼叫信息
	 * 
	 * 
	 *主键ID 生成  IdentifierUtils.getId().generate()
	 * @return
	 */
	public int saveCallInfo(TbMessage message);
	
	
	/**
	 * 
	 * 更新呼叫信息
	 * @param message
	 * @return
	 */
	public int updateCallInfo(TbMessage message);
	
	/**
	 * 
	 * 更新呼叫服务员的状态
	 * @param message
	 * @return
	 */
	public int updateCallInfoStatus(String orderid);
	
	/**
	 * 
	 * 查询呼叫信息
	 * 
	 * @return
	 */
	public TbMessage queryCallInfo(Map<String,String> paramMap);

}
