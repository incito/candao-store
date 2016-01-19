package com.candao.www.webroom.controller;

import java.util.HashMap;
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
import com.candao.www.webroom.model.TCouponGroup;
import com.candao.www.webroom.service.CouponsService;

@Controller
@RequestMapping("/coupons")
public class CouponsController {
	@Autowired
	private CouponsService couponsService;
	
	@RequestMapping("/index")
	public String index(){
		return "coupons/show";
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
		Page<Map<String, Object>> pageMap = couponsService.grid(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		return mav;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public String save(@RequestBody TCouponGroup tCouponGroup) {
		boolean b = false;
		String id = tCouponGroup.gettCoupons().getCouponid();
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				tCouponGroup.gettCoupons().setCouponid(IdentifierUtils.getId().generate().toString());
				tCouponGroup.gettCoupons().setStatus(0);
				b = couponsService.save(tCouponGroup);
			} else {// 修改
				b = couponsService.update(tCouponGroup);
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
	
	/**
	 * 根据id查询单个优惠
	 * @param id
	 * @param model
	 * @return 
	 */
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		TCouponGroup tCouponGroup = couponsService.findById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tCouponGroup", tCouponGroup);
		return mav;
	}
	
	/**
	 * 根据id删除单个优惠
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "id") String id) {
		boolean b = false;
		 b = couponsService.deleteById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
	
	/**
	 * 根据id删除单个优惠
	 * @param id
	 * @return
	 */
	@RequestMapping("/getparterner")
	@ResponseBody
	public String getparterner() {
		 
		return "";
	}
	
	
	
	/**
	 * 查询出关联这个优惠的所有菜
	 */
//	@RequestMapping("/getDishListByCouponsId/{CouponsId}")
//	@ResponseBody
//	public ModelAndView getDishListByCouponsId(@PathVariable(value = "CouponsId") String CouponsId){
//		ModelAndView mav = new ModelAndView();
//		List<Map<String,Object>> list=couponsService.getDishListByCouponsId(CouponsId);
//		mav.addObject(list);
//		return mav;
//		
//	}
	

}
