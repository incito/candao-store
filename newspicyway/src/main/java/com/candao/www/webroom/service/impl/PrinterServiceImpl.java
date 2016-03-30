package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.print.entity.TbPrinter;
import com.candao.print.service.PrinterService;
import com.candao.www.data.dao.TbPrinterDao;
@Service
public class PrinterServiceImpl implements PrinterService {
@Autowired
  private TbPrinterDao tbPrinterDao;
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
		return tbPrinterDao.page(params, current, pagesize);
	}
	public List<Map<String,Object>> find(Map<String, Object> params){
		return tbPrinterDao.find(params);
	}
	
	/*public List<Map<String,Object>> findAll(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id",  id);
		return tbasicDataDao.find( params);
	}*/
	@Override
	public boolean save(TbPrinter tbPrinter) {
		return tbPrinterDao.insert(tbPrinter)>0;
	}
	@Override
	public TbPrinter findById(String id) {
		return tbPrinterDao.get(id);
	}
	@Override
	public TbPrinter findById2(String id) {
		return tbPrinterDao.findOne(id);
	}
	@Override
	public boolean update(TbPrinter tbPrinter) {
		return tbPrinterDao.update(tbPrinter)>0;
	}
	@Override
	public boolean deleteById(String id) {
		return tbPrinterDao.delete(id)>0;
	}
	 
	@Override
	public List<Map<String, Object>> getTableTag() {
		return tbPrinterDao.getTableTag();
	}
	@Override
	public TbPrinter findByCode(String printerCode){
		return tbPrinterDao.findByCode(printerCode);
	}
	@Override
	public int queryPrintIsExsit(String customerPrinterIp, String customerPrinterPort) {
		Map<String, Object>  result=tbPrinterDao.queryPrintIsExsit(customerPrinterIp,customerPrinterPort);
		 if(result!=null){
				Object isExsit=result.get("isExsit");
				if(isExsit!=null){
					int num=Integer.parseInt(String.valueOf(isExsit));
					if(num>0){
						return num;
					}
				}
			}
		 return 0;
	}
}

