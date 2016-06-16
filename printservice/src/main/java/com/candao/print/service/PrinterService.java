package com.candao.print.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.print.entity.TbPrinter;


public interface PrinterService {
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
  * @param tbPrinter
  * @return
  */
 public List<Map<String,Object>> find(Map<String, Object> params);

 /**
  * 查询所有数据，不强制过滤状态
  * @param params
  * @return
     */
 public List<Map<String,Object>> findAll(Map<String, Object> params);

 public boolean save(TbPrinter tbPrinter);
 /**
  * 更改数据
  * @param tbPrinter
  * @return
  */
 public boolean update(TbPrinter tbPrinter);
 /**
  * 查询单个数据
  * @param id
  * @return
  */
 public TbPrinter findById(String id);
 public TbPrinter findById2(String id);
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
 
  public List<Map<String,Object>> getTableTag();
  public TbPrinter findByCode(String string);
  public int queryPrintIsExsit(String customerPrinterIp, String customerPrinterPort);

 /**
  * 更新打印机工作状态
  * @return
     */
  public int updateWorkState(String ip,short workstatus);

 public List<Map<String,Object>> queryPrinterWorkStatus();

 /**
  * 清除打印机状态列表
  * @return
     */
 public int clearWorkStatus();
}
