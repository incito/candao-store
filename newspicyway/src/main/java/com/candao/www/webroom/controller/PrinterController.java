package com.candao.www.webroom.controller;
//在响应中查看jason数据
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.ValidateUtils;
import com.candao.print.entity.TbPrinter;
import com.candao.print.service.PrinterService;
import com.candao.www.constant.Constant;

@Controller
@RequestMapping("/printer")
public class PrinterController {
	@Autowired
	private PrinterService printerService;

	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = printerService.grid(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		// return JacksonJsonMapper.objectToJson(page);
		return mav;
	}


	@RequestMapping("/find")
	@ResponseBody
	public ModelAndView find(@RequestParam Map<String, Object> params) {
		List<Map<String,Object>> list = printerService.find(params);
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("printerid", "printerid000");
		maps.put("printername", "添加打印机");
		maps.put("printerNo", "0");
		
		list.add(maps);
		for (int i = 0; i < list.size(); i++) {
			lists.add(list.get(i));
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("find", list);
		// return JacksonJsonMapper.objectToJson(page);
		return mav;
	}

	@RequestMapping("/index")
	public String index() {
		return "printer/printer";
	}

	@RequestMapping("/save")
	@ResponseBody
	public String save(@RequestBody TbPrinter tbPrinter, String json, Model model) {
		boolean b = false;
		tbPrinter.setStatus(1);
		String id = tbPrinter.getPrinterid();
		System.out.println("=========================================="+id+"==========================================");
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				tbPrinter.setPrinterid(IdentifierUtils.getId().generate().toString());
				b = printerService.save(tbPrinter);
			} else {// 修改
				b = printerService.update(tbPrinter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			if (ValidateUtils.isEmpty(id)) {
				map.put("maessge", "添加成功");
			} else {
				map.put("maessge", "修改成功");
			}
		} else {
			if (ValidateUtils.isEmpty(id)) {
				map.put("maessge", "添加失败");
			} else {
				map.put("maessge", "修改失败");
			}
		}
		return JacksonJsonMapper.objectToJson(map);
	}
	
	@RequestMapping("/validatePrinter")
	@ResponseBody
	public ModelAndView validateArticle(TbPrinter tbPrinter){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("printerNo", tbPrinter.getPrinterNo());
		params.put("status", 1);
		List<Map<String, Object>> list=printerService.find(params);
		TbPrinter a=printerService.findById(tbPrinter.getPrinterid());
		//新增
		if(ValidateUtils.isEmpty(tbPrinter.getPrinterid())){
			
			if(list!=null&&list.size()>0){
				mav.addObject("message", "打印机编号不能重复");
			}
		}else{
			//修改
			if(!a.getPrinterNo().equals(tbPrinter.getPrinterNo())){
				if(list!=null&&list.size()>0){
					mav.addObject("message", "打印机编号不能重复");
				}
			}else{
				if(list!=null&&list.size()>1){
					mav.addObject("message", "打印机编号不能重复");
				}
			}
		}
		
		return mav;
	}
	
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		TbPrinter tbPrinter = printerService.findById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbPrinter", tbPrinter);
		return mav;
	}
	@RequestMapping("/findById2/{id}")
	@ResponseBody
	public ModelAndView findById2(@PathVariable(value = "id") String id, Model model) {
		TbPrinter tbPrinter = printerService.findById2(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbPrinter", tbPrinter);
		return mav;
	}
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "id") String id) {
		boolean b = printerService.deleteById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
	
	
	
	@RequestMapping("/getTableTag")
	@ResponseBody
	public ModelAndView getTableTag() {
		List<Map<String, Object>> list = printerService.getTableTag();
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("printername", "全部");
		maps.put("printerid", "");
		lists.add(maps);
		for (int i = 0; i < list.size(); i++) {
			lists.add(list.get(i));
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject(lists);
		return mav;
	}
}
