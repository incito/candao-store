package com.candao.inorder.service;

import java.util.Map;

import com.candao.inorder.pojo.TblCheck;

/**
 * 
 * @author liangDong
 * 订单信息
 *
 */
public interface InorderCheckTableService {
	/**添加订单信息**/
   public String addCheck(TblCheck tblCheck);
   /**查询餐台订单信息*/
   public TblCheck openTable(Map<String, Object> params);
   
}
