/**
 * 
 */
package com.candao.www.webroom.controller;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.www.webroom.service.OtherCouponService;
//*
/**
 * 更多优惠方式的添加
 *  @author zhouyao
 *  @serialData 2016-1-21
 */
@Controller
@RequestMapping("/otherCoupon")
public class OtherCouponController {
  @Autowired
  private OtherCouponService otherCouponService;
  
    @RequestMapping("/topage")
	public ModelAndView topage(@RequestParam Map<String, Object> params){
		String path = params.get("path").toString();
		ModelAndView newProduct = new ModelAndView(path);
		return newProduct;
    }
    
    /**
	 * 新增更多优惠
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/addOtherCoupon")
	@ResponseBody
	public ModelAndView addOtherCoupon(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			if(!params.get("id").equals("")&&params.get("id")!=null){
				otherCouponService.updateActivity(params);
			}else{
				otherCouponService.addCouponActivity(params);
			}
			mav.addObject("code","1");
			mav.addObject("msg","保存成功!");
		} catch (Exception e) {
			mav.addObject("code","0");
			mav.addObject("msg","保存失败!");
		}
		return mav;
	}
	
	/**
	 * 查看明细
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getCoupon")
	@ResponseBody
	public ModelAndView getCoupon(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView("coupons/otherCouponMod");//
		String id = params.get("id").toString();
		mav.addObject("id", params.get("id"));
		mav.addObject("isModify", params.get("isModify"));
		try {
			List<Map<String,Object>> resultList = otherCouponService.getCoupon(id);
			//根据优惠id查询门店编号和门店名称
			List<Map<String,Object>> branchInfo = otherCouponService.getBranchInfo(id);
			JSONArray js = JSONArray.fromObject(resultList);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
			System.out.println(js);
			mav.addObject("resultList",js);
			mav.addObject("branchInfos",JSONArray.fromObject(branchInfo));
			
		} catch (Exception e) {
			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	
	/**
	 * 查询优惠类型列表
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getTypeDict")
	@ResponseBody
	public ModelAndView getTypeDict(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			List<Map<String,Object>> resultList = otherCouponService.getTypeDict(params);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
			mav.addObject("resultList",resultList);
		} catch (Exception e) {
			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	/**
	 * 合作单位活动方式
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getCooperationNnit")
	@ResponseBody
	public ModelAndView getCooperationNnit(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			List<Map<String,Object>> resultList = otherCouponService.getCooperationNnit(params);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
			mav.addObject("resultList",resultList);
		} catch (Exception e) {
			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	/**
	 * 手工优免活动方式
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getActivityType")
	@ResponseBody
	public ModelAndView getActivityType(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			List<Map<String,Object>> resultList = otherCouponService.getActivityType(params);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
			mav.addObject("resultList",resultList);
		} catch (Exception e) {
			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	/**
	 * 在线支付优免支付类型
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getPaywayType")
	@ResponseBody
	public ModelAndView getPaywayType(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			List<Map<String,Object>> resultList = otherCouponService.getPaywayType(params);
			mav.addObject("code","1");
			mav.addObject("msg","查询成功!");
			mav.addObject("resultList",resultList);
		} catch (Exception e) {
			mav.addObject("code","0");
			mav.addObject("msg","查询失败!");
		}
		return mav;
	}
	

/*	*//**
	 * 修改更多优惠
	 * @param params
	 * @return
	 * @author zhouyao
	 *//*
	@RequestMapping("/updateOtherCoupon")
	@ResponseBody
	public ModelAndView updateOtherCoupon(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		try {
			otherCouponService.updateActivity(params);
			otherCouponService.updateDetail(params);
			mav.addObject("code","0");
			mav.addObject("msg","修改成功!");
		} catch (Exception e) {
			mav.addObject("code","1");
			mav.addObject("msg","修改失败!");
		}
		return mav;
	}*/
	/**
	 * 根据id查询优惠活动表
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getActivity")
	@ResponseBody
	public ModelAndView getActivity(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		String id = params.get("id").toString();
		try {
			otherCouponService.getActivity(id);
			mav.addObject("code","1");
			mav.addObject("msg","保存成功!");
		} catch (Exception e) {
			mav.addObject("code","0");
			mav.addObject("msg","保存失败!");
		}
		return mav;
	}
	/**
	 * 根据id查询优惠活动表明细表
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/getDetail")
	@ResponseBody
	public ModelAndView getDetail(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		String id = params.get("id").toString();
		try {
			otherCouponService.getDetail(id);
			mav.addObject("code","1");
			mav.addObject("msg","保存成功!");
		} catch (Exception e) {
			mav.addObject("code","0");
			mav.addObject("msg","保存失败!");
		}
		return mav;
	}
	
	/**
	 * 根据id删除优惠活动
	 * @param params
	 * @return
	 * @author zhouyao
	 */
	@RequestMapping("/detalOtherCoupon")
	@ResponseBody
	public ModelAndView detalOtherCoupon(@RequestParam Map<String, Object> params) {
		ModelAndView mav = new ModelAndView();
		String id = params.get("id").toString();
		try {
			otherCouponService.deleteActivity(id);
			otherCouponService.deleteDetail(id);
			mav.addObject("code","1");
			mav.addObject("msg","删除成功!");
		} catch (Exception e) {
			mav.addObject("code","0");
			mav.addObject("msg","删除失败!");
		}
		return mav;
	}
}
