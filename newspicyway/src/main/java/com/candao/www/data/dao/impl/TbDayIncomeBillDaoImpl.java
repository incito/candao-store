package com.candao.www.data.dao.impl;
import java.util.HashMap;
import java.util.List;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbDayIncomeBillDao;
import com.candao.www.webroom.model.BusinessReport;
import com.candao.www.webroom.model.ItemReport;
import com.candao.www.webroom.model.PreferentialReport;
import com.candao.www.webroom.model.SettlementReport;


@Repository
public class TbDayIncomeBillDaoImpl<K> implements TbDayIncomeBillDao {

	@Autowired
    private DaoSupport dao;
	
    @Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return  dao.find(PREFIX + ".find", params);
	}

	@Override
	public <T, K, V> List<T> findO(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findO", params);
	}

	@Override
	public <T, K, V> List<T> findT(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findT", params);
	}
	
	@Override
	public <T, K, V> List<T> findTh(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findTh", params);
	}

	@Override
	public <T, K, V> List<T> findF(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findF", params);
	}
	@Override
	public  <T, K, V> List<T> getTableNum(Map<K, V> params){
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".getTableNum", params);
	}
	@Override
	public  <T, K, V> List<T> getTableNo(Map<K, V> params){
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".getTableNo", params);
	}

	@Override
	public <T, K, V> List<T> findFv(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findFv", params);
	}
	
	@Override
	public  <T, K, V> List<T> findDataStatisticsHalf(Map<K, V> params){
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".getDataStatisticsHalf", params);
	}
	@Override
	public  <T, K, V> List<T> findDataStatisticsDay(Map params){
		if(params.get("shiftId")!=null&&params.get("shiftId").toString().equals("-1")) {
			params.put("shiftId", "");
		}
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".getDataStatisticsDay", params);
	}
    
	@Override
	public <T, K, V> List<T> findItemNumQushiReport(Map<K, V> params){
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findItemNumQushiReport", params);
	}
	
	@Override
	public <T, K, V> List<T> findItemSharezhuzhuangReport(Map<K, V> params){
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findItemSharezhuzhuangReport", params);
	}
	
	@Override
	public <T, K, V> List<T> findItemShareQushiReport(Map<K, V> params){
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findItemShareQushiReport", params);
	}
	@Override
	public <T, K, V> List<T> page(Map<K, V> params) {
		return dao.find(PREFIX + ".page", params);
	}
	@Override
	public <T, K, V> List<T> findItemDetail(Map<K, V> params) {
		return dao.find(PREFIX + ".itemDetail", params);
	}
	@Override
	public <E, K, V> Page<E> pageB(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".pageB", params, current, pagesize);
	}
	
	@Override
	public<T, K, V> List<T> pageC(Map<K, V> params) {
		return dao.find(PREFIX + ".pageC", params);
	}

	@Override
	public <T, K, V> List<T> findFA(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".page", params);
	}

	@Override
	public <T, K, V> List<T> findFB(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".pageB", params);
	}

	@Override
	public <T, K, V> List<T> findFC(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".pageC", params);
	}
	/**
	 * 营业图标数据明细查询
	 */
	@Override
	public <T, K, V> List<T> getBusinessDetail(Map<K, V> params){
		return dao.find(PREFIX + ".getBusinessDetail", params);
	}
	/**
	 * 优惠明细数据统计
	 */
	@Override
	public <E, K, V> Page<E> pageCoupons(Map<String, Object> params,
			int current, int pagesize) {
		return dao.page(PREFIX + ".pageCoupons", params, current, pagesize);
	}
	/**
	 * 优惠明细数据明细统计
	 */
	@Override
	public <T, K, V> List<T> findCouponsDtail(Map<String, Object> params) {
		return dao.find(PREFIX + ".pageCouponsDtail", params);
	}
	
	@Override
	public <T, K, V> List<T> findCoupons(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".pageCoupons", params);
	}
	 @Override
	 public <T, K, V> List<T> findBusinessReport(Map<K, V> params){
		 return dao.find(PREFIX + ".getreportbuiss", params);
	 }

	@Override
	 public <T, K, V> List<T> getorderMembervalue(Map<K, V> params){
		 return dao.find(PREFIX + ".getorderMembervalue", params);
	 }
	@Override
	 public <T, K, V> List<T> findItemReport(Map<K, V> params){
		 return dao.find(PREFIX + ".findItemReport", params);
	 }
	@Override
	 public <T, K, V> List<T> findSettlementReport(Map<K, V> params){
		 return dao.find(PREFIX + ".findSettlementReport", params);
	 }
	@Override
	public  <T, K, V> List<T> findGroupByNmaeReport(Map<K, V> params){
		 return dao.find(PREFIX + ".findGroupByNmaeReport", params);
	}
	@Override
	public  <T, K, V> List<T> findGroupByNmaeAndTimeReport(Map<K, V> params){
		return dao.find(PREFIX + ".findGroupByNmaeAndTimeReport", params);
	}
	@Override
	public  <T, K, V> List<T> findGroupBytypeReport(Map<K, V> params){
		return dao.find(PREFIX + ".findGroupBytypeReport", params);
	}
	@Override
	public  <T, K, V> List<T> findGroupBytypeAndTimeReport(Map<K, V> params){
		return dao.find(PREFIX + ".findGroupBytypeAndTimeReport", params);
	}
	/**
	 * 优惠活动图标分析（根据支付方式分组查询）
	 * @param params
	 * @return
	 */
	@Override
    public <T, K, V> List<T> findGroupByPaywayandName(Map<K, V> params){
		return dao.find(PREFIX + ".findGroupByPaywayandName", params);
	}
	@Override
	public <T, K, V> List<T> findGroupByPaywayandNameandtiem(Map<K, V> params){
		return dao.find(PREFIX + ".findGroupByPaywayandNameandtiem", params);
	}
	@Override
	public <T, K, V> List<T> findGroupByPaywayandType(Map<K, V> params){
		return dao.find(PREFIX + ".findGroupByPaywayandType", params);
	}
	@Override
	public <T, K, V> List<T> findGroupByPaywayandTypeandTime(Map<K, V> params){
		return dao.find(PREFIX + ".findGroupByPaywayandTypeandTime", params);
	}
	
	@Override
	public  <T, K, V> List<T> findCode(Map<K, V> params){
		return dao.find(PREFIX + ".getCode", params);
	}
	
    public <T, K, V> List<T> findPreferentialActivity (Map<K, V> params){
    	return dao.find(PREFIX + ".getPreferentialActivity", params);
    }
	
	public <T, K, V> List<T> findPreferentialTypeDict(Map<K, V> params){
		return dao.find(PREFIX + ".getPreferentialTypeDict", params);
	}
	@Override
	public  <T, K, V> List<T> findTableArea(Map<K, V> params){
		return dao.find(PREFIX + ".getTablearea", params);
	}
   /**
    * 查询品项销售明细列表
    * @author weizhifang
    * @since 2015-5-15
    * @param params
    * @return
    */
   public <T, K, V> List<T> getItemDetailList(Map<K, V> params){
//	   return dao.find(PREFIX + ".pageItemDetail", params);
	   return dao.find(PREFIX + ".getItemDetailForDishid", params);
   }
   
   /**
    * 查询品项列表
    * @author weizhifang
    * @since 2015-5-24
    */
   public <T, K, V> List<T> getItemReportForList(Map<K, V> params){
	   return dao.find(PREFIX + ".getItemReportForList",params);
   }
   
     /**
	  * 查询品类列表
	  * @author weizhifang
	  * @since 2015-5-16
	  * @param itemid
	  * @return
	  */
	 public List<Map<String,Object>> getItemDescList(){
		 return dao.find(PREFIX + ".getItemDescList");
	 }
	 
	 @Override
	 public <T, K, V> List<T> getDishidCountbydishid(Map<K, V> params){
		 return dao.find(PREFIX + ".getDishidCountbydishid", params);
	 }

	 @Override
	 public int getDishidCount(Map params) {
		 int result = dao.getSqlSessionTemplate().selectOne(PREFIX + ".getDishidCount", params);
		 return result;
	 }
	 
	 /**
	  * 退菜明细列表
	  * @author weizhifang
	  * @since 2015-05-22
	  * @param params
	  * @return
	  */
	 @Override
	 public <T, K, V> List<T> getReturnDishList(Map<K, V> params){
		 return dao.find(PREFIX + ".getReturnDishList", params);
	 }
	
	 /**
	  * 查询餐具
	  * @author weizhifang
	  * @since 2015-5-28
	  * @param params
	  * @return
	  */
	 public <T, K, V> List<T> getDishesList(Map<K, V> params){
		 return dao.find(PREFIX + ".getDishesList", params);
	 }
	 
	 /**
	  * 得到品项数量总和
	  * @author weizhifang
	  * @since 2015-6-2
	  * @param params
	  * @return
	  */
	 public Double getAllItemReportCout(Map<String,Object> params){
		 return dao.getSqlSessionTemplate().selectOne(PREFIX + ".getAllItemReportCout", params);
	 }


	@Override
	public <T, K, V> List<T> getxuzheng(Map<K, V> params){
		return dao.find(PREFIX + ".getxuzheng", params);
	}

	/**
	 * 查询退菜授权人
	 * @author weizhifang
	 * @since 2015-06-24
	 * @param params
	 * @return
	 */
	public String findUserNameBydiscarduserid(Map<String,Object> params){
		return dao.getSqlSessionTemplate().selectOne(PREFIX + ".findUserNameBydiscarduserid", params);
	}


}
