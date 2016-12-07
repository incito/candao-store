package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.InvoiceDao;
import com.candao.www.data.model.Tinvoice;
import com.candao.www.data.model.Torder;
import com.candao.www.webroom.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements  InvoiceService{

	@Autowired
	private InvoiceDao invoiceDao;
	
	@Override
	public int insertInvoice(Tinvoice iinvoice) {
		return invoiceDao.insertInvoice(iinvoice);
	}
	public Torder selectByorderId(String orderid){
		return invoiceDao.selectByorderId(orderid);
	}
	@Override
	public List<Tinvoice> findTinvoice(Map<String, Object> params){
		 List<Map<String,Object>> TinvoiceList = invoiceDao.findTinvoice(params);
		 List<Tinvoice> listTinvoice = new ArrayList<Tinvoice>();
		if(TinvoiceList.size()>0){
			for (int i = 0; i < TinvoiceList.size(); i++) {
				Tinvoice tinvoice =  new Tinvoice();
				if(TinvoiceList.get(i).get("cardno")!=null && !TinvoiceList.get(i).get("cardno").equals("")){
					tinvoice.setCardno(TinvoiceList.get(i).get("cardno").toString());
				}
				if(TinvoiceList.get(i).get("invoice_title")!=null && !TinvoiceList.get(i).get("invoice_title").equals("")){
					tinvoice.setInvoice_title(TinvoiceList.get(i).get("invoice_title").toString());
				}
				listTinvoice.add(tinvoice);
			}
		}
		return listTinvoice;
	}
	@Override
	public List<Tinvoice> findInvoiceByOrderid(Map<String, Object> params) {
		 
		 return invoiceDao.findInvoiceByOrderid(params);
	}
	@Override
	public void updateInvoice(Map<String, Object> params) {
		   invoiceDao.updateInvoice(params);
	}
}
