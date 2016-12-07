package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TServiceChargeDao;
import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.data.model.TServiceCharge;
import com.candao.www.preferential.calcpre.StrategyFactory;
import com.candao.www.webroom.service.TServiceChargeService;

@Service
public class TServiceChargeServiceImpl implements TServiceChargeService {
	private Logger logger = LoggerFactory.getLogger(TServiceChargeServiceImpl.class);

	@Autowired
	private TServiceChargeDao serviceChargeDao;

	@Autowired
	TbDataDictionaryDao dictionaryDao;
	@Override
	public int updateChargeInfo(TServiceCharge chargeInfo) {
		return serviceChargeDao.updateChargeInfo(chargeInfo);
	}
	
	@Override
	public int changChargeInfo(TServiceCharge chargeInfo) {
		// TODO Auto-generated method stub
		return serviceChargeDao.changChargeInfo(chargeInfo);
	}
	@Override
	public int insertChargeInfo(TServiceCharge charge) {
		return serviceChargeDao.insertChargeInfo(charge);
	}
	@Override
	public TServiceCharge getChargeInfo(Map<String, Object> params) {
		return serviceChargeDao.getChargeInfo(params);
	}
	@Override
	public int delChargeInfo(Map<String, Object> params) {
		return serviceChargeDao.delChargeInfo(params);
	}
	@Override
	public TServiceCharge serviceCharge(String orderid, Map<String, Object> userOrderInfo, BigDecimal payDecimal,
			BigDecimal MenuDecimal) {
		Map<String, Object> serParams = new HashMap<>();
		serParams.put("orderId", orderid);

		BigDecimal calcServiceCharge = null;
		int chargeOn = userOrderInfo.get("chargeOn") == null ? 0 : (int) userOrderInfo.get("chargeOn");
		int chargeType = userOrderInfo.get("chargeType") == null ? 0 : (int) userOrderInfo.get("chargeType");
		int chargeRateRule = userOrderInfo.get("chargeRateRule") == null ? 0
				: (int) userOrderInfo.get("chargeRateRule");
		int chargeRate = userOrderInfo.get("chargeRate") == null ? 0 : (int) userOrderInfo.get("chargeRate");
		String chargeTime = userOrderInfo.get("chargeTime") == null ? "" : (String) userOrderInfo.get("chargeTime");
		TServiceCharge servceCharageBean = null;
		if (chargeOn != 0) {
			servceCharageBean = serviceChargeDao.getChargeInfo(serParams);
			if (servceCharageBean == null || (servceCharageBean != null && servceCharageBean.getIsCustom() == 0
					&& servceCharageBean.getChargeOn() == 1)) {
				Map<String, Object> params = new HashMap<>();
				params.put("itemid", chargeType);
				params.put("type", "TABLECHARGE");
				List<Object> dataDictionary = dictionaryDao.find(params);
				if (!dataDictionary.isEmpty()) {
					// 0比例 1 固定 2 时长
					calcServiceCharge = StrategyFactory.INSTANCE.calcServiceCharge(userOrderInfo, payDecimal,
							MenuDecimal);
				}
			} else if (servceCharageBean.getIsCustom() == 1 ) {
				calcServiceCharge = servceCharageBean.getChargeAmount();
			}else if(servceCharageBean.getIsCustom()==0&&servceCharageBean.getChargeOn()==0){
				calcServiceCharge= servceCharageBean.getChargeAmount();
			}
			
			if (servceCharageBean == null) {
				servceCharageBean = new TServiceCharge(orderid, chargeOn, chargeType, chargeRateRule, chargeRate,
						chargeTime);
				servceCharageBean.setChargeAmount(calcServiceCharge);
				servceCharageBean.setAutho("");
				long id = serviceChargeDao.insertChargeInfo(servceCharageBean);
				servceCharageBean.setId(id);
			} else {
				servceCharageBean.setChargeType(chargeType);
				servceCharageBean.setChargeRateRule(chargeRateRule);
				servceCharageBean.setChargeRate(chargeRate);
				servceCharageBean.setChargeTime(chargeTime);
				servceCharageBean.setChargeAmount(calcServiceCharge);
				try {
					serviceChargeDao.updateChargeInfo(servceCharageBean);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("计算服务费失败======》",e.fillInStackTrace());
				}

			}

		}
		//设置预打印的服务费
		if(servceCharageBean!=null&&servceCharageBean.getChargeOn()==0){
			servceCharageBean.setReserveChargeAmout(new BigDecimal("0"));
		}else if(servceCharageBean!=null&&servceCharageBean.getChargeOn()==1){
			servceCharageBean.setReserveChargeAmout(servceCharageBean.getChargeAmount());
		}
		return servceCharageBean;
	}
	

}
