/**
 * 
 */
package com.candao.www.webroom.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.model.TbGroupon;
import com.candao.www.data.model.TbHandFree;
import com.candao.www.data.model.TbInnerFree;
import com.candao.www.data.model.TbPreferentialActivityBranch;
import com.candao.www.data.model.TbPreferentialActivitySpecialStamp;
import com.candao.www.data.model.Tbranchshop;
import com.candao.www.data.model.User;
import com.candao.www.utils.SessionUtils;
import com.candao.www.webroom.model.DiscountTicketsVo;
import com.candao.www.webroom.model.GrouponTicketsVO;
import com.candao.www.webroom.model.PreferentialActivitySpecialStampVO;
import com.candao.www.webroom.model.VoucherVo;
import com.candao.www.webroom.service.PreferentialActivityService;
import com.candao.www.webroom.service.PreferentialExternalInterface;
//*
/**
 * @author zhao
 *
 */
@Controller
@RequestMapping("/preferential")
public class PreferentialActivityController {
  @Autowired
  private PreferentialActivityService preferentialActivityService;
  @Autowired
  private PreferentialExternalInterface preferentialExternalInterface ;
  
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
   // Page<Map<String, Object>> pageMap = preferentialActivityService.page(params, page, rows);
	User currentUser=SessionUtils.getCurrentUser();
	Page<Map<String, Object>> pageMap = preferentialActivityService.pageForBranchs(currentUser, params, page, rows);
    ModelAndView mav = new ModelAndView();
    mav.addObject("page", pageMap);
    return mav;
  }
  
  @RequestMapping("")
  public ModelAndView index(){
	ModelAndView mav = new ModelAndView("coupons/preferential");
    return mav;
  }
  

  @RequestMapping(value = "/toVoucher")
  public ModelAndView toVoucher(HttpServletRequest request) {
	  ModelAndView mv = new ModelAndView("coupons/voucher");
	  String id = request.getParameter("id");
    if(id != null && !id.equals("")){
      boolean isModify = Boolean.parseBoolean(request.getParameter("isModify"));
      VoucherVo voucher = preferentialActivityService.getVoucherVo(request.getParameter("id"));
      //重新设置券面金额为小数点后2位
      if(voucher.getAmount() != null){
        voucher.setAmount(voucher.getAmount().setScale(2));
      }
      mv.addObject("voucher", JSONObject.fromObject(voucher));
      mv.addObject("isModify", isModify);
      mv.addObject("preferentialId", id);
    } else {
      mv.addObject("isModify", false);
      mv.addObject("preferentialId", "");
    }
    return mv;
  }
  
  @RequestMapping(value = "/toDiscountCoupon")
  public ModelAndView toDiscountCoupon(HttpServletRequest request) {
	  ModelAndView mv = new ModelAndView("coupons/discountCoupon");
	  String id = request.getParameter("id");
	  if(id != null && !id.equals("")){
  		boolean isModify = Boolean.parseBoolean(request.getParameter("isModify"));
      DiscountTicketsVo discountTicket = preferentialActivityService.getDiscountTicketsVo(request.getParameter("id"));
      //将精度设置为小数点后一位
      if(discountTicket.getDiscount() != null){
        discountTicket.setDiscount(discountTicket.getDiscount().setScale(1));
      }
      mv.addObject("discountTicket", JSONObject.fromObject(discountTicket));
      mv.addObject("isModify", isModify);
      mv.addObject("preferentialId", id);
	  } else {
	    mv.addObject("isModify", false);
	    mv.addObject("preferentialId", "");
	  }
	  return mv;
  }
  @RequestMapping(value = "/toGiftCoupon")
  public ModelAndView toGiftCoupon() {
	  ModelAndView mv = new ModelAndView("coupons/giftCoupon");
	  return mv;
  }
  
  
  
  // 测试用
  @RequestMapping(value = "/toTenant")
  public ModelAndView toTenant() {
	  ModelAndView mv = new ModelAndView("tenant/tenant");
	  return mv;
  }
  @RequestMapping(value = "/toEmployees")
  public ModelAndView toEmployees() {
	  ModelAndView mv = new ModelAndView("employee/employees");
	  return mv;
  }
  @RequestMapping(value = "/toRole")
  public ModelAndView toRole() {
	  ModelAndView mv = new ModelAndView("employee/employeeRole");
	  return mv;
  }
  @RequestMapping(value = "/toNormal")
  public ModelAndView toNormal() {
	  ModelAndView mv = new ModelAndView("account/account");
	  return mv;
  }
  @RequestMapping(value = "/toAccountRole")
  public ModelAndView toAccountRole() {
	  ModelAndView mv = new ModelAndView("account/accountRole");
	  return mv;
  }
  @RequestMapping(value = "/toEmployeesAccount")
  public ModelAndView toEmployeesAccount() {
	  ModelAndView mv = new ModelAndView("employee/myAccount");
	  return mv;
  }
  // 测试用结束
  
  
  
  
  @RequestMapping(value = "/toOtherCoupon")
  public ModelAndView toOtherCoupon(HttpServletRequest request) {
	  ModelAndView mv = new ModelAndView("coupons/otherCoupon");
	  boolean isDetail = Boolean.parseBoolean(request.getParameter("isDetail"));
	  String preferential = request.getParameter("id");
	  mv.addObject("isDetail", isDetail);
    mv.addObject("preferentialId", preferential);
	  return mv;
  }
  
  @RequestMapping(value = "/toGroupBuying")
  public ModelAndView toGroupBuying( HttpServletRequest request  ) {
	  boolean isModify = Boolean.parseBoolean(request.getParameter("isModify"));
	  ModelAndView mv = new ModelAndView("coupons/groupBuying");
	  String id=request.getParameter("id");
	  
	  if ( isModify ){
		  //新增或者编辑
		  
	  }else{
		  //查看
		  mv=new ModelAndView("coupons/groupBuyingDetail");
	  }
	  
	  if( !StringUtils.isBlank( id )){
		  GrouponTicketsVO grouponVO = this.preferentialActivityService.getGrouponTicket(id);
		  //设置金额精度
		  TbGroupon groupon = grouponVO.getGroupon();
		  if(groupon.getBill_amount() != null){
		    groupon.setBill_amount(groupon.getBill_amount().setScale(2));
		  }
		  if(groupon.getToken_amount() != null){
		    groupon.setToken_amount(groupon.getToken_amount().setScale(2));
		  }
		  
		  mv.addObject("grouponVO", grouponVO);
		  //门店 ID 和标题
		  List ids= new ArrayList();
		  List names= new ArrayList();
		  for( TbPreferentialActivityBranch branch:grouponVO.getBranchs() ){
			  ids.add(branch.getBranch());
			  names.add(branch.getBranch_name());
		  }
		  
		  mv.addObject("branch_ids", StringUtils.join(ids, ","));
		  mv.addObject("branch_names", StringUtils.join(names, "、"));
	  }
	  
	  
	  mv.addObject("isModify", isModify);
	  return mv;
  }
  
  @RequestMapping(value = "/toSpecialStamp")
  public ModelAndView toSpecialStamp( HttpServletRequest request ) {
	  //ModelAndView mv = new ModelAndView("coupons/specialStamp");
	  boolean isModify = Boolean.parseBoolean(request.getParameter("isModify"));
	  if ( isModify ){
		  return this.specialStampIndex(request); //编辑
	  }else{
		  return this.specialStampDetail(request); //查看
	  }
  }

  //------------------------特价券------------------------------//
  /**
   * 特价券 -> 添加页面
   * @return
   */
  @RequestMapping("/specialStamp/edit")
  public ModelAndView specialStampIndex( HttpServletRequest request ){
	  ModelAndView mv = new ModelAndView("coupons/specialStampEdit");
	  String id=request.getParameter("id");
	  //根据主键，获取VO对象
	  if( !StringUtils.isBlank( id )){
		  PreferentialActivitySpecialStampVO specialStampVO = this.preferentialActivityService.getPreferentialActivitySpecialStampVO(id);
		  //设置特价券精度
		  List<TbPreferentialActivitySpecialStamp> stamps = specialStampVO.getStamps();
		  if(stamps != null){
		    for(TbPreferentialActivitySpecialStamp stamp : stamps){
		      if(stamp.getPrice() != null){
	          stamp.setPrice(stamp.getPrice().setScale(2));
	        }
	      }
		  }
		  
		  mv.addObject("specialStampVO", specialStampVO);
		  //门店 ID 和标题
		  List ids= new ArrayList();
		  List names= new ArrayList();
		  for( TbPreferentialActivityBranch branch:specialStampVO.getBranchs() ){
			  ids.add(branch.getBranch());
			  names.add(branch.getBranch_name());
		  }
		  
		  mv.addObject("branch_ids", StringUtils.join(ids, ","));
		  mv.addObject("branch_names", StringUtils.join(names, "、"));
		  
	  }
	  return mv;
  }
  
  
  @RequestMapping("/specialStamp/detail")
  public ModelAndView specialStampDetail( HttpServletRequest request ){
	  ModelAndView mv = new ModelAndView("coupons/specialStampDetail");
	  String id=request.getParameter("id");
	  PreferentialActivitySpecialStampVO specialStampVO = this.preferentialActivityService.getPreferentialActivitySpecialStampVO(id);
    //设置特价券精度
    List<TbPreferentialActivitySpecialStamp> stamps = specialStampVO.getStamps();
    if(stamps != null){
      for(TbPreferentialActivitySpecialStamp stamp : stamps){
        if(stamp.getPrice() != null){
          stamp.setPrice(stamp.getPrice().setScale(2));
        }
      }
    }

	  mv.addObject("specialStampVO", specialStampVO);
	  
	  //门店 ID 和标题
	  List names= new ArrayList();
	  for( TbPreferentialActivityBranch branch:specialStampVO.getBranchs() ){
		  names.add(branch.getBranch_name());
	  }
	  
	  mv.addObject("branch_names", StringUtils.join(names, "、"));
	  
	  return mv;
  }
  
  /**
   * 特价券 -> 保存
   * @param activity
   * @return
   */
  @RequestMapping("/specialStamp/save")
  @ResponseBody
  public Map<String, Object> specialStampSave(
      @RequestBody PreferentialActivitySpecialStampVO preferential, HttpServletRequest request) {
    boolean b = false;
    String id = preferential.getActivity().getId();
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (ValidateUtils.isEmpty(id)) {// 增加
        b = preferentialActivityService.addSpecialStamp(preferential);
      } else {// 修改
        b = this.preferentialActivityService.updateSpecialStamp(preferential);
      }
    } catch (RuntimeException re) {
      map.put("result", false);
      map.put("message", re.getMessage());
      re.printStackTrace();
      return map;
    } catch (Exception e) {
      e.printStackTrace();
      b = false;
    }

    if (b) {
      map.put("result", true);
      if (ValidateUtils.isEmpty(id)) {
        map.put("message", "添加成功");
      } else {
        map.put("message", "修改成功");
      }
    } else {
      map.put("result", false);
      if (ValidateUtils.isEmpty(id)) {
        map.put("message", "添加失败");
      } else {
        map.put("message", "修改失败");
      }
    }
    return map;
  }
  
  //----------------------end 特价券-----------------------------//
  
  
  //------- begin-团购券----------//
  
  @RequestMapping("/saveGrouponTicket")
  @ResponseBody
  public ModelAndView saveGrouponTicket( @RequestBody GrouponTicketsVO groupBuying ){
		ModelAndView mav = new ModelAndView();
		
		boolean b = false;
		String id = groupBuying.getActivity().getId();
		try { 
			  if (ValidateUtils.isEmpty(id)) {// 增加
				 b = this.preferentialActivityService.addGrouponTicket(groupBuying);
			  } else {// 修改
				 b = this.preferentialActivityService.updateGrouponTicket(groupBuying);
			  }
		} catch (RuntimeException re){
		  mav.addObject("isSuccess", false);
      mav.addObject("message", re.getMessage());
      re.printStackTrace();
      return mav;
		} catch (Exception e) {
			  e.printStackTrace();
			  b = false;
		}
		
		if (b) {
			mav.addObject("isSuccess", true);
			mav.addObject("message", "保存成功");
		} else {
			mav.addObject("isSuccess", false);
			mav.addObject("message", "保存失败");
		}
		return mav;
  }
  
  //--------end 团购券------------//
  /**
	 * 获取分类和分类下面的菜品，所有的，组成json数据，多计量单位分为多个菜品返回
	 * @return
	 */
	@RequestMapping("/getTypeAndDishList")
	@ResponseBody
	public ModelAndView getTypeAndDishList(){
		Map<String,List<Map<String,Object>>> dishList= preferentialExternalInterface.getGroupedDishList2();
		ModelAndView mav = new ModelAndView();
		mav.addObject("dishList", dishList);
		return mav;
	}
	
  
  @RequestMapping("/delete")
  @ResponseBody
  public ModelAndView deleteById(String id) {
    boolean b = preferentialActivityService.deleteByStatus(id);
    ModelAndView mav = new ModelAndView();
    if (b) {
      mav.addObject("isSuccess", true);
      mav.addObject("message", "删除成功");
    } else {
      mav.addObject("isSuccess", false);
      mav.addObject("message", "删除失败");
    }
    return mav;
  }
  
  @RequestMapping("/addDiscountTicket")
  @ResponseBody
  public ModelAndView addDiscountTicket(@RequestBody DiscountTicketsVo discountTicket){
    ModelAndView mav = new ModelAndView();
    boolean b = true;
    try{
      b = preferentialActivityService.addDiscountTicket(discountTicket);
    } catch (RuntimeException e){
      mav.addObject("isSuccess", false);
      mav.addObject("message", e.getMessage());
      return mav;
    }
    if (b) {
      mav.addObject("isSuccess", true);
      mav.addObject("message", "保存成功");
    } else {
      mav.addObject("isSuccess", false);
      mav.addObject("message", "保存失败");
    }
    return mav;
  }
  
  @RequestMapping("/loadDishSelect")
  public ModelAndView loadDishSelect(){
    ModelAndView mav = new ModelAndView("coupons/dishSelect");
    return mav;
  }
  
  @RequestMapping("/saveDiscountTicket")
  @ResponseBody
  public ModelAndView saveDiscountTicket(@RequestBody DiscountTicketsVo discountTicket){
    ModelAndView mav = new ModelAndView();
    try{
      boolean b = preferentialActivityService.updateDiscountTicket(discountTicket);
      if (b) {
        mav.addObject("isSuccess", true);
        mav.addObject("message", "保存成功");
      } else {
        mav.addObject("isSuccess", false);
        mav.addObject("message", "保存失败");
      }
    } catch (RuntimeException e){
      mav.addObject("isSuccess", false);
      mav.addObject("message", e.getMessage());
      e.printStackTrace();
    }
    return mav;
  }
  
  @RequestMapping("/addVoucher")
  @ResponseBody
  public ModelAndView addVoucher(@RequestBody VoucherVo voucher){
    ModelAndView mav = new ModelAndView();
    boolean isSuccess = true;
    try{
      isSuccess = preferentialActivityService.addVoucher(voucher);
    } catch (RuntimeException e){
      mav.addObject("isSuccess", false);
      mav.addObject("message", e.getMessage());
      return mav;
    }
    if (isSuccess) {
      mav.addObject("isSuccess", true);
      mav.addObject("message", "保存成功");
    } else {
      mav.addObject("isSuccess", false);
      mav.addObject("message", "保存失败");
    }
    return mav;
  }
  
  @RequestMapping("/saveVoucher")
  @ResponseBody
  public ModelAndView saveVoucher(@RequestBody VoucherVo voucher){
    ModelAndView mav = new ModelAndView();
    try{
      boolean b = preferentialActivityService.updateVoucher(voucher);
      if (b) {
        mav.addObject("isSuccess", true);
        mav.addObject("message", "保存成功");
      } else {
        mav.addObject("isSuccess", false);
        mav.addObject("message", "保存失败");
      }
    } catch (RuntimeException e){
      mav.addObject("isSuccess", false);
      mav.addObject("message", e.getMessage());
      e.printStackTrace();
    }
    return mav;
  }
  
  @RequestMapping("/listHandFree")
  @ResponseBody
  public ModelAndView listHandFree(String preferential){
    ModelAndView mav = new ModelAndView();
    Map params = new HashMap();
    if(preferential != null && preferential.trim().length() > 0){
      params.put("preferential", preferential);
    }
    List<TbHandFree> handFrees = preferentialActivityService.findHandFree(params);
    mav.addObject("handFree", JSONArray.fromObject(handFrees));
    return mav;
  }
  
  @RequestMapping("/addHandFree")
  @ResponseBody
  public ModelAndView addHandFree(@RequestBody TbHandFree handFree){
    ModelAndView mav = new ModelAndView();
    Map params = new HashMap();
    boolean isSuccess = true;
    try{
      isSuccess = preferentialActivityService.addHandFree(handFree);
    } catch (RuntimeException e){
      mav.addObject("isSuccess", false);
      mav.addObject("message", e.getMessage());
      return mav;
    }
    if(isSuccess){
      mav.addObject("message", "保存成功");
      mav.addObject("id", handFree.getId());
    } else {
      mav.addObject("message", "保存失败");
    }
    mav.addObject("isSuccess", isSuccess);
    return mav;
  }
  
  @RequestMapping("/updateHandFree")
  @ResponseBody
  public ModelAndView updateHandFree(@RequestBody TbHandFree handFree){
    ModelAndView mav = new ModelAndView();
    try{
    boolean isSuccess = preferentialActivityService.updateHandFree(handFree);
      if(isSuccess){
        mav.addObject("message", "保存成功");
      } else {
        mav.addObject("message", "保存失败");
      }
      mav.addObject("isSuccess", isSuccess);
    } catch (RuntimeException e){
      mav.addObject("isSuccess", false);
      mav.addObject("message", e.getMessage());
    }
    
    return mav;
  }
  
  @RequestMapping("/delHandFree")
  @ResponseBody
  public ModelAndView delHandFree(String id){
    ModelAndView mav = new ModelAndView();
    boolean isSuccess = preferentialActivityService.deleteHandFree(id);
    if(isSuccess){
      mav.addObject("message", "删除成功");
    } else {
      mav.addObject("message", "删除失败");
    }
    mav.addObject("isSuccess", isSuccess);
    return mav;
  }
  
  
  /**
   * 保存 内部减免（合作单位）优惠
   * @param innerfree
   * @return
   */
  @RequestMapping("/saveInnerFree")
  @ResponseBody
  public ModelAndView saveInnerFree( @RequestBody TbInnerFree innerfree ){
		ModelAndView mav = new ModelAndView();
		boolean b = false;
		//TODO 保存内部优惠
		try{
  		if( StringUtils.isBlank( innerfree.getId() )){
  	      b = preferentialActivityService.saveInnerFree(innerfree);
  		}else{
  			b=this.preferentialActivityService.updateInnerFree(innerfree);
  		}
		} catch (RuntimeException e){
      mav.addObject("isSuccess", false);
      mav.addObject("message", e.getMessage());
      return mav;
    }
		if (b) {
			mav.addObject("isSuccess", true);
			mav.addObject("message", "保存成功");
		} else {
			mav.addObject("isSuccess", false);
			mav.addObject("message", "保存失败");
		}
		return mav;
  }
  
   /**
    * 获取内部减免（合作单位）优惠列表
    * @param preferential
    * @return
    */
    @RequestMapping("/pageInnerFree")
	@ResponseBody
    public ModelAndView listInnerFree(@RequestParam Map<String, Object> params, int current, int pagesize){
	    Page<Map<String, Object>> pageMap = preferentialActivityService.pageInnerFree(params, current, pagesize);
	    //设置内部优免折扣精度
	    if(pageMap != null){
	      for(Map<String, Object> innerFree: pageMap.getRows()){
	        if(innerFree.get("discount") != null){
	          innerFree.put("discount", ((BigDecimal)innerFree.get("discount")).setScale(1));
	        }
	      }
	    }
	    ModelAndView mav = new ModelAndView();
	    mav.addObject("page", pageMap);
	    return mav;
	}
    
    /**
     * 根据ID获取内部减免（合作单位）优惠对象
     * @param id
     * @return
     */
	@RequestMapping("/findInnerFree/{id}")
	@ResponseBody
	public ModelAndView findInnerFreeById(@PathVariable(value = "id") String id ) {
		TbInnerFree innerfree = preferentialActivityService.findInnerFreeById(id);
		if(innerfree.getDiscount() != null){
		  innerfree.setDiscount(innerfree.getDiscount().setScale(1));
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("innerfree", innerfree);
		return mav;
	}
  
	/**
	 * 删除 内部减免（合作单位）优惠
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteInnerFree/{id}")
	@ResponseBody
	public ModelAndView deleteInnerFreeById(@PathVariable(value = "id") String id ) {
		boolean b = preferentialActivityService.deleteInnerFreeById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addObject("isSuccess", true);
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("isSuccess", false);
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
	
	
}
