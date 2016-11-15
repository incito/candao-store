package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.entity.PrintObj;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TServiceChargeDao;
import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.data.dao.TbPrintObjDao;
import com.candao.www.data.dao.TbTableDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.model.TCouponRule;
import com.candao.www.data.model.TCoupons;
import com.candao.www.data.model.TServiceCharge;
import com.candao.www.data.model.TbDataDictionary;
import com.candao.www.data.model.TbOpenBizLog;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.Torder;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.data.model.User;
import com.candao.www.dataserver.mapper.CaleTableAmountMapper;
import com.candao.www.dataserver.mapper.OrderMapper;
import com.candao.www.dataserver.mapper.OrderOpMapper;
import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.dataserver.util.StringUtil;
import com.candao.www.permit.service.UserService;
import com.candao.www.preferential.calcpre.StrategyFactory;
import com.candao.www.utils.OrderDetailParse;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.model.Coupons;
import com.candao.www.webroom.model.CouponsInterface;
import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.service.CouponsService;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.DishUnitService;
import com.candao.www.webroom.service.NotifyService;
import com.candao.www.webroom.service.OpenBizService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.PreferentialActivityService;
import com.candao.www.webroom.service.TableService;
import com.candao.www.webroom.service.ToperationLogService;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	TableService tableService;

	@Autowired
	TorderMapper torderMapper;
	@Autowired
	TorderDetailMapper torderDetailMapper;
	@Autowired
	OrderOpMapper orderOpMapper;
	@Autowired
	private DataDictionaryService datadictionaryService;

	@Autowired
	TdishDao dishDao;

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
	UserService userService;

	@Autowired
	PrintCommonServiceImpl printService;

	@Autowired
	TbDataDictionaryDao dictionaryDao;
	@Autowired
	private ToperationLogService toperationLogService;

	@Autowired
	OpenBizService openBizService;
	@Autowired
	private NotifyService notifyService;
	@Autowired
	private OrderOpService orderOpService;
	@Autowired
	private TorderDetailPreferentialDao detailPreferentialDao;
	@Autowired
	private CaleTableAmountMapper caleTableAmountMapper;
	@Autowired
	private PreferentialActivityService preferentialActivityService;
	@Autowired
	DataDictionaryService dataDictionaryService;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private TbTableDao tableDao;

	@Autowired
	private TServiceChargeDao serviceChargeDao;

	public Torder findOrderByTableId(Torder order) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currenttableid", order.getCurrenttableid());
		map.put("orderstatus", "0");

		List<Torder> list = torderMapper.findByOrderNo(map);

		if (list != null) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int update(Torder order) {
		return torderMapper.update(order);
	}

	@Override
	public Map<String, Object> findOrderById(String orderId) {

		return torderMapper.findOne(orderId);
	}

	// @Transactional(propagation=Propagation.REQUIRED,
	// rollbackFor=Exception.class)
	@Override
	public synchronized String startOrder(Torder tOrder) {
		Map<String, Object> mapRet = new HashMap<>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableNo", tOrder.getTableNo());
		// map.put("status", "0");
		List<Map<String, Object>> resultMap = tableService.find(map);
		String tableType = (String) resultMap.get(0).get("tabletype");
		tableType = tableType == null ? "" : tableType;
		if (!resultMap.isEmpty() && !"2".equals(tableType) && !"3".equals(tableType)) {
			Map<String, Object> validateResult = userService.validateLoginTypeByAccount(tOrder.getUsername(),
					PropertiesUtils.getValue("logintype.030101"));
			if (!Boolean.valueOf(String.valueOf(validateResult.get("success")))) {
				String msg = String.valueOf(validateResult.get("msg"));
				mapRet = ReturnMap.getFailureMap(msg);
				logger.error("开台失败，" + msg);
				return JacksonJsonMapper.objectToJson(mapRet);
			}
		}

		TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
		if (tbOpenBizLog == null) {
			// mapRet.put("result", "3");
			mapRet = ReturnMap.getFailureMap("开台失败，开业记录为空");
			logger.error("开台失败，开业记录为空");
			return JacksonJsonMapper.objectToJson(mapRet);
		}

		if (resultMap == null || resultMap.size() == 0 || resultMap.size() > 1) {
			// logger.error("开台失败！ 查找不到桌台");
			mapRet = ReturnMap.getFailureMap("开台失败！ 查找不到桌台");
			return JacksonJsonMapper.objectToJson(mapRet);
		}
		if (!"0".equals(String.valueOf(resultMap.get(0).get("status")))) {
			logger.error("开台失败，桌台状态不对！0");
			// mapRet.put("result", "1");
			mapRet = ReturnMap.getFailureMap("开台失败，桌台状态不对！");
			return JacksonJsonMapper.objectToJson(mapRet);
		}
		// 1.预定桌子

		// boolean bTable = tableService.updateStatus(tTable);
		// if(!bTable){
		// mapRet.put("result", "1");
		// return JacksonJsonMapper.objectToJson(mapRet);
		// }

		// 2.生成订单号码 正在下单
		// String orderId = IdentifierUtils.getId().generate().toString();
		String tableId = String.valueOf(resultMap.get(0).get("tableid"));

		int shiftid = 0;

		String currentTime = DateUtils.getCurrentTime();

		List<Map<String, Object>> listDict = dictionaryDao.getDatasByType("BIZPERIODDATE");
		if (listDict == null || listDict.size() == 0) {
			shiftid = 0;
		} else {
			for (Map<String, Object> mapWorkDay : listDict) {

				if (mapWorkDay.get("itemid") == null) {
					continue;
				}
				String itemId = (String) mapWorkDay.get("itemid");
				String data_type = (String) mapWorkDay.get("datetype");
				String begin_time = (String) mapWorkDay.get("begintime");
				String end_time = (String) mapWorkDay.get("endtime");

				currentTime = currentTime.replaceAll(":", "");
				begin_time = begin_time.replaceAll(":", "");
				end_time = end_time.replaceAll(":", "");

				// 0 午市
				if ("0".equals(itemId)) {
					// T today N next day
					if ("T".equalsIgnoreCase(data_type)) {

						if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) <= 0) {
							shiftid = 0;
						} else if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) >= 0) {
							shiftid = 1;
						}
					} else if ("N".equalsIgnoreCase(data_type)) {
						if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) <= 0) {
							shiftid = 0;
						} else {
							if (currentTime.compareTo(begin_time) < 0 && currentTime.compareTo(end_time) <= 0) {
								shiftid = 0;
							} else {
								shiftid = 1;
							}
						}
					}
				} else if ("1".equals(itemId)) {
					// 1 晚市
					// T today N next day
					if ("T".equalsIgnoreCase(data_type)) {

						if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) <= 0) {
							shiftid = 1;
						} else {
							shiftid = 0;
						}
					} else if ("N".equalsIgnoreCase(data_type)) {
						if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) <= 0) {
							shiftid = 1;
						} else {
							if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) >= 0) {
								shiftid = 1;
							} else {
								shiftid = 0;
							}
						}
					}
				}
			}

		}

		String orderId = torderMapper.getPrimaryKey();
		String orderIdDate = "H" + DateUtils.toOrderIdString(new Date());
		Torder toder = torderMapper.getMaxOrderNum(orderIdDate);
		// 使用了最大客户数返回的最大订单数
		int maxOrderNum = toder.getCustnum();
		Torder order = new Torder();
		order.setTableids(tableId);
		order.setOrderid(orderId);
		order.setOrderstatus(Constant.ORDERSTATUS.ORDER_STATUS);
		order.setChildNum(tOrder.getChildNum() == null ? 0 : tOrder.getChildNum());
		order.setCurrenttableid(tableId);
		order.setCustnum((tOrder.getManNum() == null ? 0 : tOrder.getManNum())
				+ (tOrder.getWomanNum() == null ? 0 : tOrder.getWomanNum()));
		order.setManNum(tOrder.getManNum() == null ? 0 : tOrder.getManNum());
		order.setSpecialrequied(tOrder.getSpecialrequied());
		order.setUserid(tOrder.getUsername());
		order.setWomanNum(tOrder.getWomanNum() == null ? 0 : tOrder.getWomanNum());
		order.setBranchid(Integer.valueOf(PropertiesUtils.getValue("current_branch_id")));
		// 根据数据字典配置 得出早市还是晚市
		order.setShiftid(shiftid);
		order.setAgeperiod(tOrder.getAgeperiod());
		order.setMeid(tOrder.getMeid());
		order.setOrderNum(maxOrderNum);
		order.setIsFree(tOrder.getIsFree());
		order.setNumOfMeals(tOrder.getNumOfMeals());
		torderMapper.insert(order);

		TbTable tTable = new TbTable();
		tTable.setTableid(tableId);
		tTable.setOrderid(orderId);
		// 判断餐台类型
		// 外卖，咖啡外卖不更改餐台状态

		if (StringUtils.isEmpty(tableType) || (!Constant.TABLETYPE.TAKEOUT.equals(tableType)
				&& !Constant.TABLETYPE.TAKEOUT_COFFEE.equals(tableType))) {

			tTable.setStatus(Constant.TABLESTATUS.EAT_STATUS);
		} else {
			tTable.setStatus(Constant.TABLESTATUS.FREE_STATUS);
		}
		tableService.updateStatus(tTable);

		// 设定用户排序数量限定
		// String user_order_num =
		// PropertiesUtils.getValue("user_order_num");
		//// int orderNum = 0;
		// if(user_order_num != null){
		// orderNum = Integer.parseInt(user_order_num);
		// }
		// 查询当前t_user 表最大的排序数目
		// select max(ordernum) from t_user
		// TbUser tbUser = userService.findById(tOrder.getUsername());

		User tbUser = userService.findMaxOrderNum();

		userService.updateUserOrderNum(tOrder.getUsername(), tbUser.getOrderNum());

		// if(tbUser != null && (tbUser.getOrderNum() == 0 ||
		// tbUser.getOrderNum() < orderNum)){
		//// if( tbUser.getOrdernum() < orderNum){
		// userService.updateUserOrderNum(tOrder.getUsername(),tbUser.getOrderNum());
		//// }
		// }

		TbDataDictionary dd = datadictionaryService.findById("backpsd");
		TbDataDictionary vipaddress = datadictionaryService.findById("vipaddress");
		TbDataDictionary locktime = datadictionaryService.findById("locktime");
		TbDataDictionary delaytime = datadictionaryService.findById("delaytime");

		Map<String, Object> result = new HashMap<>();
		// result.put("result", "0");
		result.put("orderid", orderId);
		result.put("backpsd", dd == null ? "" : dd.getItemid());// 退菜密码

		result.put("vipaddress", vipaddress == null ? "" : vipaddress.getItemid()); // 雅座的VIP地址
		result.put("locktime", locktime == null ? "" : locktime.getItemid()); // 屏保锁屏时间
		result.put("delaytime", delaytime == null ? "" : delaytime.getItemid()); // 屏保停留时间
		// 添加日志
		// Tworklog tworklog = new Tworklog();
		// tworklog.setWorkid(UUID.randomUUID().toString());
		// List<Map<String, Object>> list =
		// datadictionaryService.getDatasByType("WORKTYPE");
		// for(int i=0;i<list.size();i++){
		// if(list.get(i).get("itemDesc").equals("开桌")){
		// tworklog.setWorktype(list.get(i).get("itemid").toString());
		// };
		// }
		// tworklog.setUserid(tOrder.getUsername());
		// tworklog.setBegintime(new Date());
		// tworklog.setEndtime(new Date());
		// tworklog.setIpaddress("127.0.0.1");
		// tworklog.setStatus(1);
		// tworklog.setTableid(tOrder.getTableNo());
		// workLogService.saveLog(tworklog);

		// 开台前清空当前台的操作日记
		toperationLogService.deleteToperationLogByTableNo(tOrder.getTableNo());
		mapRet = ReturnMap.getSuccessMap(result);
		return JacksonJsonMapper.objectToJson(mapRet);
	}

	@Override
	public String updateOrder(Torder tOrder) {
		// 开台前清空当前台的操作日记
		toperationLogService.deleteToperationLogByTableNo(tOrder.getTableNo());
		Torder order = new Torder();
		order.setOrderid(tOrder.getOrderNo());
		order.setChildNum(tOrder.getChildNum());
		order.setCustnum((tOrder.getManNum() == null ? 0 : tOrder.getManNum())
				+ (tOrder.getWomanNum() == null ? 0 : tOrder.getWomanNum()));
		order.setManNum(tOrder.getManNum());
		order.setUserid(tOrder.getUsername());
		order.setWomanNum(tOrder.getWomanNum());
		order.setAgeperiod(tOrder.getAgeperiod());
		order.setIsFree(tOrder.getIsFree());
		order.setNumOfMeals(tOrder.getNumOfMeals());
		User tbUser = userService.findMaxOrderNum();
		userService.updateUserOrderNum(tOrder.getUsername(), tbUser.getOrderNum());

		int result = torderMapper.update(order);

		Map<String, Object> param = new HashMap<>();
		param.put("targetOrderid", order.getOrderid());
		param.put("custnum", order.getCustnum());
		tbPrintObjDao.updateByOrderno(param);

		if (result > 0) {
			return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap());
		}
		return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap());
	}

	@Override
	public String querycoupons(String dishIds) {

		List<String> listIds = Arrays.asList(dishIds.split(","));
		// dishDao.querycoupons(listIds);
		// 首先取到 计量单位信息
		// 取优惠规则信息
		List<Coupons> list = new ArrayList<Coupons>();
		for (String dishId : listIds) {
			Coupons coupons = new Coupons();
			// Map<String, Object> map = new HashMap<String,Object>();
			// map.put("dishid", dishId);
			Tdish dish = dishDao.get(dishId);
			// dishUnitService.getUnitsBydishId(dishId);
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
		if (tdishes == null) {
			return Constant.FAILUREMSG;
		}
		List<Coupons> list = new ArrayList<Coupons>();
		for (Tdish dish : tdishes) {
			Coupons coupons = new Coupons();
			List<TCouponRule> rules = couponsService.findRuleByDishId(dish.getDishid());
			TCoupons tCoupons = couponsService.findCouponByDishId(dish.getDishid());

			coupons.setDishid(dish.getDishid());
			coupons.setDishname(dish.getTitle());
			coupons.setOrdernum(String.valueOf(dish.getOrderNum() == null ? "0" : dish.getOrderNum()));
			coupons.setLabel(dish.getLabel());
			if (tCoupons != null) {
				coupons.setDesc(tCoupons.getDescription());
				coupons.setRuleDesc(tCoupons.getRuledescription());
				String period = "";

				switch (tCoupons.getCoupontype()) {
				case 0:
					period = "每天-" + (tCoupons.getBegintime() == null ? "" : tCoupons.getBegintime()) + " --- "
							+ (tCoupons.getEndtime() == null ? "" : tCoupons.getEndtime());
					break;
				case 1:
					period = "每周-" + (tCoupons.getBegintime() == null ? "" : tCoupons.getBegintime()) + " --- "
							+ (tCoupons.getEndtime() == null ? "" : tCoupons.getEndtime());
					break;
				case 2:
					period = "每月-" + (tCoupons.getCouponperiod() == null ? "" : tCoupons.getCouponperiod() + "日");
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
		if (resultMap != null && resultMap.size() > 0) {
			String orderid = String.valueOf(resultMap.get(0).get("orderid"));
			if (orderid != null && !"".equals(orderid)) {
				Map<String, Object> orderMap = torderMapper.findOne(orderid);
				if (orderMap != null) {
					if ("3".equals(String.valueOf(orderMap.get("orderstatus")))) {
						return ReturnMap.getFailureMap("该桌已结账");
					}
					if ("0".equals(String.valueOf(orderMap.get("orderstatus")))) {
						// t.mannum,t.childNum,t.womanNum
						// mapRet.put("flag", "1");
						// mapRet.put("desc", "获取数据成功");
						mapRet.put("currenttableid", params.get("tableNo"));
						mapRet.put("orderid", orderid);
						mapRet.put("memberno", orderMap.get("memberno"));
						mapRet.put("manNum", orderMap.get("manNum"));
						mapRet.put("womanNum", orderMap.get("womanNum"));
						mapRet.put("waiterNum", orderMap.get("userid"));
						mapRet.put("ageperiod", orderMap.get("ageperiod"));
						mapRet.put("begintime", orderMap.get("begintime"));
						Map<String, Object> mappa = new HashMap<String, Object>();
						mappa.put("orderid", orderid);
						TbDataDictionary dd = datadictionaryService.findById("backpsd");
						TbDataDictionary vipaddress = datadictionaryService.findById("vipaddress");
						TbDataDictionary locktime = datadictionaryService.findById("locktime");
						TbDataDictionary delaytime = datadictionaryService.findById("delaytime");
						mapRet.put("result", "0");
						mapRet.put("orderid", orderid);
						mapRet.put("backpsd", dd == null ? "" : dd.getItemid());// 退菜密码
						mapRet.put("vipaddress", vipaddress == null ? "" : vipaddress.getItemid()); // 雅座的VIP地址
						mapRet.put("locktime", locktime == null ? "" : locktime.getItemid()); // 屏保锁屏时间
						mapRet.put("delaytime", delaytime == null ? "" : delaytime.getItemid()); // 屏保停留时间
						mapRet.put("rows", getMapData(orderid));// 获取订单数据
						// 原订单meid不为空，推送清空
						if (orderMap.get("meid") != null && params.get("meid") != null
								&& !String.valueOf(orderMap.get("meid")).equals(String.valueOf(params.get("meid")))) {
							// StringBuffer str=new
							// StringBuffer(Constant.TS_URL);
							// str.append(Constant.MessageType.msg_1005+"/"+String.valueOf(orderMap.get("meid")));
							// System.out.println(str.toString());
							// new Thread(new TsThread(str.toString())).run();
							notifyService.notifyClearTable(params.get("tableNo").toString());
						}
						// 当前pad的meid不为空，更新meid
						if (params.get("meid") != null) {
							Torder order = new Torder();
							order.setOrderid(orderid);
							order.setMeid(String.valueOf(params.get("meid")));
							// order.setShiftid(Integer.valueOf(String.valueOf(orderMap.get("shiftid"))));
							// update(order);
							torderMapper.update(order);

						}
						// 删除桌子日志
						toperationLogService.deleteToperationLogByTableNo(String.valueOf(params.get("tableNo")));
						return ReturnMap.getSuccessMap("获取数据成功", mapRet);
					}
					return ReturnMap.getFailureMap("订单已取消");
				} else {
					return ReturnMap.getFailureMap("订单不存在");
				}

			} else {
				return ReturnMap.getFailureMap("桌台未绑定订单");
			}
		} else {
			// 没找到这个桌号
			return ReturnMap.getFailureMap("未找到这个桌号");
		}
	}

	// public class TsThread extends Thread{
	// String str ;
	// TsThread(String str){
	// this.str = str;
	// }
	// @Override
	// public void run(){
	// //根据动作打印不同的小票
	// URL urlobj;
	// try {
	// urlobj = new URL(str);
	// URLConnection urlconn = urlobj.openConnection();
	// urlconn.connect();
	// InputStream myin = urlconn.getInputStream();
	// BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
	// String content = reader.readLine();
	// JSONObject object = JSONObject.fromObject(content.trim());
	// @SuppressWarnings("unchecked")
	// List<Map<String,Object>> resultList = (List<Map<String, Object>>)
	// object.get("result");
	// if("1".equals(String.valueOf(resultList.get(0).get("Data")))){
	// System.out.println("清空pad推送成功");
	// }else{
	// System.out.println("清空pad推送失败");
	// }
	// } catch (IOException e) {
	// logger.error("-->",e);
	// System.out.println("推送异常"+e.toString());
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * 组织菜谱数据给pad，在更换pad时使用
	 *
	 * @author shen
	 * @date:2015年5月14日下午8:09:13
	 * @Description: TODO
	 */
	public List<Map<String, Object>> getMapData(String orderid) {
		Map<String, Object> mappa = new HashMap<String, Object>();
		// 单品
		mappa.put("dishtype", "0");
		mappa.put("orderid", orderid);
		List<Map<String, Object>> resultMap = torderDetailMapper.findOrderDetailPad(mappa);
		// 处理忌口信息
		for (Map<String, Object> map : resultMap) {
			String sperequire = (String) map.get("sperequire");
			map.put("sperequire", OrderDetailParse.getSperequire(sperequire));
			map.put("taste", OrderDetailParse.getTaste(sperequire));
			map.put("globalsperequire", OrderDetailParse.getGlobalRemark(sperequire));
			map.put("freeuser", OrderDetailParse.getFreeUser(sperequire));
			map.put("freeauthorize", OrderDetailParse.getFreeAuthorize(sperequire));
			map.put("freereason", OrderDetailParse.getFreeReason(sperequire));
		}
		// 火锅
		mappa.put("dishtype", "1");
		mappa.put("ismaster", "1");
		List<Map<String, Object>> listfishpot = torderDetailMapper.findOrderDetailPad(mappa);
		if (listfishpot != null && listfishpot.size() > 0) {
			for (Map<String, Object> fishpotMap : listfishpot) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("orderid", orderid);
				params.put("dishtype", "1");
				params.put("ismaster", "0");
				params.put("parentkey", fishpotMap.get("primarykey"));
				List<Map<String, Object>> listfishorpot = torderDetailMapper.findOrderDetailPad(params);
				fishpotMap.put("dishes", updateDishType(listfishorpot, "0"));
				resultMap.add(fishpotMap);
			}
		}
		// 套餐
		mappa.put("dishtype", "2");
		mappa.put("ismaster", "1");
		mappa.put("childdishtype", "2");
		List<Map<String, Object>> listCombodish = torderDetailMapper.findOrderDetailPad(mappa);
		if (listCombodish != null && listCombodish.size() > 0) {
			for (Map<String, Object> combodishMap : listCombodish) {
				List<Map<String, Object>> listCombodishChild = new ArrayList<Map<String, Object>>();
				// 套餐中的单品
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("orderid", orderid);
				params.put("dishtype", "2");
				params.put("ismaster", "0");
				params.put("childdishtype", "0");
				params.put("parentkey", combodishMap.get("primarykey"));
				listCombodishChild = torderDetailMapper.findOrderDetailPad(params);
				listCombodishChild = updateDishType(listCombodishChild, "0");
				// 套餐中的鱼锅
				params.put("childdishtype", "1");
				params.put("ismaster", "1");
				List<Map<String, Object>> fishpotlist = torderDetailMapper.findOrderDetailPad(params);
				if (fishpotlist != null && fishpotlist.size() > 0) {
					for (Map<String, Object> fishpotmap : fishpotlist) {
						Map<String, Object> fishpotparams = new HashMap<String, Object>();
						fishpotparams.put("orderid", orderid);
						fishpotparams.put("dishtype", "2");
						fishpotparams.put("ismaster", "0");
						fishpotparams.put("childdishtype", "1");
						fishpotparams.put("parentkey", fishpotmap.get("primarykey"));
						List<Map<String, Object>> listfishorpot = torderDetailMapper.findOrderDetailPad(fishpotparams);
						fishpotmap.put("dishes", updateDishType(listfishorpot, "0"));
						fishpotmap.put("dishtype", "1");
						listCombodishChild.add(fishpotmap);
					}
				}
				combodishMap.put("dishes", listCombodishChild);
				resultMap.add(combodishMap);
			}
		}
		return resultMap;
	}

	public List<Map<String, Object>> updateDishType(List<Map<String, Object>> list, String dishType) {
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				map.put("dishtype", dishType);
			}
		}
		return list;
	}
	// @Override
	// public String queryallcoupons(){
	// List<String> listIds = Arrays.asList(dishIds.split(","));
	// List<Coupons> list = new ArrayList<Coupons>();
	// for(String dishId : listIds){
	// Coupons coupons = new Coupons();
	//
	// Tdish dish = dishDao.get(dishId);
	// List<TCouponRule> rules = couponsService.findRuleByDishId(dishId);
	//
	// coupons.setDishid(dishId);
	// coupons.setDishname(dish.getTitle());
	// coupons.setOrdernum(String.valueOf(dish.getOrderNum()));
	// coupons.setCoupons(rules);
	//
	// list.add(coupons);
	// }
	// CouponsInterface couponsInterface = new CouponsInterface();
	// couponsInterface.setResult("0");
	// couponsInterface.setCoupons(list);
	//
	// return JacksonJsonMapper.objectToJson(couponsInterface);
	// }

	@Override
	public Map<String, Object> updateDishWeight(Map<String, Object> params) {
		// TODO Auto-generated method stub
		boolean flag = true;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		String orderid = (String) params.get("orderId");
		if (orderid != null && !orderid.trim().isEmpty()) {
			if (orderid != null && !"".equals(orderid)) {

				params.put("orderid", orderid);
				// 更新t_order_detail
				flag = flag && torderDetailMapper.updateDishWeight(params) > 0;
				// 重新计算应收金额
				orderOpService.calcOrderAmount(orderid);
				// 更新t_printDish
				params.put("orderno", orderid);
				PrintObj printObj = tbPrintObjDao.find(params);
				params.remove("orderno");
				if (printObj != null) {
					params.put("printobjid", printObj.getId());
					flag = flag && tbPrintObjDao.updateDishWeight(params) > 0;
				}
				if (flag) {
					mapRet = ReturnMap.getSuccessMap("更新成功", orderid);
				} else {
					mapRet = ReturnMap.getFailureMap("未找到相应的菜品");
				}
			}
		} else {
			mapRet = ReturnMap.getFailureMap("订单为空");
		}
		return mapRet;
	}

	public class PrintThread extends Thread {

		PrintObj printObj;

		PrintThread(PrintObj printObj) {
			this.printObj = printObj;
		}

		@Override
		public void run() {
			// 称重后打印单
			// printService.setDestination(printDishQueue);
			printService.sendMessage(printObj);

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
	// @Override
	// public int verifyAllCLean() {
	// return torderMapper.verifyAllCLean();
	// }
	//
	// @Override
	// public int verifyAllTableClear() {
	// return torderMapper.verifyAllTableClear();
	// }

	@Override
	public String callEndWork(String userName, String isSucess) {
		return torderMapper.callEndWork(userName, isSucess);
	}

	@Override
	public void setOrderMember(Map<String, Object> params) {
		torderMapper.setOrderMember(params);
	}

	/**
	 * 服务员pad开台
	 */
	@Override
	public String startOrderwaiter(Torder tOrder) {

		Map<String, String> mapRet = new HashMap<String, String>();

		TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
		if (tbOpenBizLog == null) {
			mapRet.put("result", "3");
			return JacksonJsonMapper.objectToJson(mapRet);
		}

		synchronized (this) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tableNo", tOrder.getTableNo());
			// map.put("status", "0");
			List<Map<String, Object>> resultMap = tableService.find(map);

			if (resultMap == null || resultMap.size() == 0 || resultMap.size() > 1) {
				mapRet.put("result", "2");
				return JacksonJsonMapper.objectToJson(mapRet);
			}
			if (!"0".equals(String.valueOf(resultMap.get(0).get("status")))) {
				mapRet.put("result", "1");
				return JacksonJsonMapper.objectToJson(mapRet);
			}
			// 1.预定桌子

			// boolean bTable = tableService.updateStatus(tTable);
			// if(!bTable){
			// mapRet.put("result", "1");
			// return JacksonJsonMapper.objectToJson(mapRet);
			// }

			// 2.生成订单号码 正在下单
			// String orderId = IdentifierUtils.getId().generate().toString();
			String tableId = String.valueOf(resultMap.get(0).get("tableid"));

			int shiftid = 0;

			String currentTime = DateUtils.getCurrentTime();

			List<Map<String, Object>> listDict = dictionaryDao.getDatasByType("BIZPERIODDATE");
			if (listDict == null || listDict.size() == 0) {
				shiftid = 0;
			} else {
				for (Map<String, Object> mapWorkDay : listDict) {

					if (mapWorkDay.get("itemid") == null) {
						continue;
					}
					String itemId = (String) mapWorkDay.get("itemid");
					String data_type = (String) mapWorkDay.get("datetype");
					String begin_time = (String) mapWorkDay.get("begintime");
					String end_time = (String) mapWorkDay.get("endtime");

					currentTime = currentTime.replaceAll(":", "");
					begin_time = begin_time.replaceAll(":", "");
					end_time = end_time.replaceAll(":", "");

					// 0 午市
					if ("0".equals(itemId)) {
						// T today N next day
						if ("T".equalsIgnoreCase(data_type)) {

							if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) <= 0) {
								shiftid = 0;
							} else if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) >= 0) {
								shiftid = 1;
							}
						} else if ("N".equalsIgnoreCase(data_type)) {
							if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) <= 0) {
								shiftid = 0;
							} else {
								if (currentTime.compareTo(begin_time) < 0 && currentTime.compareTo(end_time) <= 0) {
									shiftid = 0;
								} else {
									shiftid = 1;
								}
							}
						}
					} else if ("1".equals(itemId)) {
						// 1 晚市
						// T today N next day
						if ("T".equalsIgnoreCase(data_type)) {

							if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) <= 0) {
								shiftid = 1;
							} else {
								shiftid = 0;
							}
						} else if ("N".equalsIgnoreCase(data_type)) {
							if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) <= 0) {
								shiftid = 1;
							} else {
								if (currentTime.compareTo(begin_time) > 0 && currentTime.compareTo(end_time) >= 0) {
									shiftid = 1;
								} else {
									shiftid = 0;
								}
							}
						}
					}
				}

			}

			String orderId = torderMapper.getPrimaryKey();
			String orderIdDate = "H" + DateUtils.toOrderIdString(new Date());
			Torder toder = torderMapper.getMaxOrderNum(orderIdDate);
			// 使用了最大客户数返回的最大订单数
			int maxOrderNum = toder.getCustnum();
			Torder order = new Torder();
			order.setTableids(tableId);
			order.setOrderid(orderId);
			order.setOrderstatus(Constant.ORDERSTATUS.ORDER_STATUS);
			order.setChildNum(tOrder.getChildNum() == null ? 0 : tOrder.getChildNum());
			order.setCurrenttableid(tableId);
			order.setCustnum(tOrder.getCustnum());
			order.setManNum(tOrder.getManNum() == null ? 0 : tOrder.getManNum());
			order.setSpecialrequied(tOrder.getSpecialrequied());
			order.setUserid(tOrder.getUsername());
			order.setWomanNum(tOrder.getWomanNum() == null ? 0 : tOrder.getWomanNum());
			order.setBranchid(Integer.valueOf(PropertiesUtils.getValue("current_branch_id")));
			// 根据数据字典配置 得出早市还是晚市
			order.setShiftid(shiftid);
			order.setAgeperiod(tOrder.getAgeperiod());
			order.setMeid(tOrder.getMeid());
			order.setOrderNum(maxOrderNum);
			order.setCustnum(tOrder.getCustnum());
			torderMapper.insert(order);

			TbTable tTable = new TbTable();
			tTable.setTableid(tableId);
			tTable.setOrderid(orderId);

			tTable.setStatus(Constant.TABLESTATUS.EAT_STATUS);
			tableService.updateStatus(tTable);

			// 设定用户排序数量限定
			// String user_order_num =
			// PropertiesUtils.getValue("user_order_num");
			//// int orderNum = 0;
			// if(user_order_num != null){
			// orderNum = Integer.parseInt(user_order_num);
			// }
			// 查询当前t_user 表最大的排序数目
			// select max(ordernum) from t_user
			// TbUser tbUser = userService.findById(tOrder.getUsername());

			User tbUser = userService.findMaxOrderNum();

			userService.updateUserOrderNum(tOrder.getUsername(), tbUser.getOrderNum());

			// if(tbUser != null && (tbUser.getOrderNum() == 0 ||
			// tbUser.getOrderNum() < orderNum)){
			//// if( tbUser.getOrdernum() < orderNum){
			// userService.updateUserOrderNum(tOrder.getUsername(),tbUser.getOrderNum());
			//// }
			// }

			TbDataDictionary dd = datadictionaryService.findById("backpsd");
			TbDataDictionary vipaddress = datadictionaryService.findById("vipaddress");
			TbDataDictionary locktime = datadictionaryService.findById("locktime");
			TbDataDictionary delaytime = datadictionaryService.findById("delaytime");

			mapRet.put("result", "0");
			mapRet.put("orderid", orderId);
			mapRet.put("backpsd", dd == null ? "" : dd.getItemid());// 退菜密码

			mapRet.put("vipaddress", vipaddress == null ? "" : vipaddress.getItemid()); // 雅座的VIP地址
			mapRet.put("locktime", locktime == null ? "" : locktime.getItemid()); // 屏保锁屏时间
			mapRet.put("delaytime", delaytime == null ? "" : delaytime.getItemid()); // 屏保停留时间
			// 添加日志
			// Tworklog tworklog = new Tworklog();
			// tworklog.setWorkid(UUID.randomUUID().toString());
			// List<Map<String, Object>> list =
			// datadictionaryService.getDatasByType("WORKTYPE");
			// for(int i=0;i<list.size();i++){
			// if(list.get(i).get("itemDesc").equals("开桌")){
			// tworklog.setWorktype(list.get(i).get("itemid").toString());
			// };
			// }
			// tworklog.setUserid(tOrder.getUsername());
			// tworklog.setBegintime(new Date());
			// tworklog.setEndtime(new Date());
			// tworklog.setIpaddress("127.0.0.1");
			// tworklog.setStatus(1);
			// tworklog.setTableid(tOrder.getTableNo());
			// workLogService.saveLog(tworklog);

			// 开台前清空当前台的操作日记
			toperationLogService.deleteToperationLogByTableNo(tOrder.getTableNo());
		}
		return JacksonJsonMapper.objectToJson(mapRet);
	}

	/**
	 * 服务员pad换台
	 */
	@Override
	public Map<String, Object> switchPadOrderInfowaiter(Map<String, Object> params) {
		// TODO Auto-generated method stub
		Map<String, Object> mapRet = new HashMap<String, Object>();
		List<Map<String, Object>> resultMap = tableService.find(params);
		if (resultMap != null && resultMap.size() > 0) {
			String orderid = String.valueOf(resultMap.get(0).get("orderid"));
			if (orderid != null && !"".equals(orderid)) {
				Map<String, Object> orderMap = torderMapper.findOne(orderid);
				if (orderMap != null) {
					if ("3".equals(String.valueOf(orderMap.get("orderstatus")))) {
						mapRet.put("flag", "3");
						mapRet.put("desc", "该桌已结账");
						return mapRet;
					}
					if ("0".equals(String.valueOf(orderMap.get("orderstatus")))) {
						// t.mannum,t.childNum,t.womanNum
						mapRet.put("flag", "1");
						mapRet.put("desc", "获取数据成功");
						mapRet.put("currenttableid", params.get("tableNo"));
						mapRet.put("orderid", orderid);
						mapRet.put("memberno", orderMap.get("memberno"));
						mapRet.put("manNum", orderMap.get("manNum"));
						mapRet.put("womanNum", orderMap.get("womanNum"));
						mapRet.put("custnum", orderMap.get("custnum"));
						mapRet.put("waiterNum", orderMap.get("userid"));
						mapRet.put("ageperiod", orderMap.get("ageperiod"));
						mapRet.put("begintime", orderMap.get("begintime"));
						Map<String, Object> mappa = new HashMap<String, Object>();
						mappa.put("orderid", orderid);
						TbDataDictionary dd = datadictionaryService.findById("backpsd");
						TbDataDictionary vipaddress = datadictionaryService.findById("vipaddress");
						TbDataDictionary locktime = datadictionaryService.findById("locktime");
						TbDataDictionary delaytime = datadictionaryService.findById("delaytime");
						mapRet.put("result", "0");
						mapRet.put("orderid", orderid);
						mapRet.put("backpsd", dd == null ? "" : dd.getItemid());// 退菜密码
						mapRet.put("vipaddress", vipaddress == null ? "" : vipaddress.getItemid()); // 雅座的VIP地址
						mapRet.put("locktime", locktime == null ? "" : locktime.getItemid()); // 屏保锁屏时间
						mapRet.put("delaytime", delaytime == null ? "" : delaytime.getItemid()); // 屏保停留时间
						mapRet.put("rows", getMapData(orderid));// 获取订单数据
						// xk
						// 当前pad的meid不为空，更新meid
						if (params.get("meid") != null) {
							Torder order = new Torder();
							order.setOrderid(orderid);
							order.setMeid(String.valueOf(params.get("meid")));
							// order.setShiftid(Integer.valueOf(String.valueOf(orderMap.get("shiftid"))));
							// update(order);
							torderMapper.update(order);

						}
						// xk
						// 原订单meid不为空，推送清空

						/*
						 * 服务员pad不清台
						 * if(orderMap.get("meid")!=null&&params.get("meid")!=
						 * null&&!String.valueOf(orderMap.get("meid")).equals(
						 * String.valueOf(params.get("meid")))){ StringBuffer
						 * str=new StringBuffer(Constant.TS_URL); String
						 * msg=String.valueOf(orderMap.get("meid"))+"|"+params.
						 * get("tableNo");
						 * str.append(Constant.MessageType.msg_1005+"/"+msg);
						 * System.out.println(str.toString()); new Thread(new
						 * TsThread(str.toString())).run(); }
						 */

						// 删除桌子日志
						toperationLogService.deleteToperationLogByTableNo(String.valueOf(params.get("tableNo")));

						return mapRet;
					}
					mapRet.put("flag", "4");
					mapRet.put("desc", "订单为空");
					return mapRet;
				} else {
					mapRet.put("flag", "4");
					mapRet.put("desc", "订单为空");
					return mapRet;
				}

			} else {
				mapRet.put("flag", "2");
				mapRet.put("desc", "已清机");
				return mapRet;
			}

		} else {
			// 没找到这个桌号
			mapRet.put("flag", "0");
			mapRet.put("desc", "未找到这个桌号");
			return mapRet;
		}
	}

	@Override
	public String updateOrderwaiter(Torder tOrder) {

		// 开台前清空当前台的操作日记
		toperationLogService.deleteToperationLogByTableNo(tOrder.getTableNo());
		Torder order = new Torder();
		order.setOrderid(tOrder.getOrderNo());
		order.setChildNum(tOrder.getChildNum());
		order.setCustnum(tOrder.getCustnum());
		order.setManNum(tOrder.getManNum());
		order.setUserid(tOrder.getUsername());
		order.setWomanNum(tOrder.getWomanNum());
		order.setAgeperiod(tOrder.getAgeperiod());

		User tbUser = userService.findMaxOrderNum();
		userService.updateUserOrderNum(tOrder.getUsername(), tbUser.getOrderNum());

		int result = torderMapper.update(order);
		if (result > 0) {
			return Constant.SUCCESSMSG;
		}

		return Constant.FAILUREMSG;
	}

	@Override
	public String createChildOrderid(String orderid) throws Exception {
		synchronized (this) {
			// 查询主订单
			Map<Object, Object> masterOrder = torderMapper.findOne(orderid);
			if (masterOrder == null || masterOrder.isEmpty()) {
				throw new Exception("查询订单失败，订单不存在！订单号：" + orderid);
			}
			String currenttableid = (String) masterOrder.get("currenttableid");
			TbTable table = tableService.findById(currenttableid);
			// 判断是正常加菜还是咖啡模式加菜
			String orderstatus = String.valueOf(masterOrder.get("orderstatus")).trim();
			switch (orderstatus) {
			case "0":
				return orderid;
			case "3":
				if (table.getStatus() != 1) {
					throw new Exception("餐台状态异常,不能加菜！订单号：" + orderid);
				}
				break;
			default:
				throw new Exception("订单状态异常！订单号：" + orderid);
			}
			String masterOrderid = orderid;
			// 生成子订单号
			int number = 1;
			int index = orderid.indexOf("-");
			if (index > 0) {
				masterOrderid = orderid.substring(0, index);
				number = Integer.parseInt(orderid.substring(index + 1)) + 1;
			}
			String newOrderid = masterOrderid + "-" + number;

			// 创建子订单
			Torder order = new Torder();
			order.setTableids((String) masterOrder.get("tableids"));
			order.setOrderid(newOrderid);
			order.setOrderstatus(Constant.ORDERSTATUS.ORDER_STATUS);
			order.setChildNum((Integer) masterOrder.get("childNum"));
			order.setCurrenttableid(currenttableid);
			order.setCustnum((Integer) masterOrder.get("custnum"));
			order.setManNum((Integer) masterOrder.get("mannum"));
			order.setSpecialrequied((String) masterOrder.get("specialrequied"));
			order.setUserid((String) masterOrder.get("userid"));
			order.setWomanNum((Integer) masterOrder.get("womanNum"));
			order.setBranchid(Integer.parseInt(PropertiesUtils.getValue("current_branch_id")));
			order.setShiftid((int) masterOrder.get("shiftid"));
			order.setAgeperiod((String) masterOrder.get("ageperiod"));
			order.setMeid((String) masterOrder.get("meid"));
			torderMapper.insert(order);

			// 更新餐台当前订单
			TbTable tTable = new TbTable();
			tTable.setTableid(currenttableid);
			tTable.setOrderid(newOrderid);
			tTable.setStatus(1);
			tableService.updateStatus(tTable);

			// 更新服务员订单数量
			User tbUser = userService.findMaxOrderNum();
			userService.updateUserOrderNum(order.getUserid(), tbUser.getOrderNum());
			return newOrderid;
		}
	}

	@Override
	public Map<String, Object> calGetOrderInfo(Map<String, Object> params) {
		String orderid = (String) params.get("orderid");
		Map<String, Object> mapRet = new HashMap<String, Object>();
		String branchid = PropertiesUtils.getValue("current_branch_id");
		params.put("branchid", branchid);
		params.put("id", orderid);
		if (orderid != null && !orderid.isEmpty() && !orderid.equals("null")) {
			// 如果是外卖订单，强制绑定对应餐台
			doBindTableForTakeOutFood(orderid);

			if (!params.containsKey("clean")) {
				mapRet.put("rows", getMapData(orderid));// 获取订单数据
			}
			// 或POS使用数据订单信息
			Map<String, Object> userOrderInfo = findOrderByInfo(orderid);
			mapRet.put("userOrderInfo", userOrderInfo);
			// 优惠信息
			params.put("branchid", branchid);
			params.put("id", orderid);
			Map<String, Object> preMap = new HashMap<>();
			preMap.put("orderid", orderid);
			String menberNo = userOrderInfo.get("memberno") == null ? ""
					: String.valueOf(userOrderInfo.get("memberno")).equals("null") ? ""
							: String.valueOf(userOrderInfo.get("memberno"));
			params.put("memberno", menberNo);
			OperPreferentialResult result = preResult(params);
			mapRet.put("preferentialInfo", result);
			// 服务费信息
			TServiceCharge serviceCharge = serviceCharge(orderid, userOrderInfo,
					result.getPayamount().subtract(result.getTipAmount()), result.getMenuAmount());
			mapRet.put("serviceCharge", serviceCharge);
		}

		return ReturnMap.getSuccessMap(mapRet);
	}

	private TServiceCharge serviceCharge(String orderid, Map<String, Object> userOrderInfo, BigDecimal payDecimal,
			BigDecimal MenuDecimal) {
		Map<String, Object> serParams = new HashMap<>();
		serParams.put("orderId", orderid);
	
		BigDecimal calcServiceCharge = null;
		int chargeOn = (int) userOrderInfo.get("chargeOn");
		int chargeType=(int) userOrderInfo.get("chargeType");
		int chargeRateRule=(int) userOrderInfo.get("chargeRateRule");
		int chargeRate=(int) userOrderInfo.get("chargeRate");
		String chargeTime=(String)userOrderInfo.get("chargeTime");
		TServiceCharge servceCharageBean=null;
		if(chargeOn!=0){
			   servceCharageBean = serviceChargeDao.getChargeInfo(serParams);
			if(servceCharageBean==null||(servceCharageBean!=null&&servceCharageBean.getIsCustom()==0)){
				Map<String, Object> params = new HashMap<>();
				params.put("itemid", chargeType);
				params.put("type", "TABLECHARGE");
				List<Object> dataDictionary = dictionaryDao.find(params);
				if (!dataDictionary.isEmpty()) {
					// 0比例 1 固定 2 时长
					calcServiceCharge = StrategyFactory.INSTANCE.calcServiceCharge(userOrderInfo, payDecimal,
							MenuDecimal);
				}
			}else if(servceCharageBean.getIsCustom()==1){
				calcServiceCharge=servceCharageBean.getChargeAmount();
			}
		
			if (servceCharageBean == null) {
				servceCharageBean = new TServiceCharge(orderid, chargeOn, chargeType, chargeRateRule, chargeRate,
						chargeTime);
				servceCharageBean.setChargeAmount(calcServiceCharge);
				servceCharageBean.setAutho("");
				long id  =serviceChargeDao.insertChargeInfo(servceCharageBean);
				servceCharageBean.setId(id);
			} else  {
				servceCharageBean.setChargeAmount(calcServiceCharge);
				try {
					serviceChargeDao.updateChargeInfo(servceCharageBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
		

		return servceCharageBean;

	}

	/**
	 * 强制外卖订单绑定餐台
	 */
	private void doBindTableForTakeOutFood(String orderid) {
		if (!StringUtils.isEmpty(orderid)) {
			Map<String, Object> res = torderMapper.findOne(orderid);
			if (!MapUtils.isEmpty(res)) {
				if (!StringUtil.isEmpty(res.get("currenttableid"))) {
					TbTable table = tableService.findById(res.get("currenttableid").toString());
					if (table != null && ("2".equals(table.getTabletype()) || "3".equals(table.getTabletype()))) {
						Map<String, Object> param = new HashMap<>();
						param.put("orderid", orderid);
						param.put("tableid", res.get("currenttableid"));
						tableDao.updateTableById(param);
					}
				}
			}
		}
	}

	private Map<String, Object> findOrderByInfo(String orderid) {
		// orderInvoiceTitle 发票抬头
		// orderStatus; 订单状态
		// tableStatus 餐桌状态
		// customerNumber 多少人
		Map<String, Object> outresultMap = new HashMap<>();
		List<Map<String, Object>> resultMapList = torderDetailMapper.findOrderByInfo(orderid);
		if (resultMapList != null && !resultMapList.isEmpty()) {
			List<Object> tipMapList = torderDetailMapper.findOrderByTip(orderid);
			Map<String, Object> resultMap = resultMapList.get(0);
			Map<String, Object> tipMap = !tipMapList.isEmpty() ? (Map<String, Object>) tipMapList.get(0) : null;
			outresultMap.put("orderInvoiceTitle", resultMap.get("invoice_title"));
			outresultMap.put("orderStatus", resultMap.get("orderstatus"));
			outresultMap.put("tableStatus", resultMap.get("status"));
			outresultMap.put("isFree", (Boolean) resultMap.get("isfree") ? "1" : "0");
			outresultMap.put("numOfMeals", resultMap.get("num_of_meals"));
			outresultMap.put("customerNumber", resultMap.get("custnum"));
			outresultMap.put("womanNum", resultMap.get("womanNum"));
			outresultMap.put("childNum", resultMap.get("childNum"));
			outresultMap.put("mannum", resultMap.get("mannum"));
			outresultMap.put("memberno", resultMap.get("memberno"));
			outresultMap.put("begintime", DateUtils.formatDateToString((Date) resultMap.get("begintime")));
			Date date = (Date) resultMap.get("endtime");
			outresultMap.put("endtime", date == null ? "" : DateUtils.formatDateToString(date));
			outresultMap.put("areaname", resultMap.get("areaname"));
			outresultMap.put("tableName", resultMap.get("tableName"));
			outresultMap.put("fullName", resultMap.get("userid"));
			outresultMap.put("waiterName", resultMap.get("name"));
			// 小费相关
			outresultMap.put("tipWaiterNum", tipMap != null ? tipMap.get("waiter_number") : "");
			outresultMap.put("tipWaiterName", tipMap != null ? tipMap.get("name") : "");
			/** 预打印 **/
			int printcount = Integer.valueOf(String.valueOf(resultMap.get("befprintcount")));
			outresultMap.put("befprintcount", printcount + 1);

			/** 服务费 **/
			outresultMap.put("chargeOn", resultMap.get("chargeOn"));
			outresultMap.put("chargeType", resultMap.get("chargeType"));
			outresultMap.put("chargeRateRule", resultMap.get("chargeRateRule"));
			outresultMap.put("chargeRate", resultMap.get("chargeRate"));
			outresultMap.put("chargeAmount", resultMap.get("chargeAmount"));
			outresultMap.put("chargeTime", resultMap.get("chargeTime"));
		}
		outresultMap.put("orderid", orderid);
		return outresultMap;
	}

	/**
	 * 计算优惠信息
	 *
	 * @param params
	 * @param memberno
	 * @return
	 */
	private OperPreferentialResult preResult(Map<String, Object> params) {
		// 获取所有订单优惠
		List<TorderDetailPreferential> allDetailPre = detailPreferentialDao.getTorderDetailSbyOrderid(params);
		// 清空订单对应得优惠券

		String orderid = (String) params.get("orderid");
		Map<String, Object> setMap = new HashMap<>();

		// 如果是获取优惠列表 则由优惠所有算出结果后在同一计算优惠信息
		OperPreferentialResult operPreferentialResult = new OperPreferentialResult();
		// 新辣道特殊新编码处理
		if (PropertiesUtils.getValue("tenant_id").equals("100011")) {
			autoPre(orderid, params.get("memberno"), operPreferentialResult);
			// if (allDetailPre == null || allDetailPre.isEmpty()) {
			// autoPre(orderid,params.get("memberno"), operPreferentialResult);
			// } else {
			// boolean falg = false;
			// for (TorderDetailPreferential branchDataSyn : allDetailPre) {
			// if (branchDataSyn.getIsCustom() == 2) {
			// falg = true;
			// break;
			// }
			// }
			// if (!falg) {
			//
			// }
			// }
		}

		// 需要得到已经是有优惠的值
		for (TorderDetailPreferential branchDataSyn : allDetailPre) {
			if (branchDataSyn.getIsCustom() == 2) {
				continue;
			}
			setMap.clear();
			setMap.put("orderid", orderid);
			setMap.put("preferentialid", branchDataSyn.getPreferential());
			setMap.put("disrate", branchDataSyn.getDiscount().toString());
			setMap.put("type", branchDataSyn.getActivity().getType());
			setMap.put("subtype", branchDataSyn.getActivity().getSubType());
			setMap.put("preferentialNum", "1");
			setMap.put("dishid", branchDataSyn.getDishid());
			setMap.put("unit", branchDataSyn.getUnit());
			setMap.put("preferentialAmt", operPreferentialResult.getAmount().toString());
			setMap.put("isCustom", String.valueOf(branchDataSyn.getIsCustom()));
			setMap.put("updateId", branchDataSyn.getId());
			setMap.put("insertime", branchDataSyn.getInsertime());
			setMap.put("resultAmount", "0");
			setMap.put("preferentialAmout", branchDataSyn.getDeAmount().toString());
			setMap.put("memberno", params.get("memberno"));
			if (branchDataSyn.getIsCustom() == 5) {
				// 雅座
				setMap.put("preferentialAmout", branchDataSyn.getDeAmount().toString());
				setMap.put("type", branchDataSyn.getPreType());
				setMap.put("preferentialName", branchDataSyn.getPreName());
			}
			calALLAmout(setMap, operPreferentialResult);
		}

		StrategyFactory.INSTANCE.calcAmount(detailPreferentialDao, caleTableAmountMapper, orderid,
				dataDictionaryService, operPreferentialResult, orderMapper, orderOpMapper,
				(String) params.get("itemid"));
		return operPreferentialResult;
	}

	/**
	 *
	 */
	private void calALLAmout(Map<String, Object> setMap, OperPreferentialResult operPreferentialResult) {
		OperPreferentialResult operResult = this.preferentialActivityService.updateOrderDetailWithPreferential(setMap);
		BigDecimal preferentialAmt = operPreferentialResult.getAmount();
		BigDecimal toalFreeAmount = operPreferentialResult.getToalFreeAmount();
		BigDecimal toalDebitAmount = operPreferentialResult.getToalDebitAmount();
		BigDecimal toalDebitAmountMany = operPreferentialResult.getToalDebitAmountMany();
		// 就算每次优免总额
		for (TorderDetailPreferential dep : operResult.getDetailPreferentials()) {
			preferentialAmt = preferentialAmt.add(dep.getDeAmount());
			toalFreeAmount = toalFreeAmount.add(dep.getToalFreeAmount());
			toalDebitAmount = toalDebitAmount.add(dep.getToalDebitAmount());
			toalDebitAmountMany = toalDebitAmountMany.add(dep.getToalDebitAmountMany());
		}
		operPreferentialResult.setAmount(preferentialAmt);
		operPreferentialResult.setToalFreeAmount(toalFreeAmount);
		operPreferentialResult.setToalDebitAmount(toalDebitAmount);
		operPreferentialResult.setToalDebitAmountMany(toalDebitAmountMany);
		List<TorderDetailPreferential> detailPreferentials = operPreferentialResult.getDetailPreferentials();
		if (detailPreferentials == null || detailPreferentials.isEmpty()) {
			operPreferentialResult.setDetailPreferentials(operResult.getDetailPreferentials());
		} else {
			detailPreferentials.addAll(operResult.getDetailPreferentials());
			operPreferentialResult.setDetailPreferentials(detailPreferentials);
		}

	}

	private void autoPre(String orderid, Object memberno, OperPreferentialResult operPreferentialResult) {
		Map<String, Object> setMap = new HashMap<>();
		setMap.put("orderid", orderid);
		setMap.put("type", "03");
		setMap.put("isCustom", "2");
		setMap.put("resultAmount", "0");
		setMap.put("memberno", memberno);
		calALLAmout(setMap, operPreferentialResult);
	}

	@Override
	public String consumInfo() {
		Map<String, Object> consumInfo = torderMapper.selectConsumInfo();
		if (null == consumInfo) {
			consumInfo = new HashMap<>();
		}
		if (consumInfo.isEmpty()) {
			consumInfo.put("totalAmount", "0.00");
			consumInfo.put("dueamount", "0.00");
			consumInfo.put("ssamount", "0.00");
			consumInfo.put("custnum", "0");
			consumInfo.put("orderCount", "0");
		} else {
			for (Map.Entry<String, Object> entry : consumInfo.entrySet()) {
				if (null == entry.getValue()) {
					entry.setValue(new BigDecimal(0));
				}
			}
			BigDecimal dueamount = (BigDecimal) consumInfo.get("dueamount");
			BigDecimal ssamount = (BigDecimal) consumInfo.get("ssamount");
			BigDecimal totalAmount = dueamount.add(ssamount);
			consumInfo.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_DOWN).toString());
			consumInfo.put("dueamount", dueamount.setScale(2, BigDecimal.ROUND_DOWN).toString());
			consumInfo.put("ssamount", ssamount.setScale(2, BigDecimal.ROUND_DOWN).toString());
		}
		return JSON.toJSONString(ReturnMap.getSuccessMap(consumInfo));
	}
}
