package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.PadConfigDao;
import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.webroom.model.PadConfig;
import com.candao.www.webroom.service.PadConfigService;

@Service
public class PadConfigServiceImpl  implements PadConfigService{

	
	@Autowired
	private PadConfigDao     padConfigDao;
	@Autowired
	private TbDataDictionaryDao tbDataDictionaryDao;

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
		if(padConfig.getSeatimageurls()!=null && padConfig.getSeatimagenames()!=null){
			padConfig.setSeatImagename(padConfig.getSeatimagenames().split(";"));
			padConfig.setSeatImagefileurls(padConfig.getSeatimageurls().split(";"));
		}
		
		return padConfig;
	}   
	
	
	private String getValue(Map<String, Object> map,String key){
		if(map==null){return "";}
		return map.get(key).toString();
	}
}
