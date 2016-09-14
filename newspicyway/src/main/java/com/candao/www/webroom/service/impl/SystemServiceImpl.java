package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.constant.SystemConstant;
import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.data.model.TbDataDictionary;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TdishUnit;
import com.candao.www.webroom.service.DishService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: johnson
 * Date: 5/25/15
 * Time: 3:53 下午
 */
@Service
public class SystemServiceImpl {

	@Autowired
	private TbDataDictionaryDao tbDataDictionaryDao;
	
	@Autowired
	private DishService dishService;
	
	private static final Logger logger = LoggerFactory.getLogger(SystemServiceImpl.class);


	/**
	 * 修改字典数据
	 *
	 * @param
	 * @return
	 */
	public void updateTdictionarySingle(JSONObject jsonObject, String type, String typeName) {
		TbDataDictionary tbDataDictionary = new TbDataDictionary();

		tbDataDictionary.setId(jsonObject.getString("dictid"));
		tbDataDictionary.setType(type);
		tbDataDictionary.setTypename(typeName);

		initBeen(jsonObject, tbDataDictionary);

		tbDataDictionaryDao.update(tbDataDictionary);
	}
	
	/**
	 * 插入t_dish
	 *
	 * @param jsonObject
	 */
	public void insertDish(JSONObject jsonObject) {
		JSONObject jsonObjectDish = JSONObject.fromObject(jsonObject.get("data"));
		String dishiid=jsonObjectDish.getString("dictid");
		String id ="null";
		if(dishiid!=null&&!dishiid.equals("")){
			Map<Object, Object> list  = tbDataDictionaryDao.getDish(dishiid);
			if(list!=null){
				id=list.get("dishid").toString();
			}
		}
		if(id.equals("null")){
			if(dishiid.equals("DISHES_98")){
				Tdish tdish=new Tdish();
				tdish.setDishid("DISHES_98");
				tdish.setPrice(jsonObjectDish.getString("price"));
				tdish.setVipprice(jsonObjectDish.getString("member_price"));
				tbDataDictionaryDao.insertDish(tdish);
			}
		}else{
			Tdish tdish=new Tdish();
			tdish.setDishid(dishiid);
			tdish.setPrice(jsonObjectDish.getString("price"));
			tdish.setVipprice(jsonObjectDish.getString("member_price"));
			tbDataDictionaryDao.updatetDish(tdish);
		}
	}
	
