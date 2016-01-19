package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TbUser;
import com.candao.www.data.model.Tinvoice;
import com.candao.www.data.model.Torder;

public interface InvoiceDao {

	public final static String PREFIX = InvoiceDao.class.getName();
	/**
	 * 
	 *  插入每次pad 请求的json数据
	 *  
	 */
	public int insertInvoice(Tinvoice tinvoice);
	public  List<Map<String,Object>> findTinvoice(Map<String, Object> params);
	public  List<Map<String,Object>> findTinvoiceLimit1(Map<String, Object> params);
	public int update(Torder torder);
	public Torder selectByorderId(String orderid);
	
	public List<Tinvoice> findInvoiceByOrderid(Map<String, Object> params);
	public void updateInvoice(Map<String, Object> params);
}
