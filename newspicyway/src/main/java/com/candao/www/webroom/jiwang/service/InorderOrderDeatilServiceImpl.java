package com.candao.www.webroom.jiwang.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.inorder.dao.InorderCheckPeriodDao;
import com.candao.inorder.dao.InorderCheckTableDao;
import com.candao.inorder.dao.InorderDayInfoDao;
import com.candao.inorder.dao.InorderEmployeeDao;
import com.candao.inorder.dao.InorderItemDao;
import com.candao.inorder.dao.InorderMenuDao;
import com.candao.inorder.dao.InorderPrintQueueDao;
import com.candao.inorder.dao.InorderStationDao;
import com.candao.inorder.data.LinuxPrintEmpBean;
import com.candao.inorder.pojo.TblCheck;
import com.candao.inorder.pojo.TblDayinfo;
import com.candao.inorder.pojo.TblEmployee;
import com.candao.inorder.pojo.TblItem;
import com.candao.inorder.pojo.TblMenu;
import com.candao.inorder.pojo.TblPrintqueue;
import com.candao.inorder.pojo.TblStation;
import com.candao.inorder.utils.CommonUtil;
import com.candao.inorder.utils.CustomerContextHolder;
import com.candao.inorder.utils.DataSourceInstances;
import com.candao.inorder.utils.LiunxSSHScpclientUtil;
import com.candao.inorder.utils.print.PrintQueueUtil;
import com.candao.www.data.dao.TbTableDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.Torder;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.webroom.model.Order;
import com.candao.www.webroom.service.impl.OrderDetailServiceImpl;

/**
 * 
 * @author Candao
 *
 */

@Service
public class InorderOrderDeatilServiceImpl extends OrderDetailServiceImpl {
	private Logger logger = LoggerFactory.getLogger(InorderOrderDeatilServiceImpl.class);
	/** 订单信息 **/
	@Autowired
	private InorderCheckTableDao checkTableDao;

	/** 订单详细信息 **/
	@Autowired
	private InorderItemDao itemDao;

	/** 菜单信息 **/
	@Autowired
	private InorderMenuDao menDao;
	/** 工作信息 **/
	@Autowired
	private InorderDayInfoDao dayInfoDao;

	/** 获取工作台信息 **/
	@Autowired
	private InorderStationDao stationDao;

	@Autowired
	private TbTableDao tbTableDao;
	@Autowired
	private TorderMapper torderMapper ;

	@Autowired
	private TdishDao tdishDao;

