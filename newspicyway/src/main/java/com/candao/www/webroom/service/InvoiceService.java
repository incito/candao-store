package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.Tinvoice;
import com.candao.www.data.model.Torder;

public interface InvoiceService {

	/**
	 * 插入pad 的json 数据
	 * @author zhao
	 * @param tinvoice
	 * @return
	 */
	public int insertInvoice(Tinvoice tinvoice);
	
	public Torder selectByorderId(String orderid);
	
	public List<Tinvoice> findTinvoice(Map<String, Object> params);
	
	public  int findTinvoiceLimit1(Map<String, Object> params);
	
	public int update(Torder torder);

	public List<com.candao.www.data.model.Tinvoice> findInvoiceByOrderid(
			Map<String, Object> params);

	public void updateInvoice(Map<String, Object> params);
}
