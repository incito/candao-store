package com.candao.www.webroom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.model.Tbbranch;
import com.candao.www.security.controller.BaseController;
import com.candao.www.security.service.ShopMgService;
import com.candao.www.utils.PingyingUtil;
//*
 

/**
 * 门店管理
 * 
 * @author zt
 * 
 */
@Controller
@RequestMapping("/shopMg")
public class ShopMgController extends BaseController {

  @Autowired
  private ShopMgService shopMgService;

  /**
   * 新增门店/更新门店
   * 
   * @param branchMap
   * @return
   */
  @RequestMapping(value = "/addShop")
  @ResponseBody
  public HashMap<String, String> addShop(@RequestBody HashMap<String, Object> branchMap) {

    HashMap<String, String> result = new HashMap<String, String>();
    try {
      if (branchMap.get("branchid") != null && !branchMap.get("branchid").toString().equals("") ) {// 更新
        shopMgService.update(branchMap);
      } else {// 新增
        shopMgService.addShop(branchMap);
      }
      result.put("msg", "0");
    } catch (Exception e) {
      result.put("msg", "1");
    }
    return result;
  }
/**
 * 根据id获取门店信息
 * @param param
 * @return
 */
  @RequestMapping(value = "/get")
  @ResponseBody
  public HashMap<String, Object> get(@RequestBody HashMap<String, Object> param) {

    HashMap<String, Object> result = new HashMap<String, Object>();
    try {
      result.put("data", shopMgService.getOne(param.get("id").toString()));
      result.put("msg", "0");
    } catch (Exception e) {
      result.put("msg", "1");
    }
    return result;
  }
  
  /**
   * 获取所有门店信息
   * @return
   */
  @RequestMapping(value = "/getall")
  @ResponseBody
  public String getAll() {
    String json = "";
    List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
    try {
    	result = shopMgService.getAll();
		return JacksonJsonMapper.objectToJson(result);
    } catch (Exception e) {
    	json = "";
    }
    return json;
  }

  /**
   * 门店管理 查询
   * 
   * @return
   */
  @RequestMapping(value = "")
  public ModelAndView index() {
    ModelAndView mv = new ModelAndView("branchshop/shopMg");
    mv.addObject("current","1");
    Page<Map<String, Object>> page = shopMgService.page(new HashMap<String, Object>(),
        1,
        18);
    mv.addObject("datas", page.getRows());     
    mv.addObject("total", page.getTotal());
    mv.addObject("totalpage", page.getPageCount());
    
    return mv;
  }
  
  private void handlerAddress(HashMap<String, Object>  branchMap){
    String address = branchMap.get("address").toString();
    if(!"".equals(address)){
      String[] args = address.split(" ");
      branchMap.put("addressquery", args[args.length-1]);
    }
  }
/**
 * 根据条件获取响应门店信息

 * @return
 */
  @RequestMapping(value = "/page")
  public ModelAndView page() {
    ModelAndView mv = new ModelAndView("branchshop/shopMg");
    HashMap<String, Object>  branchMap = this.getReqParamMap();
    handlerAddress(branchMap);
    String cur =branchMap.get("current").toString();
    
    //店名拼音添加
    branchMap.put("branchnamepingying", PingyingUtil.getStringPinYin((branchMap.get("branchname")+"").toLowerCase()));
    //店名拼音首字母添加
    branchMap.put("branchnamepyfirstchar", PingyingUtil.getPinYinHeadChar( (branchMap.get("branchname")+"").toLowerCase()));
    
    Page<Map<String, Object>> page = shopMgService.page(branchMap,
        Integer.parseInt(cur),
        Integer.parseInt(branchMap.get("pagesize").toString()));
    mv.addObject("datas", page.getRows());
    mv.addAllObjects(branchMap);
    mv.addObject("total", page.getTotal());
    mv.addObject("totalpage", page.getPageCount());
    return mv;
  }
/**
 * 删除门店信息
 * @param branchMap
 * @return
 */
  @RequestMapping(value = "/del")
  @ResponseBody
  public HashMap<String, Object> del(@RequestBody HashMap<String, Object> branchMap) {
    HashMap<String, Object> result = new HashMap<String, Object> ();
    try {
      if (shopMgService.update_del(branchMap) > 0) {
        result.put("msg", "0");
      } else {
        result.put("msg", "1");
      }
    } catch (Exception e) {
      result.put("msg", "1");
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 查询同一县是否已经存在预加的店名。
   * @param branchMap  店名，城市或区县。
   * @return 1：存在；0：不存在。
   */
  @RequestMapping(value = "/isRepeatShopName")
  @ResponseBody
  public HashMap<String, Object> isRepeatShopName(@RequestBody HashMap<String, Object> queryParam) {
    HashMap<String, Object> result = new HashMap<String, Object> ();
    try {
      if (shopMgService.getCountByNameAddress(queryParam) > 0) {
        //存在相同店名
        result.put("status", "1"); 
      } else {
        result.put("status", "0");
      }
    } catch (Exception e) {
      result.put("status", "1");
      e.printStackTrace();
    }
    return result;
  }
  /**
	 * 所有分店名不能重复验证
	 * @param tbbranch
	 * @return
	 */
	@RequestMapping("/validateShop")
	@ResponseBody
	public ModelAndView validateArticle(@RequestBody Tbbranch tbbranch){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("branchname", tbbranch.getBranchname());
//		params.put("status", 0);
		List<HashMap<String, Object>> list=shopMgService.findByParams(params);
		HashMap<String, Object> a=shopMgService.getOne((Integer.toString(tbbranch.getBranchid())));
		//新增
		if(tbbranch.getBranchid()==0){
			if(list!=null&&list.size()>0){
				mav.addObject("message", "门店名称不能重复");
			}
		}else{
			//修改
			if(!a.get("branchname").equals(tbbranch.getBranchname())){
				
				if(list!=null&&list.size()>0){
					mav.addObject("message", "门店名称不能重复");
				}
			}else{
				Map<String,Object> map1=new HashMap<String,Object>();
				
				for(Map<String,Object> param : list){
					if(!param.get("branchid").equals(a.get("branchid"))){
						map1=param;
					}
				}
				if(list!=null&&map1.get("branchid")!=null){
					mav.addObject("message", "门店名称不能重复");
				}
			}
		}
		
		return mav;
	}
	@RequestMapping("/findBranchid")
	@ResponseBody
	public String findBranchid() {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			String branch_id = PropertiesUtils.getValue("current_branch_id");
			if(!ValidateUtils.isEmpty(branch_id)){
				resultMap.put("branch_id", branch_id);
				resultMap.put("Retcode", 0);
				resultMap.put("RetInfo", "OK");
			}else{
				resultMap.put("Retcode", 1);
				resultMap.put("RetInfo", "失败");
			}
			
		}catch(Exception e){
			resultMap.put("Retcode", 1);
			resultMap.put("RetInfo", "失败");
		}
		
		return JacksonJsonMapper.objectToJson(resultMap);
	}

}
