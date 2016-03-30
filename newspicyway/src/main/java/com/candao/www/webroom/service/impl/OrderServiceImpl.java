package com.candao.www.webroom.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.candao.common.utils.DateUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.entity.PrintObj;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.data.dao.TbPrintObjDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.model.TCouponRule;
import com.candao.www.data.model.TCoupons;
import com.candao.www.data.model.TbDataDictionary;
import com.candao.www.data.model.TbOpenBizLog;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.Torder;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.User;
import com.candao.www.permit.service.UserService;
import com.candao.www.webroom.model.Coupons;
import com.candao.www.webroom.model.CouponsInterface;
import com.candao.www.webroom.service.CouponsService;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.DishUnitService;
import com.candao.www.webroom.service.OpenBizService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.TableService;
import com.candao.www.webroom.service.ToperationLogService;

import net.sf.json.JSONObject;

@Service
public class OrderServiceImpl implements OrderService{
	
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	TableService  tableService;
	
	@Autowired
	TorderMapper  torderMapper;
	@Autowired
	TorderDetailMapper  torderDetailMapper;
	
	@Autowired
	private DataDictionaryService datadictionaryService;
	
	@Autowired
	TdishDao  dishDao;
	
	@Autowired
	DishUnitService dishUnitService;
	
	@Autowired
	CouponsService couponsService;
	@Autowired
	TbPrintObjDao tbPrintObjDao;
	
	@Autowired
	TbPrinterManagerDao tbPrinterManagerDao;
	
	@Autowired
	@Qualifier("t_userService")
	UserService   userService;
	
	@Autowired
	PrintCommonServiceImpl  printService;
	
	@Autowired
	@Qualifier("weightQueue")
	private Destination printDishQueue;
	
	@Autowired
	TbDataDictionaryDao  dictionaryDao;
	@Autowired
	private ToperationLogService  toperationLogService;
	
	@Autowired
	OpenBizService  openBizService;

