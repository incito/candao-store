package com.candao.print.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.entity.TbPrinterArea;
import com.candao.print.entity.TbPrinterDetail;
import com.candao.print.entity.TbPrinterManager;
import com.candao.print.service.PrinterManagerService;

@Service
public class PrinterManagerServiceImpl implements PrinterManagerService {
@Autowired
  private TbPrinterManagerDao tbPrinterManagerDao;
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
		return tbPrinterManagerDao.page(params, current, pagesize);
	}
	public List<Map<String,Object>> find(Map<String, Object> params){
		return tbPrinterManagerDao.find(params);
	}
	
	/*public List<Map<String,Object>> findAll(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id",  id);
		return tbasicDataDao.find( params);
	}*/
	@Override
	public boolean save(TbPrinterManager tbPrinter) {
		return tbPrinterManagerDao.insert(tbPrinter)>0;
	}
	@Override
	public TbPrinterManager findById(String id) {
		return tbPrinterManagerDao.get(id);
	}
	@Override
	public TbPrinterManager findById2(String id) {
		return tbPrinterManagerDao.findOne(id);
	}
	@Override
	public boolean update(TbPrinterManager tbPrinter) {
		return tbPrinterManagerDao.update(tbPrinter)>0;
	}
	@Override
	public boolean deleteById(String id) {
		return tbPrinterManagerDao.delete(id)>0;
	}
	 
	@Override
	public List<Map<String, Object>> getAreaslistTag(String printerid) {
		return tbPrinterManagerDao.getAreaslistTag(printerid);
	}
	@Override
	public List<Map<String, Object>> getDishTypeslistTag(String printerid) {
		return tbPrinterManagerDao.getDishTypeslistTag(printerid);
	}
	@Override
	public TbPrinterManager findByCode(String printerCode){
		return tbPrinterManagerDao.findByCode(printerCode);
	}
	@Override
	public int addPrinterTables(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		for(Map<String,Object> map:list){
			map.put("id", IdentifierUtils.getId().generate().toString());
		}
		return tbPrinterManagerDao.addPrinterTables(list);
	}
	@Override
	public int addPrinterDishes(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		for(Map<String,Object> map:list){
			map.put("id", IdentifierUtils.getId().generate().toString());
		}
		return tbPrinterManagerDao.addPrinterDishes(list);
	}
	@Override
	public List<TbPrinterArea> findArea(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tbPrinterManagerDao.findArea(params);
	}
	@Override
	public List<TbPrinterDetail> findDishes(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tbPrinterManagerDao.findDishes(params);
	}
	@Override
	public boolean deleteTablesByPrinterd(String printerid) {
		return tbPrinterManagerDao.deleteTablesByPrinterd(printerid)>0;
	}
	@Override
	public boolean deleteDishesByPrinterd(String printerid) {
		return tbPrinterManagerDao.deleteDishesByPrinterd(printerid)>0;
	}
	@Override
	public List<Map<String, Object>> findPrintername() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Map<String, Object>> findPrinternameByTableids(Map<String, Object> params) {
		return tbPrinterManagerDao.findPrinternameByTableids(params);
	}
	@Override
	public List<Map<String, Object>> findPrinternameByDishids(
			Map<String, Object> paramsDish) {
		return tbPrinterManagerDao.findPrinternameByDishids(paramsDish);
	}
}

