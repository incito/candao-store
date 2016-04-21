package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.IdentifierUtils;
import com.candao.www.data.dao.TSocialDao;
import com.candao.www.data.model.TbGift;
import com.candao.www.webroom.service.SocialService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("socialService")
public class SocialServiceImpl implements SocialService {
	
	private static final Logger logger = LoggerFactory.getLogger(SocialServiceImpl.class);

	@Autowired
	private TSocialDao tsocialDao;
	
	/**
	 * 保存礼物
	 * @author weizhifang
	 * @since 2015-11-11
	 * @param tbGift
	 * @return
	 */
	public void saveGift(String gifts){
		if(StringUtils.isNotBlank(gifts)){
			tsocialDao.deleteGift();
		}
		try{
			JSONArray jsonArray = JSONArray.fromObject(gifts);
			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i); 
				String dish = jsonObject.getString("dish");
				String dishTitle = jsonObject.getString("dish_title");
				String unit = jsonObject.getString("unit");
				String price = jsonObject.getString("price");
				String vipprice = jsonObject.getString("vipprice");
				if(price == null || price.equals("")){
					price = "0";
				}
				if(vipprice == null || vipprice.equals("")){
					vipprice = "0";
				}
				TbGift tbGift = new TbGift();
				String id = IdentifierUtils.getId().generate().toString();
				tbGift.setId(id);
				tbGift.setGiftNo(dish);
				tbGift.setGiftTypeId("");
				tbGift.setGiftName(dishTitle);
				tbGift.setGiftUnit(unit);
				tbGift.setGiftPrice(Float.valueOf(price));
				tbGift.setMemberPrice(Float.valueOf(vipprice));
				tsocialDao.saveGift(tbGift);
			}
		}catch(Exception e){
			logger.error("-->",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询桌子信息
	 * @author weizhifang
	 * @since 2015-11-12
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryTableInfo(String orderid){
		//查询所有当前已开台桌子信息，不包含自己桌
		List<Map<String,Object>> tableList = tsocialDao.queryTableInfo(orderid);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> tbl : tableList){
			Map<String,Object> map = new HashMap<String,Object>();
			String currenttableid = (String)tbl.get("currenttableid");
			Integer womanNum = (Integer)tbl.get("womanNum");
			Integer mannum = (Integer)tbl.get("mannum");
			String ageperiod = (String)tbl.get("ageperiod");
			List<String> ageList = new ArrayList<String>();
			if(StringUtils.isNotBlank(ageperiod)){
				for(int i=0;i<ageperiod.length();i++){
					String age = ageperiod.substring(i, i+1);
					ageList.add(age);
				}
			}
			List<Map<String,Object>> gift = tsocialDao.sendGiftList(currenttableid);
			map.put("currenttableid",currenttableid);
			map.put("order_id", tbl.get("orderid"));
			map.put("ageperiod",ageList);
			map.put("womanNum",womanNum.toString());
			map.put("mannum",mannum.toString());
			map.put("gift_num", gift.size());
			map.put("gift", gift);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 系统设置-查询礼物列表
	 * @author weizhifang
	 * @since 2015-11-13
	 * @return
	 */
	public List<TbGift> getGiftList(){
		return tsocialDao.getGiftList();
	}
	
	/**
	 * 查询已送礼物列表
	 * @author weizhifang
	 * @since 2015-11-17
	 * @return
	 */
	public List<Map<String,Object>> sendGiftList(String orderId){
		return tsocialDao.sendGiftList(orderId);
	}
}
