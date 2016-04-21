package com.candao.www.webroom.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.dao.DaoSupport;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.model.TbUser;
import com.candao.www.data.model.User;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.utils.SessionUtils;

/**
 * 小费
 *  @author lizongren
 *  @serialData 2016-4-7
 */
@Service
public class TipService {
  public final static String PREFIX = TipService.class.getName();
  @Autowired
  private DaoSupport dao;
 
	/**
	   * 小费设置
	   * @param groupon
	   */
	public void TipSet(Map<String, Object> params) {

		 dao.insert(PREFIX + ".tipSet", params);
	}
	 
	
	/**
	   * 修改小费金额
	   * @param groupon
	   */
	public int TipUpdate(Map<String, Object> params) {

		return dao.update(PREFIX + ".tipUpdate", params);
	}
	/**
	   * 拉取小费信息
	   * @param groupon
	   */
	public  List<Map<String, Object>> TipLoadData(Map<String, Object> params) {

		return dao.find(PREFIX + ".tipLoadData", params);
	}
	/**
	   * 没有查询到小费记录的时候返回服务员姓名
	   * @param groupon
	   */
	public  List<Map<String, Object>> TipFindNamebyorderid(Map<String, Object> params) {

		return dao.find(PREFIX + ".tipFindNamebyorderid", params);
	}
	/**
	   * 删除小费记录
	   * @param groupon
	   */
	public int TipDelete(Map<String, Object> params) {

		return dao.delete(PREFIX + ".tipDelete", params);
	}
	
	/**
	   * 小费结算
	   * @param groupon
	   */
	public int TipBilling(Map<String, Object> params) {

		return dao.update(PREFIX + ".tipBilling", params);
	}
	
	/**
	   * 查询挂门店服务员小费list
	   * @param groupon
	   */
	public List<Map<String, Object>> TipList(Map<String, Object> params) {

		return  dao.find(PREFIX + ".tipList", params);
	}
	
	
	
}
