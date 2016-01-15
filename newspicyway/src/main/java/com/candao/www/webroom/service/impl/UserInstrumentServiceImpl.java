package com.candao.www.webroom.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.IdentifierUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbUserInstrumentDao;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.model.TbMessageInstrument;
import com.candao.www.data.model.TbUserInstrument;
import com.candao.www.data.model.Torder;
import com.candao.www.utils.TsThread;
import com.candao.www.webroom.service.MessageInstrumentService;
import com.candao.www.webroom.service.TableService;
import com.candao.www.webroom.service.UserInstrumentService;
@Service
public class UserInstrumentServiceImpl implements UserInstrumentService{
	@Autowired
    private TbUserInstrumentDao tbUserInstrumentDao;
	@Autowired
	TableService  tableService;
	@Autowired
	TorderMapper  torderMapper;
	@Autowired
	private MessageInstrumentService messageInstrumentService;
	@Override
	public boolean save(TbUserInstrument tbUserInstrument) {
		return tbUserInstrumentDao.insert(tbUserInstrument)>0;
	}

	@Override
	public boolean update(TbUserInstrument tbUserInstrument) {
		return tbUserInstrumentDao.update(tbUserInstrument)>0;
	}
	
	@Override
	public boolean updateByid(TbUserInstrument tbUserInstrument) {
		return tbUserInstrumentDao.updateByid(tbUserInstrument)>0;
	}

	@Override
	public int insertByParams(Map<String, Object> params) {
		//查询这个桌子的订单号
		params.put("tableNo", params.get("tableno"));
		List<Map<String, Object>> tableList=tableService.find(params);
		if(tableList!=null&&tableList.size()>0){
			String orderid=String.valueOf(tableList.get(0).get("orderid"));
			Torder torder=torderMapper.get(orderid);
			if(torder!=null&&torder.getUserid()!=""){
				String userid=torder.getUserid();
				Map<String,Object> map1=new HashMap<String,Object>();
				map1.put("status", "0");
				map1.put("userid", userid);
				List<TbUserInstrument> listuser=tbUserInstrumentDao.findByParams(map1);
				String tbMessageInstrumentid = IdentifierUtils.getId().generate().toString();
				StringBuilder message=new StringBuilder(Constant.TS_URL+Constant.MessageType.msg_2001+"/");
				if(listuser!=null&&listuser.size()>0){
					//服务员还在线
					//服务员编号|消息类型|区号|台号|消息id
				}else{
					//服务员退出了,找到同一区的服务员 进行推送
					String areaid=String.valueOf(tableList.get(0).get("areaid"));
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("areaid", areaid);
					map.put("status", "1");
					List<Map<String, Object>> retableList=tableService.find(map);
					userid=findrelateUserid(retableList);
				}
				String areaname = null;
				try {
					 areaname = java.net.URLEncoder.encode(String.valueOf(tableList.get(0).get("areaname")),"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				message.append(userid+"|"+String.valueOf(params.get("msg_type"))+"|"+areaname+"|"+(String) params.get("tableno")+"|"+tbMessageInstrumentid);
				new Thread(new TsThread(message.toString())).run();
				TbMessageInstrument tbMessageInstrument = new TbMessageInstrument();
				
				tbMessageInstrument.setId(tbMessageInstrumentid);
				tbMessageInstrument.setMsg_type((String) params.get("msg_type"));
				tbMessageInstrument.setStatus(0);
				tbMessageInstrument.setTableno((String) params.get("tableno"));
				tbMessageInstrument.setUserid(userid);
				messageInstrumentService.save(tbMessageInstrument);
			}else{
				return 0;
			}
		}else{
			return 0;
		}
		return 1;
	}
	/**
	 * 找到关联桌子的服务员
	 * @return
	 */
	@Override
	public String findrelateUserid(List<Map<String, Object>> retableList){
		String  userid="";
		if(retableList!=null&&retableList.size()>0){
			for(Map<String,Object> map:retableList){
				String orderid=String.valueOf(map.get("orderid"));
				Torder torder=torderMapper.get(orderid);
				if(torder!=null&&torder.getUserid()!=""){
					userid=torder.getUserid();
					Map<String,Object> map1=new HashMap<String,Object>();
					map1.put("status", "0");
					map1.put("userid", userid);
					List<TbUserInstrument> listuser=tbUserInstrumentDao.findByParams(map1);
					if(listuser!=null&&listuser.size()>0){
						break;
					}
				}
			}
		}
		
		return userid;
		
	}

	@Override
	public boolean deleteByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, Object>> find(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateStatus(TbUserInstrument tbUserInstrument) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, Object>> findUseridByParams(
			Map<String, Object> params) {
		return tbUserInstrumentDao.findUseridByParams(params);
	}

	@Override
	public List<TbUserInstrument> findByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tbUserInstrumentDao.findByParams(params);
	}

}
