package com.candao.www.security.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.data.model.User;
import com.candao.www.security.service.ShopMgService;
import com.candao.www.utils.PingyingUtil;
import com.candao.www.utils.SessionUtils;

@Service
public class ShopMgServiceImpl implements ShopMgService {

  @Autowired
  private TbBranchDao tbBranchDao;
  
  @Override
  public int addShop(HashMap<String, Object> branchMap) {
    branchMap.put("branchid", new Double(Math.random()*1000000).intValue());
    branchMap.put("tenantid", new Integer(11));
    // 店名拼音添加
    branchMap.put("branchnamepingying", PingyingUtil.getStringPinYin(branchMap.get("branchname")+""));
   // 店名拼音首字母添加
    branchMap.put("branchnamepyfirstchar", PingyingUtil.getPinYinHeadChar(branchMap.get("branchname")+""));
    
    User user = SessionUtils.getCurrentUser();
    if(user != null){
    	branchMap.put("tenantid", user.getTenantid());
    }
    return tbBranchDao.insert(branchMap);
  }
  
  @Override   
  public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
    return tbBranchDao.page( params, current, pagesize);
  } 

  @Override
  public int update(HashMap<String, Object> tbBranchMap) {
    this.handlerCity(tbBranchMap);
    // 店名拼音添加
    tbBranchMap.put("branchnamepingying", PingyingUtil.getStringPinYin(tbBranchMap.get("branchname")+""));
   // 店名拼音首字母添加
    tbBranchMap.put("branchnamepyfirstchar", PingyingUtil.getPinYinHeadChar(tbBranchMap.get("branchname")+""));
  
    return tbBranchDao.update(tbBranchMap);
  }

  /**
   * 对特别行政区和直辖市进行特别处理。
   * @param param
   */
  public static void handlerCity(Map<String,Object> param){
    String city = param.get("city")!=null ?param.get("city")+"":"";
    if(city.equals("重庆") || city.equals("天津") || city.equals("上海") || city.equals("北京")){//直辖市
      param.put("province", null);//省清空。
    }else if(city.equals("香港") || city.equals("澳门") || city.equals("台湾") ){//特别行政区
      param.put("province", null);// 省清空
      param.put("region", null);// 区、县清空
    }
    if(param.get("region") !=null && param.get("region").equals("")){param.put("region", null);}
  }
  @Override
  public int update_del(HashMap<String, Object> tbBranchMap) {
    // TODO Auto-generated method stub
    return tbBranchDao.update_del(tbBranchMap);
  }

  @Override
  public HashMap<String, Object> getOne(String id) {
    // TODO Auto-generated method stub (^i^)
    HashMap<String, Object> tbBranchMap = new HashMap<String, Object> ();
    tbBranchMap.put("branchid", id);
    return  tbBranchDao.getOne(tbBranchMap);
  }

	@Override
	public List<HashMap<String, Object>> getAll() {
	    return  tbBranchDao.getAll();
	}

  @Override
  public int getCountByNameAddress(HashMap<String, Object> queryParam) {
    // TODO Auto-generated method stub
    return tbBranchDao.getCountByNameAddress(queryParam) ;
  }
  
  @Override
  public Map<String ,Object> getBranchInfo(){
	  return tbBranchDao.getBranchInfo();
  }

@Override
public List<HashMap<String, Object>> findByParams(Map<String, Object> params) {
	// TODO Auto-generated method stub
	return tbBranchDao.findByParams(params);
}
}
