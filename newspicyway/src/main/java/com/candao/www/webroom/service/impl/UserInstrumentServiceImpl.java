package com.candao.www.webroom.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.IdentifierUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbUserInstrumentDao;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.model.TbMessageInstrument;
import com.candao.www.data.model.TbUserInstrument;
import com.candao.www.data.model.Torder;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.webroom.service.MessageInstrumentService;
import com.candao.www.webroom.service.TableService;
import com.candao.www.webroom.service.UserInstrumentService;
@Service
public class UserInstrumentServiceImpl implements UserInstrumentService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserInstrumentServiceImpl.class);
	
	@Autowired
    private TbUserInstrumentDao tbUserInstrumentDao;
	@Autowired
	TableService  tableService;
	@Autowired
	TorderMapper  torderMapper;
	@Autowired
	private MessageInstrumentService messageInstrumentService;
	@Autowired
	private MsgForwardService msgForwardService;
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
				StringBuilder message=new StringBuilder();
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
				String areaname =String.valueOf(tableList.get(0).get("areaname"));
				message.append(userid+"|"+String.valueOf(params.get("msg_type"))+"|"+areaname+"|"+(String) params.get("tableno")+"|"+tbMessageInstrumentid);
				msgForwardService.broadCastMsg4Netty(Constant.MessageType.msg_2001,10*60,message.toString());
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
	public List<TbUserInstrument> findByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tbUserInstrumentDao.findByParams(params);
	}

}
