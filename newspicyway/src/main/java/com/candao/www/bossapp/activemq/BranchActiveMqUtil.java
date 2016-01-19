package com.candao.www.bossapp.activemq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.BossappUtilDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * bossapp 上传数据工具类
 * @author YANGZHONGLI
 *
 */
public class BranchActiveMqUtil {
	
	private static String UPDATE_TIME = "";
	
	@Autowired
	@Qualifier("bossappJmsTemplate")
	private JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier("branchBusiness")
	private Destination branchBusiness;
	
	@Autowired
	@Qualifier("branchOrder")
	private Destination branchOrder;
	
	@Autowired
	@Qualifier("branchOrderClear")
	private Destination branchOrderClear;
	
	@Autowired
	@Qualifier("branchTables")
	private Destination branchTables;
	
	@Autowired
	private BossappUtilDao bossappDao;
	
	/**
	 * 上传桌子信息
	 */
	public void uploadTablesInfo(){
		String branchid =  PropertiesUtils.getValue("current_branch_id");
		//查询所有桌子信息
		List<Map<String,Object>> tablesInfo = bossappDao.getAllTablesInfo(branchid);
		if(tablesInfo==null||tablesInfo.size()<=0){
			return ;
		}
		JSONArray tables = new JSONArray();
		for(Map<String,Object> table : tablesInfo){
			if(table==null||table.size()!=3){
				continue;
			}
			JSONObject obj = new JSONObject();
			String tableid = String.valueOf(table.get("tableid"));
			String tableNo = String.valueOf(table.get("tableNo"));
			String personNum = String.valueOf(table.get("personNum"));
			obj.put("tableNo", tableNo);
			obj.put("tableid", tableid);
			obj.put("personNum", personNum);
			tables.add(obj);
		}
		JSONObject tablesObj = new JSONObject();
		tablesObj.put("branchId", branchid);
		tablesObj.put("info", tables.toString());
		
		
		if(tables.size()>0){
			jmsTemplate.convertAndSend(branchTables, tablesObj.toString());
		}
	}
	
	
	/**
	 * 上传订单信息
	 * type 1 标示结账
	 */
	public void uploadOrderInfo(String orderid,int type){
		
		if(StringUtils.isBlank(orderid)){
			return;
		}
		
		String branchid =  PropertiesUtils.getValue("current_branch_id");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("orderid", orderid);
		Map<String, Object> orderInfo = null;
		if(type==0){
			orderInfo = bossappDao.getOrderInfoTemp(paramMap);
		}else if(type==1){
			orderInfo = bossappDao.getOrderInfo(paramMap);
			uploadBusinessInfo();
		}
		if(orderInfo==null||orderInfo.size()!=7){
			return ;
		}
		JSONObject obj = new JSONObject();
		String tableids = String.valueOf(orderInfo.get("tableids"));
		String begintime = String.valueOf(orderInfo.get("begintime"));
		String custnum = String.valueOf(orderInfo.get("custnum"));
		String endtime = String.valueOf(orderInfo.get("endtime"));
		String orderstatus = String.valueOf(orderInfo.get("orderstatus"));
		String shouldamount = String.valueOf(orderInfo.get("shouldamount"));
		
		obj.put("tableids", tableids);
		obj.put("orderid", orderid);
		obj.put("begintime", begintime);
		obj.put("custnum", custnum);
		obj.put("endtime", endtime);
		obj.put("orderstatus", orderstatus);
		obj.put("shouldamount", shouldamount);
		obj.put("branchId", branchid);
		
		jmsTemplate.convertAndSend(branchOrder, obj.toString());
		
		if(UPDATE_TIME.equals("")||!UPDATE_TIME.equals(DateUtils.currentStringDate())){
			UPDATE_TIME = DateUtils.currentStringDate();
			uploadTablesInfo();
		}
	}
	
	/**
	 * 上传清台订单信息
	 */
	public void uploadClearOrderInfo(String orderid){
		if(StringUtils.isBlank(orderid)){
			return;
		}
		String branchid =  PropertiesUtils.getValue("current_branch_id");
		JSONObject orderobj = new JSONObject();
		orderobj.put("orderid", orderid);
		orderobj.put("branchId", branchid);
		
		jmsTemplate.convertAndSend(branchOrderClear, orderobj);
	}
	
	/**
	 * 上传当天和当月营业信息
	 */
	public void uploadBusinessInfo(){
		
	}
}
