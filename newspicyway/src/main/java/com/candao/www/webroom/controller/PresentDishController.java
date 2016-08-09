package com.candao.www.webroom.controller;

/**
 * Created by ytq on 2016/6/22.
 */

import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.PresentDishDetailService;
import com.candao.www.webroom.service.impl.ExportReturnDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 赠菜明细
 *
 * @author ytq
 */
@Controller
@RequestMapping("/presentDish")
public class PresentDishController {
    @Autowired
    private PresentDishDetailService presentDishDetailService;
    @Autowired
    private ExportReturnDishService exportReturnDishService;

    @Autowired
    private ItemDetailService itemDetailService;

    //赠菜细表跳转
    @RequestMapping("/presentDishDetail")
    public String presentDishDetail() {
        return "/billDetails/presentDishDetail";
    }

    @RequestMapping("/getPreDishDetailList")
    public ModelAndView getReturnDishList(@RequestParam Map<String, Object> params) {
        String branchid = PropertiesUtils.getValue("current_branch_id");
        params.put("branchid", branchid);// 门店id
        params.put("result", "success");// 返回结果
        int offset = Integer.valueOf(params.get("pageNums") + "") * (Integer.valueOf(params.get("currPage") + ""));
        params.put("offset", offset);
        List<Map<String, Object>> dishResult = presentDishDetailService.presentDishList(params);
        return new ModelAndView("/billDetails/presentDishDetail", "dishResult", dishResult);
    }

    @RequestMapping("/exportXls")
    @ResponseBody
    public void exportXls(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
        params.put("result", "success");// 返回结果
        String branchid = PropertiesUtils.getValue("current_branch_id");
        params.put("branchid", branchid);// 门店id
        String branchname = itemDetailService.getBranchName(branchid);
        params.put("branchname", branchname);
        params.put("currPage", "0");
        params.put("offset", 0);
        int pageNums = 1000;
        params.put("pageNums", pageNums);
        List<Map<String, Object>> dishResult = new ArrayList<>();
        while (true) {
            List<Map<String, Object>> dishResultTemp = presentDishDetailService.presentDishList(params);
            if (dishResultTemp.isEmpty()) {
                break;
            } else {
                dishResult.addAll(dishResultTemp);
                params.put("offset", Integer.valueOf(params.get("offset") + "") + pageNums);
            }
        }
        exportReturnDishService.exportPresentDishExcel(dishResult, request, response, params);
    }
}
