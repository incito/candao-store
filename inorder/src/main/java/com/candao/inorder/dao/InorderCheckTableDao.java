package com.candao.inorder.dao;

import java.util.Map;

import com.candao.inorder.pojo.TblCheck;

/**
 * 
 * @author liangdong
 *
 */
public interface InorderCheckTableDao {
	public final static String PREFIX = InorderCheckTableDao.class.getName();
	/**查询餐台对应信息--状态为开台的**/
   public TblCheck tableInfoMes(Map<String, Object> params);
   /**添加订单信息**/
   public String addCheck(TblCheck check);
   /**更新订单价格*/
   public String updateCheckForTot(TblCheck check);
}
