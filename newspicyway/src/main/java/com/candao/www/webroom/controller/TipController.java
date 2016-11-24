/**
 *
 */
package com.candao.www.webroom.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.OtherCouponService;


//*

/**
 * 小费
 *
 * @author
 * @serialData
 */
@Controller
@RequestMapping("/tip")
//@RequestMapping("/printer2")
public class TipController {
    @Autowired
    private com.candao.www.webroom.service.impl.TipService tipService;
    @Autowired
    private ItemDetailService itemDetailService;
    private LoggerHelper logger = LoggerFactory.getLogger(TipController.class);

    /**
     * 小费设置
     *
     * @param params
     * @return
     * @author lizongren
     */
    @RequestMapping("/tipSet")
    @ResponseBody
    public ModelAndView TipSet(@RequestBody String json) {
        logger.debug("start method saveAreaManager");
        ModelAndView mav = new ModelAndView();
        try {
            Map<String, Object> map = JacksonJsonMapper.jsonToObject(json, Map.class);
            String branchid = PropertiesUtils.getValue("current_branch_id");
            map.put("branchid", branchid);
//			解决并台时订单号重复的问题
            tipService.TipDelete(map);
            tipService.TipSet(map);
            mav.addObject("code", "0");
            mav.addObject("msg", "操作成功!");
        } catch (Exception e) {
            mav.addObject("code", "1");
            mav.addObject("msg", "操作失败!");
            e.printStackTrace();
        }
        return mav;
    }

    /**
     * 修改小费金额
     *
     * @param params
     * @return
     * @author lizongren
     */
    @RequestMapping("/tipUpdate")
    @ResponseBody
    public ModelAndView TipUpdate(@RequestBody String json) {
        ModelAndView mav = new ModelAndView();
        try {
            Map<String, Object> map = JacksonJsonMapper.jsonToObject(json, Map.class);
            String branchid = PropertiesUtils.getValue("current_branch_id");
            map.put("branchid", branchid);
            String receivables = (String) map.get("receivables");
            if ((Integer.valueOf(receivables)) > 0) {
                tipService.TipUpdate(map);
            } else {
                tipService.TipDelete(map);
            }

            mav.addObject("code", "0");
            mav.addObject("msg", "修改成功!");
//			mav.addObject("resultList",resultList);
        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("code", "1");
            mav.addObject("msg", "修改失败!");
        }
        return mav;
    }

    /**
     * 换pad拉取小费数据
     *
     * @param params
     * @return
     * @author lizongren
     */
    @RequestMapping("/tipLoadData")
    @ResponseBody
    public ModelAndView TipLoadData(@RequestBody String json) {
        ModelAndView mav = new ModelAndView();
        try {
            Map<String, Object> map = JacksonJsonMapper.jsonToObject(json, Map.class);
            String branchid = PropertiesUtils.getValue("current_branch_id");
            map.put("branchid", branchid);

            List<Map<String, Object>> result = tipService.TipLoadData(map);
            if (result.size() > 0) {

                mav.addObject("data", result);
            } else {
//				没有查询到小费记录的时候返回服务员姓名
                result = tipService.TipFindNamebyorderid(map);
                mav.addObject("data", result);
            }
            mav.addObject("code", "0");
            mav.addObject("msg", "拉取成功!");
//			mav.addObject("resultList",resultList);
        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("code", "1");
            mav.addObject("msg", "拉取失败!");
        }
        return mav;
    }

    /**
     * 小费结算
     *
     * @param params
     * @return
     * @author lizongren
     */
    @RequestMapping(value = "/tipBilling", method = RequestMethod.POST)
//	@RequestMapping("/find")
    @ResponseBody
    public ModelAndView TipBilling(@RequestBody String json) {
        ModelAndView mav = new ModelAndView();
//		Map<String, Object> params = new HashMap<String, Object>();
        try {
            Map<String, Object> map = JacksonJsonMapper.jsonToObject(json, Map.class);
            String branchid = PropertiesUtils.getValue("current_branch_id");
            map.put("branchid", branchid);
//			map.put("orderid", orderid);
//			map.put("paid", paid);
            tipService.TipBilling(map);
            mav.addObject("code", "0");
            mav.addObject("msg", "结算成功!");
//			mav.addObject("resultList",resultList);
        } catch (Exception e) {

            mav.addObject("code", "1");
            mav.addObject("msg", "结算失败!");
        }
        return mav;
    }

    /**
     * 查询门店服务员小费list
     *
     * @param params
     * @return
     * @author lizongren
     */
    @RequestMapping("/tipList")
//	@RequestMapping("/find")
    @ResponseBody

