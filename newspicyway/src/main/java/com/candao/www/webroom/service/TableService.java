package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.ToperationLog;
import com.candao.www.webroom.model.AccountCash;
import com.candao.www.webroom.model.Table;

public interface TableService {
	/**
	 * 分页查询数据
	 * 
	 * @param params
	 * @param current
	 * @param pagesize
	 * @return
	 */
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize);

	/**
	 * 保存数据
	 * 
	 * @param tbTable
	 * @return
	 */
	public boolean save(TbTable tbTable);

	/**
	 * 更改数据
	 * 
	 * @param tbTable
	 * @return
	 */
	public boolean update(TbTable tbTable);

	/**
	 * 查询单个数据
	 * 
	 * @param id
	 * @return
	 */
	public TbTable findById(String id);

	/**
	 * 查询单个数据
	 * 
	 * @param id
	 * @return
	 */
	public TbTable findByTableNo(String tableNo);

	/**
	 * 删除单个数据
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);

	/**
	 * 获取所有桌号
	 * 
	 * @author zhao
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> find(Map<String, Object> params);

	/**
	 * 取得数据字典
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getTableTag();

	public List<Map<String, Object>> getPrinterTag();

	public List<Map<String, Object>> getTableTag2();

	public List<Map<String, Object>> getTableTag3();

	public int updateStatus(TbTable tbTable);

	public String switchTable(Table table, ToperationLog toperationLog);

	public String mergetable(Table table, ToperationLog toperationLog);

	public String accountcash(AccountCash accountCash);

	public TbTable findTableNoAndAreaNameById(String tableId);

	public int updateCleanStatus(TbTable tbTable);

	public int updateSettleStatus(TbTable tbTable);

	public int updateSettleOrderNull(TbTable tbTable);

	/**
	 * 根据桌号查询桌子的基本信息，和打印机信息
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findDetail(Map<String, Object> params);

	public List<Map<String, Object>> getbuildingNoANDTableTypeTag();

	public List<TbTable> getTablesByTableType(String tableid);

	public boolean deleteTablesByAreaid(String areaid);

	public TbTable findByOrder(Map<String, Object> map);

	public TbTable findTableByOrder(String orderid);
	
	public String mergetableMultiMode(Table table, ToperationLog toperationLog) throws Exception;
}
