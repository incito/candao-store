package com.candao.www.webroom.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.page.Page;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.print.entity.PrintObj;
import com.candao.print.service.PrinterService;
import com.candao.print.service.TableOptionService;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbTableDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.dao.TsettlementMapper;
import com.candao.www.data.model.TbDataDictionary;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.ToperationLog;
import com.candao.www.data.model.Tworklog;
import com.candao.www.data.model.User;
import com.candao.www.permit.common.Constants;
import com.candao.www.permit.service.UserService;
import com.candao.www.webroom.model.AccountCash;
import com.candao.www.webroom.model.Table;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.TableService;
import com.candao.www.webroom.service.ToperationLogService;
import com.candao.www.webroom.service.WorkLogService;
@Service
public class TableServiceImpl implements TableService {
	
	@Autowired
	@Qualifier("t_userService")
	UserService userService ;
	
	@Autowired
    private TbTableDao tableDao;
    @Autowired
    TorderMapper torderMapper;
    
	@Autowired
	TsettlementMapper settlementMapper;
	
	@Autowired
	private ToperationLogService  toperationLogService;
	
	@Autowired
	private WorkLogService workLogService;
	
	@Autowired
	private DataDictionaryService datadictionaryService;
	
	@Autowired
	TorderDetailMapper torderDetailMapper;
	@Autowired
	OrderServiceImpl orderServiceImpl;
	
