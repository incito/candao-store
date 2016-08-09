package com.candao.www.webroom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateUtils;
import com.candao.common.utils.timeSlotUtils;
import com.candao.print.service.PrinterService;
import com.candao.www.data.model.TbasicData;
import com.candao.www.data.model.Tdish;
import com.candao.www.utils.SessionUtils;
import com.candao.www.webroom.service.BillDetailsService;
import com.candao.www.webroom.service.DishService;
import com.candao.www.webroom.service.DishTypeService;

@Controller
@RequestMapping("/billDetails")
public class BillDetailsController {
	@Autowired
	private BillDetailsService billDetails;

	@RequestMapping("/index")
	public String index(){
		return "/billDetails/billDetails";
	}
	/**
	 * 分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(@RequestParam Map<String, Object> params, int page, int rows) {
	
		
		Page<Map<String, Object>> pageMap = billDetails.grid(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		return mav;
	}

	
	
	@RequestMapping("/indexAndEnd/{date}")
	@ResponseBody
	public ModelAndView indexAndEnd	(@PathVariable(value = "date") int date) {
		
		
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		
		
		maps=timeSlotUtils.choose(date);
		lists.add(maps);
		ModelAndView mav = new ModelAndView();
		mav.addObject(maps);
		return mav;
	}

}
