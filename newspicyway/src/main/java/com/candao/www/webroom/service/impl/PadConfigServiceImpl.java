package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.PadConfigDao;
import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.data.model.TbDataDictionary;
import com.candao.www.dataserver.util.IDUtil;
import com.candao.www.webroom.model.PadConfig;
import com.candao.www.webroom.service.PadConfigService;
import com.candao.www.weixin.dao.WeixinDao;

@Service
public class PadConfigServiceImpl  implements PadConfigService{

	
	@Autowired
	private PadConfigDao     padConfigDao;
	@Autowired
	private TbDataDictionaryDao tbDataDictionaryDao;
    @Autowired
	private WeixinDao weixinDao;
	@Autowired
	private SystemServiceImpl systemServiceImpl;

	private final String PADIMG="PADIMG";
	
	private final String FASTDFSURL="fastdfs.url";
	@Override
	public int saveorupdate(PadConfig padConfig) {
		int count =padConfigDao.selectisExsit();
		if(count>0){
			return padConfigDao.update(padConfig);
		}else{
			return padConfigDao.insert(padConfig);
		}
	}
	@Override
	public PadConfig saveorupdateToDic(String iupItemid,String imgName){
		//获取数据库ID
		Map<String, String> idsMap=new HashMap<>();
		//
		PadConfig  padConfig=new PadConfig();
		List<Map<String, Object>>   maps = systemServiceImpl.getImgByType(PADIMG);
		if(maps!=null && maps.size()>0){
			for(Map<String, Object> map:maps){
				String itemid=getValue(map, "itemid");
				String value = getValue(map, "itemValue");
				idsMap.put(itemid, getValue(map, "id"));
				if("1".equals(itemid)){//logo图片
					padConfig.setLogourl(value);
				}else if ("2".equals(itemid)) {//背景图片
					padConfig.setBackgroudurl(value);
				}
			}
		}
		TbDataDictionary dictionary = new TbDataDictionary();
		 dictionary.setId(idsMap.get(iupItemid)==null?IDUtil.getID():idsMap.get(iupItemid));
		 dictionary.setItemid(iupItemid);
		 dictionary.setItemDesc(iupItemid.equals("2")?"pad背景图":"padLOGO图");
		 dictionary.setItemSort(Integer.valueOf(iupItemid));
		 dictionary.setStatus(1);
		 dictionary.setType(PADIMG);
		 dictionary.setTypename("Pad图片");
		 dictionary.setChargesstatus(iupItemid.equals("2")?"5":"4");
		 dictionary.setItemValue(imgName);
		 
		if(iupItemid.equals("1")){
			 if(padConfig.getLogourl()==null){
				 tbDataDictionaryDao.insertPadimg(dictionary);
			 }else{
				 int i=tbDataDictionaryDao.update(dictionary);
				 i++;
			 }
			
		}else {
			 if(padConfig.getBackgroudurl()==null){
				 tbDataDictionaryDao.insertPadimg(dictionary);
			 }else{
				 tbDataDictionaryDao.update(dictionary);
			 }
		}
		
		return  padConfig;
	}

	@Override
	public PadConfig getconfiginfos() {
		PadConfig  padConfig=new PadConfig();
		padConfig=padConfigDao.getconfiginfos();
		List<Map<String, Object>>   maps = systemServiceImpl.getImgByType(PADIMG);
//		String urlprexx=PropertiesUtils.getValue(FASTDFSURL);
		if(maps!=null && maps.size()>0){
			for(Map<String, Object> map:maps){
				String itemid=getValue(map, "itemid");
				String value = getValue(map, "itemValue");
				if("1".equals(itemid)){//logo图片
					padConfig.setLogourl(value);
				}else if ("2".equals(itemid)) {//背景图片
					padConfig.setBackgroudurl(value);
				}
			}
		}
		if(padConfig.getSeatimageurls()!=null && padConfig.getSeatimagenames()!=null&& !"".equals(padConfig.getSeatimageurls())&& !"".equals(padConfig.getSeatimagenames())){
			
			padConfig.setSeatImagename(padConfig.getSeatimagenames().split(";"));
			List<String> imagename=new ArrayList<>();
			Collections.addAll(imagename, padConfig.getSeatimagenames().split(";"));
			padConfig.setImagename(imagename);
			List<String> seatImageurl=new ArrayList<>();
			String urls=padConfig.getSeatimageurls();
			if(urls!=null){
				String[] lss=urls.split(";");
				padConfig.setSeatImagefileurls(lss);
				for(String url :lss){
					seatImageurl.add(url.replaceAll("\\\\", "/"));
				}
			}
			padConfig.setSeatImageurl(seatImageurl);
		}
		
		Map<String, Object>  map=weixinDao.queryWeixinInfoBybranchid(PropertiesUtils.getValue("current_branch_id"));
		if(map!=null){
			Object weixintype=map.get("weixintype");
			if(weixintype!=null){
				padConfig.setWeixintype(Integer.parseInt(weixintype.toString()));
			}
			padConfig.setPersonweixinurl(map.get("personweixinurl")==null?"":map.get("personweixinurl").toString());
			
			//不启用表示没有配置微信支付
			Object weixinstatus=map.get("status");
			if(weixinstatus!=null){
				if("0".equals(weixinstatus.toString())){
					padConfig.setWeixintype(0);//没配置微信
				}
			}else{//为空也表示没有启用微信
				padConfig.setWeixintype(0);//没配置微信
			}
		}else{
			padConfig.setWeixintype(0);//没配置微信
		}
		
		return padConfig;
	}   
	
	
	private String getValue(Map<String, Object> map,String key){
		if(map==null){return "";}
		return map.get(key).toString();
	}
}
