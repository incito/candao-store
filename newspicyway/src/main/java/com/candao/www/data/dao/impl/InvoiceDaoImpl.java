package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.InvoiceDao;
import com.candao.www.data.model.TbUser;
import com.candao.www.data.model.Tinvoice;
import com.candao.www.data.model.Torder;

@Repository
public class InvoiceDaoImpl implements InvoiceDao{

	
	@Autowired
	private DaoSupport dao;
	
	@Override
	public int insertInvoice(Tinvoice tinvoice) {
		return dao.insert(PREFIX + ".insert", tinvoice);
	}
	@Override
	public Torder selectByorderId(String orderid){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", orderid);
		return dao.get(PREFIX + ".selectByorderId", params);
	}
	@Override
	public  List<Map<String,Object>> findTinvoice(Map<String, Object> params){
		return dao.find(PREFIX + ".findTinvoice", params);
	}
	@Override
	public  List<Map<String,Object>> findTinvoiceLimit1(Map<String, Object> params){
		return dao.find(PREFIX + ".findTinvoiceLimit1", params);
	}
	@Override
	public int update(Torder torder){
		return dao.update(PREFIX + ".updateInvoiceid", torder);
	}
	@Override
	public List<Tinvoice> findInvoiceByOrderid(Map<String, Object> params) {
		return dao.find(PREFIX + ".findInvoiceByOrderid", params);
	}
	@Override
	public void updateInvoice(Map<String, Object> params) {
		  dao.update(PREFIX + ".updateInvoice", params);
		
	}
}
