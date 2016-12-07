package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.candao.common.page.Page;
import com.candao.www.webroom.model.Code;
import com.candao.www.webroom.model.CouponsRept;
import com.candao.www.webroom.model.CouponsReptDtail;
import com.candao.www.webroom.model.DataStatistics;
import com.candao.www.webroom.model.Dishsalerept;
import com.candao.www.webroom.model.ItemReport;
import com.candao.www.webroom.model.PaywayRpet;
import com.candao.www.webroom.model.PreferentialReport;
import com.candao.www.webroom.model.SettlementReport;
import com.candao.www.webroom.model.TableArea;
import com.candao.www.webroom.model.TjObj;

public interface DayIncomeBillService {

	public List<TjObj> getDaliyReport(Map<String, Object> params);

	public Page<Map<String, Object>> getDaliyReport1(Map<String, Object> params, int page, int rows);

	public List<CouponsReptDtail> findCouponsDtail(Map<String, Object> params);

	public List<CouponsRept> findCoupons(Map<String, Object> params);



	public List<ItemReport> findItemReport(Map<String, Object> params);

	public List<ItemReport> findItemNumQushiReport(Map<String, Object> params);

	public List<ItemReport> findItemSharezhuzhuangReport(Map<String, Object> params);

	public List<ItemReport> findItemShareQushiReport(Map<String, Object> params);

	public List<SettlementReport> findSettlementReport(Map<String, Object> params);

	public List<TjObj> getDaliyReport2(Map<String, Object> params);


	public List<Dishsalerept> findItemDetail(Map<String, Object> params);

	public Page<Map<String, Object>> gridB(Map<String, Object> params, int current, int pagesize);

	public List<PaywayRpet> gridC(Map<String, Object> params);

	public  List<DataStatistics> findDataStatisticsHalf(Map<String, Object> params);
	
	public List<DataStatistics> findDataStatisticsDay(Map<String, Object> params);
	
	public List<PreferentialReport> findGroupByNmaeReport(Map<String, Object> params);

	public List<PreferentialReport> findGroupByNmaeAndTimeReport(Map<String, Object> params);

	public List<PreferentialReport> findGroupBytypeReport(Map<String, Object> params);

	public List<PreferentialReport> findGroupBytypeAndTimeReport(Map<String, Object> params);
	
	public List<PreferentialReport> findGroupByPaywayandName(Map<String, Object> params);
	
	public List<PreferentialReport> findGroupByPaywayandNameandtiem(Map<String, Object> params);
	
	public List<PreferentialReport> findGroupByPaywayandType(Map<String, Object> params);
	
	public List<PreferentialReport> findGroupByPaywayandTypeandTime(Map<String, Object> params);
	
	public List<Code> findCode(Map<String, Object> params);
	
	public List<Code> findPreferentialActivity (Map<String, Object> params);
	
	public List<Code> findPreferentialTypeDict(Map<String, Object> params);
	
	public  List<Code>  getTableNo(Map<String, Object>  params);

	public List<TableArea> findTableArea(Map<String, Object> params);

	/**
	 * 查询品项销售明细列表
	 *
	 * @param params
	 * @return
	 * @author weizhifang
	 * @since 2015-5-15
	 */
	public List<Map<String,Object>> getItemDetailList(Map<String, Object> params);
	
	/**
	 * 查询品项列表
	 * @author weizhifang
	 * @since 2015-5-24
	 */
	public List<Map<String,Object>> getItemReportForList(Map<String,Object> params);

	/**
	 * 查询品类列表
	 *
	 * @param itemid
	 * @return
	 * @author weizhifang
	 * @since 2015-5-16
	 */
	public List<Code> getItemDescList();

	/**
	 * 退菜明细列表
	 * @author weizhifang
	 * @since 2015-05-22
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getReturnDishList(Map<String, Object> params);
	
	/**
	 * 导出日报表
	 *
	 * @param params
	 */
	public void exportDaliyRport(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception;


	public void exportDaliyRport2(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception;


	public void exportxlsB(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception;

	public void exportxlsC(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception;

}