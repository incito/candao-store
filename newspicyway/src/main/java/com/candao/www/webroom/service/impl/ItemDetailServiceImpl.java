package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TItemDetailDao;
import com.candao.www.webroom.model.Code;
import com.candao.www.webroom.service.ItemDetailService;

/**
 * 品项分析图表
 * @author Administrator
 *
 */
@Service
public class ItemDetailServiceImpl implements ItemDetailService {

	@Autowired
	private TItemDetailDao titemDetailDao;
	
	/**
	 * 查询品项销售明细总表存储过程
	 * @author weizhifang
	 * @since 2015-7-3
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemDetailProcedure(Map<String, Object> params){
		List<Map<String,Object>> itemList = titemDetailDao.itemDetailProcedure(params);
		return itemList;
	}

	/**
	 * 查询品项销售明细子表存储过程
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemSubDetailProcedure(Map<String,Object> params){
		List<Map<String,Object>> itemList = titemDetailDao.itemSubDetailProcedure(params);
		return itemList;
	}
	
	/**
	 * 查询品类列表
	 * 
	 * @author weizhifang
	 * @since 2015-5-16
	 * @param itemid
	 * @return
	 */
	public List<Code> getItemDescList() {
		List<Map<String,Object>> codeList = titemDetailDao.getItemDescList();
		List<Code> codeResultList = new ArrayList<Code>();
		if(codeList.size()>0){
			for (int i = 0; i < codeList.size(); i++) {
				Code code =  new Code();
				if(codeList.get(i).get("id")!=null && codeList.get(i).get("id")!=""){
					code.setCodeId(codeList.get(i).get("id").toString());
				}else{
					code.setCodeId("");
				}
				if(StringUtils.isNotBlank((String) codeList.get(i).get("itemDesc"))){
					code.setCodeDesc((codeList.get(i).get("itemDesc").toString()));
				}else{
					code.setCodeDesc("");
				}
				codeResultList.add(code);
			}
		}
		return codeResultList;
	}
	
	/**
	 * 查询当前门店名称
	 * @author weizhifang
	 * @since 2015-7-29
	 * @param branchid
	 * @return
	 */
	public String getBranchName(String branchid){
		return titemDetailDao.getBranchName(branchid);
	}
}
