package com.candao.inorder.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.inorder.dao.DaoSupport;
import com.candao.inorder.dao.InorderPrintQueueDao;
import com.candao.inorder.pojo.TblPrintqueue;

@Repository
public class InorderPrintQueueDaoImp implements InorderPrintQueueDao {

	@Autowired
	private DaoSupport daoSupport;

	@Override
	public TblPrintqueue queryPrintQueue(String printq) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("printq", printq);

		return daoSupport.get(PREFIX + ".queryPrintqueueBean", params);
	}

	@Override
	public Map<String, TblPrintqueue> queryPritQueueALL() {
		List<TblPrintqueue> tblList = daoSupport.find(PREFIX + ".queryPrintqueueALL");
		Map<String,TblPrintqueue> result=new HashMap<String,TblPrintqueue>();
		for(TblPrintqueue printqueue:tblList){
			result.put(String.valueOf(printqueue.getPrintq()), printqueue);
		}
 		return result;
	}

}
