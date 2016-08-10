package com.candao.www.webroom.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.candao.print.entity.ResultInfo4Pos;
import com.candao.print.entity.ResultTip4Pos;
import com.candao.print.entity.SettlementInfo4Pos;
import com.candao.www.dataserver.controller.OrderInterfaceController;
import com.candao.www.dataserver.controller.StoreInterfaceController;
import com.candao.www.dataserver.util.StringUtil;
import com.candao.www.webroom.service.Print4POSService;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * POS打印业务
 */
@Controller
@RequestMapping("/print4POS")
public class Print4POSController {

	@Autowired
	private OrderInterfaceController orderInfo;
	@Autowired
	private StoreInterfaceController storeInfo;
	@Autowired
	private Print4POSService print4posService;
	@Autowired
	private PadInterfaceController padInterface;
	@Autowired
	private TipController tipController;
	@Autowired
	private ItemDetailController itemDetailController;
	@Autowired
	private DayIncomeBillController dayIncomeBillController;

	private final Log log = LogFactory.getLog(getClass());

	private final static String RESULT = "result";
	@Autowired
	private PreferentialAnalysisChartsController prefertialinfo;
	@Autowired
	private RegisterBillController registerBillinfo;

	@RequestMapping(value = "/getOrderInfo/{aUserId}/{orderId}/{printType}", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getOrderInfo(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId,
			@PathVariable("printType") String printType) {
		String res = null;
		boolean flag = true;
		String msg = "";
		try {
			res = parse("getOrderInfo", orderInfo, new Class[] { String.class, String.class, String.class }, aUserId,
					orderId, printType);
			res = parseDSJson(res);
			List<SettlementInfo4Pos> settlementInfos = new ArrayList<>();
			settlementInfos = JSON.parseArray(res, SettlementInfo4Pos.class);
			print4posService.print(settlementInfos, printType);
		} catch (Exception e) {
			msg = e.getMessage();
			flag = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", msg, flag);
	}

	@RequestMapping(value = "/getClearMachineData/{aUserid}/{jsorder}/{posid}/", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getClearMachineData(@PathVariable("aUserid") String aUserId, @PathVariable("jsorder") String jsOrder,
			@PathVariable("posid") String posId) {
		// TODO
		String res = null;
		boolean flag = true;
		String msg = "";
		try {
			res = parse("getClearMachineData", storeInfo, new Class[] { String.class, String.class, String.class },
					aUserId, jsOrder, posId);
			res = parseDSJson(res);
			List<SettlementInfo4Pos> settlementInfos = new ArrayList<>();
			settlementInfos = JSON.parseArray(res, SettlementInfo4Pos.class);
			print4posService.printClearMachine(settlementInfos);
		} catch (Exception e) {
			msg = e.getMessage();
			flag = false;
			e.printStackTrace();
			log.error("", e);
		}

		return getResponseMsg("", msg, flag);
	}

	@RequestMapping(value = "/getMemberSaleInfo/{aUserId}/{orderId}/", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getMemberSaleInfo(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId) {
		// TODO
		String res = null;
		boolean flag = true;
		String msg = "";
		try {
			res = parse("getMemberSaleInfo", orderInfo, new Class[] { String.class, String.class }, aUserId, orderId);
			res = parseDSJson(res);
			List<SettlementInfo4Pos> settlementInfos = new ArrayList<>();
			settlementInfos = JSON.parseArray(res, SettlementInfo4Pos.class);
			print4posService.printMemberSaleInfo(settlementInfos);
		} catch (Exception e) {
			msg = e.getMessage();
			flag = false;
			e.printStackTrace();
			log.error("", e);
		}

		return getResponseMsg("", msg, flag);
	}

	private String parseDSJson(String src) {
		if (StringUtil.isEmpty(src)) {
			return null;
		}
		JSONObject obj = JSON.parseObject(src);
		if (!obj.containsKey(RESULT)) {
			return null;
		}
		return obj.get(RESULT).toString();
	}

	/**
	 * 获取品项销售明细的打印数据
	 *
	 * @return
	 */
	@RequestMapping("/getItemSellDetail")
	@ResponseBody
	public String getItemSellDetail(String flag) {
		// TODO
		String res = null;
		boolean sucess = true;
		String msg = "";
		try {
			res = parse("getItemSellDetail", padInterface, new Class[] { String.class }, flag);
			ResultInfo4Pos resultInfo4Pos = JSON.parseObject(res, ResultInfo4Pos.class);
			print4posService.printItemSellDetail(resultInfo4Pos);
		} catch (Exception e) {
			msg = e.getMessage();
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", msg, sucess);
	}

	/**
	 * 查询门店服务员小费list
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/tipList")
	@ResponseBody
	public String TipList(String flag) {
		// TODO
		String res = null;
		boolean sucess = true;
		String msg = "";
		try {
			res = parse("TipList", tipController, new Class[] { String.class }, flag);
			ResultTip4Pos resultInfo4Pos = JSON.parseObject(res, ResultTip4Pos.class);
			print4posService.printTip(resultInfo4Pos);
		} catch (Exception e) {
			msg = e.getMessage();
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", msg, sucess);
	}

	/**
	 * 发票单
	 *
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/printInvoice")
	@ResponseBody
	public String printInvoice(@RequestBody String param) {
		boolean sucess = true;
		String msg = "";
		if (StringUtils.isEmpty(param)) {
			sucess = false;
			return getResponseMsg("", "", sucess);
		}
		try {
			Map<String, Object> map = JSON.parseObject(param, Map.class);
			print4posService.printInvoice(map);
		} catch (Exception e) {
			msg = e.getMessage();
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", msg, sucess);
	}

	/**
	 * 会员储值
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/StoreCardToNewPos")
	@ResponseBody
	public String StoreCardToNewPos(@RequestBody String param) {
		boolean sucess = true;
		String msg = "";
		if (StringUtils.isEmpty(param)) {
			sucess = false;
			return getResponseMsg("", "", sucess);
		}
		try {
			Map<String, Object> map = JSON.parseObject(param, Map.class);
			print4posService.printStoredCard(map);
		} catch (Exception e) {
			msg = e.getMessage();
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", msg, sucess);
	}

	/**
	 * 门店消费明细表
	 *
	 * @param flag
	 * @return
	 */
	@RequestMapping("/printBusinessDetail")
	@ResponseBody
	public String printBusinessDetail(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		boolean sucess = true;
		String msg = "";
		if (StringUtils.isEmpty(params)) {
			sucess = false;
			return getResponseMsg("", "", sucess);
		}
		String itemList = null;
		String preferList = null;
		String dayReportList = null;
		String billCountList = null;
		String tipList = null;
		Map<String, Object> temp = new HashMap<>();
		temp.putAll(params);
		try {
			// 获取营业明细（品类、金额）
			itemList = parse("getItemForList", itemDetailController,
					new Class<?>[] { Map.class, HttpServletRequest.class }, temp, request);
			// 获取营业明细(团购券)
			Map<String, Object> temp0 = new HashMap<>();
			temp0.putAll(params);
			temp0.put("shiftid", "-1");
			temp0.put("bankcardno", "-1");
			temp0.put("settlementWay", "-1");
			temp0.put("type", "-1");
			preferList = parse("findPreferential", prefertialinfo, new Class<?>[] { Map.class }, temp0);
			// 获取营业明细（其它）
			dayReportList = parse("getReportList", dayIncomeBillController, new Class<?>[] { Map.class }, temp);
			// 获取营业明细（获取挂账单位）
			temp0.clear();
			temp0.putAll(params);
			temp0.put("billName", "0");
			temp0.put("clearStatus", "0");
			billCountList = parse("BillCount", registerBillinfo, new Class<?>[] { Map.class }, temp0);
			// 获取小费总额
			tipList = parse("TipListByTime", tipController, new Class[] { String.class, String.class },
					String.valueOf(temp.get("beginTime")), String.valueOf(temp.get("endTime")));
			print4posService.printBusinessDetail(params, itemList, preferList, dayReportList, billCountList, tipList);
		} catch (Exception e) {
			msg = e.getMessage();
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}

		return getResponseMsg("", msg, sucess);
	}

	/**
	 * dataserver接口转换
	 *
	 * @param name
	 * @param obj
	 * @param args
	 * @return
	 * @throws @throws
	 *             Exception
	 */
	private String parse(String name, Object obj, Class<?>[] classname, Object... args) throws Exception {
		if (StringUtils.isEmpty(name) || obj == null) {
			return null;
		}
		Object res = null;
		Method method = null;
		Class<?>[] insts = null;
		if (ArrayUtils.isEmpty(classname)) {
			if (args != null) {
				insts = new Class<?>[args.length];
				for (int i = 0; i < args.length; i++) {
					insts[i] = args[i] == null ? null : args[i].getClass();
				}
			}
		} else {
			insts = classname;
		}
		method = obj.getClass().getMethod(name, insts);
		res = method.invoke(obj, args);
		res = typeResolve(res);
		res = StringUtil.unicodeTOUtf8(String.valueOf(res));
		return res.toString();
	}

	private Object typeResolve(Object res) {
		if (!String.class.isAssignableFrom(res.getClass())) {
			if (res instanceof ModelAndView) {
				res = JSON.toJSONString(((ModelAndView) res).getModel());
			} else {
				res = JSON.toJSONString(res);
			}
		}
		return res;
	}

	private String getResponseMsg(Object data, String msg, boolean sucess) {
		// Assert.notEmpty(new Object[] { data, msg, sucess });
		Map<String, Object> res = new HashMap<>();
		res.put("result", sucess ? 0 : 1);
		res.put("data", data);
		res.put("msg", msg);
		return JSON.toJSONString(res);
	}
}
