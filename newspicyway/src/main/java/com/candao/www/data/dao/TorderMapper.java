package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Torder;
import com.candao.www.webroom.model.TableStatus;

public interface TorderMapper {
//    int insert(Torder record);
//
//    int insertSelective(Torder record);
//    
    
    public final static String PREFIX = TorderMapper.class.getName();
    
  	public Torder get(java.lang.String id);
  	
  	public <K, V> Map<K, V> findOne(java.lang.String id);
  	
  	public <T, K, V> List<T> find(Map<K, V> params);
  	
  	public int insert(Torder torder);
  	
  	public int update(Torder torder);
  	
  	public int updateInvoiceid(Torder torder);
  	
  	public int updateOrderMeid(Torder torder);
  	
  	public int delete(java.lang.String id );

  	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

	public String getPrimaryKey();

	public int updateAllTableNo(Map<String, Object> orderMap);

	public List<Torder> findByOrderNo(Map<String, Object> map);

	public List<Torder> findOrdersByTableids(Map<String, Object> mapOrder);
	
	public List<Torder> findontimeOrdersByTableids(Map<String, Object> mapOrder);

//	public int updateSwitchTable(Map<String, Object> mapParam);
	public List<TableStatus> selectSwitchTable(Map<String, Object> mapParam);

	public List<TableStatus> selectMergeTable(Map<String, Object> mapParam);

	public  List<TableStatus> setOrderDish(Map<String, Object> mapParam);

	public Torder getMaxOrderNum(String orderIdDate);

	public void executeSql(String sql);
	
	public void setOrderMember(Map<String, Object> mapParam);

 
	public List<Torder> verifyAllOrder();

//	public int verifyAllCLean();
//
//	public int verifyAllTableClear();

	public String callEndWork(String userName, String isSucess);

	/**
	 * 查询是否结账
	 * @param orderid
	 * @return
	 */
	public int selectIsJZ(String orderid);

 
}