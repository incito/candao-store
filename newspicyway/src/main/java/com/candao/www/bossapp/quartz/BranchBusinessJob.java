package com.candao.www.bossapp.quartz;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.jms.Destination;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import com.candao.common.utils.DateUtils;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.BossappUtilDao;
import com.candao.www.data.dao.TBusinessDataDetailDao;
import com.candao.www.data.dao.TItemAnalysisChartsDao;
import com.candao.www.data.dao.TPreferentialAnalysisChartsDao;
import com.candao.www.data.dao.TReturnDishDetailDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class BranchBusinessJob {
	
	@Autowired
	private TBusinessDataDetailDao bussDataDao;
	
	@Autowired
	private TItemAnalysisChartsDao itemChartsDao;
	
	@Autowired
	private TPreferentialAnalysisChartsDao preferentialAnalysisChartsDao;
	
	@Autowired
	private TReturnDishDetailDao returnDishDetailDao;
	
	@Autowired
	@Qualifier("bossappJmsTemplate")
	private JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier("branchBusiness")
	private Destination branchBusiness;
	
	@Autowired
	@Qualifier("branchflow")
	private Destination branchflow;
	
	@Autowired
	private BossappUtilDao bossappDao;

	/**
	 * 执行门店营业数据上传
	 */
	@SuppressWarnings("resource")
	public void uploadBusinessData() {
		System.out.println("start BranchBusinessJob.." + DateUtils.currentStringDate());
		Set<String> orderTimeSet = new HashSet<String>();
		Connection conn = null;
		Statement statment = null;
		ResultSet rs = null;
		String branchId = PropertiesUtils.getValue("current_branch_id");
		String date = DateUtils.getBeforDay();
		try {
			Properties dbproperties = new Properties();     
			InputStream in =  BranchBusinessJob.class.getResourceAsStream("/jdbc.properties");
			dbproperties.load(in);
			String url = dbproperties.getProperty("master.jdbc.url").toString();  
			String user = dbproperties.getProperty("master.jdbc.username").toString();  
			String password= dbproperties.getProperty("master.jdbc.password").toString(); 
			conn = getConn(url,user,password);
			System.out.println(url);
			// 第一步:查询门店所有订单数据日期
			String ordersql = "select distinct begintime from t_order where begintime <= '" + date + " 23:59:59' ";
			//conn = daoSupport.getConnection();
			if(conn==null){
				System.out.println("获取数据库连接失败");
				return ;
			}
			statment = conn.createStatement();
			rs = statment.executeQuery(ordersql);
			if (rs != null) {
				while (rs.next()) {
					String begintime = rs.getString("begintime");
					if (StringUtils.isBlank(begintime) || begintime.length() < 10) {
						continue;
					}
					orderTimeSet.add(begintime.substring(0, 10));
					orderTimeSet.add(begintime.substring(0, 7));
				}
			}
			// 第三步:查询已经生成的数据的日期
			String businesssql = "select distinct business_date from t_branch_business";
			rs = statment.executeQuery(businesssql);
			if (rs != null) {
				while (rs.next()) {
					String branchdate = rs.getString("business_date");
					if (StringUtils.isBlank(branchdate) || branchdate.length() < 10) {
						continue;
					}
					orderTimeSet.remove(branchdate.substring(0, 10));
					orderTimeSet.remove(branchdate.substring(0, 7));
				}
			}
			// 第二步:查询上一天反结算的订单数据的日期
			String setordersql = "select distinct o.begintime from t_settlement s left join t_order o on o.orderid = s.orderid where s.inserttime >= '"+ date + " 00:00:00' and s.inserttime <= '" + date + " 23:59:59'";
			rs = statment.executeQuery(setordersql);
			if (rs != null) {
				while (rs.next()) {
					String begintime = rs.getString("begintime");
					if (StringUtils.isBlank(begintime) || begintime.length() < 10) {
						continue;
					}
					orderTimeSet.add(begintime.substring(0, 10));
					orderTimeSet.add(begintime.substring(0, 7));
				}
			}
			// 第四步:除去当天的日期和当天的月份
			orderTimeSet.remove(date.substring(0, 7));
			// 第五步:遍历没有数据的日期，生成数据，保存，上传到mq
			for (String dataDate : orderTimeSet) {
				System.out.println("生成数据：" + dataDate);
				JSONObject object = null;
				JSONArray flowarray = new JSONArray();
				if(dataDate.length()==10){//天
					String beginTime = dataDate + " 00:00:00";
			        String endTime = dataDate + " 23:59:59";
					object = getInfo(branchId,beginTime,endTime,dataDate,0);
					//上传流水
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("beginTime",beginTime );
					paramMap.put("endTime", endTime);
					paramMap.put("branchid",branchId );
					List<Map<String,Object>> flowList = bossappDao.getDayFlow(paramMap);
					if(flowList!=null&&flowList.size()>0){
						for(Map<String,Object> flow : flowList){
							if(flow==null||flow.size()!=3){
								continue;
							}
							String tableNo = String.valueOf(flow.get("tableNo"));
							String orderbegintime = String.valueOf(flow.get("begintime"));
							String ordershouldamount = String.valueOf(flow.get("shouldamount"));
							JSONObject orderobj = new JSONObject();
							orderobj.put("tableNo",tableNo );
							orderobj.put("orderbegintime",orderbegintime );
							orderobj.put("ordershouldamount", ordershouldamount);
							flowarray.add(orderobj);
						}
					}
					JSONObject dayfloworderobj = new JSONObject();
					dayfloworderobj.put("flowinfo", flowarray);
					dayfloworderobj.put("branchId", branchId);
					dayfloworderobj.put("date", dataDate);
					
					jmsTemplate.convertAndSend(branchflow, dayfloworderobj.toString());
					
				}else if(dataDate.length()==7){
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.YEAR, Integer.parseInt(dataDate.substring(0, 4)));
					calendar.set(Calendar.MONTH, Integer.parseInt(dataDate.substring(5, 7)));
					calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));  
					String beginTime = dataDate + "-01 00:00:00";
			        String endTime = DateUtils.toString(calendar.getTime()) + " 23:59:59";
			        object = getInfo(branchId,beginTime,endTime,dataDate,1);
				}
				if(object==null){
					continue;
				}
				
				StringBuffer insertsql = new StringBuffer("insert into t_branch_business (id,branch_id,business_date,shouldamount,paidinamount,discountamount,noonshouldamount,nightshouldamount,tablecount,tableconsumption,settlementnum,returnDishAmount,overtaiwan,memberPercent,couponName,couponShouldAmount,itemName,itemShouldAmount) values (");
				insertsql.append("'").append(IdentifierUtils.getId().generate().toString()).append("',");
				insertsql.append("'").append(branchId).append("',");
				insertsql.append("'").append(object.getString("date")).append("',");
				insertsql.append("'").append(object.containsKey("shouldamount")?object.getString("shouldamount"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("paidinamount")?object.getString("paidinamount"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("discountamount")?object.getString("discountamount"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("noonshouldamount")?object.getString("noonshouldamount"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("nightshouldamount")?object.getString("nightshouldamount"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("tablecount")?object.getString("tablecount"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("tableconsumption")?object.getString("tableconsumption"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("settlementnum")?object.getString("settlementnum"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("returnDishAmount")?object.getString("returnDishAmount"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("overtaiwan")?object.getString("overtaiwan"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("memberPercent")?object.getString("memberPercent"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("couponName")?new String(new BASE64Decoder().decodeBuffer(object.getString("couponName")),"UTF-8"):"").append("',");
				insertsql.append("'").append(object.containsKey("couponShouldAmount")?object.getString("couponShouldAmount"):"0.00").append("',");
				insertsql.append("'").append(object.containsKey("itemName")?new String(new BASE64Decoder().decodeBuffer(object.getString("itemName")),"UTF-8"):"").append("',");
				insertsql.append("'").append(object.containsKey("itemShouldAmount")?object.getString("itemShouldAmount"):"0.00").append("'");
				insertsql.append(")");
				
				statment = conn.createStatement();
				statment.execute("delete from t_branch_business where branch_id = '"+branchId+"' and business_date ='" +object.getString("date")+"'");
				statment.execute(insertsql.toString());
				object.put("is_save", "1");
				jmsTemplate.convertAndSend(branchBusiness, object.toString());
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(statment!=null){
				try {
					statment.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("end BranchBusinessJob.." + DateUtils.currentStringDate());
	}

	private JSONObject getInfo(String branchId, String beginTime, String endTime, String date, int type) {
		JSONObject object = new JSONObject();
		object.put("date", date);// 统计日期
		object.put("branchId", branchId);// 统计日期
		try{
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("branchid", branchId);
			params.put("shiftid", -1);
			params.put("beginTime", beginTime);
			params.put("endTime", endTime);
			List<Map<String, Object>> dayData = bussDataDao.isgetBusinessDetail(params);
			if(dayData!=null&&dayData.size()>0){
				Map<String, Object> dayInfoMap = dayData.get(0);
				object.put("shouldamount", dayInfoMap.containsKey("shouldamount")?dayInfoMap.get("shouldamount"):0.0);//应收总额
		        object.put("paidinamount", dayInfoMap.containsKey("paidinamount")?dayInfoMap.get("paidinamount"):0.0);//实收总额
		        object.put("discountamount", dayInfoMap.containsKey("discountamount")?dayInfoMap.get("discountamount"):0.0);//折扣总额
		        object.put("tablecount", dayInfoMap.containsKey("tablecount")?dayInfoMap.get("tablecount"):0.0);//营业数据统计(桌数）
		        object.put("tableconsumption", dayInfoMap.containsKey("tableconsumption")?dayInfoMap.get("tableconsumption"):0.0);//营业数据统计(桌均消费）  
		        object.put("settlementnum", dayInfoMap.containsKey("settlementnum")?dayInfoMap.get("settlementnum"):0.0);//营业数据统计(总人数）
		        object.put("overtaiwan", dayInfoMap.containsKey("overtaiwan")?dayInfoMap.get("overtaiwan"):0.0);//营业数据统计(翻台率）
		        object.put("memberPercent", dayInfoMap.containsKey("viporderpercent")?dayInfoMap.get("viporderpercent"):0.0);//会员消费占比
			}
			//品项
			params.put("dateType", type);
			params.put("branchId", branchId);
			List<Map<String, Object>> daypxData = itemChartsDao.itemAnalysisChartsForPro(params);
			if(daypxData!=null&&daypxData.size()>0){
	        	 float pxnum = 0;
	        	 String pxname = "";
	        	 for(Map<String, Object> pxmap : daypxData){
	        		 if(!pxmap.containsKey("showtype")||!pxmap.get("showtype").toString().equals("0")){
	        			 continue;
	        		 }
	        		 if(!pxmap.containsKey("total_num")){
	        			 continue;
	        		 }
	        		 float temppxnum = partFlaot(pxmap.get("total_num").toString());
	        		 if(temppxnum>pxnum){
	        			 pxnum =  temppxnum;
	        			 pxname = pxmap.containsKey("title")?pxmap.get("title").toString():"";
	        		 }
	        	 }
	        	 object.put("itemName", new BASE64Encoder().encode(pxname.getBytes("UTF-8")));//品项top1名称
	             object.put("itemShouldAmount", pxnum);//品项top1数量
	        }
			params.put("pi_branchid",branchId);
			params.put("pi_sb", -1);
			params.put("pi_ksrq", beginTime);
			params.put("pi_jsrq", endTime);
			params.put("pi_hdmc", -1);
			params.put("pi_jsfs", -1);
			params.put("pi_hdlx", -1);
			List<Map<String, Object>> dayyhData  = preferentialAnalysisChartsDao.findPreferential(params);
			// 优惠
			if(dayyhData!=null&&dayyhData.size()>0){
	        	float shouldamount = 0.0f;
	        	String name = "";
	        	for(Map<String,Object> info : dayyhData){
	        		if(info.containsKey("shouldamount")){
	        			float ampunt = partFlaot(info.get("shouldamount").toString());
	        			if(ampunt>shouldamount){
	        				shouldamount = ampunt;
	        				name = info.containsKey("pname")?info.get("pname").toString():"";
	        			}
	        		}
	        	}
	        	 object.put("couponName", new BASE64Encoder().encode(name.getBytes("UTF-8")));//优惠top1名称
	             object.put("couponShouldAmount", shouldamount);//优惠top1数量
	        }
			// 午市
			params.put("shiftid", 0);
			List<Map<String, Object>> noonData = bussDataDao.isgetBusinessDetail(params);
			if(noonData!=null&&noonData.size()>0&&noonData.get(0)!=null){
			    object.put("noonshouldamount", noonData.get(0).containsKey("shouldamount")?noonData.get(0).get("shouldamount"):0.0);//午市应收总额
	        }
	        //晚市
			params.put("shiftid", 1);
	        List<Map<String, Object>> nightData = bussDataDao.isgetBusinessDetail(params);
	        if(nightData!=null&&nightData.size()>0&&nightData.get(0)!=null){
	        	object.put("nightshouldamount", nightData.get(0).containsKey("shouldamount")?nightData.get(0).get("shouldamount"):0.0);//晚市应收总额
	        }
			// 退菜
	        params.put("pi_branchid", branchId);
	        params.put("pi_sb",-1 );
	        params.put("pi_ksrq",beginTime );
	        params.put("pi_jsrq", endTime);
	        params.put("pi_dqym", 0);
	        params.put("pi_myts", 100000);
	        
	        List<Map<String, Object>> tcData = returnDishDetailDao.getReturnDishList(params);
	        if(tcData!=null&&tcData.size()>0){
	        	float sum = 0;
	        	for(Map<String, Object> map : tcData){
	        		float temp = map.containsKey("num")?partFlaot(map.get("num").toString()):0.0f;
	        		sum += temp;
	        	}
	        	object.put("returnDishAmount", sum);//营业数据统计(退菜数）
	        }
	        
			return object;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	private float partFlaot(String value) {
		try {
			return Float.parseFloat(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0.0f;
	}
	
	private Connection getConn(String url,String user,String password){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
