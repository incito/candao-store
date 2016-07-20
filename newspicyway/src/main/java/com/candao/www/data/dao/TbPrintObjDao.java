package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;

public interface TbPrintObjDao {
	
        public final static String PREFIX = TbPrintObjDao.class.getName();
    
		public PrintObj find(Map<String, Object> params);
		
		public <T, K, V> List<T> findDish(Map<K, V> params);
		
		public int update(PrintObj printObj);
		
		public int updateDish(PrintDish printDish);
		public int  updateDishWeight(Map<String, Object> map);
		
		public int insertPrintDish(PrintDish printDish);

		public int  deleteDish(Map<String, Object> map);

		public int deletePrintObj(Map<String, Object> map2);

		public List<PrintDish> findNowDish(Map<String, Object> map);

		public List<PrintDish> findDishExceptNow(Map<String, Object> map);

		public List<PrintDish> findDishType1(Map<String, Object> map) ;

		public List<PrintDish> findDishType(Map<String, Object> map);

		public int updateDishCall(Map<String, Object> printObjMap);

		public List<PrintDish> findDishType3(Map<String, Object> map);

		public List<PrintDish> findDishGroupByParentKey(Map<String, Object> map0);

		public List<PrintDish> findDishNoPot(Map<String, Object> map0);

		public List<PrintDish> findPrinterByDishId(String dishId);
		public void deletefishpot(Map<String, Object> map);

		public List<PrintDish> findDishByPrimaryKey(Map<String, Object> map0);

		public void updateDishByPrimaryKey(Map<String, Object> map0);

		public void updateDetailByPrimaryKey(Map<String, Object> map0);

		public List<PrintDish> findDishByObjId(Map<String, Object> map0);

		public void updateDishByObjId(Map<String, Object> map0);

		public void updateDetailByObjId(Map<String, Object> map0);
		
		public List<PrintDish> findDishGroupBySuperKey(Map<String,Object> map);

		public int findPrintTable(Map<String, Object> paramMap);
		
		public int updatePrintdishForMerge(String sourceOrderid, String targetOrderid);
		
		public int updateByOrderno(Map<String, Object> paramMap);

		public <T, K, V> List<T> findDishBycolumn(Map<K, V> params);
		public int insertPrintDishBatch(List<PrintDish> printDishs);
		public int insertPrintObj(PrintObj printObj);

}
