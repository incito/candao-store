package com.candao.www.data.dao;

import java.util.Map;

import com.candao.www.webroom.model.Complain;

/**
 * 
 * 投诉
 * 
 * 
 * @author YANGZHONGLI
 *
 */
public interface ComplainDao {
	
	public final static String PREFIX = ComplainDao.class.getName();
	/**
	 * 
	 * 保存投诉信息
	 * 
	 * 
	 *主键ID 生成  IdentifierUtils.getId().generate()
	 * @return
	 */
	public int saveComplain(Complain complain);
	
	/**
	 * 
	 * 保存投诉信息
	 * 
	 * @return
	 */
	public int saveComplainType(Map<String,String> paramMap);

}
