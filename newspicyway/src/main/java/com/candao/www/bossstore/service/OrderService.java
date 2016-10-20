package com.candao.www.bossstore.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.bossstore.dao.TOrderDao;
import com.candao.www.bossstore.dao.TTableDao;
import com.candao.www.bossstore.util.DateTimeUtils;
import com.candao.www.bossstore.util.DateUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: 张吉超 johnson
 * Date: 7/13/15
 * Time: 1:47 下午
 */
@Service("bOrderService")
@Transactional(readOnly=false)
public class OrderService {

	@Autowired
	private TOrderDao tOrderDao;
	@Autowired
	private TTableDao tTableDao;
	private static Map<String,List<Map<String, Object>>>  flowDataMap = new HashMap<String,List<Map<String, Object>>>();
	
	
	private static Map<String,JSONObject>  yyDataMap = new HashMap<String,JSONObject>();
    /**
     * 获取当月每天营业明细
     * @return  返回json结果
     */
    public JSONArray getCurrMonDataByDay() {
    	
        String branchId = PropertiesUtils.getValue("current_branch_id");

        JSONArray array = new JSONArray();
        // 获取当月的天数列表
        List<String> dayList = DateTimeUtils.getCurrentDayList();
        
        for (int i = 0; i < dayList.size(); i++) {
        	String date = dayList.get(i);
        	
        	if(yyDataMap.containsKey(date)){
        		JSONObject object  = yyDataMap.get(date);
        		array.add(object);
        		continue;
        	}
        	
            String beginTime = date + " 00:00:00";
            String endTime = date + " 23:59:59";
            
            JSONObject object = getInfo(branchId,beginTime,endTime,date,0);
            
            if(!date.equals(DateTimeUtils.getCurrentDate())){
            	yyDataMap.put(date, object);
            }
            array.add(object);
        }
        return array;
    }

    private JSONObject getInfo(String branchId,String beginTime,String endTime,String date,int type){
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("branchId", branchId);
    	params.put("beginTime", beginTime);
    	params.put("endTime", endTime);
    	params.put("xslx", -1);
    	List<Map<String, Object>> dayData = tOrderDao.getBusinessData(params);
        if(dayData==null||dayData.size()<=0){
        	return null;
        }
        Map<String, Object> dayInfoMap = dayData.get(0);
        JSONObject object = new JSONObject();
        object.put("date", date);//统计日期
        object.put("shouldamount", dayInfoMap.containsKey("shouldamount")?dayInfoMap.get("shouldamount"):0.0);//应收总额
        object.put("paidinamount", dayInfoMap.containsKey("paidinamount")?dayInfoMap.get("paidinamount"):0.0);//实收总额
        object.put("discountamount", dayInfoMap.containsKey("discountamount")?dayInfoMap.get("discountamount"):0.0);//折扣总额
        object.put("tablecount", dayInfoMap.containsKey("tablecount")?dayInfoMap.get("tablecount"):0.0);//营业数据统计(桌数）
        object.put("tableconsumption", dayInfoMap.containsKey("tableconsumption")?dayInfoMap.get("tableconsumption"):0.0);//营业数据统计(桌均消费）  
        object.put("settlementnum", dayInfoMap.containsKey("settlementnum")?dayInfoMap.get("settlementnum"):0.0);//营业数据统计(总人数）
        object.put("overtaiwan", dayInfoMap.containsKey("overtaiwan")?dayInfoMap.get("overtaiwan"):0.0);//营业数据统计(翻台率）
        object.put("memberPercent", dayInfoMap.containsKey("viporderpercent")?dayInfoMap.get("viporderpercent"):0.0);//会员消费占比
        //品项
    	params.put("xslx", type);
        List<Map<String, Object>> daypxData = tOrderDao.getpxBusinessData(params);
        if(daypxData!=null&&daypxData.size()>0){
        	 float pxnum = 0;
        	 String pxname = "品项";
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
        	 object.put("itemName", pxname);//品项top1名称
             object.put("itemShouldAmount", pxnum);//品项top1数量
        }else{
        	object.put("itemName", "品项");//品项top1名称
            object.put("itemShouldAmount", 0);//品项top1数量
        }
        //优惠
        List<Map<String, Object>> dayyhData = tOrderDao.getyhBusinessData(params);
        if(dayyhData!=null&&dayyhData.size()>0){
        	float shouldamount = 0.0f;
        	String name = "优惠";
        	for(Map<String,Object> info : dayyhData){
        		if(info.containsKey("shouldamount")){
        			float ampunt = partFlaot(info.get("shouldamount").toString());
        			if(ampunt>shouldamount){
        				shouldamount = ampunt;
        				name = info.containsKey("pname")?info.get("pname").toString():"";
        			}
        		}
        	}
        	 object.put("couponName", name);//优惠top1名称
             object.put("couponShouldAmount", shouldamount);//优惠top1数量
        }else{
        	object.put("couponName", "优惠");//优惠top1名称
            object.put("couponShouldAmount", 0);//优惠top1数量
        }
        //午市
        params.put("xslx", 0);
        List<Map<String, Object>> noonData = tOrderDao.getBusinessData(params);
        if(noonData!=null&&noonData.size()>0&&noonData.get(0)!=null){
        	object.put("noonshouldamount", noonData.get(0).containsKey("shouldamount")?noonData.get(0).get("shouldamount"):0.0);//午市应收总额
        }
        //晚市
        params.put("xslx", 1);
        List<Map<String, Object>> nightData = tOrderDao.getBusinessData(params);
        if(nightData!=null&&nightData.size()>0&&nightData.get(0)!=null){
        	object.put("nightshouldamount", nightData.get(0).containsKey("shouldamount")?nightData.get(0).get("shouldamount"):0.0);//晚市应收总额
        }
        //退菜
        params.put("xslx", -1);
        List<Map<String, Object>> tcData = tOrderDao.gettcBusinessData(params);
        if(tcData!=null&&tcData.size()>0){
        	Integer sum = 0;
        	for(Map<String, Object> map : tcData){
        		String num = map.containsKey("num")?map.get("num").toString():null;
        		Integer temp = num!=null?Integer.valueOf(num.substring(0,num.indexOf("."))):0;
        		sum += temp;
        	}
        	object.put("returnDishAmount", sum);//营业数据统计(退菜数）
        }
        
        return object;
    }

