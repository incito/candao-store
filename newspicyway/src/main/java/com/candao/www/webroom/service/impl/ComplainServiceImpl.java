/**
 * 
 */
package com.candao.www.webroom.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.IdentifierUtils;
import com.candao.www.data.dao.ComplainDao;
import com.candao.www.webroom.model.Complain;
import com.candao.www.webroom.service.ComplainService;

import net.sf.json.JSONObject;

/**
 * @author zhao
 *
 */
@Service
public class ComplainServiceImpl implements ComplainService {

	@Autowired
	private ComplainDao complainDao;

	@Override
	public int saveComplain(JSONObject data) {
		int returnnum = -1;
		if(data==null){
			return returnnum;
		}
		if (!data.containsKey("orderid")||data.getString("orderid")==null||data.getString("orderid").equals("")) {
			return returnnum;
		}
		if (!data.containsKey("complaintType")||data.getString("complaintType")==null||data.getString("complaintType").equals("")) {
			return returnnum;
		}
		if (!data.containsKey("complaintOpinion")||data.getString("complaintOpinion")==null||data.getString("complaintOpinion").equals("")) {
			return returnnum;
		}
		if (!data.containsKey("deviceNo")||data.getString("deviceNo")==null||data.getString("deviceNo").equals("")) {
			return returnnum;
		}
		if (!data.containsKey("userid")||data.getString("userid")==null||data.getString("userid").equals("")) {
			return returnnum;
		}
		if (!data.containsKey("deviceType")||data.getString("deviceType")==null||data.getString("deviceType").equals("")) {
			return returnnum;
		}
		String orderid = data.getString("orderid");
		String complaintType = data.getString("complaintType");
		String complaintOpinion = data.getString("complaintOpinion");
		String deviceNo = data.getString("deviceNo");
		String photoUrl = data.containsKey("photoUrl")?data.getString("photoUrl"):"";
		String deviceType = data.containsKey("deviceType")?data.getString("deviceType"):"0";
		String userid = data.getString("userid");
		String messageid= data.getString("messageid");
		Complain complain = new Complain();
		complain.setId(IdentifierUtils.getId().generate().toString());
		complain.setOrderid(orderid);
		complain.setDeviceType(deviceType);
		complain.setDeviceNo(deviceNo);
		complain.setComplainTime(new Date());
		complain.setOpinion(complaintOpinion);
		complain.setOrderid(orderid);
		complain.setPhotoUrl(photoUrl);
		complain.setUserid(userid);
		complain.setMessageid(messageid);
		
		returnnum = complainDao.saveComplain(complain);
		
		if(returnnum>0){
			Map<String,String> param = new HashMap<String,String>(2);
			param.put("complaintId", complain.getId());
			param.put("complaintType", complaintType);
			returnnum = complainDao.saveComplainType(param);
		}
		return returnnum;
	}
}
