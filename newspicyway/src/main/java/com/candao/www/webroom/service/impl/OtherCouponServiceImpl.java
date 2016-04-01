package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.DateUtils;
import com.candao.common.utils.Pinyin;
import com.candao.common.utils.UUIDGenerator;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.OtherCouponDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.model.TbPreferentialActivityBranch;
import com.candao.www.data.model.TbPreferentialTypeDict;
import com.candao.www.utils.SessionUtils;
import com.candao.www.webroom.service.OtherCouponService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 更多优惠方式的添加
 *  @author zhouyao
 *  @serialData 2016-1-21
 */
@Service
public class OtherCouponServiceImpl implements OtherCouponService {
	
	private static final Logger logger = LoggerFactory.getLogger(OtherCouponServiceImpl.class);
	
	@Autowired
    private OtherCouponDao otherCouponDao;
	@Autowired
	private TbPreferentialActivityDao tbPreferentialActivityDao;
	  
	/**
	   * 根据ID查询优惠活动信息表
	   * @param groupon
	   */
	 @Override
	  public List<Map<String,Object>> getActivity(String id){
		 return  otherCouponDao.getActivity(id);
	  }
	  /**
	   * 根据ID查询优惠活动信息明细表
	   * @param groupon
	   */
	  @Override
	  public List<Map<String,Object>> getDetail(String id){
		  return  otherCouponDao.getDetail(id);
	  }
	  /**
	   * 添加优惠活动信息表
	   * @param groupon
	   */
	  @Override
	  public int addCouponActivity(Map<String, Object> params){
		  int resultCount = 0;
		  try {
			  //生成uid
			  UUIDGenerator iDGenerator = new UUIDGenerator();
			  String id = iDGenerator.generate().toString().replaceAll("-", "");
			  params.put("id", id);
			  //生成code
			  params.put("code",getNextPreferentialActivityCode());
			  
			  if(params.get("type").equals("08")){
				  if(!params.get("can_credit").equals("")&&params.get("can_credit")!=null){
					  params.put("can_credit", params.get("can_credit").toString());
				  }
				  if(("0").equals(params.get("free_reason"))||("2").equals(params.get("free_reason"))){
					  BigDecimal amount= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  params.put("amount",amount);
				  }else{
					  BigDecimal amount= new BigDecimal(params.get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  params.put("amount",amount);
				  }
				  if(("1").equals(params.get("free_reason"))||("2").equals(params.get("free_reason"))){
					  BigDecimal discount= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  params.put("discount",discount);
				  }else{
					  BigDecimal discount= new BigDecimal(params.get("discount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  params.put("discount",discount);
				  }
			  }
			  //判断是否在线支付
			  if(params.get("type").equals("09")){
				  if(!params.get("payway").equals("")&&params.get("payway")!=null){
					  params.put("can_credit", params.get("payway").toString());
				  }
			  }
			  //优惠类型名称（根据活动code查询活动名称）
			  if(!params.get("type").equals("")&&params.get("type")!=null){
				  TbPreferentialTypeDict typeDict=tbPreferentialActivityDao.getPreferentialType(params.get("type").toString());
				  if(typeDict!=null){
					  params.put("type_name",typeDict.getName());
				  }
			  }
			 /* //判断活动名称
			  if(!("08").equals(params.get("type"))){*/
				  if(!params.get("name").equals("")&&params.get("name") != null){ 
					     //设置拼音首字母
					     params.put("name_first_letter",Pinyin.getPinYinHeadChar(params.get("name").toString()));
				   }
			  /*}*/
			  //优惠的开始时间
			  if(!params.get("starttime").equals("")&&params.get("starttime")!=null){
			    	String starttime = DateUtils.stringDateFormat(params.get("starttime").toString())+" 00:00:00";
			    	params.put("starttime",starttime);
			  }else{
				  params.put("starttime",null);
			  }
			  //优惠的结束时间
			  if(!params.get("endtime").equals("")&&params.get("endtime")!=null){
				  String endtime = DateUtils.stringDateFormat(params.get("endtime").toString())+" 23:59:59";
			      params.put("endtime",endtime);
			  }else{
				  params.put("endtime",null);
			  }
			  params.put("subtable_name", "subtable_name");
			  //创建者
			  params.put("creator",SessionUtils.getCurrentUser().getId());
			  //创建时间
			  params.put("createtime",new Date());
			  if(!params.get("activityIntroduction").equals("")&&params.get("activityIntroduction")!=null){
			      params.put("activity_introduction",params.get("activityIntroduction"));
			  }
			  if(!params.get("useNotice").equals("")&&params.get("useNotice")!=null){
			      params.put("use_notice",params.get("useNotice"));
			  }
			 //是否适应所有的门店(如果不是适应所有的门店，则往t_p_preferential_branch表中循环插入数据)
			  if(!params.get("apply_all").equals("")&&params.get("apply_all")!=null){
				  if(params.get("apply_all").toString().equals("0")){
					//获取branchid
					  if(!params.get("branchInfos").equals("")&&params.get("branchInfos")!=null){
						  JSONArray array = JSONArray.fromObject(params.get("branchInfos"));
						   //1根据传过来的门店id查询门店信息
						    List<TbPreferentialActivityBranch> selectedBranchs= new ArrayList<TbPreferentialActivityBranch>();
						    for (int i = 0; i < array.size(); i++) {
						    	TbPreferentialActivityBranch tbranch = new TbPreferentialActivityBranch();
						    	JSONObject jsonObject = array.getJSONObject(i);
						    	tbranch.setBranch(Integer.parseInt(jsonObject.get("branchId").toString()));
						    	tbranch.setBranch_name(jsonObject.get("branchName").toString());
						    	tbranch.setPreferential(params.get("id").toString());
						    	selectedBranchs.add(tbranch);
						    }
						    System.out.println(selectedBranchs.size());
							//批量保存门店
						    tbPreferentialActivityDao.savePreferentialActivityBranchs(selectedBranchs);
					  }
				  }
			  }
			  this.addCouponDetail(params);
			  resultCount = otherCouponDao.addCouponActivity(params);
			  
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
		  return resultCount;
	  }
	  /**
	   * 添加优惠活动信息明细表
	   * @param groupon
	   */
	  @Override
	  public int addCouponDetail(Map<String, Object> params){
		int resultCount = 0;
		try {
			  params.put("preferential",params.get("id"));
			 /* params.put("unitflag","1");
			  //判断内部优免的情况单位名称
			  if(("08").equals(params.get("type"))){
				  if(!params.get("company_name").equals("")&&params.get("company_name") != null){ 
					     //设置拼音首字母
					     params.put("company_first_letter",Pinyin.getPinYinHeadChar(params.get("company_name").toString()));
					}
			  }*/
			  resultCount = otherCouponDao.addCouponDetail(params);
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
	     return resultCount;
	  }
	  /**
	   * 更新优惠活动信息表
	   * @param groupon
	   */
	  @Override
	  public int updateActivity(Map<String, Object> params){
		  int resultCount = 0;
		  try {
			  params.put("preferential",params.get("id"));
			  if(params.get("type").equals("08")){
				  if(!params.get("can_credit").equals("")&&params.get("can_credit")!=null){
					  params.put("can_credit", params.get("can_credit").toString());
				  }
				  if(("0").equals(params.get("free_reason"))||("2").equals(params.get("free_reason"))){
					  BigDecimal amount= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  params.put("amount",amount);
				  }else{
					  BigDecimal amount= new BigDecimal(params.get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  params.put("amount",amount);
				  }
				  if(("1").equals(params.get("free_reason"))||("2").equals(params.get("free_reason"))){
					  BigDecimal discount= new BigDecimal(("0.00").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  params.put("discount",discount);
				  }else{
					  BigDecimal discount= new BigDecimal(params.get("discount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					  params.put("discount",discount);
				  }
				  
			  }
			  //判断是否在线支付
			  if(params.get("type").equals("09")){
				  if(!params.get("payway").equals("")&&params.get("payway")!=null){
					  params.put("can_credit", params.get("payway").toString());
				  }
			  }
			  //优惠类型名称（根据活动code查询活动名称）
			  if(!params.get("type").equals("")&&params.get("type")!=null){
				  TbPreferentialTypeDict typeDict=tbPreferentialActivityDao.getPreferentialType(params.get("type").toString());
				  params.put("type_name",typeDict.getName());
			  }
			  /*  //判断活动名称
			  if(!("08").equals(params.get("type"))){*/
				  //判断活动名称
				  if(!params.get("name").equals("")&&params.get("name") != null){ 
				     //设置拼音首字母
				     params.put("name_first_letter",Pinyin.getPinYinHeadChar(params.get("name").toString()));
				  }
			  /*  }*/
			  //优惠的开始时间
			  if(!params.get("starttime").equals("")&&params.get("starttime")!=null){
			    	String starttime =DateUtils.stringDateFormat(params.get("starttime").toString())+" 00:00:00";
			    	params.put("starttime",starttime);
			  }else{
				  params.put("starttime",null);
			  }
			  //优惠的结束时间
			  if(!params.get("endtime").equals("")&&params.get("endtime")!=null){
				  String endtime = DateUtils.stringDateFormat(params.get("endtime").toString())+" 23:59:59";
			    	params.put("endtime",endtime);
			  }else{
				  params.put("endtime",null);
			  }
			  params.put("subtable_name", "subtable_name");
			  //创建者
			  params.put("creator",SessionUtils.getCurrentUser().getId());
			  //创建时间
			  params.put("createtime",new Date());
			  
			  if(!params.get("activityIntroduction").equals("")&&params.get("activityIntroduction")!=null){
			      params.put("activity_introduction",params.get("activityIntroduction"));
			  }
			  if(!params.get("useNotice").equals("")&&params.get("useNotice")!=null){
			      params.put("use_notice",params.get("useNotice"));
			  }
			 //是否适应所有的门店(如果不是适应所有的门店，则往t_p_preferential_branch表中循环插入数据)
			 List<TbPreferentialActivityBranch> selectedBranchs= new ArrayList<TbPreferentialActivityBranch>();
			  if(!params.get("apply_all").equals("")&&params.get("apply_all")!=null){
				  if(params.get("apply_all").toString().equals("0")){
					  //1删除优惠门店信息表中的优惠重新插入
				      otherCouponDao.deleteBranch(params);
					  //获取branchid
					  if(!params.get("branchInfos").equals("")&&params.get("branchInfos")!=null){
						  JSONArray array = JSONArray.fromObject(params.get("branchInfos"));
						   //1根据传过来的门店id查询门店信息
						    for (int i = 0; i < array.size(); i++) {
						    	TbPreferentialActivityBranch tbranch = new TbPreferentialActivityBranch();
						    	JSONObject jsonObject = array.getJSONObject(i);
						    	tbranch.setBranch(Integer.parseInt(jsonObject.get("branchId").toString()));
						    	tbranch.setBranch_name(jsonObject.get("branchName").toString());
						    	tbranch.setPreferential(params.get("id").toString());
						    	selectedBranchs.add(tbranch);
						    }
							//批量保存门店
					  }
					  tbPreferentialActivityDao.savePreferentialActivityBranchs(selectedBranchs);
				  }
			  }
			  resultCount =  otherCouponDao.updateActivity(params);
			  this.updateDetail(params);
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
		  return resultCount;
	  }
	  
	  /**
	   * 更新优惠活动信息明细表
	   * @param groupon
	   */
	  @Override
	  public int updateDetail(Map<String, Object> params){
		  int resultCount = 0;
		  try {
			  params.put("preferential",params.get("preferential"));
			  /*//判断内部优免的情况单位名称
			  if(("08").equals(params.get("type"))){
				  if(!params.get("company_name").equals("")&&params.get("company_name") != null){ 
					     //设置拼音首字母
					     params.put("company_first_letter",Pinyin.getPinYinHeadChar(params.get("company_name").toString()));
					  }
			  }*/
			  resultCount = otherCouponDao.updateDetail(params);
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
		  return resultCount;
	  }
	  
	  /**
	   * 查看详细
	   * @param groupon
	   */
	  public List<Map<String,Object>> getCoupon(String id){
		 List<Map<String,Object>> resultlistMap = new ArrayList<Map<String,Object>>();
		 List<Map<String,Object>> listMap = null;
		 try {
			 Map<String, Object> map = new HashMap<String, Object>();
			 Map<String, Object> mapC = new HashMap<String, Object>();
			 mapC.put("id", id);
			 listMap= otherCouponDao.getCoupon(mapC);
			 if(listMap!=null){
				 for (int i = 0; i < listMap.size(); i++) {
					  map.put("name", listMap.get(i).get("name"));
					  map.put("type", listMap.get(i).get("type"));
					  map.put("type_name", listMap.get(i).get("type_name"));
					  map.put("color", listMap.get(i).get("color"));
					  map.put("apply_all", listMap.get(i).get("apply_all"));
					  map.put("discount", listMap.get(i).get("discount"));
					  map.put("free_reason", listMap.get(i).get("free_reason"));
					  map.put("company_name", listMap.get(i).get("company_name"));
					  map.put("can_credit", listMap.get(i).get("can_credit"));
					  if(listMap.get(i).get("starttime")!=null){
						  map.put("starttime",DateUtils.stringDateFormat(listMap.get(i).get("starttime").toString()));
					  }else{
						  map.put("starttime","");
					  }
					  if(listMap.get(i).get("endtime")!=null){
						  map.put("endtime",DateUtils.stringDateFormat(listMap.get(i).get("endtime").toString()));
					  }else{
						  map.put("endtime","");
					  }
					  map.put("activityIntroduction", listMap.get(i).get("activity_introduction"));
					  map.put("useNotice", listMap.get(i).get("use_notice"));
					  map.put("payway",listMap.get(i).get("can_credit"));
					  map.put("amounts", listMap.get(i).get("amount"));
					  map.put("cooperationType", listMap.get(i).get("free_reason"));
					  List<Map<String,Object>> branchInfo = this.getBranchInfo(id);
					  map.put("branchInfos", branchInfo);
					  resultlistMap.add(map);
					}
			 }
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
		  return resultlistMap;
	  }
	  /**
	   * 根据优惠的id查询门店id和名称
	   * @param groupon
	   */
	  public List<Map<String,Object>> getBranchInfo(String id){
		 List<Map<String,Object>> resultlistMap = new ArrayList<Map<String,Object>>();
		 List<Map<String,Object>> listMap = null;
		 try {
			 Map<String, Object> mapC = new HashMap<String, Object>();
			 mapC.put("id", id);
			 listMap= otherCouponDao.getBranchInfo(mapC);
			 if(listMap!=null){
				 for (int i = 0; i < listMap.size(); i++) {
					  Map<String, Object> map = new HashMap<String, Object>();
					  map.put("branch", listMap.get(i).get("branch"));
					  map.put("branch_name", listMap.get(i).get("branch_name"));
					  resultlistMap.add(map);
					}
			 }
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
		  return resultlistMap;
	  }
	  /**
	   * 优惠类型查询
	   * @param groupon
	   */
	  public List<Map<String,Object>> getTypeDict(Map<String, Object> params){
		         List<Map<String,Object>> resultlistMap = new ArrayList<Map<String,Object>>();
		     try {
		    	 List<Map<String,Object>> dictList = otherCouponDao.getTypeDict(params);
		    	 if(dictList!=null){
					 for (int i = 0; i < dictList.size(); i++) {
						  Map<String, Object> map = new HashMap<String, Object>();
						  map.put("code", dictList.get(i).get("code"));
						  map.put("name", dictList.get(i).get("name"));
						  resultlistMap.add(map);
						}
				 }
			} catch (Exception e) {
				logger.error("-->",e);
				e.printStackTrace();
			}
		     return resultlistMap;
	  }
	  /**
	   * 合作单位活动方式
	   * @param groupon
	   */
	  public List<Map<String,Object>> getCooperationNnit(Map<String, Object> params){
	         List<Map<String,Object>> resultlistMap = new ArrayList<Map<String,Object>>();
	     try {
	    	 List<Map<String,Object>> dictList = otherCouponDao.getCooperationNnit(params);
	    	 if(dictList!=null){
				 for (int i = 0; i < dictList.size(); i++) {
					  Map<String, Object> map = new HashMap<String, Object>();
					  map.put("code", dictList.get(i).get("code"));
					  map.put("name", dictList.get(i).get("name"));
					  resultlistMap.add(map);
					}
			 }
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
	     return resultlistMap;
	  }
	  /**
	   * 在线支付优免支付类型
	   * @param groupon
	   */
	  public List<Map<String,Object>> getPaywayType(Map<String, Object> params){
	         List<Map<String,Object>> resultlistMap = new ArrayList<Map<String,Object>>();
	     try {
	    	 List<Map<String,Object>> list = otherCouponDao.getPaywayType(params);
	    	 if(list!=null){
				 for (int i = 0; i < list.size(); i++) {
					  Map<String, Object> map = new HashMap<String, Object>();
					  map.put("code", list.get(i).get("code"));
					  map.put("name", list.get(i).get("name"));
					  resultlistMap.add(map);
					}
			 }
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
	     return resultlistMap;
	  }
	  /**
	   * 手工优免活动方式
	   * @param groupon
	   */
	  public List<Map<String,Object>> getActivityType(Map<String, Object> params){
	         List<Map<String,Object>> resultlistMap = new ArrayList<Map<String,Object>>();
	     try {
	    	 List<Map<String,Object>> list = otherCouponDao.getActivityType(params);
	    	 if(list!=null){
				 for (int i = 0; i < list.size(); i++) {
					  Map<String, Object> map = new HashMap<String, Object>();
					  map.put("code", list.get(i).get("code"));
					  map.put("name", list.get(i).get("name"));
					  resultlistMap.add(map);
					}
			 }
		} catch (Exception e) {
			logger.error("-->",e);
			e.printStackTrace();
		}
	     return resultlistMap;
	  }
	  /**
	   * 删除优惠活动信息表
	   * @param groupon
	   */
	  @Override
	  public int deleteActivity(String id){
		  return otherCouponDao.deleteActivity(id);
	  }
	  /**
	   * 删除优惠活动信息明细表
	   * @param groupon
	   */
	  @Override
	  public int deleteDetail(String id){
		  return otherCouponDao.deleteDetail(id);
	  }
	  /**
	   * 删除优惠活动门店信息
	   * @param groupon
	   */
	  @Override
	  public int deleteBranch(Map<String,Object> params){
		  return otherCouponDao.deleteBranch(params);
	  }
	  
	  /**
	   * 获取新的优惠活动编码，编码格式为8xxxx。暂时没处理并发的问题
	   * @return
	   */
	  private String getNextPreferentialActivityCode(){
	    String lastCode = tbPreferentialActivityDao.getLastPreferentialActivityCode();
	    if(lastCode != null && StringUtils.isNumeric(lastCode)){
	      return Integer.toString((Integer.parseInt(lastCode) + 1));
	    } else {
	      return Constant.PREFERENTIAL_INIT_CODE;
	    }
	  }
}