    /**
     * 按月获取当年的营业明细
     * @return  返回json结果
     */
        
        public JSONArray getBusinessDataByMon() {
        	System.out.println("获取月：");
        
        JSONArray orders = new JSONArray();
        	
        String branchId = PropertiesUtils.getValue("current_branch_id");

        Calendar calendar = Calendar.getInstance();
        
        int num = calendar.get(Calendar.MONTH);
        for(int i = 0 ; i <= num ; i ++){
        	calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.DATE, 1);
            String beginTime = DateUtils.toString(calendar.getTime())+" 00:00:00";
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String endTime = DateUtils.toString(calendar.getTime())+" 23:59:59";
            String mouth = calendar.get(Calendar.MONTH)+1<10?"0"+(calendar.get(Calendar.MONTH)+1):(calendar.get(Calendar.MONTH)+1)+"";
            String date = calendar.get(Calendar.YEAR)+"-"+mouth;
            if(i==num){
            	endTime = DateTimeUtils.getCurrentDate()+" 23:59:59";
            }
            if(yyDataMap.containsKey(date)){
        		JSONObject object  = yyDataMap.get(date);
        		orders.add(object);
        		continue;
        	}
            JSONObject object = getInfo(branchId,beginTime,endTime,date,1);
            if(i!=num){
            	yyDataMap.put(date, object);
            }
            orders.add(object);
        }
        return orders;
    }

    /**
     * 获取当月营业明细数据
     * @param monTime 月份
     * @param branchId 门店id
     * @return map格式的数据
     */
    private Object getMonBusinessData(String monTime, String branchId) {
//        String beginTime = getTime(monTime, "0");// 存储过程参数：开始时间
//
//        String endTime = getTime(monTime, "1");// 存储过程参数：结束时间
//        Map<String, Object> allDayData = tOrderDao.getBusinessData(branchId, beginTime, endTime, "-1");
//        Map<String, Object> noonData = tOrderDao.getBusinessData(branchId, beginTime, endTime, "0");
//        Map<String, Object> nightData = tOrderDao.getBusinessData(branchId, beginTime, endTime, "1");
//
//        Object discardCount = tOrderDao.getDiscardCount(beginTime, endTime); // 退菜数
//        Object itemTop = tOrderDao.getItemTopOne(beginTime, endTime); // 品项top1
//        Object couponsTop = tOrderDao.getCouponsTopOne(beginTime, endTime); // 优惠top1
        // 数据转换为map，并加入返回集合中

//        return formatBusinessData(beginTime.substring(0, 10), allDayData, noonData, nightData, discardCount, itemTop, couponsTop);
        return null;
    }

    /**
     * 为存储过程封装参数
     * @param monTime 月份
     * @param flag 0 取当月第一天 1 取当月最后一天
     * @return 2015-01-01 00:00:00  2015-01-31 23:59:59
     */
    private String getTime(String monTime, String flag) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parse(monTime, "yyyy-MM"));
        if ("0".equals(flag)) {
            result = DateTimeUtils.getMonthFirstDay(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
            result = result + " 00:00:00";
        } else {
            if (DateUtils.toString(new Date(), "yyyy-MM").equals(monTime)) {// 如果是当月 取当天23:59:59
                result = DateUtils.toString(new Date()) + " 23:59:59";
            } else {
                result = DateTimeUtils.getMonthLastDay(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
                result = result + " 23:59:59";
            }
        }
        return result;
    }

    /**
     * 封装要返回的数据
     * @param date 日期
     * @param allDay 全天营业数据
     * @param noon 午市营业数据
     * @param night 晚市营业数据
     * @param discardCount 退菜数
     * @param itemTop 菜品top1
     * @param couponsTop 优惠top1
     * @return 返回map格式的数据
     */
    private Map<String, Object> formatBusinessData(String date, Map<String, Object> allDay, Map<String, Object> noon, Map<String, Object> night, Object discardCount, Object itemTop, Object couponsTop) {
        allDay.put("date", date);
        allDay.put("noonshouldamount", noon.get("shouldamount"));// 午市应收
        allDay.put("nightshouldamount", night.get("shouldamount"));// 晚市应收
        allDay.put("memberPercent", allDay.get("viporderpercent"));// 会员消费占比
        if (couponsTop != null) {
            // 优惠top1
            Object[] couponsTopArr = (Object[]) couponsTop;
            allDay.put("couponName", couponsTopArr[0]);
            allDay.put("couponShouldAmount", couponsTopArr[1]); // 份数
        }else{
        	allDay.put("couponName","优惠");
        }

        // 品项top1
        if (itemTop != null) {
            Object[] itemTopArr = (Object[]) itemTop;
            allDay.put("itemName", itemTopArr[0]);
            allDay.put("itemShouldAmount", itemTopArr[1]);
        }else{
        	allDay.put("itemName","");
        }

        // 退菜数
        allDay.put("returnDishAmount", discardCount);
        return allDay;
    }


    /**
     * 取当天流水
     */
    public JSONArray getCurrDayFlowData() {
        Map<String,Object> params = new HashMap<String,Object>();
        JSONArray flowDatas = new JSONArray();
        // 获取当月的天数列表
        List<String> dayList = DateTimeUtils.getCurrentDayList();
        for (int i = 0; i < dayList.size(); i++) {
        	String date = dayList.get(i);
            String beginTime = date + " 00:00:00";
            String endTime = date + " 23:59:59";
            params.put("beginTime", beginTime);
            params.put("endTime", endTime);
            JSONObject dayFlow = new JSONObject();
            if(flowDataMap.containsKey(date)){
            	dayFlow.put("date", date);
                dayFlow.put("data", flowDataMap.get(date));
            }else{
            	dayFlow.put("date", date);
            	List<Map<String, Object>> data = tOrderDao.getFlowData(params);
            	dayFlow.put("data", data);
            	if(!date.equals(DateTimeUtils.getCurrentDate())){
            		flowDataMap.put(date, data);
            	}
            }
            flowDatas.add(dayFlow);
        }
        System.out.println("得到当天流水："+flowDatas.toString());
        return flowDatas;
    }

    /**
     * 查询当天桌子统计信息
     */
    public JSONArray getCurrDayTableData() {
        List<Object[]> list = tTableDao.queryTablesStatus();
        return JSONArray.fromObject(formatTableStatisticsData(list));
    }
    

    /**
     * 查询服务信息
     */
    public JSONObject getCurrServiceData() {
    	JSONObject jsonobject = new JSONObject();
    	
    	//当天日期
        String today  =DateTimeUtils.getCurrentDate();
        //当前年月
        String mouth = today.substring(0,7);
        //拼凑当年的第一天
        String fristday = today.substring(0,4)+"-01-01 00:00:00";
        String branchId = PropertiesUtils.getValue("current_branch_id");
        //查询所有的投诉的服务信息
        Map<String,String> params = new HashMap<String,String>();
        params.put("beginTime", fristday);
        params.put("endTime", today+" 23:59:59");
        params.put("branchId", branchId);
        List<Object[]> list = tTableDao.queryService(params);
        //遍历所有的服务信息
        Map<String,Map<String,Integer>> mouthMapInfo = new TreeMap<String,Map<String,Integer>>();//存放每月的总超时，总投诉信息  以及 分类的投诉的数量
        Map<String,Map<String,Integer>> dayMapInfo = new TreeMap<String,Map<String,Integer>>();//存放每天的总超时，总投诉信息  以及 分类的投诉的数量
        Map<String,Map<String,Object>> mouthWiterMapInfo = new TreeMap<String,Map<String,Object>>();//存放每月服务员投诉服务相关信息
        Map<String,Map<String,Object>> dayWiterMapInfo = new TreeMap<String,Map<String,Object>>();//存放每天服务员投诉服务相关信息
        
        List<String> dayList = DateTimeUtils.getCurrentDayList();
        for (int i = 0; i < dayList.size(); i++) {
        	Map<String,Integer> dayInfo = new HashMap<String,Integer>();
        	dayInfo.put("allcomplainttimes", 0);
        	dayInfo.put("alltimeouttimes", 0);
        	dayInfo.put("quality", 0);
        	dayInfo.put("setting", 0);
        	dayInfo.put("attitude", 0);
        	dayInfo.put("replytime", 0);
        	dayMapInfo.put(dayList.get(i),dayInfo);
        }
        Calendar calendar = Calendar.getInstance();
        int num = calendar.get(Calendar.MONTH);
        for(int i = 0 ; i <= num ; i ++){
        	String mouthstr = i+1<10?"0"+(i+1):(i+1)+"";
            String date = calendar.get(Calendar.YEAR)+"-"+mouthstr;
            
        	Map<String,Integer> mouthInfo = new HashMap<String,Integer>();
        	mouthInfo.put("allcomplainttimes", 0);
        	mouthInfo.put("alltimeouttimes", 0);
        	mouthInfo.put("quality", 0);
        	mouthInfo.put("setting", 0);
        	mouthInfo.put("attitude", 0);
        	mouthInfo.put("replytime", 0);
        	mouthMapInfo.put(date,mouthInfo);
        }
        
        if(list!=null&&list.size()>0){
        	for(Object[] info : list){
            	int userid = partInt(info[0]==null?"0":info[0].toString());
            	String typeid = info[2]==null?"":info[2].toString();
            	String starttime = info[3]==null?"":info[3].toString();
            	String endtime = info[4]==null?"":info[4].toString();
            	String outtime = info[5]==null?"":info[5].toString();
            	String name = info[1]==null?"":info[1].toString();
            	int response = partInt(info[6]==null?"0":info[6].toString());
            	
            	if(name==null||name.equals("")||outtime==null||outtime.equals("")||userid<=0||typeid==null||typeid.equals("")||starttime==null||starttime.equals("")||endtime==null||endtime.equals("")){
            		continue;
            	}
            	
            	long timeset = getTimeDiff(outtime,starttime);
            	long outseconds = getTimeDiff(endtime,starttime);
            	int outnum = 0;
            	if(outseconds>0){
            		outnum = (int)outseconds/(int)timeset;
            	}
            	if(typeid.equals("")||starttime.equals("")||endtime.equals("")||outtime.equals("")){
            		continue;
            	}
            	//计算超时次数
            	String startday = starttime.substring(0, 10);
            	String startmouth = starttime.substring(0, 7);
            	String[] typeids = typeid.split(",");
            	if(typeids==null||typeids.length<=0){
            		continue;
            	}
            	
            	Map<String,Integer> dayInfo = dayMapInfo.containsKey(startday)?dayMapInfo.get(startday):new HashMap<String,Integer>();
            	if(starttime.startsWith(mouth)){
        			dayInfo.put("allcomplainttimes", dayInfo.containsKey("allcomplainttimes")?dayInfo.get("allcomplainttimes")+1:1);//总投诉次数
        			dayInfo.put("alltimeouttimes", dayInfo.containsKey("alltimeouttimes")?dayInfo.get("alltimeouttimes")+outnum:outnum);//总超时次数
        		}
            	
            	//处理月份信息
        		Map<String,Integer> mouthInfo = mouthMapInfo.containsKey(startmouth)?mouthMapInfo.get(startmouth):new HashMap<String,Integer>();
        		mouthInfo.put("allcomplainttimes", mouthInfo.containsKey("allcomplainttimes")?mouthInfo.get("allcomplainttimes")+1:1);//总投诉次数
        		mouthInfo.put("alltimeouttimes", mouthInfo.containsKey("alltimeouttimes")?mouthInfo.get("alltimeouttimes")+outnum:outnum);//总超时次数
            	
            	for(String comid : typeids){
            		int intcomid =  partInt(comid);
            		if(intcomid <=0 || intcomid > 4){
            			continue;
            		}
            		//判断是否在当月
            		if(starttime.startsWith(mouth)){
            			if(intcomid==1){//1 菜品质量
            				dayInfo.put("quality", dayInfo.containsKey("quality")?dayInfo.get("quality")+1:1);
            			}
            			if(intcomid==2){//2 服务态度
            				dayInfo.put("attitude", dayInfo.containsKey("attitude")?dayInfo.get("attitude")+1:1);
            			}
            			if(intcomid==3){//3 用餐环境
            				dayInfo.put("setting", dayInfo.containsKey("setting")?dayInfo.get("setting")+1:1);
            			}
            			if(intcomid==4){//4 响应时间
            				dayInfo.put("replytime", dayInfo.containsKey("replytime")?dayInfo.get("replytime")+1:1);
            			}
            			if(!dayInfo.containsKey("quality")){
            				dayInfo.put("quality", 0);
            			}
    					if(!dayInfo.containsKey("setting")){
    						dayInfo.put("setting", 0);    				
    					}
    					if(!dayInfo.containsKey("attitude")){
    						dayInfo.put("attitude", 0);
    					}
    					if(!dayInfo.containsKey("replytime")){
    						dayInfo.put("replytime", 0);
    					}
            		}
            		//处理月份信息
        			if(intcomid==1){//1 菜品质量
        				mouthInfo.put("quality", mouthInfo.containsKey("quality")?mouthInfo.get("quality")+1:1);
        			}
        			if(intcomid==2){//2 服务态度
        				mouthInfo.put("attitude", mouthInfo.containsKey("attitude")?mouthInfo.get("attitude")+1:1);        				
        			}
        			if(intcomid==3){//3 用餐环境
        				mouthInfo.put("setting", mouthInfo.containsKey("setting")?mouthInfo.get("setting")+1:1);
        			}
        			if(intcomid==4){//4 响应时间
        				mouthInfo.put("replytime", mouthInfo.containsKey("replytime")?mouthInfo.get("replytime")+1:1);
        			}
        			if(!mouthInfo.containsKey("quality")){
        				mouthInfo.put("quality", 0);
        			}
    				if(!mouthInfo.containsKey("setting")){
    					mouthInfo.put("setting", 0);    				
    				}
    				if(!mouthInfo.containsKey("attitude")){
    					mouthInfo.put("attitude", 0);
    				}
    				if(!mouthInfo.containsKey("replytime")){
    					mouthInfo.put("replytime", 0);
    				}
            	}
            	if(starttime.startsWith(mouth)){
            		dayMapInfo.put(startday, dayInfo);
        		}
            	mouthMapInfo.put(startmouth, mouthInfo);
            	
            	//处理当月每天的服务员投诉信息
            	if(starttime.startsWith(mouth)){
            		Map<String,Object> dayWiterInfo = dayWiterMapInfo.containsKey(startday+"_"+userid)?dayWiterMapInfo.get(startday+"_"+userid):new HashMap<String,Object>();
        			dayWiterInfo.put("name", name);
        			dayWiterInfo.put("complainttimes", dayWiterInfo.containsKey("complainttimes")?partInt(dayWiterInfo.get("complainttimes").toString())+1:1);
        			dayWiterInfo.put("timeouttimes", dayWiterInfo.containsKey("timeouttimes")?partInt(dayWiterInfo.get("timeouttimes").toString())+outnum:outnum);
        			dayWiterInfo.put("servicetimes", dayWiterInfo.containsKey("servicetimes")?partInt(dayWiterInfo.get("servicetimes").toString())+response:response);
        			dayWiterMapInfo.put(startday+"_"+userid, dayWiterInfo);
            	}
    			
    			//处理当年每月的服务员投诉信息
    			Map<String,Object> mouthWiterInfo = mouthWiterMapInfo.containsKey(startmouth+"_"+userid)?mouthWiterMapInfo.get(startmouth+"_"+userid):new HashMap<String,Object>();
    			mouthWiterInfo.put("name", name);
    			mouthWiterInfo.put("complainttimes", mouthWiterInfo.containsKey("complainttimes")?partInt(mouthWiterInfo.get("complainttimes").toString())+1:1);
    			mouthWiterInfo.put("timeouttimes", mouthWiterInfo.containsKey("timeouttimes")?partInt(mouthWiterInfo.get("timeouttimes").toString())+outnum:outnum);
    			mouthWiterInfo.put("servicetimes", mouthWiterInfo.containsKey("servicetimes")?partInt(mouthWiterInfo.get("servicetimes").toString())+response:response);
    			mouthWiterMapInfo.put(startmouth+"_"+userid, mouthWiterInfo);
            }
        }
        
        if(mouthMapInfo.size()>0){
        	JSONArray mouthInfoArray = new JSONArray();
        	for(Entry<String,Map<String,Integer>> entry : mouthMapInfo.entrySet()){
        		JSONObject info = JSONObject.fromObject(entry.getValue());
        		info.put("datetime", entry.getKey());
        		mouthInfoArray.add(info);
        	}
        	jsonobject.put("mouthinfo", mouthInfoArray);
        }
		if(dayMapInfo.size()>0){
			JSONArray dayInfoArray = new JSONArray();
			for(Entry<String,Map<String,Integer>> entry : dayMapInfo.entrySet()){
				JSONObject info = JSONObject.fromObject(entry.getValue());
        		info.put("datetime", entry.getKey());
				dayInfoArray.add(info);
        	}
			jsonobject.put("dayinfo", dayInfoArray);
		}
		if(mouthWiterMapInfo.size()>0){
			Map<String,List<Map<String,Object>>> mouthwiterlist = new HashMap<String,List<Map<String,Object>>>();
			for(Entry<String,Map<String,Object>> entry : mouthWiterMapInfo.entrySet()){
				String keystr = entry.getKey().substring(0, entry.getKey().indexOf("_"));
				List<Map<String,Object>> mouthlist = mouthwiterlist.containsKey(keystr)?mouthwiterlist.get(keystr):new ArrayList<Map<String,Object>>();
				mouthlist.add(entry.getValue());
				mouthwiterlist.put(keystr, mouthlist);
        	}
			jsonobject.put("mouthwiterinfo", JSONObject.fromObject(mouthwiterlist));
			mouthwiterlist.clear();
		}
		if(dayWiterMapInfo.size()>0){
			Map<String,List<Map<String,Object>>> daywiterlist = new HashMap<String,List<Map<String,Object>>>();
			for(Entry<String,Map<String,Object>> entry : dayWiterMapInfo.entrySet()){
				String keystr = entry.getKey().substring(0, entry.getKey().indexOf("_"));
				List<Map<String,Object>> daylist = daywiterlist.containsKey(keystr)?daywiterlist.get(keystr):new ArrayList<Map<String,Object>>();
				daylist.add(entry.getValue());
				daywiterlist.put(keystr, daylist);
        	}
			jsonobject.put("daywiterinfo", JSONObject.fromObject(daywiterlist));
			daywiterlist.clear();
		}
		mouthMapInfo.clear();
		dayMapInfo.clear();
		mouthWiterMapInfo.clear();
		dayWiterMapInfo.clear();
    	return jsonobject;
    }
    

    /**
     * 封装返回数据
     * @param list 统计出来的桌子数据
     * @return
     */
    private List<Map<String, Object>> formatTableStatisticsData(List<Object[]> list) {
        int totalCount = 0;// 桌子总数
        int totalInUse = 0;// 在用桌子数
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int moreThanEightInUse = 0;
        int moreThanEightTotal = 0;
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Integer personNum = (Integer) list.get(i)[0];
            BigDecimal inUseNum = (BigDecimal) list.get(i)[1];
            BigDecimal totalNum = (BigDecimal) list.get(i)[2];
            /*if (personNum > 6 && personNum < 13) {// 8 -12 人处理
                moreThanEightInUse += inUseNum.intValue();
                moreThanEightTotal += totalNum.intValue();

            } else {
                map.put("personNum", list.get(i)[0]);
                map.put("inUse", list.get(i)[1]);
                map.put("total", list.get(i)[2]);
                map.put("tabletype", list.get(i)[3]);
            }*/
            map.put("personNum", list.get(i)[0]);
            map.put("inUse", list.get(i)[1]);
            map.put("total", list.get(i)[2]);
            map.put("tabletype", list.get(i)[3]);
            totalCount += totalNum.intValue();
            totalInUse += inUseNum.intValue();
            resultList.add(map);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        /*map.put("personNum", "moreThanEight");
        map.put("inUse", moreThanEightInUse);
        map.put("total", moreThanEightTotal);
        resultList.add(map);*/
        map = new HashMap<String, Object>();
        map.put("personNum", "all");
        map.put("inUse", totalInUse);
        map.put("total", totalCount);
        resultList.add(map);
        return resultList;
    }

    /**
     * 获取当天桌子明细数据
     * @return
     */
    public JSONArray getCurrDayTableDetail() {
    	String branchId = PropertiesUtils.getValue("current_branch_id");
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("branchId", branchId);
        List<Object[]> orders = tOrderDao.getCurrDayOrders(params);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < orders.size(); i++) {
            Map<String, Object> map = formatTableDetailToMap(orders.get(i));
            if(map==null){
            	continue;
            }
            list.add(map);
        }
        orders.clear();
        return JSONArray.fromObject(list);
    }

    /**
     * 把桌子明细转成map
     * @param detail 桌子明细
     * @return 返回map格式的明细
     */
    private Map<String, Object> formatTableDetailToMap(Object[] detail) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("time", calTime((Date)detail[0]));
        Map<String,Object> params = new HashMap<String,Object>();
    	params.put("orderId", detail[1].toString());
    	Object temp = tOrderDao.getShouldAmountByOrderId(params);
        resultMap.put("shouldAmount",temp == null ? 0 :temp);
        resultMap.put("realPersonNum", detail[2]);
        resultMap.put("tabletype", detail[4]);
        String temp1 = detail[1]==null?"":detail[1].toString();
        params.put("orderId", temp1);
        List<Object[]> cominfo = tOrderDao.getOrderCom(params);
        int tnum = 0;
    	int onum = 0;
        if(cominfo!=null&&cominfo.size()>0){
        	for(Object[] info : cominfo){
        		if(info[4]==null||info[4].equals("")){
        			continue;
        		}
        		String starttime = info[6]==null?"":info[6].toString();
        		String outtime = info[7]==null?"":info[7].toString();
        		String endtime = info[8]==null?"":info[8].toString();
        		if(info[4].equals("1")){
        			tnum++;
        		}
        		long timeset = getTimeDiff(outtime,starttime);
            	long outseconds = getTimeDiff(endtime,starttime);
            	int outnum = 0;
            	if(outseconds>0){
            		outnum = (int)outseconds/(int)timeset;
            	}
        		onum += outnum;
        	}
        }
        resultMap.put("tnum", tnum);
        resultMap.put("onum", onum);
        String tableIds = (String) detail[3];
        String firstTableId = tableIds.split(",")[0];
        params.put("tableId", firstTableId);
        Object[] tableInfo = tTableDao.queryTableInfoByTableId(params);
        if(tableInfo==null||tableInfo.length!=2||tableInfo[0]==null||tableInfo[0].equals("")||tableInfo[1]==null||tableInfo[1].equals("")){
        	return null;
        }
        resultMap.put("personNum", tableInfo[0]);
        resultMap.put("tableNo", tableInfo[1]);
        return resultMap;
    }
    
    
    /**
     * 获取当天桌子订单详细信息
     * @return
     */
    public JSONObject getTableOrderData() {
    	String branchId = PropertiesUtils.getValue("current_branch_id");
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("branchId", branchId);
        List<Object[]> orders = tOrderDao.getCurrDayOrders(params);
        Map<String,JSONArray> info = new HashMap<String,JSONArray>();
        for (int i = 0; i < orders.size(); i++) {
            Map<String, Object> map = getOrderCInfoByOder(orders.get(i));
            if(map==null){
            	continue;
            }
            String tableNo = map.containsKey("tableNo")?map.get("tableNo").toString():"";
            if(tableNo.equals("")){
            	continue;
            }
            JSONArray array = info.containsKey(tableNo)?info.get(tableNo):new JSONArray();
            array.add(map);
            info.put(tableNo, array);
        }
        orders.clear();
        return JSONObject.fromObject(info);
    }
    
    /**
     * 查询订单的投诉详细信息
     * @param detail 桌子明细
     * @return 返回map格式的明细
     */
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<String, Object> getOrderCInfoByOder(Object[] detail) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("time", detail[0]==null?"":detail[0].toString());
        Map<String,Object> params = new HashMap<String,Object>();
    	params.put("orderId", detail[1].toString());
    	Object temp = tOrderDao.getShouldAmountByOrderId(params);
        resultMap.put("shouldAmount", temp == null ? 0 : temp);
        resultMap.put("realPersonNum", detail[2]);
        String temp1 = detail[1]==null?"":detail[1].toString();
        params.put("orderId", temp1);
        List<Object[]> cominfo = tOrderDao.getOrderCom(params);
        int tnum = 0;
    	int onum = 0;
    	String name = "";
    	int servicetimes = 0;
    	Set<String> imgPath = new HashSet<String>();
    	Set<String> ctype = new HashSet<String>();
    	Set<String> ttype = new HashSet<String>();
        if(cominfo!=null&&cominfo.size()>0){
        	for(Object[] info : cominfo){
        		if(info[4]==null||info[4].equals("")){
        			continue;
        		}
        		name = info[0]==null?"":info[0].toString();
        		int tempnum = info[1]==null?0:partInt(info[1].toString());
        		servicetimes += tempnum;
        		if(info[3]!=null){
        			imgPath.add(info[3].toString());
        		}
        		
        		String starttime = info[6]==null?"":info[6].toString();
        		String outtime = info[7]==null?"":info[7].toString();
        		String endtime = info[8]==null?"":info[8].toString();
        		if(info[4].equals("1")){
        			tnum++;
        		}
        		if(info[5]!=null&&!info[5].equals("")){
        			if(info[5].toString().indexOf("1")>0){
        				ctype.add("菜品质量,");
        			}
        			if(info[5].toString().indexOf("2")>0){
        				ctype.add("服务态度,");
        			}
        			if(info[5].toString().indexOf("3")>0){
        				ctype.add("用餐环境,");
        			}
        			if(info[5].toString().indexOf("4")>0){
        				ctype.add("响应时间 ,");
        			}
        		}
        		long timeset = getTimeDiff(outtime,starttime);
            	long outseconds = getTimeDiff(endtime,starttime);
            	int outnum = 0;
            	if(outseconds>0){
            		outnum = (int)outseconds/(int)timeset;
            	}
        		onum += outnum;
        		if(outnum>0){
        			if(info[4].equals("1")){
        				ttype.add("投诉超时");
        			}
					if(info[4].equals("2")){
						ttype.add("呼叫服务员超时");			
					}
					if(info[4].equals("3")){
						ttype.add("买单超时");
					}
        		}
        	}
        }
        resultMap.put("tnum", tnum);
        resultMap.put("onum", onum);
        resultMap.put("waiter", name);
        resultMap.put("servicetimes", servicetimes);
        resultMap.put("imgPath", imgPath.size()>0?imgPath.toString().replaceAll("\\[", "").replaceAll("\\]", ""):"");
        resultMap.put("ttype", ctype.size()>0?ctype.toString().replaceAll("\\[", "").replaceAll("\\]", ""):"");
        resultMap.put("timeout", ttype.size()>0?ttype.toString().replaceAll("\\[", "").replaceAll("\\]", ""):"");
        String tableIds = (String) detail[3];
        String firstTableId = tableIds.split(",")[0];
        params.put("tableId", firstTableId);
        Object[] tableInfo = tTableDao.queryTableInfoByTableId(params);
        if(tableInfo==null||tableInfo.length!=2||tableInfo[0]==null||tableInfo[0].equals("")||tableInfo[1]==null||tableInfo[1].equals("")){
        	return null;
        }
        resultMap.put("personNum", tableInfo[0]);
        resultMap.put("tableNo", tableInfo[1]);
        return resultMap;
    }
    /**
     * 计算开始时间与当前时间的间隔 01:23 1小时23分
     * @param beginTime 开始时间
     * @return 格式化好的格式串
     */
	private String calTime(Date beginTime) {
		long interval = System.currentTimeMillis() - beginTime.getTime();
		long mins = interval / 1000 / 60 % 60;
		long hours = interval / 1000 / 60 / 60;

		return String.format("%02d", hours) + ":" + String.format("%02d", mins);
	}

	private float partFlaot(String value) {
		try {
			return Float.parseFloat(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0.0f;
	}
	
	private int partInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	
	private long getTimeDiff(String endtime,String starttime){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long diff = 0l;
		try{
			diff = sd.parse(endtime).getTime() - sd.parse(starttime).getTime();
		}catch(Exception ex){
			
		}
		return diff;
		
	}
}
