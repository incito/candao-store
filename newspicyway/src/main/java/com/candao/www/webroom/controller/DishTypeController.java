package com.candao.www.webroom.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.candao.www.constant.Constant;
import com.candao.www.data.model.TbasicData;
import com.candao.www.webroom.service.DishTypeService;
//*
@Controller
@RequestMapping("/dishtype")
public class DishTypeController {
	@Autowired
	private DishTypeService dishTypeService;
	
	@RequestMapping("/index")
	public String index(){
		return "/dishtype/show";
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
		Page<Map<String, Object>> pageMap = dishTypeService.grid(params, page, rows,Constant.DISH_DATADICTIONARY);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		return mav;
	}
	@RequestMapping("/findAll/{id}")
	@ResponseBody
	public ModelAndView findAll(@PathVariable(value = "id") String id, Model model){
		List<Map<String,Object>> list=dishTypeService.findAll(id);
		ModelAndView mav=new ModelAndView();
		mav.addObject("all", list);
		return mav;
	}
	@RequestMapping("/findAllNote/{id}")
	@ResponseBody
	public ModelAndView findAllNote(@PathVariable(value = "id") String id, Model model){
		ModelAndView mav=new ModelAndView();
		List<Object> listTotal= new ArrayList<Object>();
		List<Map<String,Object>> list=dishTypeService.findAll(id);
		Map<String,Object> tb=new HashMap<String,Object>();
		if(list.size()>0){
		for(int i=0;i<list.size();i++){
			tb= list.get(i);
			TbasicData tbasicData=getTreeNode(dishTypeService.findById((String)tb.get("id")));
//			TbasicData tbasicData=dishTypeService.findById((String)tb.get("id"));
//			List<TbasicData> listChild=dishTypeService.findAll(tbasicData.getId());
//			tbasicData.setChildren(listChild);
			listTotal.add(tbasicData);
		}
		}
		mav.addObject("all", listTotal);
		return mav;
	}
	public TbasicData getTreeNode(TbasicData tbasicDataRoot){
		List<TbasicData> childNodeList = new ArrayList<TbasicData>();
		List<Map<String,Object>> childNodeList2=dishTypeService.findAll(tbasicDataRoot.getId());
		for(int j=0;j<childNodeList2.size();j++){
			Map<String,Object> map=childNodeList2.get(j);
			TbasicData tba=new TbasicData();
			tba.setItemid(map.get("itemid").toString());
			tba.setId(map.get("id").toString());
			tba.setItemdesc(map.get("itemdesc").toString());
			tba.setRemark(map.get("remark").toString());
			tba.setDepthnum(Integer.valueOf(map.get("depthnum").toString()));
			tba.setFitemDesc(map.get("fitemDesc")==null?"":map.get("fitemDesc").toString());
			childNodeList.add(tba);
		}
		tbasicDataRoot.setChildren(childNodeList);
		for(int i=0;i<childNodeList.size();i++){
			getTreeNode(childNodeList.get(i));
		}
		
		return tbasicDataRoot;
		
	}
	@RequestMapping("/save")
	@ResponseBody
	public String save(@RequestBody TbasicData tbasicData, String json, Model model) {
		boolean b = false;
		tbasicData.setStatus(1);
		String id = tbasicData.getId();
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				tbasicData.setId(IdentifierUtils.getId().generate().toString());
				tbasicData.setCreateTime(new Date());
				tbasicData.setItemtype(Constant.DISH_DATADICTIONARY);
				if("0".equals(tbasicData.getFid())){
					tbasicData.setDepthnum(1);
				}else{
					TbasicData ftsd = dishTypeService.findById(tbasicData.getFid());
					tbasicData.setDepthnum(ftsd.getDepthnum()+1);
				}
				b = dishTypeService.save(tbasicData);
			} else {// 修改
				TbasicData tsd = dishTypeService.findById(id);
				tsd.setFid(tbasicData.getFid());
				tsd.setItemid(tbasicData.getItemid());
				tsd.setItemdesc(tbasicData.getItemdesc());
				tsd.setRemark(tbasicData.getRemark());
				tsd.setItemsort(tbasicData.getItemsort());
				tsd.setUpdateTime(new Date());
				tsd.setIsShow(tbasicData.getIsShow());
				b = dishTypeService.update(tsd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加成功");
			} else {
				map.put("message", "修改成功");
			}
		} else {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加失败");
			} else {
				map.put("message", "修改失败");
			}
		}
		return JacksonJsonMapper.objectToJson(map);
	}
/**
 * 根据分类id获取分类对象

 * @param id
 * @param model
 * @return
 */
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		TbasicData tbasicData = dishTypeService.findById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbasicData", tbasicData);
		return mav;
	}
