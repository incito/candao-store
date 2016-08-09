package com.candao.www.data.dao;
import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.webroom.model.Code;
import com.candao.www.webroom.model.PreferentialReport;
import com.candao.www.webroom.model.TableArea;
 
public interface TbDayIncomeBillDao {
	public final static String PREFIX = TbDayIncomeBillDao.class.getName();
	
	public <T, K, V> List<T> getBusinessDetail(Map<K, V> params);
	
	public <T, K, V> List<T> find(Map<K, V> params);

	public <T, K, V> List<T> findO(Map<K, V> params);
	
	public <T, K, V> List<T> findT(Map<K, V> params);

	public <T, K, V> List<T> findTh(Map<K, V> params);
	
	public <T, K, V> List<T> findF(Map<K, V> params);
	
	public <T, K, V> List<T> findFv(Map<K, V> params);
	
	public <T, K, V> List<T> page(Map<K, V> params);
	
	public <T, K, V> List<T> findItemDetail(Map<K, V> params);
	
	public <E, K, V> Page<E> pageB(Map<K, V> params, int current, int pagesize);
	
	public <T, K, V> List<T> pageC(Map<K, V> params);
	
	public <T, K, V> List<T> findFA(Map<K, V> params);
	
	public <T, K, V> List<T> findFB(Map<K, V> params);
	
	public <T, K, V> List<T> findFC(Map<K, V> params);

	public <E, K, V> Page<E> pageCoupons(Map<String, Object> params, int current, int pagesize);
	
	public  <T, K, V> List<T> findCouponsDtail(Map<String, Object> params);
	
	public <T, K, V> List<T> findCoupons(Map<K, V> params) ; 
	
	public <T, K, V> List<T> findBusinessReport(Map<K, V> params);

	public <T, K, V> List<T> getorderMembervalue(Map<K, V> params);
	
    public  <T, K, V> List<T> findDataStatisticsHalf(Map<K, V> params);
    
    public  <T, K, V> List<T> findDataStatisticsDay(Map params);
    
	public  <T, K, V> List<T> findCode(Map<K, V> params);
	
	public <T, K, V> List<T> findPreferentialActivity (Map<K, V> params);
	
	public <T, K, V> List<T> findPreferentialTypeDict(Map<K, V> params);

	public  <T, K, V> List<T> findTableArea(Map<K, V> params);
	
	public  <T, K, V> List<T> getTableNum(Map<K, V> params);
	
	public  <T, K, V> List<T> getTableNo(Map<K, V> params);
    /**
     *优惠活动分析
     * @param params
     * @return
     */
	public  <T, K, V> List<T> findGroupByNmaeReport(Map<K, V> params);
	
	public  <T, K, V> List<T> findGroupByNmaeAndTimeReport(Map<K, V> params);
	
	public  <T, K, V> List<T> findGroupBytypeReport(Map<K, V> params);
	
	public  <T, K, V> List<T> findGroupBytypeAndTimeReport(Map<K, V> params);
	 
	public  <T, K, V> List<T> findSettlementReport(Map<K, V> params);
	
	/**
	 * 优惠活动图标分析（根据支付方式分组查询）
	 * @param params
	 * @return
	 */
    public <T, K, V> List<T> findGroupByPaywayandName(Map<K, V> params);
	
	public <T, K, V> List<T> findGroupByPaywayandNameandtiem(Map<K, V> params);
	
	public <T, K, V> List<T> findGroupByPaywayandType(Map<K, V> params);
	
	public <T, K, V> List<T> findGroupByPaywayandTypeandTime(Map<K, V> params);
	
	/**
	 * 品项分析
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> findItemNumQushiReport(Map<K, V> params);
	
	public  <T, K, V> List<T> findItemReport(Map<K, V> params);
	
	public <T, K, V> List<T> findItemSharezhuzhuangReport(Map<K, V> params);
	
	public <T, K, V> List<T> findItemShareQushiReport(Map<K, V> params);
	
	 /**
	  * 查询品项销售明细列表
	  * @author weizhifang
	  * @since 2015-5-15
	  * @param params
	  * @return
	  */
	 public <T, K, V> List<T> getItemDetailList(Map<K, V> params);
	 
	 /**
	  * 查询品项列表
	  * @author weizhifang
	  * @since 2015-5-24
	  */
	 public <T, K, V> List<T> getItemReportForList(Map<K, V> params);
	 
	 /**
	  * 查询品类列表
	  * @author weizhifang
	  * @since 2015-5-16
	  * @param itemid
	  * @return
	  */
	 public List<Map<String,Object>> getItemDescList();
	 
	 public int getDishidCount(Map<String,Object> params);
	 
	 public <T, K, V> List<T> getDishidCountbydishid(Map<K, V> params);
	 
	 /**
	  * 退菜明细列表
	  * @author weizhifang
	  * @since 2015-05-22
	  * @param params
	  * @return
	  */
	 public <T, K, V> List<T> getReturnDishList(Map<K, V> params);
	 
	 /**
	  * 查询餐具
	  * @author weizhifang
	  * @since 2015-5-28
	  * @param params
	  * @return
	  */
	 public <T, K, V> List<T> getDishesList(Map<K, V> params);
	 
	 /**
	  * 得到品项数量总和
	  * @author weizhifang
	  * @since 2015-6-2
	  * @param params
	  * @return
	  */
	 public Double getAllItemReportCout(Map<String,Object> params);

	/**
	 * 獲取虛增值的問題
	 * @param params
	 * @param <T>
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public <T, K, V> List<T> getxuzheng(Map<K, V> params);
	
	/**
	 * 查询退菜授权人
	 * @author weizhifang
	 * @since 2015-06-24
	 * @param params
	 * @return
	 */
	public String findUserNameBydiscarduserid(Map<String,Object> params);
}