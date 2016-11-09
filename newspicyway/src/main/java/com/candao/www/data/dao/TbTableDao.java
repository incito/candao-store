package com.candao.www.data.dao;


import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.Ttable;

/**
 * 数据访问接口
 *
 */
public interface TbTableDao {    
    public final static String PREFIX = TbTableDao.class.getName();
    
	public TbTable get(java.lang.String id);
	
	public <K, V> Map<K, V> findOne(java.lang.String id);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbTable tbTable);
	
	public int update(TbTable tbTable);
	
	public int delete(java.lang.String id );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	
	/**
	 * 取得数据分类
	 * @return
	 */
	public List<Map<String,Object>> getTableTag();
	public List<Map<String,Object>> getPrinterTag();
	public List<Map<String,Object>> getTableTag2();
	public List<Map<String,Object>> getTableTag3();

	public int updateStatus(TbTable tbTable);

	public <T, K, V> List<T> findIds(List<String> listIds);

	
	public int updateAllStatus(Map<String, Object> tableMap);

	public int   updateStatusByNo(TbTable table);

	public TbTable findTableNoAndAreaNameById(String tableId);

	public int updateCleanStatus(TbTable tbTable);

	public int updateSettleStatus(TbTable tbTable);

	public int updateSettleOrderNull(TbTable tbTable);
	/**
	 * 根据桌号查询桌子的基本信息，和打印机信息
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>>  findDetail(Map<String,Object> params);

	public List<Map<String, Object>> getbuildingNoANDTableTypeTag();

	



	public List<TbTable> getTablesByTableType(Map<String, Object> params);

	public int deleteTablesByAreaid(String areaid);

	/**
	 * 通过餐桌编号获取餐桌
	 * @author tom_zhao
	 * @param tableNo
	 * @return
	 */
	public TbTable getByTableNO(String tableNo);

	public TbTable findByOrder(Map<String, Object> map);
	public TbTable findTableByOrder(String orderid);
	
	public int updateByOrderNo(String sourceOrderid, String targetOrderid);
	
	public int updateTableById(Map<String, Object> map);
	
	/**
	 * 
	 * 菜单信息
	 * */
	public long getMenuInfoByCount(Map<String, Object> params);

	/**
	 * 更新订单号
	 * @param map
	 */
	public int updateTableByOrderId(Map<String, Object> map);

	/**
	 * 生成printobjid
	 * @return
     */
	public String generatePrintObjId();
	public Map<String, Object> getTableNoById(String tableId);
	public Map<String, Object> getByOrderId(String orderId);

	public int updatePosition(List<TbTable> tables);
}


