package com.candao.www.webroom.controller;
//在响应中查看jason数据
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.candao.www.security.controller.BaseController;
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
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.TbTableArea;
import com.candao.www.data.model.TbasicData;
import com.candao.www.webroom.service.TableAreaService;

@Controller
@RequestMapping("/tableArea")
public class TableAreaController extends BaseController{
	@Autowired
	private TableAreaService tableAreaService;

	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = tableAreaService.grid(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		// return JacksonJsonMapper.objectToJson(page);
		return mav;
	}

	@RequestMapping("/count/{id}")
	@ResponseBody
	public ModelAndView count(@PathVariable(value = "id") String id,Model model) {
		List<Map<String,Object>> list = tableAreaService.count(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("all", list);
		return mav;
	}
	@RequestMapping("/find")
	@ResponseBody
	public ModelAndView find(@RequestParam Map<String, Object> params) {
		List<Map<String,Object>> list = tableAreaService.find(params);
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("areaid", "areaid000");
		maps.put("areaname", "新增区域");
		maps.put("areaNo", "0");
		
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
		return "tableArea/tableArea";
	}

	@RequestMapping("/save/{areaLength}")
	@ResponseBody
	public String save(@RequestBody TbTableArea tbTableArea, String json, Model model,@PathVariable(value = "areaLength") Integer areaLength) {
		boolean b = false;
		tbTableArea.setStatus(1);
		String id = tbTableArea.getAreaid();
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				tbTableArea.setAreaid(IdentifierUtils.getId().generate().toString());
				tbTableArea.setAreaSort(areaLength);
				String branchId = PropertiesUtils.getValue("current_branch_id");
				tbTableArea.setBranchid(branchId);
				b = tableAreaService.save(tbTableArea);
				return JSON.toJSONString(getResponseStr(null, b ? "添加成功" : "添加失败", b));
			} else {// 修改
				b = tableAreaService.update(tbTableArea);
				return JSON.toJSONString(getResponseStr(null, b ? "修改成功" : "修改失败", b));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JSON.toJSONString(getResponseStr(null, e.getMessage(), false));
		}
	}

	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		TbTableArea tbTableArea = tableAreaService.findById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbTableArea", tbTableArea);
		return mav;
	}
	@RequestMapping("/tableAvaliableStatus/{id}")
	@ResponseBody
	public ModelAndView tableAvaliableStatus(@PathVariable(value = "id") String id, Model model) {
		TbTableArea tbTableArea = tableAreaService.tableAvaliableStatus(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbTableArea", tbTableArea);
		return mav;
	}
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "id") String id) {
		boolean b = tableAreaService.deleteById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addAllObjects(getResponseStr(null,"删除成功",true));
		} else {
			mav.addAllObjects(getResponseStr(null,"删除失败",false));
		}
		return mav;
	}
	
	
	@RequestMapping("/getTableTag")
	@ResponseBody
	public ModelAndView getTableTag() {
		List<Map<String, Object>> list = tableAreaService.getTableAreaTag();
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("areaname", "全部");
		maps.put("areaid", "");
		lists.add(maps);
		for (int i = 0; i < list.size(); i++) {
			lists.add(list.get(i));
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject(lists);
		return mav;
	}
	
	@RequestMapping("/validateArea")
	@ResponseBody
	public ModelAndView validateArticle(TbTableArea tbTableArea){
//		String areaid = tbTableArea.getAreaid();
		ModelAndView mav = new ModelAndView();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("areaname", tbTableArea.getAreaname());
//		params.put("status", 0);
		
		List<Map<String, Object>> list=tableAreaService.find(params);
		TbTableArea a=tableAreaService.findById(tbTableArea.getAreaid());
		//新增
		if(ValidateUtils.isEmpty(tbTableArea.getAreaid())){
			if(list!=null&&list.size()>0){
				
				mav.addObject("message", "分区名称不能重复");
				
			}
		}else{
			//修改
			if(!a.getAreaname().equals(tbTableArea.getAreaname())){
				if(list!=null&&list.size()>0){
					
					mav.addObject("message", "分区名称不能重复");
				}
			}else{
				if(list!=null&&list.size()>1){
					
					mav.addObject("message", "分区名称不能重复");
					
					
				
				}
			}
		}
		
		return mav;
	}

	@RequestMapping("/updateListOrder")
	public ModelAndView  updateListOrder(@RequestBody List<TbTableArea> tbTableArea){
		int b=tableAreaService.updateListOrder(tbTableArea);
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mov=new ModelAndView();
		if(b>=0){
			map.put("message", "success");
		}else{
			map.put("message", "fail");
		}
		mov.addObject(map);
		return mov; 
	}
	
	
}


