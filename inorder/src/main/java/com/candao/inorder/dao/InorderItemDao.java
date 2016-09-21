package com.candao.inorder.dao;

import java.util.List;
import java.util.Map;

import com.candao.inorder.pojo.TblItem;

/**
 * 
 * @author langdong 订单子信息
 */
public interface InorderItemDao {
	public final static String PREFIX = InorderItemDao.class.getName();
	public int itemIndex(String check);
	public int addItem(TblItem tblItem);
	public int addbatchItem(List<TblItem> items);
	public String queryItemInx(Map<String, String> params);
}
