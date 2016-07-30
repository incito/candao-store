package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.GiftLogDao;
import com.candao.www.data.model.TGiftLog;
import com.candao.www.data.model.ToperationLog;
import com.candao.www.data.model.Torder;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.webroom.model.Order;
import com.candao.www.webroom.service.GiftLogService;
import com.candao.www.webroom.service.OrderDetailService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.ToperationLogService;

import net.sf.json.JSONObject;

/**
 * 
 * 礼物列表操作接口
 * @author Administrator
 *
 */
@Service
public class GiftServiceImpl implements GiftLogService{
	
	private static final Logger logger = LoggerFactory.getLogger(GiftServiceImpl.class);
	
	@Autowired
	private GiftLogDao giftDao;
	
	@Autowired
	private ToperationLogService toperationLogService;
	
	@Autowired
	private  OrderService   orderService;

	@Autowired
	private OrderDetailService   orderDetailService;
	
	@Autowired
	DataSourceTransactionManager transactionManager ;

	@Override
	public TGiftLog saveGiftLogInfo(TGiftLog giftLog) {
		if(giftLog==null){
			return null;
		}
		giftLog.setId(IdentifierUtils.getId().generate().toString());
		int num = giftDao.saveGiftLogInfo(giftLog);
		if(num>0){
			return giftLog;
		}
		return null;
	}

	@Override
	public List<TGiftLog> getGiftLogByOrder(String orderid) {
		return giftDao.getGiftLogByOrder(orderid);
	}
	
	@Override
	public List<TGiftLog> getGiftLogByRecOrder(String orderid) {
		return giftDao.getGiftLogByRecOrder(orderid);
	}