/**
 * 根据分类id删除分类

 * @param id
 * @return
 */
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "id") String id) {
		boolean b = dishTypeService.deleteById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}

	/**
	 * 取得数据字典
	 * 
	 * @return
	 */
	@RequestMapping("/getDataDictionaryTag")
	@ResponseBody
	public ModelAndView getDataDictionaryTag() {
		List<Map<String, Object>> list = dishTypeService.getDataDictionaryTag(Constant.DISH_DATADICTIONARY);
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("id", "0");
		maps.put("itemdesc", "无");
		lists.add(maps);
		for (int i = 0; i < list.size(); i++) {
			lists.add(list.get(i));
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject(lists);
		return mav;
	}
	/**
	 * 判断是否分类名是否存在
	 * @return
	 */
	@RequestMapping("/judgeName")
	@ResponseBody
	public ModelAndView judgeDishTypeName(@RequestBody TbasicData tbasicData){
		Map<String, Object> params=new HashMap<String, Object>();
		ModelAndView mav = new ModelAndView();
		params.put("itemDesc", tbasicData.getItemdesc());
		List<Map<String,Object>> list=dishTypeService.getListByparams(params);
		if(ValidateUtils.isEmpty(tbasicData.getId())){//新增分类
			if(list!=null&&list.size()>0){
				mav.addObject("flag", 0);
				mav.addObject("id",tbasicData.getId() );
				return mav;
			}
		}else{
			TbasicData t=dishTypeService.findById(tbasicData.getId());
			if(t.getItemdesc().equals(tbasicData.getItemdesc())){//修改分类，不修改分类名称
				if(list!=null&&list.size()>1){
					mav.addObject("flag", 0);
					mav.addObject("id",tbasicData.getId() );
					return mav;
				}
			}else{//修改分类，修改分类名称
				if(list!=null&&list.size()>0){
					mav.addObject("flag", 0);
					mav.addObject("id",tbasicData.getId() );
					return mav;
				}
			}
		}
		mav.addObject("flag", 1);
		return saveDishType(tbasicData,mav);
	}
	public ModelAndView saveDishType(TbasicData tbasicData,ModelAndView mav) {
		boolean b = false;
		tbasicData.setStatus(1);
		tbasicData.setFid("0");
		tbasicData.setItemid("");
		String id = tbasicData.getId();
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				tbasicData.setId(IdentifierUtils.getId().generate().toString());
				tbasicData.setCreateTime(new Date());
				tbasicData.setItemtype(Constant.DISH_DATADICTIONARY);
				if("0".equals(tbasicData.getFid())){
					tbasicData.setDepthnum(1);
				}else{
					TbasicData ftsd = dishTypeService.findById(tbasicData.getFid());
					tbasicData.setDepthnum(ftsd.getDepthnum()+1);
				}
				b = dishTypeService.save(tbasicData);
			} else {// 修改
				TbasicData tsd = dishTypeService.findById(id);
				tsd.setFid(tbasicData.getFid());
				tsd.setItemid(tbasicData.getItemid());
				tsd.setItemdesc(tbasicData.getItemdesc());
				tsd.setRemark(tbasicData.getRemark());
				tsd.setItemsort(tbasicData.getItemsort());
				tsd.setUpdateTime(new Date());
				tsd.setIsShow(tbasicData.getIsShow());
				b = dishTypeService.update(tsd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (b) {
			if (ValidateUtils.isEmpty(id)) {
//				mav.addObject("message", "添加成功");
				mav.addObject("id",tbasicData.getId() );
			} else {
//				mav.addObject("message", "修改成功");
				mav.addObject("id",tbasicData.getId() );
			}
		} else {
			if (ValidateUtils.isEmpty(id)) {
				mav.addObject("message", "添加失败");
			} else {
				mav.addObject("message", "修改失败");
			}
		}
//		if (b) {
//			if (ValidateUtils.isEmpty(id)) {
//				mav.addObject("message", "1");//添加成功
//			} else {
//				mav.addObject("message", "1");//修改成功
//			}
//		} else {
//			if (ValidateUtils.isEmpty(id)) {
//				mav.addObject("message", "0");//添加失败
//			} else {
//				mav.addObject("message", "0");//修改失败
//			}
//		}
		return mav;
	}
	/**
	 * 菜品分类显示位置拖动调整

	 * @param tbasicDatas
	 * @return
	 */
	@RequestMapping("/updateListOrder")
	public ModelAndView  updateListOrder(@RequestBody List<TbasicData> tbasicDatas){
		int b=dishTypeService.updateListOrder(tbasicDatas);
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
