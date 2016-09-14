package com.candao.inorder.dao;

import java.util.Map;

import com.candao.inorder.pojo.TblPrintqueue;

public interface InorderPrintQueueDao {
	public final static String PREFIX = InorderPrintQueueDao.class.getName();
	public TblPrintqueue queryPrintQueue(String printq);
	public Map<String, TblPrintqueue> queryPritQueueALL();
}
