package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TDataDetailDao;
import com.candao.www.webroom.service.DataDetailService;

/**
 * 详细数据统计表
 * @author Administrator
 *
 */
@Service
public class DataDetailServiceImpl implements DataDetailService {

	@Autowired
	private TDataDetailDao tdataDetailDao;
	/**
	 * 详细数据统计
	 *  @author zhouyao
	 *  @serialData 2015-07-05
	 */
	public List<Map<String,Object>>  insertDataStatistics(Map<String,Object> params){
		String branchid=PropertiesUtils.getValue("current_branch_id");
		params.put("pi_branchid", branchid);
		params.put("pi_sb", params.get("shiftId"));
		params.put("pi_ksrq",params.get("beginTime"));
		params.put("pi_jsrq",params.get("endTime"));
		params.put("pi_cxlx",params.get("dataType"));
		params.put("pi_qy",params.get("areaid"));
		List<Map<String,Object>> listCou = tdataDetailDao.findDataStatistics(params);
		return listCou;
	}
}
