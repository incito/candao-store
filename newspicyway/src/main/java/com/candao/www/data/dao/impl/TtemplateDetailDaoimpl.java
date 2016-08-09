package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TtemplateDetailDao;
import com.candao.www.data.model.TtemplateDetail;
@Repository
public class TtemplateDetailDaoimpl implements TtemplateDetailDao {
	@Autowired
	private DaoSupport daoSupport;
	@Override
	public int addTtemplateDetail(List<TtemplateDetail> list) {
		// TODO Auto-generated method stub
		return daoSupport.insert(PREFIX+".insertOnce", list);
	}

	@Override
	public int delTtemplateDetail(String menuid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuid", menuid);
		return daoSupport.delete(PREFIX+".delTtemplateDetail", params);
	}

	@Override
	public <E, K, V> List<E> getTtemplateDetailByparams(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".findTempalteDetailList", params);
	}

	@Override
	public <E, K, V> List<E> getTtemplateDetailByparamsPad(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".findTempalteDetailListPad", params);
	}

	@Override
	public <E, K, V> List<E> getTtemplateDetailByParamsHasRedishid(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".getTtemplateDetailByParamsHasRedishid", params);
	}
}
