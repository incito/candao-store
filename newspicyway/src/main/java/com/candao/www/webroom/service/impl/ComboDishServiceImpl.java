package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.dao.ComboDishDao;
import com.candao.www.data.dao.TdishUnitDao;
import com.candao.www.data.model.TcomboDishGroup;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TdishUnit;
import com.candao.www.data.model.TgroupDetail;
import com.candao.www.webroom.model.TcomboDishGroupList;
import com.candao.www.webroom.service.ComboDishService;
import com.candao.www.webroom.service.DishService;
@Service
public class ComboDishServiceImpl implements ComboDishService {
	@Autowired
	private ComboDishDao comboDishDao;
	
	@Autowired
	private DishService dishService;
	@Autowired
	private TdishUnitDao tdishUnitDao;
	@Override
	public List<Map<String,Object>> getTdishGroupList(String dishid) {
		// TODO Auto-generated method stub
		return comboDishDao.getTdishGroupList(dishid);
	}

	@Override
	public List<Map<String,Object>> getTgroupDetailList(String tdishGroupid) {
		// TODO Auto-generated method stub
		return comboDishDao.getTgroupDetailList(tdishGroupid);
	}

	/**
	 * 保存套餐的明细
	 * TcomboDishGroup 保存套餐分类信息
	 * TgroupDetail 保存鱼锅信息
	 */
	@Override
	@Transactional
	public void save(TcomboDishGroupList tcomboDishGroupList){
		
	    Tdish tdish = tcomboDishGroupList.getDish();
	    String dishId ="";
	    if(ValidateUtils.isEmpty(tdish.getDishid())){
	    	dishId = IdentifierUtils.getId().generate().toString();
	    	tdish.setDishid(dishId);
			tdish.setIsdisplay(1);
			tdish.setIsselect(1);
			tdish.setStatus(1);
			tdish.setCreatetime(new Date());
			if("0".equals(tdish.getDishno())){
				tdish.setDishno("dishSetNo");
			}else if("1".equals(tdish.getDishno())){
				tdish.setDishno("fishPotNo");
			}
			if(tdish.getDishtype()==1){
				tdish.setUnit("份");
			}else{
				tdish.setUnit("套");
			}
			
			dishService.save(tdish);
	    }else{
	    	dishId=tdish.getDishid();
	    	dishService.update(tdish);
	    	comboDishDao.deleteGroup(tdish.getDishid());
			comboDishDao.deleteGroupDetail(tdish.getDishid());
	    }
	    saveComboDishUnit(tdish);
		comonSaveGroup(tcomboDishGroupList,dishId);
	}
	private void saveComboDishUnit(Tdish tdish){
		List<TdishUnit> tdu=new ArrayList<TdishUnit>();
		TdishUnit du=new TdishUnit();
		du.setId(IdentifierUtils.getId().generate().toString());
		du.setDishid(tdish.getDishid());
		if(tdish.getDishtype()==1){
			du.setUnit("份");
		}else{
			du.setUnit("套");
		}
		du.setPrice(new BigDecimal(tdish.getPrice()==null||tdish.getPrice()==""?"0":tdish.getPrice()));
		
		du.setVipprice(new BigDecimal((String)(tdish.getVipprice()==null||tdish.getVipprice()==""?"0":tdish.getVipprice())));
//		tbTable.setFixprice( new BigDecimal((String) (fixprice== "" ? "0":fixprice)));
		tdu.add(du);
		if(!"".equals(tdish.getDishid())||tdish.getDishid()!=null){
			 tdishUnitDao.delDishUnit(tdish.getDishid());
		 }
		tdishUnitDao.addDishUnit(tdu);
	}

	private void comonSaveGroup(TcomboDishGroupList tcomboDishGroupList,String dishId) {
		List<TcomboDishGroup> groupList = tcomboDishGroupList.getGroupList();
		if(groupList != null ){
			 int orderSeq = 1;
			for (TcomboDishGroup group : groupList) {
				String id=IdentifierUtils.getId().generate().toString();
				group.setId(id);
				group.setDishid(dishId);
				group.setOrdernum(orderSeq);
				comboDishDao.saveGroup(group);
				orderSeq ++ ;
				List<TgroupDetail> detailList = group.getGroupDetailList();
				if(detailList != null){
					for(TgroupDetail detail : detailList){
						String detailid=IdentifierUtils.getId().generate().toString();
						detail.setId(detailid);
						detail.setGroupid(id);
						detail.setDishid(dishId);
						comboDishDao.saveGroupDetail(detail);
						if(detail != null&&detail.getDishtype()==1&&detail.getList()!=null&&detail.getList().size()>0){
							for(TgroupDetail detailFish : detail.getList()){
								detailFish.setId(IdentifierUtils.getId().generate().toString());
								detailFish.setGroupid(detail.getId());
								detailFish.setDishid(dishId);
								comboDishDao.saveGroupDetail(detailFish);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 修改套餐的明细
	 * TcomboDishGroup 保存套餐分类信息
	 * TgroupDetail 保存鱼锅信息
	 */
	@Override
	@Transactional
	public void update(TcomboDishGroupList tcomboDishGroupList){
		
	    Tdish tdish = tcomboDishGroupList.getDish();
		dishService.save(tdish);
		comboDishDao.deleteGroup(tdish.getDishid());
		comboDishDao.deleteGroupDetail(tdish.getDishid());
		
		comonSaveGroup(tcomboDishGroupList,tdish.getDishid());
	}
	
	@Override
	public String getComboDishJson(Map<String, Object> parame) {
		String dishid=parame.get("dishid").toString();
		List<Map<String,Object>> tdishGroupList=comboDishDao.getTdishGroupList(dishid);
		List<Map<String,Object>> listall=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> suretgroupDetailList=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> GroupMap :tdishGroupList){//分组的list
			String groupid=GroupMap.get("id").toString();
			List<Map<String,Object>> tgroupDetailList=comboDishDao.getTgroupDetailList(groupid);
			List<Map<String,Object>> seltgroupDetailList=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> DetailMap:tgroupDetailList){//各组下面的数据
				if("1".equals(DetailMap.get("dishtype").toString())){//鱼锅查询所属的鱼和锅
					List<Map<String,Object>> tgroupDetailList2=comboDishDao.getTgroupDetailList(DetailMap.get("id").toString());
					DetailMap.put("dishes", tgroupDetailList2);
				}else{
					DetailMap.put("dishes", "");
				}
				if("1".equals(DetailMap.get("status").toString())){//可选项
					seltgroupDetailList.add(DetailMap);
				}else{//必选项
					suretgroupDetailList.add(DetailMap);
				}
			}
			GroupMap.put("alldishes", seltgroupDetailList);
			if(seltgroupDetailList!=null&&seltgroupDetailList.size()>0){
				listall.add(GroupMap);
			}
		}
		Map<String,Object> totalMap=new HashMap<String, Object>();
		totalMap.put("combo", listall);
		totalMap.put("only", suretgroupDetailList);
		return JacksonJsonMapper.objectToJson(totalMap);
	}

	@Override
	public List<Map<String, Object>> getFishPotDetailList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		 return comboDishDao.getFishPotDetailList(map);
	}

	@Override
	public List<Map<String, Object>> ifDishesDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return comboDishDao.ifDishesDetail(map);
	}

}
