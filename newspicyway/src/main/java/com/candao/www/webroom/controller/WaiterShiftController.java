package com.candao.www.webroom.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.WaiterShiftService;
import com.candao.www.webroom.service.impl.PoiExcleTest;

/**
 * 服务员考核报表
 *
 * @author YANGZHONGLI
 */
@Controller
@RequestMapping("/waiter")
public class WaiterShiftController {

    private static final Logger logger = LoggerFactory.getLogger(WaiterShiftController.class);


    @Autowired
    private WaiterShiftService shiftService;

    @Autowired
    private ItemDetailService itemDetailService;

    /**
     * 查询指定时间段内服务员信息
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/shift", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getWaiterShift(@RequestParam Map<String, String> params) {
        logger.debug("start method getWaiterShift");
        try {
            logger.debug("getWaiterShift params : {} ", params);

            if (params == null) {
                return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
            }

            String branchId = params.containsKey("branchId") ? params.get("branchId") : "";
            String beginTime = params.containsKey("beginTime") ? params.get("beginTime") : "";
            String endTime = params.containsKey("endTime") ? params.get("endTime") : "";
            String shiftid = params.containsKey("shiftid") ? params.get("shiftid") : "-1";

            if (branchId.equals("")) {
                branchId = PropertiesUtils.getValue("current_branch_id");
            }

            if (branchId.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "门店信息获取失败");
            }
            if (beginTime.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "开始时间参数不正确");
            }

            if (endTime.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "结束时间参数不正确");
            }
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("branchId", branchId);
            paramMap.put("beginTime", beginTime.length() == 10 ? beginTime + " 00:00:00" : beginTime);
            paramMap.put("endTime", endTime.length() == 10 ? endTime + " 23:59:59" : endTime);
            paramMap.put("shiftid", shiftid);

            List<Map<String, String>> returnList = shiftService.getWaiterShiftInfo(paramMap);
            if (returnList == null || returnList.size() <= 0) {
                returnList = new ArrayList<Map<String, String>>();
                return ReturnMap.getReturnMap(1, "001", "没有查询到对应的数据", returnList);
            }
            return ReturnMap.getReturnMap(1, "001", "操作成功", returnList);

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug(ex.getMessage(), ex);
        }
        return ReturnMap.getReturnMap(0, "002", "操作失败，请联系管理员");
    }

    /**
     * 查询指定时间段内服务员订单信息
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/shiftorders", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getWaiterShiftOrders(@RequestParam Map<String, String> params) {
        logger.debug("start method getWaiterShift");
        try {
            logger.debug("getWaiterShift params : {} ", params);

            if (params == null) {
                return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
            }

            String branchId = params.containsKey("branchId") ? params.get("branchId") : "";
            String userid = params.containsKey("userid") ? params.get("userid") : "";
            String beginTime = params.containsKey("beginTime") ? params.get("beginTime") : "";
            String endTime = params.containsKey("endTime") ? params.get("endTime") : "";
            String shiftid = params.containsKey("shiftid") ? params.get("shiftid") : "-1";

            if (branchId.equals("")) {
                branchId = PropertiesUtils.getValue("current_branch_id");
            }

            if (branchId.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "门店信息获取失败");
            }
            if (beginTime.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "开始时间参数不正确");
            }

            if (endTime.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "结束时间参数不正确");
            }

            if (userid.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "传入的服务员信息不正确");
            }
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("branchId", branchId);
            paramMap.put("beginTime", beginTime);
            paramMap.put("endTime", endTime);
            paramMap.put("shiftid", shiftid);
            paramMap.put("userid", userid);
            List<Map<String, String>> returnList = shiftService.getWaiterShiftOrderInfo(paramMap);

            if (returnList == null || returnList.size() <= 0) {
                returnList = new ArrayList<Map<String, String>>();
                return ReturnMap.getReturnMap(1, "001", "没有查询到对应的数据", returnList);
            }
            return ReturnMap.getReturnMap(1, "001", "操作成功", returnList);

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug(ex.getMessage(), ex);
        }
        return ReturnMap.getReturnMap(0, "002", "操作失败，请联系管理员");
    }

    /**
     * 导出表格 考核参考表
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/shift/{beginTime}/{endTime}/{shiftid}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getWaiterShiftexport(@PathVariable(value = "shiftid") String shiftid,
                                                    @PathVariable(value = "beginTime") String beginTime,
                                                    @PathVariable(value = "endTime") String endTime, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("start method getWaiterShift");
        try {

            String branchId = PropertiesUtils.getValue("current_branch_id");

            if (branchId.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "门店信息获取失败");
            }
            if (beginTime.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "开始时间参数不正确");
            }

            if (endTime.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "结束时间参数不正确");
            }
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("branchId", branchId);
            paramMap.put("beginTime", beginTime);
            paramMap.put("endTime", endTime);
            paramMap.put("shiftid", shiftid);

            List<Map<String, String>> returnList = shiftService.getWaiterShiftInfo(paramMap);
            if (returnList == null || returnList.size() <= 0) {
                returnList = new ArrayList<Map<String, String>>();
            }
            String filedisplay = "服务员考核统计表";
            response.setContentType("application/vnd.ms-excel");
            String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
            String branchName = itemDetailService.getBranchName(branchId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("branchName", branchName);
            params.put("type", shiftid);
            params.put("beginTime", beginTime);
            params.put("endTime", endTime);
            String title = ExcelUtils.setTabTitleToBusiness(filedisplay, params);
            String[] clounNames = {"服务员编号", "服务员姓名", "开台数", "结算人数", "应收总额", "实收总额", "应收人均", "实收人均", "现金", "银行卡", "会员卡消费", "挂帐", "微信支付", "支付宝支付"};
            String[] keys = {"userid", "username", "ordernum", "custnum", "shouldamount", "paidinamount", "shouldpre", "paidinpre", "xjamount", "yhkamount", "hykxfamount", "gz2amount", "wxzfamount", "zfbzfamount"};
            HSSFWorkbook hssfwof = PoiExcleTest.createExcel(returnList, params, filedisplay, title, clounNames, keys);
            OutputStream fout = response.getOutputStream();
            hssfwof.write(fout);
            fout.flush();
            fout.close();
            hssfwof.close();


        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug(ex.getMessage(), ex);
        }
        return null;
    }


    /**
     * 导出表格 考核参考表
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/shiftorders/{beginTime}/{endTime}/{shiftid}/{userid}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getWaiterOrdersexport(@PathVariable(value = "shiftid") String shiftid,
                                                     @PathVariable(value = "beginTime") String beginTime,
                                                     @PathVariable(value = "userid") String userid,
                                                     @PathVariable(value = "endTime") String endTime, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("start method getWaiterShift");
        try {

            String branchId = PropertiesUtils.getValue("current_branch_id");

            if (branchId.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "门店信息获取失败");
            }
            if (beginTime.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "开始时间参数不正确");
            }

            if (endTime.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "结束时间参数不正确");
            }

            if (userid.equals("")) {
                return ReturnMap.getReturnMap(0, "002", "服务员信息参数不正确");
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("branchId", branchId);
            paramMap.put("beginTime", beginTime);
            paramMap.put("endTime", endTime);
            paramMap.put("shiftid", shiftid);
            paramMap.put("userid", userid);


            List<Map<String, String>> returnList = shiftService.getWaiterShiftOrderInfo(paramMap);
            if (returnList == null || returnList.size() <= 0) {
                returnList = new ArrayList<Map<String, String>>();
            }
            String filedisplay = "服务员考核统计子表";
            response.setContentType("application/vnd.ms-excel");
            String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
            String branchName = itemDetailService.getBranchName(branchId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("branchName", branchName);
            params.put("type", shiftid);
            params.put("beginTime", beginTime);
            params.put("endTime", endTime);
            String title = ExcelUtils.setTabTitleToBusiness(filedisplay, params);
            String[] clounNames = {"订单号", "台号", "就餐人数", "应收", "实收", "现金", "银行卡", "会员卡消费", "挂帐", "微信支付", "支付宝支付"};
            String[] keys = {"orderid", "tableids", "custnum", "shouldamount", "paidinamount", "xjamount", "yhkamount", "hykxfamount", "gz2amount", "wxzfamount", "zfbzfamount"};
            HSSFWorkbook hssfwof = PoiExcleTest.createExcel(returnList, params, filedisplay, title, clounNames, keys);
            OutputStream fout = response.getOutputStream();
            hssfwof.write(fout);
            fout.flush();
            fout.close();
            hssfwof.close();


        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug(ex.getMessage(), ex);
        }
        return null;
    }

}
