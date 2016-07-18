package com.candao.print.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.print.entity.TbPrinterArea;
import com.candao.print.entity.TbPrinterDetail;
import com.candao.print.entity.TbPrinterManager;


public interface PrinterManagerService {
/**
 * 分页查询数据
 * @param params
 * @param current
 * @param pagesize
 * @return
 */
 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
 /**
  * 保存数据
  * @param tbPrinterManager
  * @return
  */
 public List<Map<String,Object>> find(Map<String, Object> params);

 public boolean save(TbPrinterManager tbPrinterManager);
 /**
  * 更改数据
  * @param tbPrinterManager
  * @return
  */
 public boolean update(TbPrinterManager tbPrinterManager);
 /**
  * 设置菜品分组
  * @param list
  * @return
  */
 public boolean updateDishGroup(List<Map<String, Object>> list);
 /**
  * 清理菜品分组
  * @param printerid
  * @return
  */
 public boolean cleanDishGroup(String printerid);
 /**
  * 查询单个数据
  * @param id
  * @return
  */
 public TbPrinterManager findById(String id);
 public TbPrinterManager findById2(String id);
 /**
  * 删除单个数据
  * @param id
  * @return
  */
 public boolean deleteById(String id);
 /**
  * 取得数据字典
  * @return
  */
 
  public List<Map<String,Object>> getAreaslistTag(String printerid);
  public TbPrinterManager findByCode(String string);
  public int addPrinterTables(List<Map<String, Object>> list);
public List<TbPrinterArea> findArea( Map<String, Object> params);
public List<Map<String, Object>> findDishes(Map<String, Object> params);
public List<Map<String, Object>> getDishTypeslistTag(String printerid);
public int addPrinterDishes(List<Map<String, Object>> list);
public boolean deleteTablesByPrinterd(String printerid);
public boolean deleteDishesByPrinterd(String printerid);
public List<Map<String, Object>> findPrintername();
public List<Map<String, Object>> findPrinternameByTableids(Map<String, Object> params);
public List<Map<String, Object>> findPrinternameByDishids(
		Map<String, Object> paramsDish);
}