    public Map<String, Object> TipList(String flag) {
        Map<String, Object> timeMap = getTime(flag);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Map<String, Object>> result = tipService.TipList(timeMap);
            resultMap.put("result", 0);
            resultMap.put("msg", "获取数据成功");
            resultMap.put("data", result);
            resultMap.put("time", timeMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), "");
            resultMap.put("result", 1);
            resultMap.put("msg", "获取数据失败");
            resultMap.put("data", "");
            resultMap.put("time", timeMap);
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 查询门店服务员小费list
     *
     * @return
     * @author lizongren
     */
    @RequestMapping("/tipListByTime")
//	@RequestMapping("/find")
    @ResponseBody

    public Map<String, Object> TipListByTime(String beginTime, String endTime) {
        Map<String, Object> timeMap = new HashMap<>();
        timeMap.put("startTime", beginTime);
        timeMap.put("endTime", endTime);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            float tipMoney = 0;
            Map<String, Object> result = tipService.TipListByTime(timeMap);
            if (result != null && result.containsKey("tipMoney") && !"".equals(result.get("tipMoney"))) {
                tipMoney = Float.valueOf(result.get("tipMoney") + "");
            }
            resultMap.put("result", 0);
            resultMap.put("msg", "获取数据成功");
            resultMap.put("tipMoney", tipMoney);
        } catch (Exception e) {
            logger.error(e.getMessage(), "");
            resultMap.put("result", 1);
            resultMap.put("msg", "获取数据失败");
            resultMap.put("data", "");
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 获取开始结束时间
     *
     * @param falg
     * @return
     */
    private Map<String, Object> getTime(String falg) {
        Map<String, Object> map = new HashMap<>();
        String startTime = null;
        String endTime = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = df.format(new Date());

        if (falg.equals("1")) {  //今日
            startTime = DateUtils.today() + " 00:00:00";
        } else if (falg.equals("2")) {  //本周
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_WEEK) - 1;
            if (day == 0) {
                day = 7;
            }
            c.add(Calendar.DATE, -day + 1);
            startTime = sdf.format(c.getTime());
        } else if (falg.equals("3")) {  //本月
            startTime = DateUtils.monthOfFirstDay() + " 00:00:00";
        } else if (falg.equals("4")) {   //上月
            startTime = DateUtils.beforeMonthOfFirstDay() + " 00:00:00";
            endTime = DateUtils.beforeMonthOfLastDay() + " 23:59:59";
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return map;
    }
//	/**
//	 * 结算
//	 * @param params
//	 * @return
//	 * @author lizongren
//	 */
//	@RequestMapping("/Billing")
////	@RequestMapping("/find")
//	@ResponseBody
//	public ModelAndView Billing(@RequestParam Map<String, Object> params) {
//		ModelAndView mav = new ModelAndView();
//		try {
//			String branchid = PropertiesUtils.getValue("current_branch_id");
//			params.put("branchid", branchid);
//			List<Map<String,Object>> resultList = regiterBillService.Billing(params);
//			mav.addObject("code","1");
//			mav.addObject("msg","查询成功!");
////			mav.addObject("resultList",resultList);
//		} catch (Exception e) {
//
//			mav.addObject("code","0");
//			mav.addObject("msg","查询失败!");
//		}
//		return mav;
//	}
//	
//	/**
//	 * 导出挂账统计表主表
//	 * @author weizhifang
//	 * @since 2016-3-16
//	 * @param params
//	 * @param request
//	 * @param response
//	 */
//	@RequestMapping(value="/exportRegisterBill")
//	@ResponseBody
//	public void exportRegisterBill(@RequestParam Map<String, Object> params,HttpServletRequest request, HttpServletResponse response){
////		String branchid = PropertiesUtils.getValue("current_branch_id");
////		String branchname = itemDetailService.getBranchName(branchid);
////		params.put("branchname", branchname);
////		params.put("branchId", branchid);
////		params.put("pi_dqym","-1");
////		params.put("pi_myts","-1");
////		List<Map<String,Object>> list =  regiterBillService.BillCount(params);
//		
//		String branchid = PropertiesUtils.getValue("current_branch_id");
//		String branchname = itemDetailService.getBranchName(branchid);
//		params.put("branchid", branchid);
//		params.put("branchname", branchname);
//		params.put("pi_dqym","-1");
//    	params.put("pi_myts","-1");
//		List<Map<String,Object>> list = regiterBillService.BillCount(params);
//		
//		regiterBillService.createMainExcel(request, response, list, params);
//	}

}