	@Override
	public Map<String, Object> setOrderDetailList(Order orders) {
		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
		// 获取菜品编号
		String outLet = PropertiesUtils.getValue("outlet");
		String conStation = PropertiesUtils.getValue("station");
		// 获取开业时间
		// 获取开业时间
		TblDayinfo dataInfo = dayInfoDao.getActionDayIndo();
		// checkDate
		String date = dataInfo == null ? DateUtils.toString(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				: dataInfo.getDate();
		// 根据坐台号查区域
		Map<String, Object> queryArea = new HashMap<String, Object>();
		queryArea.put("tableNo", orders.getCurrenttableid());
		List<Map<String, Object>> resultMap = tbTableDao.find(queryArea);
		Torder  torder =torderMapper.get(orders.getOrderid());
		orders.setCustnum(torder.getCustnum());
		// 获取对应吉旺餐台号
		int areaNo = (int) resultMap.get(0).get("areaNo");
		// 返回添加状态
		Map<String, Object> result = null;
		// 先判断当前餐台是否被占用
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableNo", orders.getCurrenttableid());
		params.put("floor", areaNo);
		params.put("date", date);
		TblCheck inorderTable = checkTableDao.tableInfoMes(params);
		// 获取订单号
		TblStation station = stationDao.tblStation(conStation);
		// 数据库生成订单规则
		String startCheckNO = String.valueOf(station.getStartcheckno());
		// 当前订单号
		String currentCheckNo = String.valueOf(station.getCurrentcheckno());
		// 如果长度一样长说明是数据库生成的订单号
		String nextCheckNo = startCheckNO.length() == currentCheckNo.length()
				? currentCheckNo + currentCheckNo.substring(currentCheckNo.length() - 1, currentCheckNo.length())
				: currentCheckNo;
		// 获取吉旺的Itiem(解决相同ID的问题)
		List<String> ids = getDishIds(orders.getRows());

		//
		Map<String, String> canDaoToInorderMap = new HashMap<>();
		List<String> dishForItems = new ArrayList<>();
		// 多规格集合dish dishNO关系(餐道系统对于吉旺系统)
		Map<String, String> candaoDishMap = new HashMap<>();
		// 所有餐道对于吉旺关系餐刀dishID-itemNO
		for (String dishId : ids) {
			Tdish tdish = tdishDao.get(dishId);

			if (tdish != null) {
				// 判断是否为多规格菜品
				if (tdish.getUnit().contains("/")) {
					// 多规格菜品另算
					candaoDishMap.put(tdish.getDishid(), tdish.getDishno());
				}
				dishForItems.add(tdish.getDishno());
				canDaoToInorderMap.put(tdish.getDishid(), tdish.getDishno());
			}
		}
		// 吉旺开台操作 点菜操作
		if (inorderTable == null) {
			inorderTable = addCheck(Integer.valueOf(outLet), DateUtils.parse(date), station.getStation(), nextCheckNo,
					orders, areaNo);
			if (inorderTable != null) {
				result = addItemStatus(orders, inorderTable, dishForItems, candaoDishMap, canDaoToInorderMap);
				station.setCurrentcheckno(Integer.valueOf(nextCheckNo) + 1);
				stationDao.updateStation(station);
			}
			// 成功过后更新订单表
		} else {
			result = addItemStatus(orders, inorderTable, dishForItems, candaoDishMap, canDaoToInorderMap);
		}

		// 吉旺更新餐台操作
		if (result != null) {
			inorderTable.setChecktot(inorderTable.getChecktot()+(double) result.get("allItemtot"));
			inorderTable.setItemtot(inorderTable.getItemtot()+(double) result.get("allItemtot"));
			checkTableDao.updateCheckForTot(inorderTable);

			Map<String, String> printMap;
			try {
				// 吉旺打印
				printMap = printMenu(orders, inorderTable.getCheck(), dishForItems, canDaoToInorderMap, candaoDishMap);

				PrintQueueUtil.getInstance().addQueue(printMap);
			} catch (IOException e) {
				logger.error("吉旺打印获取失败", e.fillInStackTrace());
			}
		}

		return super.setOrderDetailList(orders);
	}

	@Autowired
	private InorderEmployeeDao employeeDao;
	@Autowired
	private InorderPrintQueueDao printQueueDao;

	/**
	 * 
	 * @param order
	 * @param checkNum
	 * @param dishForItems
	 * @param dishForItems
	 * @param candaoDishMap
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	private Map<String, String> printMenu(Order order, String checkNum, List<String> dishForItems,
			Map<String, String> canDaoToInorderMap, Map<String, String> candaoDishMap) throws IOException {

		// 相同类型打印为一组
		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_INORDER);
		Map<String, TblPrintqueue> printALLMap = printQueueDao.queryPritQueueALL();
		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);

		Map<String, TorderDetail> orderDetailMap = getOderDetailMap(order, canDaoToInorderMap);
		// 查询菜品详情表（吉旺）
		List<TblMenu> menus = menDao.queryMeun(dishForItems.toArray(new String[dishForItems.size()]));
		HashMap<String, TblMenu> mapTblMenu = new HashMap<>();
		for (TblMenu menu : menus) {
			mapTblMenu.put(menu.getItem(), menu);
		}
		// 对打印信息分区 分域
		Map<String, List<LinuxPrintEmpBean>> printMes = new HashMap<String, List<LinuxPrintEmpBean>>();
		// 獲取訂單數據
		List<TorderDetail> details = order.getRows();
		for (TorderDetail detail : details) {
			// 获取5个区的打印
			TblMenu menu = mapTblMenu.get(canDaoToInorderMap.get(detail.getDishid()));
			// 吉旺用户名称
			TblEmployee emloyee = employeeDao.queryForEmpNo(orderDetailMap.get(menu.getItem()).getUserName());
			/** 点菜的名称 **/
			setPrintMes(emloyee, printALLMap,  printMes, menu, menu.getPrintq1(), checkNum,
					candaoDishMap, detail,order );
			setPrintMes(emloyee, printALLMap,  printMes, menu, menu.getPrintq2(), checkNum,
					candaoDishMap, detail,order);
			setPrintMes(emloyee, printALLMap, printMes, menu, menu.getPrintq3(), checkNum,
					candaoDishMap, detail,order);
			setPrintMes(emloyee, printALLMap, printMes, menu, menu.getPrintq4(), checkNum,
					candaoDishMap, detail,order);
			setPrintMes(emloyee, printALLMap, printMes, menu, menu.getPrintq5(), checkNum,
					candaoDishMap, detail,order);
		}
		// 封装成数据流
		Map<String, String> printMesBytes = new HashMap<String, String>();
		// 获取挂载磁盘路径
		String diskPath = PropertiesUtils.getValue("mountDisk");
		for (String key : printMes.keySet()) {
			StringBuffer buffer = new StringBuffer();
			List<LinuxPrintEmpBean> linuxPrintEmpBeans = printMes.get(key);
			int beanSize = linuxPrintEmpBeans.size();
			if (key.equals("10")) {
				LinuxPrintEmpBean empBean;
				if (beanSize > 0) {
					empBean = linuxPrintEmpBeans.get(0);
					String tempPrintMes = String.format(LiunxSSHScpclientUtil.getInstance().getEmpPrintbuffer(),
							empBean.getPrintQname(), empBean.getTableNum(), empBean.getEmpName(), empBean.getCover(),
							empBean.getCheckNum(), empBean.getDate(), empBean.getTime());
					buffer.append(tempPrintMes);
				}
				for (int i = 0; i < beanSize; i++) {
					empBean = linuxPrintEmpBeans.get(i);
					buffer.append("\r\n");
					buffer.append("^,Xred_color|**Change to red Color** ");
					buffer.append("\r\n");
					buffer.append("L2,F3,AL		|" + empBean.getMenuNum() + "  " + empBean.getMenuName());
					if ((i + 1) < beanSize) {
						buffer.append("\r\n");
						buffer.append("^,Xblack_color|**Change to black Color**");
					}else{
						buffer.append("\r\n");
						buffer.append("L2,F1,AL		|");
						buffer.append("\r\n");
						buffer.append("|");
						buffer.append("\r\n");
						buffer.append("^,Xfullcut 		|**Cut Paper**");
					}
				}
			} else {
				for (LinuxPrintEmpBean empBean : linuxPrintEmpBeans) {
					String tempPrintMes = String.format(LiunxSSHScpclientUtil.getInstance().getEmpPrintbuffer(),
							empBean.getPrintQname(), empBean.getTableNum(), empBean.getEmpName(), empBean.getCover(),
							empBean.getCheckNum(), empBean.getDate(), empBean.getTime());
					buffer.append(tempPrintMes);
					if (empBean.getDoMethod() == null) {
						buffer.append("^,Xblack_color|**Change to black Color**");
						buffer.append("\r\n");
						buffer.append("F4,AL		|" + empBean.getMenuNum() + "  " + empBean.getMenuName());
						buffer.append("\r\n");
						buffer.append("^,Xfullcut 		|**Cut Paper**");
					} else {
						buffer.append("^,Xblack_color|**Change to black Color**");
						buffer.append("\r\n");
						buffer.append("F4,AL		|" + empBean.getMenuNum() + "  " + empBean.getMenuName());
						buffer.append("\r\n");
						buffer.append("F4,AL		|  ");
						buffer.append(empBean.getDoMethod());
						buffer.append("\r\n");
						buffer.append("^,Xfullcut 		|**Cut Paper**");
					}
				}
			}

			printMesBytes.put(diskPath + CommonUtil.getPrintFileName(key, PropertiesUtils.getValue("station")),
					buffer.toString());
		}
		return printMesBytes;
	}

	/**
	 * 
	 * @param order
	 * @param canDaoToInorderMap
	 * @return 返回订单与订单号的关系
	 */
	private Map<String, TorderDetail> getOderDetailMap(Order order, Map<String, String> canDaoToInorderMap) {
		Map<String, TorderDetail> dishIdToDetail = new HashMap<>();
		for (TorderDetail detail : order.getRows()) {
			dishIdToDetail.put(canDaoToInorderMap.get(detail.getDishid()), detail);
		}
		return dishIdToDetail;
	}

	private void setPrintMes(TblEmployee emloyee, Map<String, TblPrintqueue> printALLMap, 
			Map<String, List<LinuxPrintEmpBean>> printMes, TblMenu menu, String queueNo, String checkNum,
			Map<String, String> candaoDishMap, TorderDetail detail, Order order) throws IOException {
		String tableid= order.getCurrenttableid();
		String globalsperequire=  order.getGlobalsperequire();
		String dishNum = detail.getDishnum();
		String dishUnit = detail.getDishunit();
		String cover=String.valueOf(order.getCustnum());
		/** 当前菜品个数 **/
		if (!queueNo.trim().equals("0")) {
			LinuxPrintEmpBean empBean = new LinuxPrintEmpBean(
					CommonUtil.getFomartGBK(printALLMap.get(queueNo).getNames3()), tableid,
					CommonUtil.getFomartGBK(emloyee.getShortname3()), cover, checkNum,
					CommonUtil.yearMonthFomart(), CommonUtil.dateTimeFomart(), dishNum,
					CommonUtil.getFomartGBK(menu.getName3()));

			// 拼接打印做法
			// 如果为上菜单不打印做法
			if (!queueNo.trim().equals("10")) {

				StringBuffer sb = new StringBuffer();
				// 判断是是否是有多规格菜品
				if (candaoDishMap.values().contains(menu.getItem())) {
					// empBean.setDoMethod("*" + dishUnit);
					sb.append(" ");
					sb.append(dishUnit);
				}
				if (detail.getTaste() != null && !detail.getTaste().isEmpty()) {
					sb.append(" ");
					sb.append(detail.getTaste());
				}
				if (globalsperequire != null && !globalsperequire.isEmpty()) {
					sb.append(" ");
					sb.append(globalsperequire.replaceAll(";", " "));
				}

				if (detail.getSperequire() != null && !detail.getSperequire().isEmpty()) {
					sb.append(" ");
					sb.append(detail.getSperequire().replaceAll(";", " "));
				}
				if (!sb.toString().isEmpty()) {
					sb.insert(0, "*");
					empBean.setDoMethod(sb.toString());
				}
			}

			if (!printMes.containsKey(queueNo)) {
				List<LinuxPrintEmpBean> beans = new ArrayList<LinuxPrintEmpBean>();
				beans.add(empBean);
				printMes.put(queueNo, beans);
			} else {
				List<LinuxPrintEmpBean> beans = printMes.get(queueNo);
				beans.add(empBean);
				printMes.put(queueNo, beans);
			}
		}
	}

	@Autowired
	private InorderCheckPeriodDao checkPeriodDao;

	/**
	 * 
	 * @param outLet
	 *            吉旺门店号
	 * @param nextCheckNo
	 * @param orders
	 * @param stationParams
	 *            吉旺工作台
	 */
	private TblCheck addCheck(int outLet, Date date, int inorderStation, String nextCheckNo, Order orders, int floor) {
		// 订单号规则 如果
		TblCheck check = new TblCheck(date, nextCheckNo, outLet, 0, "", 1);
		check.setOpentime(new Date());
		check.setOpenstation(Integer.valueOf(inorderStation)); 
		check.setOpenstationref(Integer.valueOf(inorderStation));
		check.setOpenperiod(checkPeriodDao.queryCurrentPeriod().getPeriodno());// 营业时间
		check.setFloor(floor);
		List<TorderDetail> rows = orders.getRows();
		if (rows != null && !rows.isEmpty()) {
			check.setTableno(orders.getCurrenttableid());

			check.setOpenemp(Integer.valueOf(rows.get(0).getUserName()));// 服务员编号
		}

		check.setChecktot(0);// 付款总额
		check.setItemtot(0);

		check.setCover(orders.getCustnum());
		check.setIspaid(0);
		check.setVoidMes("0");
		check.setIsmodified(1);
		check.setLastmodifiedtime(new Date());
		// 获取下单状态
		String checkStatus = checkTableDao.addCheck(check);
		if (checkStatus != null && !checkStatus.trim().isEmpty() && Integer.valueOf(checkStatus) > 0) {
			return check;
		}
		return null;
	}

	/**
	 * 
	 * @param orders
	 * @param checkInfo
	 * @param candaoDishMap
	 * @param canDaoToInorderMap
	 * @param inorderTable
	 * @return 返回下单状态 返回当前菜单价格总和
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> addItemStatus(Order orders, TblCheck checkInfo, List<String> inorderItems,
			Map<String, String> candaoDishMap, Map<String, String> canDaoToInorderMap) {
		// 返回状态以及总金额
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			// 加菜操作,获取所有菜品列表
			if (orders.getRows() != null && !orders.getRows().isEmpty()) {
				// 菜品总金额
				double allItemtot = 0;
				double alltRvItemtot = 0;
				/** 营业时间 **/
				int priodo = checkPeriodDao.queryCurrentPeriod().getPeriodno();
				// 保存数据
				List<TblItem> items = new ArrayList<>();
				// 单一菜品
				Map<String, Object> singleMap = createCheckItem(checkInfo,
						inorderItems.toArray(new String[inorderItems.size()]), orders, candaoDishMap,
						canDaoToInorderMap);
				items.addAll((Collection<? extends TblItem>) singleMap.get("items"));
				allItemtot += ((double) singleMap.get("allItemtot"));
				alltRvItemtot += ((double) singleMap.get("alltRvItemtot"));
				int restatus = itemDao.addbatchItem(items);
				result.put("addStatus", restatus);
				result.put("allItemtot", allItemtot);
				result.put("alltRvItemtot", alltRvItemtot);
			}
		} catch (Exception e) {
			logger.error("----addItemStatus-->", e.getStackTrace());
		}
		return result;

	}

	/**
	 * 
	 * @param checkInfo
	 *            订单信息
	 * @param inorderItems
	 *            吉旺item
	 * @param orders
	 *            餐到订单信息
	 * @param canDaoToInorderMap
	 * @param items
	 *            返回数据
	 * @param unitFlg
	 *            true 多规格 false 单品
	 */
	private Map<String, Object> createCheckItem(TblCheck checkInfo, String ids[], Order orders,
			Map<String, String> candaoDishMap, Map<String, String> canDaoToInorderMap) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			double allItemtot = 0;
			double alltRvItemtot = 0;
			List<TblItem> items = new ArrayList<TblItem>();

			Map<String, String> queryItemParams = new HashMap<String, String>();
			queryItemParams.put("check", checkInfo.getCheck());
			queryItemParams.put("date", DateUtils.toString(checkInfo.getDate()));
			int maxItemInx = itemDao.queryItemInx(queryItemParams) == null ? 0
					: Integer.valueOf(itemDao.queryItemInx(queryItemParams));

			// 吉旺menu对应餐道数关系
			Map<String, TblMenu> mapTblMenu = new HashMap<>();
			List<TblMenu> menus = menDao.queryMeun(ids);
			for (TblMenu menu : menus) {
				mapTblMenu.put(menu.getItem(), menu);
			}
			// 添加Item
			List<TorderDetail> rowList = orders.getRows();
			for (int inx = 0; inx < rowList.size(); inx++) {
				maxItemInx++;
				TorderDetail detail = rowList.get(inx);
				TblMenu tblMenu = mapTblMenu.get(canDaoToInorderMap.get(detail.getDishid()));

				// 取值如果是多规格取餐道数据
				String candaoDishId = candaoDishMap.get(detail.getDishid());

				// String
				// menuItemSuffix=candaoDishId==null?detail.getTaste():detail.getDishunit();
				// StringBuffer sb=new StringBuffer();
				// if(menuItemSuffix==null||menuItemSuffix.isEmpty()){
				// sb.append("");
				// }else{
				// sb.append("<");
				// sb.append(menuItemSuffix);
				// sb.append(">");
				// }

				TblItem tblItem = new TblItem(checkInfo.getDate(), Integer.valueOf(checkInfo.getCheck()),
						checkInfo.getOutlet(), maxItemInx, tblMenu.getItem());
				tblItem.setClone(tblMenu, "");
				if (candaoDishId == null) {
					tblItem.setOgnprice(tblMenu.getItemprice1());// 原始菜品价格
					tblItem.setPrice(tblMenu.getItemprice1());// 实际菜品价格（不能确定数据来源）
					tblItem.setItemtot(tblMenu.getItemprice1());
					tblItem.setRvitemtot(tblMenu.getItemprice1());
				} else {
					BigDecimal orderprice = detail.getOrderprice();
					tblItem.setOgnprice(orderprice.doubleValue());// 原始菜品价格
					tblItem.setPrice(orderprice.doubleValue());// 实际菜品价格（不能确定数据来源）
					tblItem.setItemtot(orderprice.doubleValue());
					tblItem.setRvitemtot(orderprice.doubleValue());
				}

				tblItem.setUnitqty(1.0);// 不能确定数据来源
				Double dishNum = Double.valueOf(detail.getDishnum());
				tblItem.setQty(dishNum);// 不能确定数据来源
				allItemtot = allItemtot + (dishNum * tblMenu.getItemprice1());
				alltRvItemtot = alltRvItemtot + (dishNum * tblMenu.getItemprice1());
				tblItem.setOrdertime(new Date());
				tblItem.setOrderstation(checkInfo.getOpenstation());
				tblItem.setOrderemp(Integer.valueOf(detail.getUserName()));

				/** 现在不支持套餐 所以默认为0 **/
				tblItem.setParentitemidx(0);
				tblItem.setChildcount(0);
				tblItem.setType(1);

				tblItem.setIsprintoncheck("1");
				tblItem.setPrintstatus(4);
				tblItem.setPricelevel(1);
				tblItem.setModimethod(-1);

				tblItem.setLastmodifiedtime(new Date());
				items.add(tblItem);
			}

			resultMap.put("allItemtot", allItemtot);
			resultMap.put("alltRvItemtot", alltRvItemtot);
			resultMap.put("items", items);
		} catch (Exception e) {
			logger.error("方法：createCheckItem---》" + e.getStackTrace());
		}
		return resultMap;
	}

	private List<String> getDishIds(List<TorderDetail> orderDetails) {
		List<String> dishIds = new ArrayList<>();
		for (TorderDetail orderDetail : orderDetails) {
			dishIds.add(orderDetail.getDishid());
		}
		return dishIds;
	}
}
