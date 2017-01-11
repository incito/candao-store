package com.candao.www.dataserver.service.member.impl;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.candao.www.constant.Constant;
import com.candao.www.dataserver.service.member.MemberHandler;
import com.candao.www.utils.HttpUtil;
import com.candao.www.webroom.model.MemberTransModel;

@Component("candaoMemberHandler")
public class CandaoMemberHandler extends MemberHandler {

	@Override
	protected Result<MemberTransModel> StoreCardDeposit(String param) {
		String result = HttpUtil.doRestfulByHttpConnection(padConfig.getVipcandaourl() + "/newspicyway/deal/StoreCardToNewPos.json",
				param);
		JSONObject resultObject = JSON.parseObject(result);
		if ("0".equals(resultObject.get("Retcode"))) {
			JSONObject paramObject = JSON.parseObject(param);
			MemberTransModel memberModel = new MemberTransModel();
			memberModel.setSystem(Constant.MEMBER_SYSYTEM.CANDAO);
			memberModel.setCtime(new Date());
			memberModel.setOperatorId(paramObject.getString("user_id"));
			memberModel.setOperateType(Constant.MEMBER_OPER.CHUZHI);
			memberModel.setRealAmount(paramObject.getDoubleValue("Amount"));
			memberModel.setAddedAmount(paramObject.getDoubleValue("giveValue"));
			memberModel.setPayway(paramObject.getShortValue("ChargeType"));
			memberModel.setSerial(paramObject.getString("Serial"));
			memberModel.setCardno(paramObject.getString("cardno"));
			return new Result<MemberTransModel>(true, result, memberModel);
		}
		return new Result<>(false, result);
	}

	@Override
	public String errorResult(String param, String errMsg) {
		// TODO Auto-generated method stub
		return null;
	}
}
