package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.PadConfigDao;
import com.candao.www.data.dao.TbDataDictionaryDao;
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
	public PadConfig getconfiginfos() {
		PadConfig  padConfig=new PadConfig();
		padConfig=padConfigDao.getconfiginfos();
		List<Map<String, Object>>   maps=tbDataDictionaryDao.getDicListByType(PADIMG);
		String urlprexx=PropertiesUtils.getValue(FASTDFSURL);
		if(maps!=null && maps.size()>0){
			for(Map<String, Object> map:maps){
						String itemid=getValue(map, "itemid");
						if("1".equals(itemid)){//logo图片
							padConfig.setLogourl(urlprexx+getValue(map, "itemValue"));
						}else if ("2".equals(itemid)) {//背景图片
							padConfig.setBackgroudurl(urlprexx+getValue(map, "itemValue"));
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
			padConfig.setPersonweixinurl(map.get("personweixinurl").toString());
			
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