	@Override
	public int updateGiftLogInfo(TGiftLog giftLog,String primarykey) {
		int returnnum = 0;
		if(giftLog==null){
			return 4;
		}
		try{
			int tempnum = giftDao.updateGiftLogInfo(giftLog);
			if(tempnum<=0){
				returnnum = 1;
				return returnnum;
			}
			if(giftLog.getGiftStatus().equals("5")){
				giftDao.updateOrderStatus(giftLog.getReceiveOrderId());
				return returnnum;
			}
			if(!giftLog.getGiftStatus().equals("2")){
				return returnnum;
			}
			//调用下单接口，执行下单
			Torder order = orderService.get(giftLog.getOrderId());
			Map<String,String> giftMap = getGiftInfo(giftLog.getGiftId());
			Map<String, String> mapDetail = new HashMap<String, String>();
			mapDetail.put("orderid", giftLog.getOrderId());
			List<TorderDetail> orderDetailList = orderDetailService.find(mapDetail);
			int orderseq = 0;
			if(orderDetailList!=null&&orderDetailList.size()>0){
				for(TorderDetail detail : orderDetailList){
					if(detail.getOrderseq()>orderseq){
						orderseq = detail.getOrderseq();
					}
				}
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("tableno", giftLog.getGiveTableNo());
			map.put("operationType",  Constant.operationType.SAVEORDERINFOLIST);
			int sequence = 0;
			ToperationLog newtoperationLog = toperationLogService.findByparams(map);
			if(newtoperationLog!=null){
				sequence = parInt(newtoperationLog.getSequence());
			}
			
			if(giftMap==null||giftMap.size()!=8){
				returnnum = 2;
				return returnnum;
			}
			String gunit = giftMap.get("gunit").toString();
			String gno = giftMap.get("giftNo").toString();
			float mprice = giftLog.getGiftPrice();
			if(StringUtils.isBlank(gno)||StringUtils.isBlank(gunit)){
				return 2;
			}
			//执行下单接口
			Map<String,Object> rowsMap = new HashMap<String,Object>();
			rowsMap.put("printtype", "0");//打印类型
			rowsMap.put("orderprice", mprice);
			rowsMap.put("dishstatus", "0");
			rowsMap.put("dishunit", gunit);
			rowsMap.put("relatedishid", "");
			rowsMap.put("sperequire", "["+giftLog.getGiveTableNo()+"-"+giftLog.getReceiveTableNo()+"]");
			rowsMap.put("primarykey", primarykey);
			rowsMap.put("dishid", gno);
			rowsMap.put("orignalprice", mprice);
			rowsMap.put("dishes", "");
			rowsMap.put("userName", order.getUserid());
			rowsMap.put("pricetype", "2");
			rowsMap.put("orderid", giftLog.getOrderId());
			rowsMap.put("orderseq", orderseq+1);
			rowsMap.put("dishtype", 0);
			rowsMap.put("ispot", "0");
			rowsMap.put("dishnum", 1);
			List<Map<String,Object>> rowsList = new ArrayList<Map<String,Object>>();
			rowsList.add(rowsMap);
			Map<String,Object> orderInfoMap = new HashMap<String,Object>();
			orderInfoMap.put("rows", rowsList);
			orderInfoMap.put("currenttableid", giftLog.getGiveTableNo());
			orderInfoMap.put("globalsperequire", "");
			orderInfoMap.put("orderid", giftLog.getOrderId());
			orderInfoMap.put("operationType", Constant.operationType.SAVEORDERINFOLIST);
			orderInfoMap.put("sequence", sequence+1);
			
			JSONObject orderObject = JSONObject.fromObject(orderInfoMap);
			
			Order saveorder = JacksonJsonMapper.jsonToObject(orderObject.toString(), Order.class);
			//判断重复下单
			ToperationLog toperationLog=new ToperationLog();
			toperationLog.setId(IdentifierUtils.getId().generate().toString());
			toperationLog.setTableno(saveorder.getCurrenttableid());
			toperationLog.setOperationtype(Constant.operationType.SAVEORDERINFOLIST);
			toperationLog.setSequence(saveorder.getSequence());
			int flag= judgeRepeatData(toperationLog);
			Map<String, Object> res = null;
			if(flag==0){
				res =  orderDetailService.setOrderDetailList(saveorder);
			}
			
			if(res==null||!res.containsKey("code")||StringUtils.isBlank(String.valueOf(res.get("code")))||!String.valueOf(res.get("code")).equals("0")){
				returnnum = 3;
				return returnnum;
			}
		}catch(Exception ex){
			logger.error("-->",ex);
			ex.printStackTrace();
			returnnum = 5;
			return returnnum;
		}finally{
			
		}
		return 0;
	}

	@Override
	public TGiftLog getGiftLogInfo(String giftLogId) {
		return giftDao.getGiftLogInfo(giftLogId);
	}
	
	@Override
	public List<TGiftLog> getGiftLogInfo(Map<String,String> params) {
		return giftDao.getGiftLogInfo(params);
	}

	@Override
	public Map<String, String> getGiftInfo(String giftId) {
		return giftDao.getGiftInfo(giftId);
	}
	
	@Override
	public Map<String, String> getOrderStatus(String orderid) {
		return giftDao.getOrderStatus(orderid);
	}
	
	private int parInt(String value){
		try{
			return Integer.parseInt(value);
		}catch(Exception ex){
			logger.error("-->",ex);
			return 0;
		}
	}
	private int judgeRepeatData(ToperationLog toperationLog){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("tableno", toperationLog.getTableno());
		map.put("operationType", toperationLog.getOperationtype());
		ToperationLog newtoperationLog=toperationLogService.findByparams(map);
		if(newtoperationLog==null){
			return 0;//第一次操作
		}else if(newtoperationLog.getSequence()==toperationLog.getSequence()){
			//本次操作的序号和上次操作的序号相等，返回操作成功
			return 2;
		}
//		else if(newtoperationLog.getSequence()+1!=toperationLog.getSequence()){
//			//本次操作的序号，必须是上次操作序号+1,不等就返回失败
//			return 1;
//		}
		else{
			return 0;
		}
	}

	@Override
	public int updateOrderStatus(String orderid) {
		return giftDao.updateOrderStatus(orderid);
	}

	@Override
	public int deleteById(String logId) {
		return giftDao.deleteById(logId);
	}
}
