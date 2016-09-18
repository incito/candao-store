//package com.candao.www.webroom.jiwang.service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.candao.common.utils.DateUtils;
//import com.candao.common.utils.PropertiesUtils;
//import com.candao.inorder.dao.InorderCheckPeriodDao;
//import com.candao.inorder.dao.InorderCheckTableDao;
//import com.candao.inorder.dao.InorderDayInfoDao;
//import com.candao.inorder.dao.InorderEmployeeDao;
//import com.candao.inorder.dao.InorderItemDao;
//import com.candao.inorder.dao.InorderMenuDao;
//import com.candao.inorder.dao.InorderPrintQueueDao;
//import com.candao.inorder.dao.InorderStationDao;
//import com.candao.inorder.data.LinuxPrintEmpBean;
//import com.candao.inorder.pojo.TblCheck;
//import com.candao.inorder.pojo.TblDayinfo;
//import com.candao.inorder.pojo.TblEmployee;
//import com.candao.inorder.pojo.TblItem;
//import com.candao.inorder.pojo.TblMenu;
//import com.candao.inorder.pojo.TblPrintqueue;
//import com.candao.inorder.pojo.TblStation;
//import com.candao.inorder.service.InorderCheckTableService;
//import com.candao.inorder.utils.CommonUtil;
//import com.candao.inorder.utils.CustomerContextHolder;
//import com.candao.inorder.utils.DataSourceInstances;
//import com.candao.inorder.utils.LiunxSSHScpclientUtil;
//import com.candao.inorder.utils.print.PrintQueueUtil;
//import com.candao.www.data.dao.TbTableDao;
//import com.candao.www.data.dao.TdishDao;
//import com.candao.www.data.model.TorderDetail;
//import com.candao.www.webroom.model.Order;
//import com.candao.www.webroom.service.impl.OrderDetailServiceImpl;
//
///**
// * 
// * @author Candao
// *
// */
//
//
//public class InorderOrderDeatilServiceImpl extends OrderDetailServiceImpl {
//	private Logger logger = LoggerFactory.getLogger(InorderOrderDeatilServiceImpl.class);
//	/** 订单信息 **/
//	@Autowired
//	private InorderCheckTableDao checkTableDao;
//
//	/** 订单详细信息 **/
//	@Autowired
//	private InorderItemDao itemDao;
//
//	/** 菜单信息 **/
//	@Autowired
//	private InorderMenuDao menDao;
//	/** 工作信息 **/
//	@Autowired
//	private InorderDayInfoDao dayInfoDao;
//
//	/** 获取工作台信息 **/
//	@Autowired
//	private InorderStationDao stationDao;
//
//	@Autowired
//	private TbTableDao tbTableDao;
//	@Autowired
//	private InorderCheckTableService checktableservice;
//
//	@Autowired
//	private TdishDao tdishDao;
//
//	@Override
//	public Map<String, Object> setOrderDetailList(Order orders) {
//		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
//		// 获取菜品编号
//		String outLet = PropertiesUtils.getValue("outlet");
//		String conStation = PropertiesUtils.getValue("station");
//		// 获取开业时间
//		// 获取开业时间
//		TblDayinfo dataInfo = dayInfoDao.getActionDayIndo();
//		// checkDate
//		String date = dataInfo == null ? DateUtils.toString(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
//				: dataInfo.getDate();
//		// 根据坐台号查区域
//		Map<String, Object> queryArea = new HashMap<String, Object>();
//		queryArea.put("tableNo", orders.getCurrenttableid());
//		List<Map<String, Object>> resultMap = tbTableDao.find(queryArea);
//		// 返回添加状态
//		Map<String, Object> result = null;
//		// 先判断当前餐台是否被占用
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("tableNo", orders.getCurrenttableid());
//		params.put("floor", resultMap.get(0).get("areaname"));
//		params.put("date", date);
//		TblCheck inorderTable = checkTableDao.tableInfoMes(params);
//		// 获取订单号
//		TblStation station = stationDao.tblStation(conStation);
//		// 数据库生成订单规则
//		String startCheckNO = String.valueOf(station.getStartcheckno());
//		// 当前订单号
//		String currentCheckNo = String.valueOf(station.getCurrentcheckno());
//		// 如果长度一样长说明是数据库生成的订单号
//		String nextCheckNo = startCheckNO.length() == currentCheckNo.length()
//				? currentCheckNo + currentCheckNo.substring(currentCheckNo.length() - 1, currentCheckNo.length())
//				: currentCheckNo;
//		if (inorderTable == null) {
//			inorderTable = addCheck(Integer.valueOf(outLet), DateUtils.parse(date), station.getStation(), nextCheckNo,
//					orders, Integer.valueOf((String) resultMap.get(0).get("areaname")));
//			if (inorderTable != null) {
//				result = addItemStatus(orders, inorderTable);
//				station.setCurrentcheckno(Integer.valueOf(nextCheckNo) + 1);
//				stationDao.updateStation(station);
//			}
//			// 成功过后更新订单表
//		} else {
//			result = addItemStatus(orders, inorderTable);
//		}
//		if (result != null) {
//			inorderTable.setChecktot((double) result.get("allItemtot"));
//			inorderTable.setItemtot((double) result.get("allItemtot"));
//			checkTableDao.updateCheckForTot(inorderTable);
//
//			Map<String, String> printMap;
//			try {
//				printMap = printMenu(orders, inorderTable.getCheck());
//
//				PrintQueueUtil.getInstance().addQueue(printMap);
//			} catch (IOException e) {
//				logger.error("吉旺打印获取失败", e.fillInStackTrace());
//			}
//		}
//
//		return super.setOrderDetailList(orders);
//	}
//
//	@Autowired
//	private InorderEmployeeDao employeeDao;
//	@Autowired
//	private InorderPrintQueueDao printQueueDao;
//
//	@SuppressWarnings("static-access")
//	private Map<String, String> printMenu(Order order, String checkNum) throws IOException {
//
//		// 相同类型打印为一组
//		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_INORDER);
//		Map<String, TblPrintqueue> printALLMap = printQueueDao.queryPritQueueALL();
//		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
//		TblEmployee emloyee = employeeDao.queryForEmpNo("14");
//
//		Map<String, TorderDetail> orderDetailMap = getOderDetailMap(order);
//		// 查询菜品详情表
//		List<TblMenu> menus = menDao.queryMeun(orderDetailMap.keySet().toArray());
//		// 对打印信息分区 分域
//		Map<String, List<LinuxPrintEmpBean>> printMes = new HashMap<String, List<LinuxPrintEmpBean>>();
//		for (TblMenu menu : menus) {
//			// 获取4个区的打印
//			String q1 = menu.getPrintq1();
//			String q2 = menu.getPrintq2();
//			String q3 = menu.getPrintq3();
//			String q4 = menu.getPrintq4();
//
//			/** 当前菜品个数 **/
//			String menuNum = orderDetailMap.get(menu.getItem()).getDishnum();
//			/** 点菜的名称 **/
//			setPrintMes(emloyee, printALLMap, order, printMes, menuNum, menu.getNames3(), q1, checkNum);
//			setPrintMes(emloyee, printALLMap, order, printMes, menuNum, menu.getNames3(), q2, checkNum);
//			setPrintMes(emloyee, printALLMap, order, printMes, menuNum, menu.getNames3(), q3, checkNum);
//			setPrintMes(emloyee, printALLMap, order, printMes, menuNum, menu.getNames3(), q4, checkNum);
//
//		}
//		// 封装成数据流
//		Map<String, String> printMesBytes = new HashMap<String, String>();
//		// 获取挂载磁盘路径
//		String diskPath = PropertiesUtils.getValue("mountDisk");
//		for (String key : printMes.keySet()) {
//			StringBuffer buffer = new StringBuffer();
//			List<LinuxPrintEmpBean> linuxPrintEmpBeans = printMes.get(key);
//			for (LinuxPrintEmpBean empBean : linuxPrintEmpBeans) {
//				String tempPrintMes = String.format(LiunxSSHScpclientUtil.getInstance().getEmpPrintbuffer(),
//						empBean.getPrintQname(), empBean.getTableNum(), empBean.getEmpName(), empBean.getCover(),
//						empBean.getCheckNum(), empBean.getDate(), empBean.getTime(), empBean.getMenuNum(),
//						empBean.getMenuName());
//				buffer.append(tempPrintMes);
//			}
//			printMesBytes.put(diskPath + CommonUtil.getPrintFileName(key, PropertiesUtils.getValue("station")),
//					buffer.toString());
//		}
//		return printMesBytes;
//	}
//
//	/**
//	 * 
//	 * @param order
//	 * @return 返回订单与订单号的关系
//	 */
//	private Map<String, TorderDetail> getOderDetailMap(Order order) {
//		Map<String, TorderDetail> dishIdToDetail = new HashMap<>();
//		for (TorderDetail detail : order.getRows()) {
//			dishIdToDetail.put(detail.getDishid(), detail);
//		}
//		return dishIdToDetail;
//	}
//
//	private void setPrintMes(TblEmployee emloyee, Map<String, TblPrintqueue> printALLMap, Order order,
//			Map<String, List<LinuxPrintEmpBean>> printMes, String menuNum, String menuName, String queueNo,
//			String checkNum) throws IOException {
//		if (!queueNo.trim().equals("0")) {
//			LinuxPrintEmpBean beanq2 = new LinuxPrintEmpBean(
//					CommonUtil.getFomartGBK(printALLMap.get(queueNo).getNames3()), order.getCurrenttableid(),
//					CommonUtil.getFomartGBK(emloyee.getShortname3()), String.valueOf("5"), checkNum,
//					CommonUtil.yearMonthFomart(), CommonUtil.dateTimeFomart(), menuNum,
//					CommonUtil.getFomartGBK(menuName));
//			if (!printMes.containsKey(queueNo)) {
//				List<LinuxPrintEmpBean> beans = new ArrayList<LinuxPrintEmpBean>();
//				beans.add(beanq2);
//				printMes.put(queueNo, beans);
//			} else {
//				List<LinuxPrintEmpBean> beans = printMes.get(queueNo);
//				beans.add(beanq2);
//				printMes.put(queueNo, beans);
//			}
//		}
//	}
//
//	@Autowired
//	private InorderCheckPeriodDao checkPeriodDao;
//
//	/**
//	 * 
//	 * @param outLet
//	 *            吉旺门店号
//	 * @param nextCheckNo
//	 * @param orders
//	 * @param stationParams
//	 *            吉旺工作台
//	 */
//	private TblCheck addCheck(int outLet, Date date, int inorderStation, String nextCheckNo, Order orders, int floor) {
//		// 订单号规则 如果
//		TblCheck check = new TblCheck(date, nextCheckNo, outLet, 0, "", 1);
//		check.setOpentime(new Date());
//		check.setOpenstation(Integer.valueOf(inorderStation));
//		check.setOpenstationref(Integer.valueOf(inorderStation));
//		check.setOpenperiod(checkPeriodDao.queryCurrentPeriod().getPeriodno());// 营业时间
//		check.setFloor(floor);
//		List<TorderDetail> rows = orders.getRows();
//		if (rows != null && !rows.isEmpty()) {
//			check.setTableno(orders.getCurrenttableid());
//
//			check.setOpenemp(Integer.valueOf(rows.get(0).getUserName()));// 服务员编号
//		}
//
//		check.setChecktot(0);// 付款总额
//		check.setItemtot(0);
//
//		check.setIspaid(0);
//		check.setVoidMes("0");
//		check.setIsmodified(1);
//		check.setLastmodifiedtime(new Date());
//		// 获取下单状态
//		String checkStatus = checkTableDao.addCheck(check);
//		if (checkStatus != null && !checkStatus.trim().isEmpty() && Integer.valueOf(checkStatus) > 0) {
//			return check;
//		}
//		return null;
//	}
//
//	/**
//	 * 
//	 * @param orders
//	 * @param checkInfo
//	 * @param inorderTable
//	 * @return 返回下单状态 返回当前菜单价格总和
//	 */
//	private Map<String, Object> addItemStatus(Order orders, TblCheck checkInfo) {
//		// 返回状态以及总金额
//		Map<String, Object> result = new HashMap<String, Object>();
//		// 加菜操作,获取所有菜品列表
//		if (orders.getRows() != null && !orders.getRows().isEmpty()) {
//			// 菜品总金额
//			double allItemtot = 0;
//			double alltRvItemtot = 0;
//			/** 营业时间 **/
//			int priodo = checkPeriodDao.queryCurrentPeriod().getPeriodno();
//			// 查询菜品个数
//			Map<String, String> queryItemParams = new HashMap<String, String>();
//			queryItemParams.put("check", checkInfo.getCheck());
//			queryItemParams.put("date", DateUtils.toString(checkInfo.getDate()));
//			int maxItemInx = itemDao.queryItemInx(queryItemParams) == null ? 0
//					: Integer.valueOf(itemDao.queryItemInx(queryItemParams));
//			// 查询菜品详情表
//			List<TblMenu> menus = menDao.queryMeun(getOderDetailMap(orders).keySet().toArray());
//			List<TblItem> items = new ArrayList<TblItem>();
//			for (int inx = 0; inx < menus.size(); inx++) {
//				maxItemInx++;
//				TblMenu tblMenu = menus.get(inx);
//				TblItem tblItem = new TblItem(checkInfo.getDate(), Integer.valueOf(checkInfo.getCheck()),
//						checkInfo.getOutlet(), maxItemInx, tblMenu.getItem());
//				tblItem.setClone(tblMenu);
//				tblItem.setOgnprice(tblMenu.getItemprice1());// 原始菜品价格
//				tblItem.setPrice(tblMenu.getItemprice1());// 实际菜品价格（不能确定数据来源）
//				tblItem.setItemtot(tblMenu.getItemprice1());
//				tblItem.setRvitemtot(tblMenu.getItemprice1());
//				tblItem.setUnitqty(1.0);// 不能确定数据来源
//				Double dishNum = Double.valueOf(orders.getRows().get(inx).getDishnum());
//				tblItem.setQty(dishNum);// 不能确定数据来源
//				allItemtot = allItemtot + (dishNum * tblMenu.getItemprice1());
//				alltRvItemtot = alltRvItemtot + (dishNum * tblMenu.getItemprice1());
//				tblItem.setOrdertime(new Date());
//				tblItem.setOrderstation(checkInfo.getOpenstation());
//				tblItem.setOrderemp(Integer.valueOf(orders.getRows().get(inx).getUserName()));
//
//				/** 现在不支持套餐 所以默认为0 **/
//				tblItem.setParentitemidx(0);
//				tblItem.setChildcount(0);
//				tblItem.setType(1);
//
//				tblItem.setIsprintoncheck("0");
//				tblItem.setPrintstatus(4);
//				tblItem.setPricelevel(1);
//				tblItem.setModimethod(-1);
//
//				tblItem.setLastmodifiedtime(new Date());
//				items.add(tblItem);
//			}
//			int restatus = itemDao.addbatchItem(items);
//			result.put("addStatus", restatus);
//			result.put("allItemtot", allItemtot);
//			result.put("alltRvItemtot", alltRvItemtot);
//		}
//		return result;
//
//	}
//
//	private List<String> getDishIds(List<TorderDetail> orderDetails) {
//		List<String> dishIds = new ArrayList<>();
//		for (TorderDetail orderDetail : orderDetails) {
//			dishIds.add(orderDetail.getDishid());
//		}
//		return dishIds;
//	}
//}
