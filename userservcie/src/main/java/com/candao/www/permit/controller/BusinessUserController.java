package com.candao.www.permit.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.HttpUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.TenantQueue;
import com.candao.www.data.model.BusinessUser;
import com.candao.www.data.model.User;
import com.candao.www.permit.common.BusinessException;
import com.candao.www.permit.service.SendService;
import com.candao.www.permit.service.UserService;
import com.candao.www.utils.SessionUtils;

/**
 * 
 * 企业用户-控制类
 * 
 * @author zhao
 * @date 2015/04/23
 */
@Controller
@RequestMapping("/tenant/")
public class BusinessUserController {

  @Resource(name = "businessUserService")
  private UserService userService;

  @Autowired
  private SendService sendService;

  /**
   * 跳转至租户管理页面
   * @return
   */
  @RequestMapping(value = "/toTenant")
  public ModelAndView toTenant() {
	 ModelAndView mv = new ModelAndView("tenant/tenant");
	 return mv;
  }
  /**
   * 跳转至用户管理页面
   * 
   * @return model and view
   */
  @RequestMapping("/list")
  public ModelAndView list() {
    ModelAndView mav = new ModelAndView("tenant/tenant");
    return mav;
  }

  /**
   * 查询企业用户分页数据，根据条件和分页参数
   * 
   * @param reqMap
   *          请求参数
   * @param page
   *          页数
   * @param rows
   *          每页条数
   * @return json object string
   */
  @RequestMapping("/page")
  @ResponseBody
  public ModelAndView queryUserPage(@RequestParam Map<String, Object> reqMap, int page, int rows) {
    Page<Map<String, Object>> pageMap = userService.queryUserPage(reqMap, page, rows);
    ModelAndView mav = new ModelAndView();
    mav.addObject("page", pageMap);
    return mav;
  }

  /**
   * 获取一条企业用户,根据主键
   * 
   * @param id
   *          主键
   * @return json obj string
   */
  @RequestMapping("/get")
  @ResponseBody
  public ModelAndView getUser(String id) {
    ModelAndView mav = new ModelAndView();
    try {
      User user = userService.getUserById(id);
      mav.addObject("isSuccess", true);
      mav.addObject("user", user);
    } catch (BusinessException be) {
      mav.addObject("isSuccess", false);
      mav.addObject("message", be.getMessage());
      // be.printStackTrace();
    }
    return mav;
  }
  /**
   * 获取一条企业用户,根据用户id
   * 
   * @param id 用户id
   * @return json obj string
   */
  @RequestMapping("/getUserById/{id}")
  @ResponseBody
  public String getUserById(@PathVariable(value = "id") String id){
	Map<String,Object> resultMap = new HashMap<String,Object>();
    try {
      User user = userService.getUserById(id);
      resultMap.put("success", true);
      resultMap.put("data", user);
    } catch (BusinessException be) {
    	resultMap.put("success", false);
        resultMap.put("msg", be.getMessage());
      // be.printStackTrace();
    }
    return JacksonJsonMapper.objectToJson(resultMap);
  }

  /**
   * 保存企业用户
   * 
   * @param req
   * @param resp
   * @return json obj string
   */
  @RequestMapping("/save")
  @ResponseBody
  public ModelAndView saveUser(BusinessUser user) {
    ModelAndView mav = new ModelAndView();
    try {
      User resultUser = userService.updateUser(user);
      if (resultUser != null) {
        mav.addObject("isSuccess", true);
        mav.addObject("message", "保存成功");
      } else {
        mav.addObject("isSuccess", false);
        mav.addObject("message", "保存失败");
      }
    } catch (BusinessException be) {
      mav.addObject("isSuccess", false);
      mav.addObject("message", be.getMessage());
      // be.printStackTrace();
    }
    return mav;
  }

  /**
   * 新增企业用户
   * 
   * @param req
   * @param resp
   * @return json obj string
   */
  @RequestMapping("/add")
  @ResponseBody
  public ModelAndView addUser(BusinessUser user) {
    ModelAndView mav = new ModelAndView();
    try {
      User resultUser = userService.addUser(user);
      if (resultUser != null) {
    	  
    	  //TODO 调用 租户接口生成 用户和数据库关系
    	  User sUser = SessionUtils.getCurrentUser();
    	  
    	 TenantQueue  tenantQueue = new TenantQueue();
    	 tenantQueue.setNodename(resultUser.getName());
    	 tenantQueue.setInserttime(new Date());
    	 tenantQueue.setTenantAccount(sUser.getTenantid());
    	 tenantQueue.setUserId(resultUser.getId());
    	 tenantQueue.setSendqueue("/root/n_b_"+sUser.getTenantid()+"_queue");
    	 tenantQueue.setReceivequeue("/root/n_y_"+sUser.getTenantid()+"_queue");
    	 tenantQueue.setUserAccount(resultUser.getAccount());
    	 
    	 JSONObject jsonObj = JSONObject.fromObject(tenantQueue);
         HttpUtils.httpPostBookorderArray(PropertiesUtils.getValue("cloud_address"), jsonObj);
        
        
        mav.addObject("isSuccess", true);
        mav.addObject("message", "添加成功");
        mav.addObject("account", user.getAccount());
      } else {
        mav.addObject("isSuccess", false);
        mav.addObject("message", "添加失败");
      }
    } catch (BusinessException be) {
      mav.addObject("isSuccess", false);
      mav.addObject("message", be.getMessage());
      // be.printStackTrace();
    }
    return mav;
  }

  /**
   * 删除企业用户,根据主键
   * 
   * @param id
   *          主键
   * @return json obj string
   */
  @RequestMapping("/delete")
  @ResponseBody
  public ModelAndView delete(String id) {
    ModelAndView mav = new ModelAndView();
    try {
      Integer count = userService.deleteUserById(id);
      if (count == 1) {
        mav.addObject("isSuccess", true);
        mav.addObject("message", "删除成功");
      } else {
        mav.addObject("isSuccess", false);
        mav.addObject("message", "删除失败");
      }
    } catch (BusinessException be) {
      mav.addObject("isSuccess", false);
      mav.addObject("message", be.getMessage());
      // be.printStackTrace();
    }
    return mav;
  }

  /**
   * 删除企业用户,根据主键
   * 
   * @param id
   *          主键
   * @return json obj string
   */
  @RequestMapping("/sendMail")
  @ResponseBody
  public ModelAndView sendMail(String email, String account) {
    ModelAndView mav = new ModelAndView();
    try {
      sendService.sendAccountByMail(email, account);
      mav.addObject("isSuccess", true);
      mav.addObject("message", "账号发送成功");
    } catch (BusinessException be) {
	     mav.addObject("isSuccess", false);
	     mav.addObject("message", be.getMessage());
    } catch (AddressException e) {
    	 mav.addObject("isSuccess", false);
         mav.addObject("message", e.getMessage());
	} catch (IOException e) {
		 mav.addObject("isSuccess", false);
	     mav.addObject("message", e.getMessage());
	} catch (MessagingException e) {
		 mav.addObject("isSuccess", false);
	     mav.addObject("message", e.getMessage());
	}
    return mav;
  }

}
