package com.candao.www.webroom.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.dataserver.controller.OrderInterfaceController;
import com.candao.www.dataserver.util.StringUtil;

/**
 * POS打印业务
 */
@Controller
@RequestMapping("/print4POS")
public class Print4POSController {

	@Autowired
	private OrderInterfaceController orderInfo;

	private final Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = "/getOrderInfo/{aUserId}/{orderId}/{printType}", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getOrderInfo(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId,
			@PathVariable("printType") String printType) {
		String res = null;
		try {
			res = parse("getOrderInfo", orderInfo, aUserId, orderId, printType);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("", e);
		}
		
		return JacksonJsonMapper.objectToJson(res);
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
		res = StringUtil.unicodeTOUtf8(String.valueOf(res));
		return res.toString();
	}
}
