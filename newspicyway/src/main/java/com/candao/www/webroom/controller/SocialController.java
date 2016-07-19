package com.candao.www.webroom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.www.data.model.TbGift;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.SocialService;

@Controller
@RequestMapping(value = "/social")
public class SocialController {

	@Autowired
	private SocialService socialService;

	private Logger logger = LoggerFactory.getLogger(SocialController.class);
	
	/**
	 * 保存礼物
	 * 
	 * @author weizhifang
	 * @since 2015-11-11
	 * @param gifts
	 * @return
	 */
	@RequestMapping(value = "/saveGift")
	public @ResponseBody JSONObject saveGift(HttpServletRequest request) {
		String gifts = request.getParameter("data");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			socialService.saveGift(gifts);
			map = ReturnMap.getReturnMap(1, "001", "保存成功");
		} catch (Exception e) {
			map = ReturnMap.getReturnMap(0, "002", "保存礼物信息失败");
			e.printStackTrace();
		}
		return JSONObject.fromObject(map);
	}

	/**
	 * 查询桌子信息
	 * 
	 * @author weizhifang
	 * @since 2015-11-12
	 * @return
	 */
	@RequestMapping(value = "/queryTableInfo", method = RequestMethod.POST)
	public @ResponseBody JSONObject queryTableInfo(@RequestBody String content) {
		String orderid = JSONObject.fromObject(content).get("orderId").toString();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			List<Map<String, Object>> tableInfo = socialService.queryTableInfo(orderid);
			JSONArray data = JSONArray.fromObject(tableInfo);
			map = ReturnMap.getSuccessMap("查询桌台信息成功", data);
		} catch (Exception e) {
			map = ReturnMap.getFailureMap("查询桌台信息失败", null);
			e.printStackTrace();
		}
		return JSONObject.fromObject(map);
	}

	/**
	 * 查询互动礼物列表
	 * 
	 * @author weizhifang
	 * @since 2015-11-13
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getGiftList", method = RequestMethod.GET)
	public @ResponseBody JSONObject getGiftList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<TbGift> giftList = socialService.getGiftList();
			JSONArray data = JSONArray.fromObject(giftList);
			logger.info("查询礼物列表成功");
			map = ReturnMap.getSuccessMap("查询礼物列表成功", data);
		} catch (Exception e) {
			logger.error("查询礼物列表失败");
			map = ReturnMap.getFailureMap("查询礼物列表失败", null);
			e.printStackTrace();
		}
		return JSONObject.fromObject(map);
	}

}