	@Override
	public int saveOrder(Torder order) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int discardOrder(Torder order) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addDishOnOrder(TorderDetail orderDetail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int reduceDishOnOrder(TorderDetail orderDetail) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Torder findOrderByTableId(Torder order){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currenttableid", order.getCurrenttableid());
		map.put("orderstatus", "0");
		
		List<Torder>  list = torderMapper.findByOrderNo(map);
		
		 
		if(list != null){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public int update(Torder order){
		return torderMapper.update(order);
	}
	
	@Override
	public int updateInvoiceid(Torder order){
		return torderMapper.updateInvoiceid(order);
	}
	
	@Override
	public Map<String, Object> findOrderById(String orderId){
		
		return torderMapper.findOne(orderId);
	}
	
//	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class) 
	@Override
	public String startOrder(Torder  tOrder){
		
		Map<String, String> mapRet = new HashMap<String, String>();
		
		TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
		if(tbOpenBizLog == null){
			mapRet.put("result", "3");
			return JacksonJsonMapper.objectToJson(mapRet); 
		}
		
	 synchronized(this){
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableNo", tOrder.getTableNo());
//		map.put("status", "0");
		List<Map<String, Object>> resultMap = tableService.find(map);
		
		if(resultMap == null || resultMap.size() == 0 || resultMap.size() > 1){
			mapRet.put("result", "2");
			return JacksonJsonMapper.objectToJson(mapRet); 
		}
		if(! "0".equals(String.valueOf(resultMap.get(0).get("status")))){
			mapRet.put("result", "1");
			return JacksonJsonMapper.objectToJson(mapRet); 
		}
		//1.预定桌子
		
//		boolean bTable = tableService.updateStatus(tTable);
//		if(!bTable){
//			mapRet.put("result", "1");
//			return JacksonJsonMapper.objectToJson(mapRet); 
//		}
		
		//2.生成订单号码 正在下单
//		String orderId = IdentifierUtils.getId().generate().toString();
		String tableId = String.valueOf(resultMap.get(0).get("tableid"));
		
        int shiftid = 0;
		
		String currentTime = DateUtils.getCurrentTime();
		
		List<Map<String, Object>> listDict = dictionaryDao.getDatasByType("BIZPERIODDATE");
		if(listDict == null || listDict.size() == 0 ){
			shiftid = 0;
		}else{
			for(Map<String, Object> mapWorkDay: listDict){
				
				if( mapWorkDay.get("itemid") == null){
					continue;
				}
				String itemId = (String)mapWorkDay.get("itemid");
				String data_type = (String)mapWorkDay.get("datetype");
				String begin_time = (String)mapWorkDay.get("begintime");
				String end_time = (String)mapWorkDay.get("endtime");
				
				currentTime = currentTime.replaceAll(":", "");
				begin_time = begin_time.replaceAll(":", "");
				end_time = end_time.replaceAll(":", "");
				
				//0 午市
				if("0".equals(itemId)){
					//T today N next day
					if("T".equalsIgnoreCase(data_type)){
						
						if(currentTime.compareTo(begin_time) >0 && currentTime.compareTo(end_time) <= 0){
							shiftid = 0;
						}else if(currentTime.compareTo(begin_time) >0 && currentTime.compareTo(end_time) >= 0){
							shiftid = 1;
						}
					}else if("N".equalsIgnoreCase(data_type)){
						if(currentTime.compareTo(begin_time) >0 && currentTime.compareTo(end_time) <= 0){
							shiftid = 0;
						}else{
							if(currentTime.compareTo(begin_time) <0 && currentTime.compareTo(end_time) <= 0){
								shiftid = 0;
							}else{
								shiftid = 1;
							}
						}
					}
				}else if("1".equals(itemId)){
					// 1 晚市
					//T today N next day
					if("T".equalsIgnoreCase(data_type)){
						
						if(currentTime.compareTo(begin_time) >0 && currentTime.compareTo(end_time) <= 0){
							shiftid = 1;
						}else {
							shiftid = 0;
						}
					}else if("N".equalsIgnoreCase(data_type)){
						if(currentTime.compareTo(begin_time) >0 && currentTime.compareTo(end_time) <= 0){
							shiftid = 1;
						}else{
							if(currentTime.compareTo(begin_time) >0 && currentTime.compareTo(end_time) >= 0){
								shiftid = 1;
							}else{
								shiftid = 0;
							}
						}
					}
				}
			}

		}
		
		String orderId = torderMapper.getPrimaryKey();
		String orderIdDate = "H"+DateUtils.toOrderIdString(new Date());
		Torder toder = torderMapper.getMaxOrderNum(orderIdDate);
		//使用了最大客户数返回的最大订单数
		int maxOrderNum = toder.getCustnum();
		Torder  order = new Torder();
		order.setTableids(tableId);
		order.setOrderid(orderId);
		order.setOrderstatus(Constant.ORDERSTATUS.ORDER_STATUS);
		order.setChildNum(tOrder.getChildNum());
		order.setCurrenttableid(tableId);
		order.setCustnum((tOrder.getManNum() == null?0:tOrder.getManNum()) + (tOrder.getWomanNum() == null?0:tOrder.getWomanNum()));
		order.setManNum(tOrder.getManNum());
		order.setSpecialrequied(tOrder.getSpecialrequied());
		order.setUserid(tOrder.getUsername());
		order.setWomanNum(tOrder.getWomanNum());
		order.setBranchid(Integer.valueOf(PropertiesUtils.getValue("current_branch_id")));
		//根据数据字典配置 得出早市还是晚市
		order.setShiftid(shiftid);
		order.setAgeperiod(tOrder.getAgeperiod());
		order.setMeid(tOrder.getMeid());
		order.setOrderNum(maxOrderNum);
		torderMapper.insert(order);
		
	
		TbTable tTable = new TbTable();
		tTable.setTableid(tableId);
		tTable.setOrderid(orderId);
		
		tTable.setStatus(Constant.TABLESTATUS.EAT_STATUS);
		tableService.updateStatus(tTable);
		
		//设定用户排序数量限定
//	    String user_order_num = PropertiesUtils.getValue("user_order_num");
////	    int orderNum = 0;
//	    if(user_order_num != null){
//	    	orderNum = Integer.parseInt(user_order_num);
//	    }
	    //查询当前t_user 表最大的排序数目
	    //select max(ordernum) from t_user
//	    TbUser tbUser = userService.findById(tOrder.getUsername());
		
	    User tbUser =  userService.findMaxOrderNum();

	    userService.updateUserOrderNum(tOrder.getUsername(),tbUser.getOrderNum());
	    
//	    if(tbUser != null && (tbUser.getOrderNum() == 0 || tbUser.getOrderNum() < orderNum)){
////	    	 if( tbUser.getOrdernum() < orderNum){
//		    	userService.updateUserOrderNum(tOrder.getUsername(),tbUser.getOrderNum());
////	    	 }
//	    }

		TbDataDictionary dd =datadictionaryService.findById("backpsd");
		TbDataDictionary vipaddress =datadictionaryService.findById("vipaddress");
		TbDataDictionary locktime =datadictionaryService.findById("locktime");
		TbDataDictionary delaytime =datadictionaryService.findById("delaytime");
		
		mapRet.put("result", "0");
		mapRet.put("orderid", orderId);
		mapRet.put("backpsd", dd==null?"":dd.getItemid());//退菜密码
		
		mapRet.put("vipaddress", vipaddress==null?"":vipaddress.getItemid()); //雅座的VIP地址
		mapRet.put("locktime", locktime==null?"":locktime.getItemid()); //屏保锁屏时间
		mapRet.put("delaytime", delaytime==null?"":delaytime.getItemid()); //屏保停留时间
		//添加日志
//		Tworklog tworklog = new Tworklog();
//		tworklog.setWorkid(UUID.randomUUID().toString());
//		List<Map<String, Object>> list = datadictionaryService.getDatasByType("WORKTYPE");
//		for(int i=0;i<list.size();i++){
//			if(list.get(i).get("itemDesc").equals("开桌")){
//				tworklog.setWorktype(list.get(i).get("itemid").toString());
//			};
//		}
//		tworklog.setUserid(tOrder.getUsername());
//		tworklog.setBegintime(new Date());
//		tworklog.setEndtime(new Date());
//		tworklog.setIpaddress("127.0.0.1");
//		tworklog.setStatus(1);
//		tworklog.setTableid(tOrder.getTableNo());
//		workLogService.saveLog(tworklog);
		
		//开台前清空当前台的操作日记
		toperationLogService.deleteToperationLogByTableNo(tOrder.getTableNo());		
	 }
		return JacksonJsonMapper.objectToJson(mapRet); 
	}

	@Override
	public String updateOrder(Torder  tOrder){
		
		//开台前清空当前台的操作日记
		toperationLogService.deleteToperationLogByTableNo(tOrder.getTableNo());		
		Torder  order = new Torder();
		order.setOrderid(tOrder.getOrderNo());
		order.setChildNum(tOrder.getChildNum());
		order.setCustnum((tOrder.getManNum() == null?0:tOrder.getManNum()) + (tOrder.getWomanNum() == null?0:tOrder.getWomanNum()));
		order.setManNum(tOrder.getManNum());
		order.setUserid(tOrder.getUsername());
		order.setWomanNum(tOrder.getWomanNum());
		order.setAgeperiod(tOrder.getAgeperiod());
		
	    User tbUser =  userService.findMaxOrderNum();
	    userService.updateUserOrderNum(tOrder.getUsername(),tbUser.getOrderNum());
	    
		int result = torderMapper.update(order);
		if(result > 0 ){
			return Constant.SUCCESSMSG;
		} 
		
		return Constant.FAILUREMSG;
	}
	
	@Override
	public String querycoupons(String dishIds){
		
		List<String> listIds = Arrays.asList(dishIds.split(","));
		// dishDao.querycoupons(listIds);
		//首先取到 计量单位信息
		//取优惠规则信息
		List<Coupons> list = new ArrayList<Coupons>();
		for(String dishId : listIds){
			Coupons coupons = new Coupons();
//			Map<String, Object> map = new HashMap<String,Object>();
//			map.put("dishid", dishId);
			Tdish dish = dishDao.get(dishId);
			//dishUnitService.getUnitsBydishId(dishId);
			List<TCouponRule> rules = couponsService.findRuleByDishId(dishId);
			
			coupons.setDishid(dishId);
			coupons.setDishname(dish.getTitle());
			coupons.setOrdernum(String.valueOf(dish.getOrderNum()));
			coupons.setCoupons(rules);
			
			list.add(coupons);
		}
		CouponsInterface couponsInterface = new CouponsInterface();
		couponsInterface.setResult("0");
		couponsInterface.setCoupons(list);
		
		return JacksonJsonMapper.objectToJson(couponsInterface);
	}

	@Override
	public String queryallcoupons() {
 
		List<Tdish> tdishes = dishDao.findAllDish();
		if(tdishes == null){
			return Constant.FAILUREMSG;
		}
		List<Coupons> list = new ArrayList<Coupons>();
		for(Tdish dish: tdishes){
			Coupons coupons = new Coupons();
			List<TCouponRule> rules = couponsService.findRuleByDishId(dish.getDishid());
			TCoupons tCoupons = couponsService.findCouponByDishId(dish.getDishid());
			
			
			coupons.setDishid(dish.getDishid());
			coupons.setDishname(dish.getTitle());
			coupons.setOrdernum(String.valueOf(dish.getOrderNum() == null?"0":dish.getOrderNum()  ));
			coupons.setLabel(dish.getLabel());
			if(tCoupons != null){
				coupons.setDesc(tCoupons.getDescription());
	 			coupons.setRuleDesc(tCoupons.getRuledescription());
	 			String period = "";
	 			 
	 		 
	 			switch (tCoupons.getCoupontype()) {
				case 0:
					period = "每天-"+(tCoupons.getBegintime() == null?"": tCoupons.getBegintime())+ " --- " + (tCoupons.getEndtime() == null?"":tCoupons.getEndtime()) ;
					break;
				case 1:
					period = "每周-"+(tCoupons.getBegintime() == null?"": tCoupons.getBegintime())+ " --- " + (tCoupons.getEndtime() == null?"":tCoupons.getEndtime()) ;
						break;
				case 2:
					period = "每月-"+(tCoupons.getCouponperiod() == null? "":tCoupons.getCouponperiod() +"日");
				   break;

				default:
					break;
				}
	 			coupons.setPeriod(period);
			}
			
			coupons.setCoupons(rules);
			list.add(coupons);
		}
		CouponsInterface couponsInterface = new CouponsInterface();
		couponsInterface.setResult("0");
		couponsInterface.setCoupons(list);
		
		return JacksonJsonMapper.objectToJson(couponsInterface);
	}

	@Override
	public Map<String, Object> switchPadOrderInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		Map<String, Object> mapRet = new HashMap<String, Object>();
		List<Map<String, Object>> resultMap = tableService.find(params);
		if(resultMap !=null &&resultMap.size() > 0 ){
			String orderid=String.valueOf(resultMap.get(0).get("orderid"));
			if(orderid!=null&&!"".equals(orderid)){
				Map<String,Object> orderMap=torderMapper.findOne(orderid);
				if(orderMap!=null){
					if("3".equals(String.valueOf(orderMap.get("orderstatus")))){
						mapRet.put("flag", "3");
						mapRet.put("desc", "该桌已结账");
						return mapRet; 
					}
					if("0".equals(String.valueOf(orderMap.get("orderstatus")))){
//						t.mannum,t.childNum,t.womanNum
						mapRet.put("flag", "1");
						mapRet.put("desc", "获取数据成功");
						mapRet.put("currenttableid",params.get("tableNo"));
						mapRet.put("orderid",orderid);
						mapRet.put("memberno",orderMap.get("memberno"));
						mapRet.put("manNum",orderMap.get("manNum"));
						mapRet.put("womanNum",orderMap.get("womanNum"));
						mapRet.put("waiterNum",orderMap.get("userid"));
						mapRet.put("ageperiod",orderMap.get("ageperiod"));
						mapRet.put("begintime",orderMap.get("begintime"));
						Map<String,Object> mappa=new HashMap<String,Object>();
						mappa.put("orderid", orderid);
						TbDataDictionary dd =datadictionaryService.findById("backpsd");
						TbDataDictionary vipaddress =datadictionaryService.findById("vipaddress");
						TbDataDictionary locktime =datadictionaryService.findById("locktime");
						TbDataDictionary delaytime =datadictionaryService.findById("delaytime");
						mapRet.put("result", "0");
						mapRet.put("orderid", orderid);
						mapRet.put("backpsd", dd==null?"":dd.getItemid());//退菜密码
						mapRet.put("vipaddress", vipaddress==null?"":vipaddress.getItemid()); //雅座的VIP地址
						mapRet.put("locktime", locktime==null?"":locktime.getItemid()); //屏保锁屏时间
						mapRet.put("delaytime", delaytime==null?"":delaytime.getItemid()); //屏保停留时间
						mapRet.put("rows", getMapData(orderid));//获取订单数据
						//原订单meid不为空，推送清空
						if(orderMap.get("meid")!=null&&params.get("meid")!=null&&!String.valueOf(orderMap.get("meid")).equals(String.valueOf(params.get("meid")))){
							StringBuffer str=new StringBuffer(Constant.TS_URL);
							str.append(Constant.MessageType.msg_1005+"/"+String.valueOf(orderMap.get("meid")));
							System.out.println(str.toString());
							new Thread(new TsThread(str.toString())).run();
						}
						//当前pad的meid不为空，更新meid
						if(params.get("meid")!=null){
							Torder order =new Torder();
							order.setOrderid(orderid);
							order.setMeid(String.valueOf(params.get("meid")));
//							order.setShiftid(Integer.valueOf(String.valueOf(orderMap.get("shiftid"))));
//							update(order);
							torderMapper.update(order);
							
						}
						//删除桌子日志
						toperationLogService.deleteToperationLogByTableNo(String.valueOf(params.get("tableNo")));	
						
						return mapRet;
					}
					mapRet.put("flag", "4");
					mapRet.put("desc", "订单为空");
					return mapRet;
				}else{
					mapRet.put("flag", "4");
					mapRet.put("desc", "订单为空");
					return mapRet; 
				}
				
			}else{
				mapRet.put("flag", "2");
				mapRet.put("desc", "已清机");
				return mapRet; 
			}
			
		
			
		}
		else{
			//没找到这个桌号
			mapRet.put("flag", "0");
			mapRet.put("desc", "未找到这个桌号");
			return mapRet; 
		}
	}
	public class TsThread extends Thread{
		   String  str ;
		   TsThread(String  str){
			   this.str = str;
		   }
		   @Override
		   public void run(){
			   //根据动作打印不同的小票
				URL urlobj;
				try {
				urlobj = new URL(str);
				URLConnection	urlconn = urlobj.openConnection();
				urlconn.connect();
				InputStream myin = urlconn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
				String content = reader.readLine();
				JSONObject object = JSONObject.fromObject(content.trim());
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> resultList = (List<Map<String, Object>>) object.get("result");
				if("1".equals(String.valueOf(resultList.get(0).get("Data")))){
					System.out.println("清空pad推送成功");
				}else{
					System.out.println("清空pad推送失败");
				}
				} catch (IOException e) {
					logger.error(e.getMessage());
					System.out.println("推送异常"+e.toString());
					e.printStackTrace();
				}
		   }
	   }
	/**
	 * 组织菜谱数据给pad，在更换pad时使用
	 * @author shen
	 * @date:2015年5月14日下午8:09:13
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getMapData(String orderid){
		Map<String,Object> mappa =new HashMap<String,Object>();
		//单品
		mappa.put("dishtype","0");
		mappa.put("orderid", orderid);
		List<Map<String,Object>> resultMap=torderDetailMapper.findOrderDetailPad(mappa);
		//火锅
		mappa.put("dishtype","1");
		mappa.put("ismaster","1");
		List<Map<String,Object>> listfishpot=torderDetailMapper.findOrderDetailPad(mappa);
		if(listfishpot!=null&&listfishpot.size()>0){
			for(Map<String,Object> fishpotMap:listfishpot){
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("orderid",orderid);
				params.put("dishtype","1");
				params.put("ismaster","0");
				params.put("parentkey",fishpotMap.get("primarykey"));
				List<Map<String,Object>> listfishorpot=torderDetailMapper.findOrderDetailPad(params);
				fishpotMap.put("dishes",updateDishType(listfishorpot,"0"));
				resultMap.add(fishpotMap);
			}
		}
		//套餐
		mappa.put("dishtype","2");
		mappa.put("ismaster","1");
		mappa.put("childdishtype","2");
		List<Map<String,Object>> listCombodish=torderDetailMapper.findOrderDetailPad(mappa);
		if(listCombodish!=null&&listCombodish.size()>0){
			for(Map<String,Object> combodishMap:listCombodish){
				List<Map<String,Object>> listCombodishChild=new ArrayList<Map<String,Object>>();
				//套餐中的单品
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("orderid",orderid);
				params.put("dishtype","2");
				params.put("ismaster","0");
				params.put("childdishtype","0");
				params.put("parentkey",combodishMap.get("primarykey"));
				listCombodishChild=torderDetailMapper.findOrderDetailPad(params);
				listCombodishChild=updateDishType(listCombodishChild,"0");
				//套餐中的鱼锅
				params.put("childdishtype","1");
				params.put("ismaster","1");
				List<Map<String,Object>> fishpotlist=torderDetailMapper.findOrderDetailPad(params);
				if(fishpotlist!=null&&fishpotlist.size()>0){
					for(Map<String,Object> fishpotmap:fishpotlist){
						Map<String,Object> fishpotparams=new HashMap<String,Object>();
						fishpotparams.put("orderid",orderid);
						fishpotparams.put("dishtype","2");
						fishpotparams.put("ismaster","0");
						fishpotparams.put("childdishtype","1");
						fishpotparams.put("parentkey",fishpotmap.get("primarykey"));
						List<Map<String,Object>> listfishorpot=torderDetailMapper.findOrderDetailPad(fishpotparams);
						fishpotmap.put("dishes",updateDishType(listfishorpot,"0"));
						fishpotmap.put("dishtype", "1");
						listCombodishChild.add(fishpotmap);
					}
				}
				combodishMap.put("dishes",listCombodishChild);
				resultMap.add(combodishMap);
			}
		}
		return resultMap;
	}
	public List<Map<String,Object>> updateDishType(List<Map<String,Object>> list,String dishType){
		if(list!=null&&list.size()>0){
			for(Map<String,Object> map:list){
				map.put("dishtype", dishType);
			}
		}
		return list;
	}
//	@Override
//	public String queryallcoupons(){
//		List<String> listIds = Arrays.asList(dishIds.split(","));
//		List<Coupons> list = new ArrayList<Coupons>();
//		for(String dishId : listIds){
//			Coupons coupons = new Coupons();
// 
//			Tdish dish = dishDao.get(dishId);
//			List<TCouponRule> rules = couponsService.findRuleByDishId(dishId);
//			
//			coupons.setDishid(dishId);
//			coupons.setDishname(dish.getTitle());
//			coupons.setOrdernum(String.valueOf(dish.getOrderNum()));
//			coupons.setCoupons(rules);
//			
//			list.add(coupons);
//		}
//		CouponsInterface couponsInterface = new CouponsInterface();
//		couponsInterface.setResult("0");
//		couponsInterface.setCoupons(list);
//		
//		return JacksonJsonMapper.objectToJson(couponsInterface);
//	}

	@Override
	public Map<String, Object> updateDishWeight(Map<String, Object> params) {
		// TODO Auto-generated method stub
		boolean flag=true;
		Map<String,Object> mapRet=new HashMap<String,Object>();
		TbTable table = tableService.findByTableNo((String)params.get("tableNo"));
		if(table !=null){
			String orderid= table.getOrderid();
			if(orderid!=null&&!"".equals(orderid)){
				
				params.put("orderid", orderid);
				//更新t_order_detail
				flag=flag&&torderDetailMapper.updateDishWeight(params)>0;
				//更新t_printDish
				PrintObj printObj=tbPrintObjDao.find(params);
				if(printObj!=null){
					params.put("printobjid", printObj.getId());
					flag=flag&&tbPrintObjDao.updateDishWeight(params)>0;
				}
				if(flag){
					mapRet.put("result", "0");
					mapRet.put("desc", "更新成功");
				}else{
					mapRet.put("result", "2");
					mapRet.put("desc", "未找到相应的菜品");
				}
			}
		}else{
			mapRet.put("result", "1");
			mapRet.put("desc", "订单为空");
		}
		return mapRet; 
	}
	
	public class PrintThread extends Thread{
		   
		   PrintObj printObj ;
		   
		   PrintThread(PrintObj printObj ){
			   this.printObj = printObj;
		   }
	 
		   @Override
		   public void run(){
			   //称重后打印单
			   printService.setDestination(printDishQueue);
			   printService.sendMessage( printObj );
			 
		   }
	   }
	@Override
	public void executeSql(String sql) {
		torderMapper.executeSql(sql);
	}

	@Override
	public Torder get(String id) {
		return torderMapper.get(id);
	}
 

	@Override
	public List<Torder> verifyAllOrder() {
		return torderMapper.verifyAllOrder();
	}
//
//	@Override
//	public int verifyAllCLean() {
//		return torderMapper.verifyAllCLean();
//	}
//
//	@Override
//	public int verifyAllTableClear() {
//	     return torderMapper.verifyAllTableClear();
//	}

	@Override
	public String callEndWork(String userName, String isSucess) {
		return  torderMapper.callEndWork(userName,isSucess);
	}
 
	
	@Override
	public void setOrderMember(Map<String,Object> params){
		torderMapper.setOrderMember(params);
	}
 
	   
}
