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

import org.apache.commons.collections.MapUtils;
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
	private ItemDetailController iteminfo;
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
		try {
			res = parse("getOrderInfo", orderInfo, aUserId, orderId, printType);
			res = parseDSJson(res);
			List<SettlementInfo4Pos> settlementInfos = new ArrayList<>();
			settlementInfos = JSON.parseArray(res, SettlementInfo4Pos.class);
			print4posService.print(settlementInfos, printType);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", "", flag);
	}

	@RequestMapping(value = "/getClearMachineData/{aUserid}/{jsorder}/{posid}/", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getClearMachineData(@PathVariable("aUserid") String aUserId, @PathVariable("jsorder") String jsOrder,
			@PathVariable("posid") String posId) {
		// TODO
		String res = null;
		boolean flag = true;
		try {
			res = parse("getClearMachineData", storeInfo, aUserId, jsOrder, posId);
			res = parseDSJson(res);
			List<SettlementInfo4Pos> settlementInfos = new ArrayList<>();
			settlementInfos = JSON.parseArray(res, SettlementInfo4Pos.class);
			print4posService.printClearMachine(settlementInfos);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			log.error("", e);
		}

		return getResponseMsg("", "", flag);
	}

	@RequestMapping(value = "/getMemberSaleInfo/{aUserId}/{orderId}/", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getMemberSaleInfo(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId) {
		// TODO
		String res = null;
		boolean flag = true;
		try {
			res = parse("getMemberSaleInfo", orderInfo, aUserId, orderId);
			res = parseDSJson(res);
			List<SettlementInfo4Pos> settlementInfos = new ArrayList<>();
			settlementInfos = JSON.parseArray(res, SettlementInfo4Pos.class);
			print4posService.printMemberSaleInfo(settlementInfos);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			log.error("", e);
		}

		return getResponseMsg("", "", flag);
	}

	/**
	 * 会员接口
	 *
	 * @param aUserId
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/StoreCardToNewPos", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String StoreCardToNewPos(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId) {
		// TODO 会员接口，需要联调
		boolean flag = true;
		return getResponseMsg("", "", flag);
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
	@RequestMapping("/getItemSellDetail.json")
	@ResponseBody
	public String getItemSellDetail(String flag) {
		// TODO
		String res = null;
		boolean sucess = true;
		try {
			res = parse("getItemSellDetail", padInterface, flag);
			ResultInfo4Pos resultInfo4Pos = JSON.parseObject(res, ResultInfo4Pos.class);
			print4posService.printItemSellDetail(resultInfo4Pos);
		} catch (Exception e) {
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", "", sucess);
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
		try {
			res = parse("TipList", tipController, flag);
			ResultTip4Pos resultInfo4Pos = JSON.parseObject(res, ResultTip4Pos.class);
			print4posService.printTip(resultInfo4Pos);
		} catch (Exception e) {
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", "", sucess);
	}

	/**
	 * 发票单
	 *
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/printInvoice.json")
	@ResponseBody
	public String printInvoice(@RequestBody String param) {
		boolean sucess = true;
		if (StringUtils.isEmpty(param)) {
			sucess = false;
			return getResponseMsg("", "", sucess);
		}
		try {
			Map<String, Object> map = JSON.parseObject(param, Map.class);
			print4posService.printInvoice(map);
		} catch (Exception e) {
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", "", sucess);
	}

	/**
	 * 会员储值
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/StoreCardToNewPos.json")
	@ResponseBody
	public String StoreCardToNewPos(@RequestBody String param) {
		boolean sucess = true;
		if (StringUtils.isEmpty(param)) {
			sucess = false;
			return getResponseMsg("", "", sucess);
		}
		try {
			Map<String, Object> map = JSON.parseObject(param, Map.class);
			print4posService.printStoredCard(map);
		} catch (Exception e) {
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}
		return getResponseMsg("", "", sucess);
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
		if (StringUtils.isEmpty(params)) {
			sucess = false;
			return getResponseMsg("", "", sucess);
		}
		String itemList = null;
		String preferList = null;
		String dayReportList = null;
		String billCountList = null;
		String tipList = null;

		try {
			itemList = parse("getItemForList", itemDetailController, params, request);
			Map<String, Object> temp0 = new HashMap<>();
			temp0.putAll(params);
			temp0.put("shiftid", "-1");
			temp0.put("bankcardno", "-1");
			temp0.put("settlementWay", "-1");
			temp0.put("type", "-1");
			preferList = parse("findPreferential", prefertialinfo, temp0);
			dayReportList = parse("getReportList", dayIncomeBillController, params);
			billCountList = parse("BillCount", registerBillinfo, params);
			tipList = parse("TipListByTime", itemDetailController, String.valueOf(params.get("beginTime")),
					String.valueOf(params.get("endTime")));
			print4posService.printBusinessDetail(itemList,preferList,dayReportList,billCountList,tipList);
		} catch (Exception e) {
			sucess = false;
			e.printStackTrace();
			log.error("", e);
		}

		return getResponseMsg("", "", sucess);
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
	private String parse(String name, Object obj, Object... args) throws Exception {
		if (StringUtils.isEmpty(name) || obj == null) {
			return null;
		}
		Object res = null;
		Method method = null;
		Class<?>[] insts = null;
		if (args != null) {
			insts = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				insts[i] = args[i].getClass();
			}
		}
		method = obj.getClass().getMethod(name, insts);
		res = method.invoke(obj, args);
		if (!String.class.isAssignableFrom(res.getClass())) {
			res = JSON.toJSONString(res);
		}
		res = StringUtil.unicodeTOUtf8(String.valueOf(res));
		return res.toString();
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
