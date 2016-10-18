package com.candao.www.bossstore.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.bossstore.dao.TOrderDao;
@Repository
public class TOrderDaoImpl implements TOrderDao{
	@Autowired
	private DaoSupport daoSupport;
	@Override
	public List<Map<String, Object>> getBusinessData(Map<String, Object> params) {
		params.put("result", "@result");
		return daoSupport.find(PREFIX+".getBusinessData", params);
	}

	@Override
	public List<Map<String, Object>> getpxBusinessData(Map<String, Object> params) {
		params.put("result", "@result");
		return daoSupport.find(PREFIX+".getpxBusinessData", params);
	}

	@Override
	public List<Map<String, Object>> getyhBusinessData(Map<String, Object> params) {
		params.put("result", "@result");
		return daoSupport.find(PREFIX+".getyhBusinessData", params);
	}

	@Override
	public List<Map<String, Object>> gettcBusinessData(Map<String, Object> params) {
		params.put("result", "@result");
		return daoSupport.find(PREFIX+".gettcBusinessData", params);
	}

	@Override
	public List<Map<String, Object>> getFlowData(Map<String, Object> params) {
		params.put("branchId", PropertiesUtils.getValue("current_branch_id"));
		return daoSupport.find(PREFIX+".getFlowData", params);
	}

	@Override
	public List<Object[]> getCurrDayOrders(Map<String, Object> params) {
		List<Object[]> result = new ArrayList<Object[]>();
		Object[] objects = new Object[4];
		List<Map<String,Object>> list = daoSupport.find(PREFIX+".getCurrDayOrders", params);
		for(Map<String,Object> map:list){
			objects[0] = map.get("beginTime");
			objects[1] = map.get("orderId");
			objects[2] = map.get("realPersonNum");
			objects[3] = map.get("tableIds");
			result.add(objects);
		}
		return result;
	}

	@Override
	public Object getShouldAmountByOrderId(Map<String, Object> params) {
		Object object = new Object();
		Map<String,Object> map = daoSupport.findOne(PREFIX+".getShouldAmountByOrderId", params);
		object = map==null?0:map.get("finalPrice");
		return object;
	}

	@Override
	public List<Object[]> getOrderCom(Map<String, Object> params) {
		List<Object[]> result = new ArrayList<Object[]>();
		Object[] objects = new Object[9];
		List<Map<String,Object>> list = daoSupport.find(PREFIX+".getOrderCom", params);
		for(Map<String,Object> map:list){
			objects[0] = map.get("name");
			objects[1] = map.get("responseNums");
			objects[2] = map.get("complaintOpinion");
			objects[3] = map.get("photoUrl");
			objects[4] = map.get("msgType");
			objects[5] = map.get("complaintType");
			objects[6] = map.get("callStartTime");
			objects[7] = map.get("timeoutStartTime");
			objects[8] = map.get("callOverDate");
			result.add(objects);
		}
		return result;
	}

	@Override
	public List<Object[]> getCurrDayOrdersToday(Map<String, Object> params) {
		List<Object[]> result = new ArrayList<Object[]>();
		Object[] objects = new Object[4];
		List<Map<String,Object>> list = daoSupport.find(PREFIX+".getCurrDayOrdersToday", params);
		for(Map<String,Object> map:list){
			objects[0] = map.get("beginTime");
			objects[1] = map.get("orderId");
			objects[2] = map.get("realPersonNum");
			objects[3] = map.get("tableIds");
			result.add(objects);
		}
		return result;
	}

	@Override
	public Object getCouponsTopOne(Map<String, Object> params) {
        Object[] result = new Object[2];
        params.put("branchId", PropertiesUtils.getValue("current_branch_id"));
        params.put("result", "@result");
		List<Map<String, Object>> list = daoSupport.find(PREFIX+".getCouponsTopOne", params);
        Double maxShouldAmount = 0d;
        Double shouldAmount = 0d;
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            shouldAmount = (Double) list.get(i).get("shouldamount");
            flag = shouldAmount > maxShouldAmount;

            if (flag) {
                result[0] = list.get(i).get("pname");
                result[1] = shouldAmount;
                maxShouldAmount = shouldAmount;
            }
            flag = false;

        }
        return result;
	}

}
