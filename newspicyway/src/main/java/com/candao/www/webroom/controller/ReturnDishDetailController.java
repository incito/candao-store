package com.candao.www.webroom.controller;

import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.ReturnDishDetailService;
import com.candao.www.webroom.service.impl.ExportReturnDishService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * 退菜明细表
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/returnDish")
public class ReturnDishDetailController<V, K> {

    @Autowired
    private ReturnDishDetailService returnDishDetailService;

    @Autowired
    private ExportReturnDishService exportReturnDishService;

    @Autowired
    private ItemDetailService itemDetailService;


    // 退菜明细表跳转
    @RequestMapping("/askedForARefund")
    public String askedForARefund() {
        return "/billDetails/askedForARefund";
    }

    //赠菜细表跳转
    @RequestMapping("/presentDishDetail")
    public String presentDishDetail() {
        return "/billDetails/presentDishDetail";
    }

    @RequestMapping("/getReturnDishList")
    public ModelAndView getReturnDishList(@RequestParam Map<String, Object> params) {
        String branchid = PropertiesUtils.getValue("current_branch_id");

        params.put("branchid", branchid);// 门店id
        params.put("result", "success");// 返回结果

        List<Map<String, Object>> dishResult = returnDishDetailService.returnDishList(params);
        if (dishResult != null && dishResult.size() > 0 && dishResult.get(0) == null) { // 出错处理
            LoggerHelper loggerHelper = LoggerHelper.getLogger("aa");
            loggerHelper.error("退菜存储过程报错", "error");
            dishResult = null;
        } /*else {
            for (Map<String, Object> map: dishResult)
            {
                map.put("beginTime", DateUtils.dateFormat((Date)map.get("beginTime"), "yyyy-MM-dd HH:mm"));
            }

        }*/
        return new ModelAndView("/billDetails/askedForARefund", "dishResult", dishResult);
    }

    @RequestMapping("/exportXls")
    @ResponseBody
    public void exportXls(
            HttpServletRequest request,
            HttpServletResponse response, @RequestParam Map<String, Object> params) {
        params.put("result", "success");// 返回结果
        String branchid = PropertiesUtils.getValue("current_branch_id");
        params.put("branchid", branchid);// 门店id
        String branchname = itemDetailService.getBranchName(branchid);
        params.put("branchname", branchname);
        params.put("currPage", "0");
        params.put("pageNums", "10000");
        List<Map<String, Object>> dishResult = returnDishDetailService.returnDishList(params);
        exportReturnDishService.exportReturnDishExcel(dishResult, request, response, params);
    }
}