	/**
	 * 插入t_template_dishunit
	 *
	 * @param jsonObject
	 */
	public void insertDishUnit(JSONObject jsonObject) {
		JSONObject jsonObjecDishUnit = JSONObject.fromObject(jsonObject.get("data"));
		String dishiid=jsonObjecDishUnit.getString("dictid");
		String id="null";
		if(dishiid!=null&&!dishiid.equals("")){
			Map<Object, Object> list  = tbDataDictionaryDao.getDishUnit(dishiid);
			if(list!=null){
				id=list.get("id").toString();
			}
		}
		if(id.equals("null")){
			if(dishiid.equals("DISHES_98")){
				TdishUnit tdishUnit=new TdishUnit();
				tdishUnit.setId("DISHES_98");
				tdishUnit.setDishid("DISHES_98");
				BigDecimal price = null;
				if(jsonObjecDishUnit.getString("price")!=null&&!jsonObjecDishUnit.getString("price").equals("")){
					 price = new BigDecimal(jsonObjecDishUnit.getString("price").toString());
				}
				tdishUnit.setPrice(price);
				BigDecimal member_price = null;
				if(jsonObjecDishUnit.getString("member_price")!=null&&!jsonObjecDishUnit.getString("member_price").equals("")){
				   member_price = new BigDecimal(jsonObjecDishUnit.getString("member_price").toString());
				}
				
				tdishUnit.setVipprice(member_price);
				tbDataDictionaryDao.insertDishUnit(tdishUnit);
			}
		}else{
			TdishUnit tdishUnit=new TdishUnit();
			tdishUnit.setId(dishiid);
			tdishUnit.setDishid(dishiid);
			BigDecimal price=null;
			if(jsonObjecDishUnit.getString("price")!=null&&!jsonObjecDishUnit.getString("price").equals("")){
				price = new BigDecimal(jsonObjecDishUnit.getString("price").toString());
			}
			tdishUnit.setPrice(price);
			BigDecimal member_price=null;
			if(jsonObjecDishUnit.getString("member_price")!=null&&!jsonObjecDishUnit.getString("member_price").equals("")){
				member_price = new BigDecimal(jsonObjecDishUnit.getString("member_price").toString());
			}
			tdishUnit.setVipprice(member_price);
			Tdish dish = dishService.findAllById(dishiid);
			if(dish!=null&&!StringUtils.isBlank(dish.getTitle())){
				tdishUnit.setUnit(dish.getTitle());
			}
			tbDataDictionaryDao.updatetDishUnit(tdishUnit);
		}
	}
	/**
	 * 插入字典数据
	 *
	 * @param jsonObject
	 */
	public void insertTdictionary(JSONObject jsonObject) {
       
		String type = jsonObject.getString("type");
 
        	if (StringUtils.equals(type, SystemConstant.PASSWORD.type()) || StringUtils.equals(type, SystemConstant.DISHES.type())||StringUtils.equals(type, SystemConstant.ONEPAGETYPE.type())) {//单个插入
    			JSONObject jsonObjectTdictionary = JSONObject.fromObject(jsonObject.get("data"));
    			if (StringUtils.isNotBlank(jsonObjectTdictionary.getString("dictid"))) {
    				updateTdictionarySingle(jsonObjectTdictionary, type, SystemConstant.valueOf(type).typename());
    			} else {
    				insertSingle(JSONObject.fromObject(jsonObjectTdictionary), type, SystemConstant.valueOf(type).typename());
    			}
    		} else { //批量插入

    			JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("data"));
    			for (int i = 0; i < jsonArray.size(); i++) {
    				JSONObject tempJsonObject = (JSONObject) jsonArray.get(i);
    				if (StringUtils.isNotBlank(tempJsonObject.getString("dictid"))) {
    					updateTdictionarySingle(tempJsonObject, type, SystemConstant.valueOf(type).typename());
    				} else {
    					insertSingle(tempJsonObject, type, SystemConstant.valueOf(type).typename());
    				}
    			}
    		}

	}

	private void insertSingle(JSONObject jsonObject, String type, String typeName) {
		TbDataDictionary tbDataDictionary = new TbDataDictionary();

		tbDataDictionary.setType(type);
		tbDataDictionary.setTypename(typeName);
		tbDataDictionary.setId(UUID.randomUUID() + "");

		initBeen(jsonObject, tbDataDictionary);

		tbDataDictionaryDao.insert(tbDataDictionary);
	}

	private void initBeen(JSONObject jsonObject, TbDataDictionary tbDataDictionary) {
		if (jsonObject.get("itemid") != null)
			tbDataDictionary.setItemid(jsonObject.getString("itemid"));

		if (jsonObject.get("member_price") != null)
			tbDataDictionary.setMemberprice(jsonObject.getString("member_price"));

			tbDataDictionary.setStatus(1);

		if (jsonObject.get("item_desc") != null){
			tbDataDictionary.setItemDesc(jsonObject.getString("item_desc"));
			if(jsonObject.get("item_desc").equals("全天")){
				tbDataDictionary.setItemid("2");
			}
            if(jsonObject.get("item_desc").equals("午市")){
            	tbDataDictionary.setItemid("0");
			}
            if(jsonObject.get("item_desc").equals("晚市")){
            	tbDataDictionary.setItemid("1");
			}
		}
			
		if (jsonObject.get("begin_time") != null)
			tbDataDictionary.setBegintime(jsonObject.getString("begin_time"));

		if (jsonObject.get("end_time") != null)
			tbDataDictionary.setEndtime(jsonObject.getString("end_time"));

		if (jsonObject.get("chargesstatus") != null)
			tbDataDictionary.setChargesstatus(jsonObject.getString("chargesstatus"));


		if (jsonObject.get("price") != null)
			tbDataDictionary.setPrice(jsonObject.getString("price"));

		if (jsonObject.get("itemSort") != null)
			tbDataDictionary.setItemSort(Integer.parseInt(jsonObject.getString("itemSort")));

		if (jsonObject.get("status") != null)
			tbDataDictionary.setStatus(Integer.parseInt(jsonObject.getString("status")));
		if (jsonObject.get("date_type") != null)
			tbDataDictionary.setDatetype(jsonObject.getString("date_type"));
	}


	/**
	 * 获取列表
	 *
	 * @return
	 */
	public JSONObject getTdictionaryList() {

		List<Map<String, Object>> mapList = tbDataDictionaryDao.getSystemList();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("JI_KOU_SPECIAL", getJsonArrary(mapList, "JI_KOU_SPECIAL"));
		jsonObject.put("BIZPERIODDATE", getJsonArrary(mapList, "BIZPERIODDATE"));
		jsonObject.put("DISHES", getJsonArrary(mapList, "DISHES"));
		jsonObject.put("RETURNDISH", getJsonArrary(mapList, "RETURNDISH"));
		jsonObject.put("PASSWORD", getJsonArrary(mapList, "PASSWORD"));
		jsonObject.put("CALLTYPE", getJsonArrary(mapList, "CALLTYPE"));
		jsonObject.put("ROUNDING", getJsonArrary(mapList, "ROUNDING"));
		jsonObject.put("ACCURACY", getJsonArrary(mapList, "ACCURACY"));
		jsonObject.put("PADIMG", getJsonArrary(mapList, "PADIMG"));
		jsonObject.put("ONEPAGETYPE", getJsonArrary(mapList, "ONEPAGETYPE"));
		return jsonObject;
	}


	/**
	 * 列表数据比对
	 *
	 * @param mapList
	 * @param type
	 * @return
	 */
	private JSONArray getJsonArrary(List<Map<String, Object>> mapList, String type) {
		JSONArray jsonArray = new JSONArray();
		if(type.equals("PASSWORD")){
			for (Map<String, Object> m : mapList) {
				if (StringUtils.equals(m.get("id").toString(), "SECRETKEY")) {
					JSONObject jsonObject = new JSONObject();
					getjsonContent(m, jsonObject);
					jsonArray.add(jsonObject);
				}
			}
		}else{
			for (Map<String, Object> m : mapList) {
				if (StringUtils.equals(m.get("type").toString(), type)) {
					JSONObject jsonObject = new JSONObject();
					getjsonContent(m, jsonObject);
					jsonArray.add(jsonObject);
				}
			}
		}
		return jsonArray;
	}

	/**
	 * 将列表返回的数据赋值
	 *
	 * @param m
	 * @param jsonObject
	 */
	private void getjsonContent(Map<String, Object> m, JSONObject jsonObject) {
		jsonObject.put("dictid", m.get("id") == null ? "" : m.get("id").toString());
		jsonObject.put("itemid", m.get("itemid") == null ? "" : m.get("itemid").toString());
		jsonObject.put("itemDesc", m.get("itemDesc") == null ? "" : m.get("itemDesc").toString());
		jsonObject.put("itemSort", m.get("itemSort") == null ? "" : m.get("itemSort").toString());
		jsonObject.put("status", m.get("status") == null ? "" : m.get("status").toString());
		jsonObject.put("type", m.get("type") == null ? "" : m.get("type").toString());
		jsonObject.put("typename", m.get("typename") == null ? "" : m.get("typename").toString());
		jsonObject.put("begin_time", m.get("begintime") == null ? "" : m.get("begintime").toString());
		jsonObject.put("end_time", m.get("endtime") == null ? "" : m.get("endtime").toString());
		jsonObject.put("charges_status", m.get("chargesstatus") == null ? "" : m.get("chargesstatus").toString());
//		jsonObject.put("charges_status", m.get("charges_status") == null ? "" : m.get("charges_status").toString());
		jsonObject.put("member_price", m.get("memberprice") == null ? "" : m.get("memberprice").toString());
//		jsonObject.put("member_price", m.get("member_price") == null ? "" : m.get("member_price").toString());
		jsonObject.put("price", m.get("price") == null ? "" : m.get("price").toString());
		jsonObject.put("date_type", m.get("datetype") == null ? "" : m.get("datetype").toString());
		jsonObject.put("item_value", m.get("itemValue") == null ? "" : m.get("itemValue").toString());
	}

	
	/**
	 * 获取某个分类下的所有字典数据
	 * 
	 * @param type 分类
	 * @return
	 */
	public JSONArray getDicListByType(String type){
		logger.info("start getDicListByType for type : {} ",type);
		JSONArray jsonArray = new JSONArray();
		List<Map<String, Object>> infoList = tbDataDictionaryDao.getDicListByType(type);
		
	    if(infoList==null||infoList.size()<=0){
	    	logger.info("the dic data is null for type : {}",type);
	    	return jsonArray;
	    }
		for (Map<String, Object> info : infoList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("dictid", info.containsKey("id")?info.get("id"):"");
			jsonObject.put("itemid", info.containsKey("itemid")?info.get("itemid"):"");
			jsonObject.put("itemDesc", info.containsKey("itemDesc")?info.get("itemDesc"):"");
			jsonObject.put("itemSort", info.containsKey("itemSort")?info.get("itemSort"):"");
			jsonObject.put("type", info.containsKey("type")?info.get("type"):"");
			jsonObject.put("typename", info.containsKey("typename")?info.get("typename"):"");
			jsonArray.add(jsonObject);
		}
		logger.info("end getDicListByType for type : {} and get data {} ",type,jsonArray);
		return jsonArray;
	}
	
	/**
	 * 获取padLogo图和背景图
	 * @return
	 */
	public List<Map<String, Object>> getImgByType(String type){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> maps = tbDataDictionaryDao.getDicListByType(type);
		if(maps!=null && maps.size()>0){
			//找出最新图的索引
			int logoMaxIndex = 0;
			int  backgroudMaxIndex = 0;
			for(Map<String, Object> map:maps){
				String itemid=getValue(map, "itemid");
				int sort = map.get("chargesstatus") == null ? 0 : Integer.parseInt((String) map.get("chargesstatus"));
				
				if("1".equals(itemid) && sort > logoMaxIndex){//logo图片
					logoMaxIndex = sort;
				}else if ("2".equals(itemid) && sort > backgroudMaxIndex) {//背景图片
					backgroudMaxIndex = sort;
				}
			}
			//只取有效的返回给PAD和后台页面
			for (Map<String, Object> map : maps) {
				String itemid=getValue(map, "itemid");
				int sort = map.get("chargesstatus") == null ? 0 : Integer.parseInt((String) map.get("chargesstatus"));
				if("1".equals(itemid) && sort == logoMaxIndex){//logo图片
					list.add(map);
				}else if ("2".equals(itemid) && sort == backgroudMaxIndex) {//背景图片
					list.add(map);
				}
			}
		}
		return list;
	}

	private String getValue(Map<String, Object> map,String key){
		if(map==null){return "";}
		return map.get(key).toString();
	}
	
	/**
	 * 修改字典表数据
	 * 
	 * @param  updataList  需要修改的数据
	 * 
	 * @return
	 */
	public int updataDicData(List<Map<String,Object>> updataList){
		logger.info("update dic data for updataList : {} ",updataList);
		int num = 0;
		if(updataList==null||updataList.size()<=0){
			logger.error("to change the dic data ,but the target is null");
		}
		logger.info("iterator the datalist to change the data for datasource");
		for(Map<String,Object> infoMap : updataList){
			logger.info("change dic data for param : {} ",infoMap);
			if(!infoMap.containsKey("dictid")||!infoMap.containsKey("itemDesc")){
				logger.error("the data is worng : {} to change dic data ",infoMap);
				continue;
			}
			if(tbDataDictionaryDao.updataDicItemDesc(infoMap)>0){
				num++;
			}
		}
		
		return num;
	}

	
	/**
	 * 修改呼叫服务员时间设置
	 * 
	 * @param  updataList  需要修改的数据
	 * 
	 * @return
	 */
	public int updataTimeset(List<Map<String,Object>> updataList){
		logger.info("update dic data for updataList : {} ",updataList);
		int num = 0;
		if(updataList==null||updataList.size()<=0){
			logger.error("to change the dic data ,but the target is null");
		}
		logger.info("iterator the datalist to change the data for datasource");
		for(Map<String,Object> infoMap : updataList){
			logger.info("change dic data for param : {} ",infoMap);
			if(!infoMap.containsKey("dictid")||!infoMap.containsKey("itemDesc")){
				logger.error("the data is worng : {} to change dic data ",infoMap);
				continue;
			}
			if(tbDataDictionaryDao.updataCallTimeSet(infoMap)>0){
				num++;
			}
		}
		
		return num;
	}

	
	/**
	 * 获取某个分类下的所有字典数据
	 * 
	 * @param type 分类
	 * @return
	 */
	public List<Map<String,String>> getDicListByTypeForClient(String type){
		logger.info("start getDicListByTypeForClient for type : {} ",type);
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		List<Map<String, Object>> infoList = tbDataDictionaryDao.getDicListByType(type);
		
	    if(infoList==null||infoList.size()<=0){
	    	logger.info("the dic data is null for type : {}",type);
	    	return returnList;
	    }
		for (Map<String, Object> info : infoList) {
			Map<String,String> infomap = new HashMap<String,String>();
			infomap.put("complaintName", info.containsKey("itemDesc")?info.get("itemDesc").toString():"");
			infomap.put("complaintType", info.containsKey("itemSort")?info.get("itemSort").toString():"");
			returnList.add(infomap);
		}
		logger.info("end getDicListByTypeForClient for type : {} and get data {} ",type,returnList);
		return returnList;
	}
	
	/**
	 * 获取某个分类下的所有字典数据
	 * 
	 * @param type 分类
	 * @return
	 */
	public List<Map<String,String>> getTimeValueByTypeForClient(String type){
		logger.info("start getTimeValueByTypeForClient for type : {} ",type);
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		List<Map<String, Object>> infoList = tbDataDictionaryDao.getDicListByType(type);
		
	    if(infoList==null||infoList.size()<=0){
	    	logger.info("the dic data is null for type : {}",type);
	    	return returnList;
	    }
		for (Map<String, Object> info : infoList) {
			Map<String,String> infomap = new HashMap<String,String>();
			infomap.put("itemid", info.containsKey("itemid")?info.get("itemid").toString():"");
			infomap.put("itemDesc", info.containsKey("itemDesc")?info.get("itemDesc").toString():"");
			infomap.put("type", "RESPONSETIME");
			infomap.put("item_value", info.containsKey("itemValue")?info.get("itemValue").toString():"");
			infomap.put("dictid", info.containsKey("id")?info.get("id").toString():"");
			returnList.add(infomap);
		}
		logger.info("end getTimeValueByTypeForClient for type : {} and get data {} ",type,returnList);
		return returnList;
	}
	/**
	 * 获取无视 ，晚市时间范围
	 * 
	 * @param type 分类
	 * @return
	 */
	public String getBusinessTime(String targetid){
		logger.debug("start getBusinessTime for type : {} ",targetid);
		List<Map<String, Object>> infoList = tbDataDictionaryDao.getDicListByType("BIZPERIODDATE");
		
	    if(infoList==null||infoList.size()<=0){
	    	logger.debug("the dic data is null for type : BIZPERIODDATE");
	    	return "";
	    }
	    String time = "";
		for (Map<String, Object> info : infoList) {
			String itemid = info.containsKey("itemDesc")?info.get("itemDesc").toString():"";
			if(!itemid.equals(targetid)){
				continue;
			}
			if(targetid.equals("0")){//午市
				time = info.containsKey("end_time")?info.get("end_time").toString():"";
			}else if(targetid.equals("1")){//晚市
				time = info.containsKey("begin_time")?info.get("begin_time").toString():"";
			}
		}
		return time;
	}
}
