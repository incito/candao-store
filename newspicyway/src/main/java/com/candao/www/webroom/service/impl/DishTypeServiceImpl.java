package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.data.dao.TbasicDataDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TmenuDao;
import com.candao.www.data.dao.TtemplateDetailDao;
import com.candao.www.data.dao.TtemplateDishUnitlDao;
import com.candao.www.data.model.TbasicData;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TtemplateDetail;
import com.candao.www.data.model.TtemplateDishUnit;
import com.candao.www.webroom.service.DishTypeService;
@Service
public class DishTypeServiceImpl implements DishTypeService {
	@Autowired
	private TbasicDataDao tbasicDataDao;
	@Autowired
	private TtemplateDetailDao ttemplateDetailDao;
	@Autowired
	private TtemplateDishUnitlDao ttemplateDishUnitlDao;
	@Autowired
	private TdishDao tdishDao;
	@Autowired
	private TbBranchDao tbBranchDao;
	@Autowired
	private TmenuDao tmenuDao;

	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params,
			int current, int pagesize,String itemtype) {
		// TODO Auto-generated method stub
		params.put("itemtype", itemtype);
		return tbasicDataDao.page(params, current, pagesize);
	}
	@Override
	public boolean save(TbasicData tbasicData) {
		return tbasicDataDao.insert(tbasicData)>0;
	}
	@Override
	public TbasicData findById(String id) {
		return tbasicDataDao.get(id);
	}
	@Override
	public boolean update(TbasicData tbasicData) {
		return tbasicDataDao.update(tbasicData)>0;
	}
	@Override
	public boolean deleteById(String id) {
		return tbasicDataDao.delete(id)>0;
	}
	@Override
	public List<Map<String, Object>> getDataDictionaryTag(String itemtype) {
		return tbasicDataDao.getDataDictionaryTag( itemtype);
	}
	@Override
	public List<Map<String,Object>> findAll(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id",  id);
		return tbasicDataDao.find( params);
	}
	
	@Override
	public List<Map<String,Object>> findAllCategory(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id",  id);
		return tbasicDataDao.findCategory( params);
	}
	
	@Override
	public List<Map<String, Object>> getAllDishAndDishType(String id) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id",  id);
		
		List<Map<String, Object>> list = tbasicDataDao.findAll( params);
		List<Map<String, Object>> listRet = new ArrayList<Map<String,Object>>();
  
		for(Map<String, Object> map : list){
			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("type", map.get("id"));
			mapRet.put("typeDesc", map.get("itemdesc"));
		 
			mapRet.put("templateType", String.valueOf(map.get("type")));
			String json=packData(String.valueOf(map.get("templateid")));
			mapRet.put( "datas",json );
			if(json!=null&&!"".equals(json)&&!"{}".equals(json)){
				listRet.add(mapRet);
			}
		}
		return listRet;
	}
	/**
	 * 组装老模板中data的数据
	 * @return
	 */
	private String packData(String templateid){
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("templateid", templateid);
		Map<String, Object> branchInfoMap = tbBranchDao.getBranchInfo();
		String branchid=null;
		if( branchInfoMap!=null ){
			branchid=(String) branchInfoMap.get("branchid");
		}
		Map<String,Object> paramsmap=new HashMap<String,Object>();
		paramsmap.put("branchid", branchid);
		List<Map<String,Object>> menuList=tmenuDao.findMenuByBrachid(paramsmap);
		Map<String,Object> menu=new HashMap<String,Object>();
		if(menuList!=null&&menuList.size()>0){
			menu=menuList.get(0);
			String menuid=String.valueOf(menu.get("menuid"));
			params.put("menuid", menuid);
		}else{
			params.put("menuid", "0928b3e2-f6ba-4456-bf46-0f8a8056ef97");
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<TtemplateDetail> list=ttemplateDetailDao.getTtemplateDetailByparams(params);
		if(list!=null&&list.size()>0){
			for(TtemplateDetail templateDetail:list){
				Tdish dish=tdishDao.get(templateDetail.getRedishid());
				params.put("dishid", templateDetail.getRedishid());
				List<TtemplateDishUnit> dishunitlist=ttemplateDishUnitlDao.getTtemplateDishUnitByparams(params);
				String dishname="";
				String dishintroduction="";
				String unit="";
				String price="";
				String vipprice="";
				if(dishunitlist!=null&&dishunitlist.size()>0){
					dishname=dishunitlist.get(0).getDishname();
					dishintroduction=dishunitlist.get(0).getDishintroduction();
					for(TtemplateDishUnit templateDishUnit:dishunitlist){
						if(templateDishUnit.getUnit()!=null){
							unit+=templateDishUnit.getUnit()+"/";
						}
						if(templateDishUnit.getPrice()!=null){
							price+=templateDishUnit.getPrice()+"/";
						}
						if(templateDishUnit.getVipprice()!=null){
							vipprice+=templateDishUnit.getVipprice()+"/";
						}
					}
				}
				Map<Object, Object> dataMap2=new HashMap<Object, Object>();
				 dataMap2.put("dishid", dish.getDishid());
				 dataMap2.put("dishname", dishname);
				 dataMap2.put("image", templateDetail.getImage());
				 dataMap2.put("normalprice", getStr(price));
				 dataMap2.put("discountprice", getStr(vipprice));
				 dataMap2.put("couponsprice", getStr(vipprice));
				 dataMap2.put("cookietype", "");
				 dataMap2.put("cusumers", 0);
				 dataMap2.put("unit", getStr(unit));
				 dataMap2.put("introduction", dishintroduction);
				 dataMap2.put("dishtype", dish.getDishtype());
				 dataMap2.put("itemtype", dish.getSource());
				 dataMap2.put("specialflag", dish.getImagetitle());
				 dataMap2.put("unitflag", dish.getHeadsort());
				 dataMap2.put("speciality", dish.getAbbrdesc());
				 dataMap.put(templateDetail.getLocation(), dataMap2);
			}
		}
		return JacksonJsonMapper.objectToJson(dataMap);
	}
	public String getStr(String str){
		if(!str.isEmpty()){
			return str.substring(0, str.length()-1);
		}else{
			return "";
		}
	}
	@Override
	public List<Map<String, Object>> getListByparams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tbasicDataDao.getListByparams( params);
	}
	@Override
	public int updateListOrder(List<TbasicData> tdus) {
		// TODO Auto-generated method stub
		return tbasicDataDao.updateListOrder( tdus);
	}
	@Override
	public int updateDishTagListOrder(List<Map<String, Object>> dishTag) {
		// TODO Auto-generated method stub
		return tbasicDataDao.updateDishTagListOrder( dishTag);
	}

}