	@Autowired
	PrinterService  printerService;
	@Autowired
	TableOptionService  tableOptionService;
    
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
		return tableDao.page(params, current, pagesize);
	}
	@Override
	public boolean save(TbTable tbTable) {
		return tableDao.insert(tbTable)>0;
	}
	@Override
	public TbTable findById(String id) {
		return tableDao.get(id);
	}
	
	@Override
	 public TbTable findByTableNo(String tableNo){
		 return tableDao.getByTableNO(tableNo);
	 }
	@Override
	public boolean update(TbTable tbTable) {
		return tableDao.update(tbTable)>0;
	}
	@Override
	public boolean deleteById(String id) {
		return tableDao.delete(id)>0;
	}
	
	@Override
	public List<Map<String, Object>>  find(Map<String, Object> params) {
		return tableDao.find(params);
	}
	@Override
	public List<Map<String, Object>> getTableTag() {
		return tableDao.getTableTag();
	}
	@Override
	public List<Map<String, Object>> getPrinterTag() {
		return tableDao.getPrinterTag();
	}
	public List<Map<String, Object>> getbuildingNoANDTableTypeTag() {
		return tableDao.getbuildingNoANDTableTypeTag();
	}
	public List<Map<String, Object>> getTableTag3() {
		return tableDao.getTableTag3();
	}
	
	
	
	
	public int updateStatus(TbTable tbTable){
		 return  tableDao.updateStatus(tbTable);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class) 
	@Override
	public String switchTable(Table switchTable,ToperationLog toperationLog){
		  //1.修改更换的桌号为  已经预定
		 //2 ,修改原来的桌号位空闲
		//3.修改订单号 桌位 为更换后的桌位
//		TbTable table = new TbTable();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableNo", switchTable.getOrignalTableNo());
//		map.put("status", "1");
		List<Map<String, Object>> orignalMap = tableDao.find(map);
		if(orignalMap == null || orignalMap.size() == 0 || orignalMap.size() > 1){
			return Constant.FAILUREMSG;
		}
		
		Map<String, Object> map2 =  torderMapper.findOne(String.valueOf(orignalMap.get(0).get("orderid")));
		if(map2 == null || map2.size() == 0 || "3".equals(String.valueOf(map2.get("orderstatus")))){
			return Constant.FAILUROPEMSG;
		}
		
//		Map<String, Object> orignalMap2 =  orignalMap.get(0);
//		String orignalTableId = String.valueOf(orignalMap2.get("tableid"));
		
		map = new HashMap<String, Object>();
		map.put("tableNo", switchTable.getTableNo());
		map.put("status", "0");
		List<Map<String, Object>> currentMap = tableDao.find(map);
		if(currentMap == null || currentMap.size() == 0 || currentMap.size() > 1){
			return Constant.FAILUREMSG;
		}
		
		
		//执行存储过程
	    String result = "1";
       Map<String, Object> mapParam = new HashMap<String, Object>();
       mapParam.put("orginalTableNo", switchTable.getOrignalTableNo());
       mapParam.put("targetTableNo", switchTable.getTableNo());
       mapParam.put("result", result);
       torderMapper.selectSwitchTable(mapParam);
       result = String.valueOf(mapParam.get("result"));
       
       if(!"0".equals(result)){
    	   return Constant.FAILUREMSG;
       }else {
    	   if(toperationLogService.save(toperationLog)){
    		   String userId = (String)map2.get("userid");
        	   User mapUser = userService.getUserByjobNum(userId);
        	   User disUser = userService.getUserByjobNum(switchTable.getDiscardUserId());
        	   PrintObj pj = new PrintObj();
        	   pj.setUserName(mapUser.getName());
        	   pj.setBillName("换台单");
        	   pj.setAbbrbillName("换");
        	   pj.setOrderNo(String.valueOf(orignalMap.get(0).get("orderid")));
        	   pj.setTimeMsg(DateUtils.dateToString(new Date()));
        	   pj.setWelcomeMsg(switchTable.getTableNo());//换到的台
        	   pj.setTableNo(switchTable.getOrignalTableNo());
        	   pj.setDiscardUserId(disUser.getName());
        	   
        	   printTableChangeBill(pj,"7");
		   		return Constant.SUCCESSMSG;
		   	}else{
		   		return Constant.FAILUREMSG;
		   	}
	    }
	}
	//printerType 6并台  7换台  
	private void printTableChangeBill( PrintObj pj,String printerType) {
		Map<String,Object> params=new HashMap<String,Object>(); 
		params.put("printertype", printerType);
		  List<Map<String,Object>>  list = printerService.find(params);
		  if(list!=null&&list.size()>0){
			  for(Map<String,Object> map:list){
				  pj.setCustomerPrinterIp(String.valueOf(map.get("ipaddress")));
		    	   pj.setCustomerPrinterPort(String.valueOf(map.get("port")));
		    	   new Thread(new PrintBillThread(pj)).run();
			  }
		  }
    	  
	}
	public class PrintBillThread extends Thread{
		   PrintObj printObj ;
		   PrintBillThread(PrintObj printObj ){
			   this.printObj = printObj;
		   }
		   @Override
		   public void run(){
			   //根据动作打印不同的小票
			   tableOptionService.sendMessage( printObj );
		   }
	   }
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class) 
	@Override
	public String mergetable(Table mergeTable,ToperationLog toperationLog){
		//1.设定当前桌 为占用
		//2.设定关联桌号为 占用
		//3.把相关菜品订单关联到主桌
		//4.订单状态为关联结算
		String orignalTableno = mergeTable.getOrignalTableNo();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableNo", orignalTableno);
		List<Map<String, Object>> resultMap = tableDao.find(map);
		if(resultMap == null || resultMap.size() == 0 || resultMap.size() > 1){
			return Constant.FAILUREMSG;
		}
		
		Map<String, Object> map2 =  torderMapper.findOne(String.valueOf(resultMap.get(0).get("orderid")));
		if(map2 == null || map2.size() == 0 || "3".equals(String.valueOf(map2.get("orderstatus")))){
			return Constant.FAILUROPEMSG;
		}
		
//		String status = String.valueOf(resultMap.get(0).get("status"));
//		String orignalTableId = String.valueOf(resultMap.get(0).get("tableid"));
		
		String tableNo = mergeTable.getTableNo();
		String [] tables = tableNo.split(",");
		List<String> listTableNos = Arrays.asList(tables);
		List<Map<String, Object>> listTables = tableDao.findIds(listTableNos);
		if(listTables == null || listTables.size() == 0  ){
			return Constant.FAILUREMSG;
		}
		//如果目标桌和原始桌的订单号相同，直接返回并台成功
		if(String.valueOf(resultMap.get(0).get("orderid")).equals(listTables.get(0).get("orderid"))){
			return Constant.SUCCESSMSG;
		}
		//---------------------------------------------------------
		//并桌的时候查询目标桌是否开台，是否下单了    下单了，并桌的时候把下单的数据返回给pad。并桌只能并一个桌
		Map<String,Object> tablemap=listTables.get(0);
		Map<String,Object> resultmap=new HashMap<String,Object>();
		resultmap.put("result", "0");
		if(tablemap.get("status")!=null&&"1".equals(String.valueOf(tablemap.get("status").toString()))){
			Map<String, Object> orderMap =  torderMapper.findOne(String.valueOf(tablemap.get("orderid")));
			if(orderMap != null && orderMap.size() != 0 && "0".equals(String.valueOf(orderMap.get("orderstatus")))){
				resultmap.put("flag", "1");
				resultmap.put("desc", "获取数据成功");
				resultmap.put("currenttableid",tablemap.get("tableNo"));
				resultmap.put("orderid",tablemap.get("orderid"));
				resultmap.put("memberno",orderMap.get("memberno"));
				resultmap.put("manNum",orderMap.get("manNum"));
				resultmap.put("womanNum",orderMap.get("womanNum"));
				resultmap.put("waiterNum",orderMap.get("userid"));
				resultmap.put("ageperiod",orderMap.get("ageperiod"));
				resultmap.put("begintime",orderMap.get("begintime"));
				Map<String,Object> mappa=new HashMap<String,Object>();
				mappa.put("orderid", tablemap.get("orderid"));
				TbDataDictionary dd =datadictionaryService.findById("backpsd");
				TbDataDictionary vipaddress =datadictionaryService.findById("vipaddress");
				TbDataDictionary locktime =datadictionaryService.findById("locktime");
				TbDataDictionary delaytime =datadictionaryService.findById("delaytime");
				resultmap.put("result", "0");
				resultmap.put("orderid", tablemap.get("orderid"));
				resultmap.put("backpsd", dd==null?"":dd.getItemid());//退菜密码
				resultmap.put("vipaddress", vipaddress==null?"":vipaddress.getItemid()); //雅座的VIP地址
				resultmap.put("locktime", locktime==null?"":locktime.getItemid()); //屏保锁屏时间
				resultmap.put("delaytime", delaytime==null?"":delaytime.getItemid()); //屏保停留时间
				resultmap.put("rows", orderServiceImpl.getMapData(String.valueOf(tablemap.get("orderid"))));
				
				//订单不为空，调用推送接口，清空目标pad的数据
//				orderMap.get("meid")
				if(orderMap.get("meid")!=null){
					StringBuffer str=new StringBuffer(Constant.TS_URL);
					str.append(Constant.MessageType.msg_1005+"/"+String.valueOf(orderMap.get("meid")));
					new Thread(new TsThread(str.toString())).run();
				}
				toperationLogService.deleteToperationLogByTableNo(String.valueOf(tablemap.get("tableNo")));
		}
		}
		
		
		//---------------------------------------------------------
		
		//执行存储过程
	    String result = "1";
       Map<String, Object> mapParam = new HashMap<String, Object>();
       mapParam.put("orginalTableNo", mergeTable.getOrignalTableNo());
       mapParam.put("targetTableNo",mergeTable.getTableNo());
       mapParam.put("result", result);
         torderMapper.selectMergeTable(mapParam);
         result = String.valueOf(mapParam.get("result"));
       if(!"0".equals(result)){
    	   return Constant.FAILUREMSG;
       }else {
    	   if(toperationLogService.save(toperationLog)){
    		   String userId = (String)map2.get("userid");
        	   User mapUser = userService.getUserByjobNum(userId);
        	   User disUser = userService.getUserByjobNum(mergeTable.getDiscardUserId());
        	   PrintObj pj = new PrintObj();
        	   pj.setUserName(mapUser.getName());
        	   pj.setBillName("并台单");
        	   pj.setAbbrbillName("并");
        	   pj.setOrderNo(String.valueOf(resultMap.get(0).get("orderid")));
        	   pj.setTimeMsg(DateUtils.dateToString(new Date()));
        	   pj.setWelcomeMsg(mergeTable.getTableNo());//换到的台
        	   pj.setTableNo(mergeTable.getOrignalTableNo());
        	   pj.setDiscardUserId(disUser.getName());
        	   
        	   printTableChangeBill(pj,"6");
		   		return JacksonJsonMapper.objectToJson(resultmap);
		   	}else{
		   		return Constant.FAILUREMSG;
		   	}
	    }
	}
	//推送的线程
	public class TsThread extends Thread{
		   String  str ;
		   TsThread(String  str){
			   this.str = str;
		   }
		   @Override
		   public void run(){
			   //根据动作打印不同的小票
				URL urlobj;
				try {
				urlobj = new URL(str);
				URLConnection	urlconn = urlobj.openConnection();
				urlconn.connect();
				InputStream myin = urlconn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
				String content = reader.readLine();
				JSONObject object = JSONObject.fromObject(content.trim());
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> resultList = (List<Map<String, Object>>) object.get("result");
				if("1".equals(String.valueOf(resultList.get(0).get("Data")))){
					System.out.println("清空pad推送成功");
				}else{
					System.out.println("清空pad推送失败");
				}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   }
	   }
	
	  public String accountcash(AccountCash accountCash){
		  //判断当前用户是不是经理
		  
		 // 1. 换班，换班是要根据值班经理的开业时间来确定到目前为止的营业款项
		  //2.清机问题  清机是清算当天开业也来得所有的营业收入
		 //1.1 查询今天经理开业的时间
		  //1.2 查询当日的营业额，
		  //1.3 现金营业额，银行卡营业额，会员营业额 ，优惠票营业额
		  //2.1 清机和换班的区别是 清机要更新订单的状态为已经清机 （入账）
		  
		 //1 查询 t_worklog 表查询当天的开业日期，条件是经理+开业存在
		 //2 计算开业当天有多少营业额
		  //3.插入每天入账表
		  
		  Map<String ,Object> param=new HashMap();
		  param.put("account", accountCash.getUserName());
		  User user = userService.getUserByAccount(accountCash.getUserName());
		  if(user == null || ! user.getUserType().equals(Constants.SUPER_ADMIN)){
			   return Constant.FAILUREMSG;
		  }
		  
//		  Date loginTime = null;
//		  Tsettlement settlement = settlementMapper.getTotalAmount(loginTime);
		//添加日志-----------------------------------------------------
			Tworklog tworklog = new Tworklog();
			tworklog.setWorkid(UUID.randomUUID().toString());
			List<Map<String, Object>> list = datadictionaryService.getDatasByType("WORKTYPE");			
			tworklog.setUserid(accountCash.getUserName());
			tworklog.setBegintime(new Date());
			tworklog.setEndtime(new Date());
			tworklog.setIpaddress("127.0.0.1");
			tworklog.setStatus(1);
		//-----------------------------------------------------------	
		  if("0".equals(accountCash.getActionType())){
			  //换班
			  for(int i=0;i<list.size();i++){
					if(list.get(i).get("itemDesc").equals("换班")){
						tworklog.setWorktype(list.get(i).get("itemid").toString());
					};
				}
		  }else if("1".equals(accountCash.getActionType())) {
			//清机
			  for(int i=0;i<list.size();i++){
					if(list.get(i).get("itemDesc").equals("清机")){
						tworklog.setWorktype(list.get(i).get("itemid").toString());
					};
				}			
		   }
		  workLogService.saveLog(tworklog);
		  return Constant.SUCCESSMSG;
	  }
	  
	  @Override
	  public TbTable findTableNoAndAreaNameById(String tableId){
		  return tableDao.findTableNoAndAreaNameById(tableId);
	  }
	  
	  @Override
	  public int  updateCleanStatus(TbTable tbTable){
		   return tableDao.updateCleanStatus(tbTable);
	  }
	  
	  @Override
	  public int  updateSettleStatus(TbTable tbTable){
		  return tableDao.updateSettleStatus(tbTable);
	  }
	@Override
	public int updateSettleOrderNull(TbTable tbTable) {
		  return tableDao.updateSettleOrderNull(tbTable);
	}
	@Override
	public List<Map<String, Object>> findDetail(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tableDao.findDetail(params);
	}
	@Override
	public List<Map<String, Object>> getTableTag2() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<TbTable> getTablesByTableType(String areaid) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("areaid", areaid);
		return tableDao.getTablesByTableType(params);
	}
	@Override
	public boolean deleteTablesByAreaid(String areaid) {
		return tableDao.deleteTablesByAreaid(areaid)>0;
	}
	@Override
	public TbTable findByOrder(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return tableDao.findByOrder(map);
	}

}

