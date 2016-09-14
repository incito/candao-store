package com.candao.inorder.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.candao.inorder.dao.DaoSupport;
import com.candao.inorder.dao.InorderItemDao;
import com.candao.inorder.pojo.TblItem;

/***
 * 
 * @author liangdong 订单子信息
 */
@Repository
public class InorderItemDaoImpl implements InorderItemDao {
	
	@Autowired
	@Qualifier("SqlSessionDaoSupportnorderPoserver")
	private DaoSupport daoSuppport;

	@Override
	public int itemIndex(String check) {
		return 0;
	}

	@Override
	public int addItem(TblItem tblItem) {
		// TODO Auto-generated method stub
		return daoSuppport.insert(PREFIX+".insertItem", tblItem);
	}

	@Override
	public int addbatchItem(List<TblItem> items) {
		return daoSuppport.insert(PREFIX+".insertItems", items);
	}

	@Override
	public String queryItemInx(Map<String, String> params) {
		return daoSuppport.get(PREFIX+".queryLastItemInx", params);
	}

}
