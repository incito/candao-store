package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.Destination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import com.candao.common.utils.DateUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.TbPrinterDetail;
import com.candao.print.entity.TbPrinterManager;
import com.candao.print.service.CustDishProducerService;
import com.candao.print.service.DishSetProducerService;
import com.candao.print.service.MutilDishProducerService;
import com.candao.print.service.NormalDishProducerService;
import com.candao.print.service.PrinterService;
import com.candao.print.service.StatentMentProducerService;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbPrintObjDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.ToperationLog;
import com.candao.www.data.model.Torder;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailSimple;
import com.candao.www.data.model.User;
import com.candao.www.permit.service.UserService;
import com.candao.www.webroom.model.Order;
import com.candao.www.webroom.model.Table;
import com.candao.www.webroom.model.UrgeDish;
import com.candao.www.webroom.service.DishService;
import com.candao.www.webroom.service.OrderDetailService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.TableAreaService;
import com.candao.www.webroom.service.TableService;
import com.candao.www.webroom.service.ToperationLogService;


@Service
public class OrderDetailServiceImpl implements OrderDetailService{
	
//	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5000));
	private Log log = LogFactory.getLog(OrderDetailServiceImpl.class.getName());
	
	@Override
	@Transactional( propagation=Propagation.REQUIRED,rollbackFor=Exception.class) 
	public String updateorderprice(Order orders) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("tableNo", orders.getCurrenttableid());
		List<Map<String, Object>> tableList=tableService.find(params);
		if(tableList!=null&&tableList.size()>0){
			orders.setOrderid(String.valueOf(tableList.get(0).get("orderid")));
		}else{
			return Constant.FAILUREMSG;
		}
		//从传过来的数据中，获取订单详情的所有信息	
	     List<TorderDetail> listall=getallTorderDetail(orders.getRows());
		  Map<String, Object> mapStatus = torderMapper.findOne(orders.getOrderid());
		  if(!"0".equals(String.valueOf(mapStatus.get("orderstatus")))){
			  return Constant.FAILUREMSG;
		  }
		  for(TorderDetail torderDetail : listall){
			  torderDetailMapper.updateOrderDetail(torderDetail);
		  }
	    
	        return Constant.SUCCESSMSG;
	}
	
   /**
    * 清桌 ，
    */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class) 
   public String cleantable(Table table){
	   String tableNo = table.getTableNo();
	   Map<String, Object> map = new HashMap<String, Object>();
	   map.put("tableNo", tableNo);
	   List<Map<String, Object>> resultMapList = tableService.find(map);
	   if(resultMapList == null || resultMapList.size() == 0){
		   return Constant.FAILUREMSG;
	   }
	   Map<String, Object> tableMap = resultMapList.get(0);
	   String tableId = String.valueOf(tableMap.get("tableid"));
		   
	   TbTable tbTable = new TbTable();
	   tbTable.setTableid(tableId);
	   tbTable.setStatus(0);
	   tbTable.setOrderid(String.valueOf(tableMap.get("orderid") == null?"":tableMap.get("orderid")));
	   tableService.updateCleanStatus(tbTable);
	   
	   if(tableMap.get("orderid") != null){
		   Torder torder = new Torder();
		   torder.setOrderid(String.valueOf(tableMap.get("orderid")));
	       torder.setOrderstatus(2);
	       torder.setEndtime(new Date());
		   torderMapper.update(torder);
	   }
	   
	  //结账之后把操作的数据删掉
	  Map<String,Object> delmap=new HashMap<String,Object>();
	  delmap.put("tableno", table.getTableNo());
	  toperationLogService.deleteToperationLog(delmap);
	   
	   return Constant.SUCCESSMSG;
   }
	 
	@Override
	public List<TorderDetail> find(Map<String, String> mapDetail){
		return torderDetailMapper.find(mapDetail);
	}
	
	@Override
	public List<Map<String, String>> findTemp(Map<String, String> mapDetail) {
		return torderDetailMapper.findTemp(mapDetail);
	}
	
	//从传过来的数据中，获取订单详情的所有信息
		/**
		 * 1 获取所有的订单详情信息   2删除个数为0的菜品 
		 * @param orderDetails
		 * @return dishtype
		 * 单品 0
		 * 组合    组合里面的鱼和锅 1
		 * 套餐 2     套餐里面单品4    组合中的鱼和锅3
		 */
		public List<TorderDetail> getallTorderDetail(List<TorderDetail> orderDetails){
			 List<TorderDetail> listall=new ArrayList<TorderDetail>();
			 for(TorderDetail t:orderDetails){
				 /*******处理网络差的情况下，下单出现多个相同的Primarykey导致退菜失败的情况*********/
				 String primarykey = t.getPrimarykey();
				 TorderDetail orderDetail = torderDetailMapper.getOrderDetailByPrimaryKey(primarykey);
				 if(orderDetail != null){
					 continue;
				 }
				 /**********end************/
				 
		    	 if("0".equals(t.getDishtype())){
		    		 if(!"0".equals(t.getDishnum())){
		    			 t.setOrdertype(0);
		    			 t.setPrimarykey(t.getPrimarykey());
		    			 t.setParentkey(t.getPrimarykey());         
		    			 t.setSuperkey(t.getPrimarykey());   
		    			 t.setIsmaster(1);
		    			 listall.add(t);
		    		 }
		    	 }else if("1".equals(t.getDishtype())){
    				 t.setRelatedishid(t.getDishid());
    				 
	    			 t.setParentkey(t.getPrimarykey());
	    			 t.setSuperkey(t.getPrimarykey());
	    			 t.setOrderprice(null);
	    			 
	    			 t.setOrdertype(1);
	    			 t.setIsmaster(1);
	    			 listall.add(t);
	    			 
		    		  List<TorderDetail> list1=t.getDishes();
		    		 for(TorderDetail t1:list1){
		    			 if(!"0".equals(t1.getDishnum())){
		    				 t1.setDishtype("1");
		    				 t1.setRelatedishid(t.getDishid());
		    				 
			    			 t1.setOrdertype(1);
			    			 t1.setPrimarykey(t1.getPrimarykey());
			    			 t1.setParentkey(t.getPrimarykey());
			    			 t1.setSuperkey(t.getPrimarykey());   
			    			 listall.add(t1);
			    		 }
		    		 }
		    		 
		    	 }else if("2".equals(t.getDishtype())){
		    		 List<TorderDetail> list2=t.getDishes();
		    		 if(!"0".equals(t.getDishnum())){
		    			 t.setDishtype("2");
		    			 t.setOrdertype(2);
		    			 t.setChilddishtype(2);
		    			 t.setParentkey(t.getPrimarykey());
		    			 t.setSuperkey(t.getPrimarykey());
		    			 t.setIsmaster(1);
		    			 listall.add(t);
		    		 for(TorderDetail t2:list2){
		    			 if("0".equals(t2.getDishtype())){
		    	    		 if(!"0".equals(t2.getDishnum())){
		    	    			 t2.setOrderprice(new BigDecimal(0));
		    	    			 t2.setDishtype("2");
		    	    			 t2.setRelatedishid(t.getDishid());
//		    	    			 t2.setPrinttype(t.getPrinttype());
		    	    			 
		    	     			 t2.setOrdertype(2);
				    			 t2.setPrimarykey(t2.getPrimarykey());
				    			 t2.setParentkey(t.getPrimarykey());
				    			 t2.setSuperkey(t.getPrimarykey());   
				    			 t2.setChilddishtype(0);
				    			 t2.setIsmaster(0);
				    			 
		    	    			 listall.add(t2);
		    	    		 }
		    	    	 }else if("1".equals(t2.getDishtype())){
		    	    		 
		    	    		 t2.setRelatedishid(t.getDishid());
			    			 t2.setOrdertype(2);
			    			 t2.setIsmaster(1);
			    			 t2.setOrderprice(null);
			    			 t2.setDishtype("2");
			    			 t2.setChilddishtype(1);
			    			 
			    			 t2.setParentkey(t.getPrimarykey());
			    			 t2.setSuperkey(t.getPrimarykey());  
			    			 
			    			 listall.add(t2);
			    			 
		    	    		 List<TorderDetail> list3=t2.getDishes();
		    	    		 for(TorderDetail t3:list3){
		    	    			 if(!"0".equals(t3.getDishnum())){
		    	    				 t3.setDishtype("2");
		    	    				 t3.setOrderprice(new BigDecimal(0));
		    	    				 t3.setRelatedishid(t.getDishid());
//		    	    				 t3.setDishtype("2");
		    	        			 t3.setOrdertype(0);
		    	        			 t3.setChilddishtype(1);
					    			 t3.setPrimarykey(t3.getPrimarykey());
					    			 t3.setParentkey(t2.getPrimarykey());
					    			 t3.setSuperkey(t.getPrimarykey());   
					    			 t3.setIsmaster(0);
		    		    			 listall.add(t3);
		    		    		 }
		    	    		 }
		    	    		 
		    	    	 }
		    		 }
		    		 }
		    	 }
		     }
			 
			 return listall;
		}
		
	    /**
	     * 下单service
	     */
 @Override 
// @Transactional( propagation=Propagation.REQUIRED,rollbackFor=java.net.ConnectException.class) 
 public String saveOrderDetailList(Order orders,ToperationLog toperationLog) {
	 
	  DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	  def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); 
	  TransactionStatus status = transactionManager.getTransaction(def); //获得事务状态
	try{
//		object = status.createSavepoint();
		String tableNo = orders.getCurrenttableid();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("tableNo", orders.getCurrenttableid());
		TbTable table = tableService.findByTableNo(tableNo);
		if(table!=null ){
			orders.setOrderid(table.getOrderid());
		}else{
			return Constant.FAILUREMSG;
		}
		
//		判断是否重复下单
		if(isRepetitionOrder(orders.getRows())){
			return Constant.SUCCESSMSG;
		}
		//从传过来的数据中，获取订单详情的所有信息	
	    List<TorderDetail> listall = getallTorderDetail(orders.getRows());
		if(listall == null || listall.size() == 0){
			return Constant.FAILUREMSG;
		}
		  Map<String, Object> mapStatus = torderMapper.findOne(orders.getOrderid());
		  if(!"0".equals(String.valueOf(mapStatus.get("orderstatus")==null?"":mapStatus.get("orderstatus")))){
			  return Constant.FAILUREMSG;
		  }
		  Map<String, Object> mapParam1 = new HashMap<String, Object>();
		  mapParam1.put("orderid", orders.getOrderid());
		  List<TorderDetail> detailList =   torderDetailMapper.find(mapParam1);
		  //调用存储过程插入订单详情的临时表
		  int success = torderDetailMapper.insertTempOnce(listall);
			if(success < 1){
				return Constant.FAILUREMSG;
			}
			
//			//执行存储过程，将订单详情临时表中的数据插入到t_order_detail
//		 
		   String result = "1";
	       Map<String, Object> mapParam = new HashMap<String, Object>();
	       mapParam.put("orderid", orders.getOrderid());
	       mapParam.put("result", result);
	       torderMapper.setOrderDish(mapParam);
	       result = String.valueOf(mapParam.get("result"));
	       
	       if("1".equals(result)){
	    	   return Constant.FAILUREMSG;
	       } 
//	       
	       int flag = (detailList == null  || detailList.size() == 0 ?0:1);
////	       if("1".equals(orders.getRows().get(0).getPrinttype())){
////	    	   flag=4;
////	       }
	       printOrderList( orders.getOrderid(),table.getTableid(), flag);
	       printweigth(listall,orders.getOrderid());
			   	 //操作成功了，插入操作日记
	        if(toperationLogService.save(toperationLog)){
	    	  transactionManager.commit(status);
		   	  return Constant.SUCCESSMSG;
		   	}else{
		     	transactionManager.rollback(status);
		   		return Constant.FAILUREMSG;
		   	}
	 }catch(Exception ex){
				ex.printStackTrace();
				 transactionManager.rollback(status);
			   	 return Constant.FAILUREMSG;
			} 	
	
		}
		/**
		 * 打印订单中的需要称重的数据，打印称重单
		 * @author shen
		 * @date:2015年6月11日下午8:43:51
		 * @Description: TODO
		 */
		public void printweigth(List<TorderDetail> list,String orderid){
			Map<String, Object> printMap = new HashMap<String, Object>();
			printMap.put("orderno", orderid);
			PrintObj printObj = tbPrintObjDao.find(printMap);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "1");
			map.put("printertype", "5");
			//称重单不需要分区域
	 
			
			List<TbPrinterManager> printerList = tbPrinterManagerDao.findPrintByType(map);
			if(list!=null&&list.size()>0){
				for(TorderDetail orderDetail:list){
					if("1".equals(orderDetail.getDishstatus())){
						for(TbPrinterManager printer : printerList){
							PrintObj object = new PrintObj();
							
							object.setCustomerPrinterIp(printer.getIpaddress());
							object.setBillName("称重单");
							object.setCustomerPrinterPort(printer.getPort());
							object.setOrderNo(orderid);
							object.setTimeMsg(DateUtils.dateToString(new Date()));
							object.setUserName(printObj.getUserName());
							object.setTableArea(printObj.getTableArea());
							object.setTableNo(printObj.getTableNo());
							List<PrintDish> printDishList = new ArrayList<>();
							
							Map<String, Object> fishMap = new HashMap<String, Object>();
							fishMap.put("printobjid", printObj.getId());
							fishMap.put("primarykey", orderDetail.getPrimarykey());
							List<PrintDish> dishList = tbPrintObjDao.findDish(fishMap);
							for(PrintDish p : dishList){
								PrintDish printDish1 = new PrintDish();
								printDish1.setDishName(p.getDishName());
								printDish1.setDishNum(p.getDishNum());
								printDish1.setDishUnit(p.getDishUnit());
//								printDish1.setSperequire((String)params.get("dishnum"));
								printDish1.setAbbrname("待称重");
								printDish1.setOrderseq(p.getOrderseq());
								printDishList.add(printDish1);
							} 
							object.setList(printDishList);
							new Thread(new WeigthThread(object)).run();
//							executor.execute(new WeigthThread(object));
						}
					}
				}
			}
		}
		
		/**
		 * flag 0 初次下菜单 1 加菜单
		 * @author tom_zhao
		 * @param orderId
		 * @param tableId
		 * @param flag
		 * @return
		 */
	private void printOrderList(String orderId,String tableId,int flag){
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("orderno", orderId);
			PrintObj printObj  = tbPrintObjDao.find(map);
			
			 //判断是厨打单还是加菜单
		     if(flag == 1){
		    	 printObj.setPrintType(Constant.PRINTTYPE.ADD_DISH);
		    	 printObj.setBillName(Constant.DISHBILLNAME.CUSTADDDISHNAME);
		    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.ADDDISHNAME_ABBR);
		     }else  if(flag == 0){
		    	 printObj.setPrintType(Constant.PRINTTYPE.NORMAL_DISH);
		    	 printObj.setBillName(Constant.DISHBILLNAME.NORMALDISHNAME);
			}else  if(flag == 3){
		    	 printObj.setPrintType(Constant.PRINTTYPE.DISCARD_DISH);
		    	 printObj.setBillName(Constant.DISHBILLNAME.DISCARDDISHNAME);
		    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR);
			}
//			else  if(flag == 4){
//		    	 printObj.setPrintType(Constant.PRINTTYPE.COOKIE_DISH);
//		    	 printObj.setBillName(Constant.DISHBILLNAME.READYNAME);
//		    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.READY_ABBR);
//			}
		     Map<String,Object> paramsMap=new HashMap<String,Object>();
		     paramsMap.put("PrintType", printObj.getPrintType());
		     paramsMap.put("billName", printObj.getBillName());
		     if(printObj.getAbbrbillName()!=null&&!"".equals(printObj.getAbbrbillName())){
		    	 paramsMap.put("AbbrbillName", printObj.getAbbrbillName());
		     }
//		     int PrintType=printObj.getPrintType();
//			  String billName=printObj.getBillName();
//			  String AbbrbillName=printObj.getAbbrbillName();
		     
		     //打印单品
		     printSingleDish(printObj,paramsMap);
			 // 打印锅和鱼
//		     printObj.setPrintType(PrintType);
//	    	 printObj.setBillName(billName);
//	    	 printObj.setAbbrbillName(AbbrbillName);
			 printFishAndHot(printObj,paramsMap);
			 //打印套餐
//			 printObj.setPrintType(PrintType);
//	    	 printObj.setBillName(billName);
//	    	 printObj.setAbbrbillName(AbbrbillName);
			 printDishSet(printObj,paramsMap);
			 
//		     added by caicai 2016-02-19  套餐小票(传菜员专用)
			 printDishSetIndividually(printObj,paramsMap);
			 
			 if(flag == 1){
		    	 printObj.setPrintType(Constant.PRINTTYPE.ADD_DISH);
		    	 printObj.setBillName(Constant.DISHBILLNAME.ADDDISHNAME);
		    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.ADDDISHNAME_ABBR);
		     }else if(flag == 0){
		    	 printObj.setPrintType(Constant.PRINTTYPE.NORMAL_DISH);
		    	 printObj.setBillName(Constant.DISHBILLNAME.NORMALCUSTDISHNAME);
			}else if(flag == 3){
		    	 printObj.setPrintType(Constant.PRINTTYPE.DISCARD_DISH);
		    	 printObj.setBillName(Constant.DISHBILLNAME.DISCARDDISHNAME);
		    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR);
			}
//			else  if(flag == 4){
//				 printObj.setPrintType(Constant.PRINTTYPE.ADD_DISH);
//		    	 printObj.setBillName(Constant.DISHBILLNAME.ADDDISHNAME);
//		    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.ADDDISHNAME_ABBR);
//			}
			 			 
			 printCustDish(printObj);
		     
		}

/**
 * 打印单品
 * @author tom_zhao
 * @param printObj
 */

		private void printSingleDish(PrintObj printObj,Map<String,Object> paramsMap) {
			//打印还没有打印的单品
		     Map<String, Object> map0 = new HashMap<String, Object>();
			  map0.put("printobjid", printObj.getId());
			  map0.put("dishtype", "0");
			  map0.put("printnum", "0");
//			  map0.put("islatecooke",0 );
			  printSingleDish(map0,printObj,0,paramsMap);
		}


/**
 * 打印套餐
 * @author tom_zhao
 * @param printObj
 */
		private void printDishSet(PrintObj printObj,Map<String,Object> paramsMap) {
			Map<String, Object> map0;
			//打印套餐   套餐中的单品
				  map0 = new HashMap<String, Object>();
				  map0.put("printobjid", printObj.getId());
				  map0.put("dishtype", "2");
				  map0.put("printnum", "0");
//				  map0.put("islatecooke",0 );
				  map0.put("childdishtype","0" );
				  printSingleDish(map0,printObj,0,paramsMap);
				
				  //打印套餐   套餐中的火锅
				  map0 = new HashMap<String, Object>();
				  map0.put("printobjid", printObj.getId());
				  map0.put("dishtype", "2");
				  map0.put("printnum", "0");
//				  map0.put("islatecooke",0 );
				  map0.put("childdishtype","1" );
				  map0.put("ismaster","1");
				  if("(备菜)".equals(printObj.getAbbrbillName())){
					  map0.put("islatecooke",1 );
				  }	  
				  printMutilDish(map0,printObj,0,paramsMap);
				  
//				  map0 = new HashMap<String, Object>();
//				  map0.put("printobjid", printObj.getId());
//				  map0.put("printnum", 0);
//				  printCustDish(map0,printObj);
				  
		}
	
	/**
	 * 打印传菜单
	 * 
	 * @param printObj
	 */
	private void printDishSetIndividually(PrintObj printObj, Map<String, Object> paramsMap) {
		Map<String, Object> map0 = new HashMap<String, Object>();

		map0.put("printobjid", printObj.getId());
		// dishtype 0 单品 1 鱼锅 2 套餐
		map0.put("dishtype", "2");
		map0.put("printnum", "0");
		// childdishtype 0 单品 1 鱼锅 2 套餐
		map0.put("childdishtype", "2");
		map0.put("ismaster", "1");
		// 查询所有套餐
		List<PrintDish> listPrint = tbPrintObjDao.findDishGroupBySuperKey(map0);
		if (listPrint != null && !listPrint.isEmpty()) {
			// 保持不变
			printObj.setPrintType(Constant.PRINTTYPE.NORMAL_DISH);
			printObj.setBillName(Constant.DISHBILLNAME.DISHSETNAME);

			for (PrintDish pd : listPrint) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("printobjid", printObj.getId());
				params.put("superkey", pd.getSuperkey());
				params.put("dishtype", "2");
				List<PrintDish> dishsetlists = tbPrintObjDao.findDish(params);
				// 去零
				for (PrintDish it : dishsetlists) {
					formatDishNum(it);
				}
				
				printObj.setList(dishsetlists);

				Map<String, Object> paramMap = new HashMap<String, Object>();
				// 查询火锅打印机
				paramMap.put("status", "1");
				paramMap.put("printertype", "1");
				paramMap.put("tableid", printObj.getTableid());

				// 需要把所有的菜品配置的打印机全部打印
				paramMap.put("dishid", pd.getDishId());
				List<String> IPList = new ArrayList<String>();
				List<TbPrinterManager> printers = tbPrinterManagerDao.findDishPrinter(paramMap);
				TbPrinterManager tbPrinter = new TbPrinterManager();

				if (printers != null && printers.size() > 0) {
					tbPrinter = printers.get(0);
				} else {
					log.info("该桌套餐未配备打印机----- 桌号:" + printObj.getTableNo() + " ;菜品:" + pd.getDishName());
				}

				if (!"(退)".equals(printObj.getAbbrbillName())) {
					int printNum = (tbPrinter.getPrintNum() == null ? 0 : tbPrinter.getPrintNum()) + 1;
					tbPrinter.setPrintNum(printNum);
					int flag = tbPrinterManagerDao.update(tbPrinter);
					if (flag <= 0) {
						System.out.println("printnum更新失败！");
						log.info("printnum更新失败！" + printObj.getId() + "打印机:" + tbPrinter.getPrintername());
					} else {
						printObj.setOrderseq(printNum);
					}
				} else {
					printObj.setOrderseq(pd.getOrderseq());
				}

				if (printers != null) {
					for (TbPrinterManager pm : printers) {
						if (IPList != null) {
							if (IPList.contains(tbPrinter.getIpaddress())) {
								continue;
							}
							IPList.add(tbPrinter.getIpaddress());
						}
						printObj.setCustomerPrinterIp(pm.getIpaddress());
						printObj.setCustomerPrinterPort(pm.getPort());
						new Thread(new PrintDishSetThread(printObj)).run();
					}
				}

			}
		}
	}

	/**
	 * 打印锅和鱼
	 * @author tom_zhao
	 * @param printObj
	 */
	private void printFishAndHot(PrintObj printObj,Map<String,Object> paramsMap){
		  //打印火锅
		 Map<String, Object> map0   = new HashMap<String, Object>();
		  map0.put("printobjid", printObj.getId());
		  map0.put("dishtype", "1");
		  map0.put("printnum", "0");
//		  map0.put("islatecooke",0 );
		  map0.put("ismaster","1" );
		  printMutilDish(map0,printObj,0,paramsMap);
	}
		
      private void printCustDish(PrintObj printObj){
    	  
	    	  Map<String, Object> map0 = new HashMap<String, Object>();
			  map0.put("printobjid", printObj.getId());
			  map0.put("printnum", 0);
		  
	          List<PrintDish> listall = tbPrintObjDao.findDish(map0);
//	          List<PrintDish> listall=new ArrayList<PrintDish>();
//	          if(listPrint!=null&&listPrint.size()>0){
//	        	  for(PrintDish printDish:listPrint){
//	        		  //客用单不打印餐具
//	        		  if(!"DISHES_98".equals(printDish.getDishId())){
//	        			  listall.add(printDish);
//	        		  }
//	        	  }
//	          }
//			  Collections.sort(listPrint);
			  printObj.setList(listall);
			  //得到区域
			  //1. 厨打单
			  //2. 客用单
			  //3.预结单
			  //4. 结账单
			  
			  Map<String,Object> paramMap = new HashMap<String, Object>();
			  paramMap.put("status", "1");
			  paramMap.put("printertype", "2");
			  paramMap.put("tableid", printObj.getTableid());
				
			  List<TbPrinterManager> tps = tbPrinterManagerDao.findNoDishPrinter(paramMap);
			  //该餐厅是否设置为不打印
			  int  result  =tbPrintObjDao.findPrintTable(paramMap);
				if(result==0){//该餐厅不打印
					listall=null;
			 }
			  if(listall != null && listall.size() > 0){
				 for(TbPrinterManager  tPrinterManager : tps){
				  printObj.setCustomerPrinterIp(tPrinterManager.getIpaddress());
				  printObj.setCustomerPrinterPort(tPrinterManager.getPort());
				  new Thread(new PrintCustThread(printObj)).run();
//				  executor.execute(new PrintCustThread(printObj));
			     } 
			  }
			  
			  
			  tbPrintObjDao.updateDishCall(map0);
		}
		
	private void printSingleDish(Map<String, Object> map0, PrintObj printObj, int refundDish, Map<String, Object> paramsMap) {
		List<PrintDish> listPrint = tbPrintObjDao.findDish(map0);

		Collections.sort(listPrint);
		printObj.setList(listPrint);

		// 得到区域
		// 1. 厨打单
		// 2. 客用单
		// 3.预结单
		// 4. 结账单
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("status", "1");
		paramMap.put("printertype", "1");
		paramMap.put("tableid", printObj.getTableid());
		// 需要把所有的菜品配置的打印机全部打印
		// 查找菜品所有符合的打印机
		List<PrintDish> printedList = new ArrayList<>();
		for (PrintDish pd : printObj.getList()) {
			if(printedList.contains(pd)){//已经合并打印了则跳过
				continue;
			}
			List<String> IPList = new ArrayList<String>();
			formatDishNum(pd);
			//查询菜品所属套餐,不包括鱼锅
			HashMap<String, Object> map1 = new HashMap<>();
			map1.put("printobjid", pd.getPrintobjid());
			map1.put("dishtype", "2");
			map1.put("printnum", "0");
			map1.put("childdishtype","2" );
			map1.put("ismaster","1");
			map1.put("primarykey", pd.getSuperkey());
			List<PrintDish> superdishes = tbPrintObjDao.findDishGroupBySuperKey(map1);
			//记录单品所属的套餐名
			if (superdishes != null && superdishes.size() == 1) {
				pd.setParentDishName(superdishes.get(0).getDishName());
			}
			if (refundDish != 1) {
				if (pd.getIslatecooke() == 1) {
					printObj.setPrintType(Constant.PRINTTYPE.COOKIE_DISH);
					printObj.setBillName(Constant.DISHBILLNAME.READYNAME);
					printObj.setAbbrbillName(Constant.DISHBILLNAME.READY_ABBR);
				} else if (paramsMap != null) {
					printObj.setPrintType(Integer.valueOf(String.valueOf(paramsMap.get("PrintType"))));
					printObj.setBillName(String.valueOf(paramsMap.get("billName")));
					if (paramsMap.get("AbbrbillName") != null) {
						printObj.setAbbrbillName(String.valueOf(paramsMap.get("AbbrbillName")));
					} else {
						printObj.setAbbrbillName("");
					}
				}
			}
			pd.setAbbrname(printObj.getAbbrbillName());
			if (("2".equals(pd.getDishtype()) && pd.getPrimarykey().equals(pd.getParentkey())
					&& pd.getPrimarykey().equals(pd.getSuperkey())) || pd.getDishId().equals("DISHES_98")) {
				continue;
			}

			// if(map0.get("discardNum")!=null ){
			// if(map0.get("discardNum")!=null&&!"".equals(String.valueOf(map0.get("discardNum")))&&String.valueOf(map0.get("discardNum")).endsWith(".0")){
			// String discardNum=String.valueOf(map0.get("discardNum"));
			// pd.setDishNum(discardNum.substring(0,
			// discardNum.lastIndexOf(".")));
			// }
			// pd.setDishNum("-"+pd.getDishNum());
			// }
			if (map0.get("discardNum") != null && !"".equals(String.valueOf(map0.get("discardNum")))) {
				String discardNum = String.valueOf(map0.get("discardNum"));
				if (String.valueOf(map0.get("discardNum")).endsWith(".0")) {
					discardNum = discardNum.substring(0, discardNum.lastIndexOf("."));
				}
				pd.setDishNum("-" + discardNum);
			}
			if (map0.get("discardNum") == null && refundDish == 1) {
				if (pd.getDishNum() != null && !"".equals(pd.getDishNum()) && pd.getDishNum().endsWith(".0")) {
					String discardNum = pd.getDishNum();
					pd.setDishNum(discardNum.substring(0, discardNum.lastIndexOf(".")));
				}
				pd.setDishNum("-" + pd.getDishNum());
			}
			if (map0.get("discardReason") != null && refundDish == 1) {
				pd.setSperequire(String.valueOf(map0.get("discardReason")));
			}
			paramMap.put("dishid", pd.getDishId());

			List<TbPrinterManager> printers = tbPrinterManagerDao.findDishPrinter(paramMap);
			if (printers != null) {
				for (TbPrinterManager tbPrinter : printers) {
					if (IPList != null) {
						if (IPList.contains(tbPrinter.getIpaddress())) {
							continue;
						}
						IPList.add(tbPrinter.getIpaddress());
					}

					if (!"(退)".equals(printObj.getAbbrbillName())) {
						int printNum = (tbPrinter.getPrintNum() == null ? 0 : tbPrinter.getPrintNum()) + 1;
						tbPrinter.setPrintNum(printNum);
						int flagB = tbPrinterManagerDao.update(tbPrinter);
						if (flagB <= 0) {
							System.out.println("printnum更新失败！");
						} else {
							updateDishPrintNum(printNum, pd.getPrimarykey());
							printObj.setOrderseq(printNum);
						}

					} else {
						printObj.setOrderseq(pd.getOrderseq());
					}
						
					// 判断是否合并打印
					boolean needMerge = false;
					List<PrintDish> pdList = new ArrayList<>();
					//退菜不合并打印;稍后上菜不合并打印;送礼的菜不合并打印;
					boolean isRefund = Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR.equals(printObj.getAbbrbillName());
					boolean gift = isGiftDish(pd);
					if (!isRefund && pd.getIslatecooke() != 1 && !gift) {
						String groupSequence = getDishGroupSequence(pd, tbPrinter);
						if (groupSequence != null) {
							List<TbPrinterDetail> findPrintDetail = getSameGroupDishList(tbPrinter, groupSequence);
							// 有两个及以上的菜才需要合并 
							//modified by caicai
							if (findPrintDetail.size() > 1) {
								int i = 0;
								for (TbPrinterDetail tbPrinterDetail : findPrintDetail) {
									for (PrintDish printDish : printObj.getList()) {
										if (printDish.getDishId().equals(tbPrinterDetail.getDishid())) {
											gift = isGiftDish(printDish);
											//退菜情况不会进入
											if(printDish.getIslatecooke() != 1 && !gift ){
												//buffer
												pdList.add(printDish);
												i++;
											}
										}
									}
								}
								// 有两个以上才打印
								if(i >= 2){
									needMerge = true;
									for (PrintDish it : pdList) {
										// 加入已打印列表
										formatDishNum(it);//格式化菜品数量，不能省略
										printedList.add(it);
										//更新打印序号
										int printNum = printObj.getOrderseq();
										String primarykey = it.getPrimarykey();
										updateDishPrintNum(printNum, primarykey);										
									}
								} else {
									pdList.clear();
								}
							}
						}
					}
					if(!needMerge){
						pdList.add(pd);
					}

					printObj.setCustomerPrinterIp(tbPrinter.getIpaddress());
					printObj.setCustomerPrinterPort(tbPrinter.getPort());
					printObj.setpDish(pdList);
					new Thread(new PrintThread(printObj)).run();
					// executor.execute(new PrintThread(printObj));
				}
			}
			if (refundDish == 1) {
				Map<String, Object> printertypeMap = new HashMap<String, Object>();
				printertypeMap.put("printertype", 4);
				List<Map> findPrinterByType = tbPrinterManagerDao.find(printertypeMap);
				IPList.clear();
				if (printers != null && "(退)".equals(printObj.getAbbrbillName())) {
					for (Map tbPrinter : findPrinterByType) {
						if (IPList != null) {
							if (IPList.contains(tbPrinter.get("ipaddress"))) {
								continue;
							}
							IPList.add((String) tbPrinter.get("ipaddress"));
						}
						printObj.setOrderseq(pd.getOrderseq());
						printObj.setCustomerPrinterIp((String) tbPrinter.get("ipaddress"));
						printObj.setCustomerPrinterPort((String) tbPrinter.get("port"));
						List<PrintDish> list = new ArrayList<>();
						list.add(pd);
						printObj.setpDish(list);
						new Thread(new PrintThread(printObj)).run();
						// executor.execute(new PrintThread(printObj));
					}
				}
			}
		}
		if (refundDish == 1) {
			printdishware(listPrint, printObj, map0);
		}
	}

	/**
	 * 更新菜品的打印序号
	 * @param printNum
	 * @param primarykey
	 */
	private void updateDishPrintNum(int printNum, String primarykey) {
		PrintDish tempPrintDish = new PrintDish();
		tempPrintDish.setOrderseq(printNum);
		tempPrintDish.setPrimarykey(primarykey);
		tbPrintObjDao.updateDish(tempPrintDish);
	}

	/**
	 * 判断菜品是否送礼
	 * @param pd
	 * @return
	 */
	private boolean isGiftDish(PrintDish pd) {
		String sperequire = pd.getSperequire();
		return !StringUtils.isEmpty(sperequire) && sperequire.contains("[");
	}

	/**
	 * 格式化菜品数量，去掉小数点
	 * @param pd
	 */
	private void formatDishNum(PrintDish pd) {
		String num = pd.getDishNum();
		if (!"".equals(num) && num.endsWith(".0")) {
			pd.setDishNum(num.substring(0, num.lastIndexOf(".")));
		}
	}

	/**
	 * 获取与当前菜品同一分组的所有菜品
	 * @param tbPrinter
	 * @param groupSequence
	 * @return
	 */
	private List<TbPrinterDetail> getSameGroupDishList(TbPrinterManager tbPrinter, String groupSequence) {
		Map<String, Object> sameGroupMap = new HashMap<>();
		sameGroupMap.put("printerid", tbPrinter.getPrinterid());
		sameGroupMap.put("groupsequence", groupSequence);
		List<TbPrinterDetail> findPrintDetail = tbPrinterManagerDao.findPrintDetail(sameGroupMap);
		// 去重，添加组合时同一个菜属于两个分类会被同时勾选
		Set<String> dishIds = new HashSet<>();
		for (Iterator<TbPrinterDetail> it = findPrintDetail.iterator(); it.hasNext();) {
			TbPrinterDetail tbPrinterDetail = it.next();
			if (dishIds.contains(tbPrinterDetail.getDishid())) {
				it.remove();
				continue;
			}
			dishIds.add(tbPrinterDetail.getDishid());
		}
		return findPrintDetail;
	}

	/**
	 * 查询菜品分组的ID
	 * @param pd
	 * @param tbPrinter
	 * @return
	 */
	private String getDishGroupSequence(PrintDish pd, TbPrinterManager tbPrinter) {
		Map<String, Object> detailMap = new HashMap<>();
		detailMap.put("printerid", tbPrinter.getPrinterid());
		detailMap.put("dishid", pd.getDishId());
		List<TbPrinterDetail> findPrintDetail = tbPrinterManagerDao.findPrintDetail(detailMap);
		String groupSequence = null;
		if (findPrintDetail != null && !findPrintDetail.isEmpty()) {
			groupSequence = findPrintDetail.get(0).getGroupSequence();
		}
		return groupSequence;
	}
	  /**
	   * 这个类主要解决打印退餐具
	   * @author shen
	   * @date:2015年6月26日下午3:31:20
	   * @Description: TODO
	   */
	  private void printdishware(List<PrintDish> listPrint,PrintObj printObj,Map<String,Object> map0){
		  printObj.setPrintType(Constant.PRINTTYPE.DISCARD_DISH);
		  printObj.setBillName(Constant.DISHBILLNAME.DISCARDDISHNAME);
		  printObj.setAbbrbillName(Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR);
		  if(listPrint!=null&&listPrint.size()>0){
			  List<String> IPList = new ArrayList<String>();
			  for(PrintDish pdish:listPrint){
				  if(pdish.getDishId().equals("DISHES_98")){
					  Map<String,Object> printertypeMap = new HashMap<String, Object>();
					  printertypeMap.put("printertype", 4);
					  List<Map<String,Object>> findPrinterByType = tbPrinterManagerDao.find(printertypeMap);
					  if(map0.get("discardNum")!=null&&!"".equals(String.valueOf(map0.get("discardNum")))){
						  String discardNum=String.valueOf(map0.get("discardNum"));
						  if(String.valueOf(map0.get("discardNum")).endsWith(".0")){
							  discardNum=discardNum.substring(0, discardNum.lastIndexOf("."));
						  }
						  pdish.setDishNum("-"+discardNum);
					  }
					  IPList.clear();
					  if(findPrinterByType != null&&findPrinterByType.size()>0){
						  for(Map<String,Object> tbPrinter :  findPrinterByType){
							  if(IPList!=null){
								  if(IPList.contains(tbPrinter.get("ipaddress"))){
									  continue;
								  }
						  			IPList.add((String) tbPrinter.get("ipaddress"));
						  		}
							  printObj.setOrderseq(0);
							  printObj.setCustomerPrinterIp((String)tbPrinter.get("ipaddress"));
							  printObj.setCustomerPrinterPort((String)tbPrinter.get("port"));
							  List<PrintDish> list  = new ArrayList<>();
							  list.add(pdish);
							  printObj.setpDish(list);
							  new Thread(new PrintThread(printObj)).run();
//							  executor.execute(new PrintThread(printObj));
						  }
					  } 
				  }
			  }
		  }
	  }
	  private void printMutilDish( Map<String, Object> map0,PrintObj printObj,int flag,Map<String,Object> paramsMap){
		  List<PrintDish> listPrint  = tbPrintObjDao.findDishGroupByParentKey(map0);
		  
		  if(listPrint != null&&listPrint.size()!=0){
			  for(PrintDish pd : listPrint){
				  if(flag!=1){
				  if(pd.getIslatecooke()==1){
				    	 printObj.setPrintType(Constant.PRINTTYPE.COOKIE_DISH);
				    	 printObj.setBillName(Constant.DISHBILLNAME.READYNAME);
				    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.READY_ABBR);
				  }else if(paramsMap!=null){
					     printObj.setPrintType(Integer.valueOf(String.valueOf(paramsMap.get("PrintType"))));
				    	 printObj.setBillName(String.valueOf(paramsMap.get("billName")));
				    	 if(paramsMap.get("AbbrbillName")!=null){
				    		 printObj.setAbbrbillName(String.valueOf(paramsMap.get("AbbrbillName")));
				    	 }
				  }
				  }
				    Map<String,Object> params=new HashMap<String,Object>();
				    params.put("printobjid",printObj.getId());
				    params.put("primarykey",pd.getPrimarykey() );
				    params.put("ismaster","1");
					List<PrintDish>  fishesList = tbPrintObjDao.findDish(params);
					params=new HashMap<String,Object>();
					params.put("printobjid",printObj.getId());
					params.put("parentkey",pd.getPrimarykey());
					params.put("ismaster","0");
					List<PrintDish> fishandpotList=tbPrintObjDao.findDish(params);
					List<PrintDish> alllist=new ArrayList<PrintDish>();
					//直接addall有问题
					if(fishesList!=null&&fishesList.size()>0){
						for(PrintDish p:fishesList){
							alllist.add(p);
						}
					}
					if(fishandpotList!=null&&fishandpotList.size()>0){
						for(PrintDish p:fishandpotList){
							alllist.add(p);
						}
					}
					if(fishandpotList!=null&&fishandpotList.size()>0){
						boolean flagfishpot=false;
						for(PrintDish printDish:fishandpotList){
							if(printDish.getIspot()==1){//1代表锅底
								flagfishpot=true;
							}
						}
						if(flagfishpot){
							fishesList.addAll(fishandpotList);
						}else{
							fishesList=fishandpotList;
						}
					}
					//查询菜品所属套餐,不包括鱼锅
					HashMap<String, Object> map1 = new HashMap<>();
					map1.put("printobjid", pd.getPrintobjid());
					map1.put("dishtype", "2");
					map1.put("printnum", "0");
					map1.put("childdishtype","2" );
					map1.put("ismaster","1");
					map1.put("primarykey", pd.getSuperkey());
					List<PrintDish> superdishes = tbPrintObjDao.findDishGroupBySuperKey(map1);
					//设置套餐信息
					if (superdishes != null && superdishes.size() == 1) {
						pd.setParentDishName(superdishes.get(0).getDishName());
					}
					
					for (PrintDish pf : fishesList) {
						pf.setAbbrname(printObj.getAbbrbillName());
						//added by caicai 增加套餐信息
						pf.setParentDishName(pd.getParentDishName());
					}
					  if(fishesList != null){
						  if(map0.get("discardReason")!=null&&flag==1 ){
							  fishesList.get(0).setSperequire(String.valueOf(map0.get("discardReason")));
						  }
						  if(flag==1){
							  for(PrintDish printDish:fishesList){
								  if(printDish.getDishNum()!=null&&!"".equals(printDish.getDishNum())){
									  String discardNum=printDish.getDishNum();
									  if(printDish.getDishNum().endsWith(".0")){
										  printDish.setDishNum(discardNum.substring(0, discardNum.lastIndexOf(".")));
									  }
									  printDish.setDishNum("-"+printDish.getDishNum());
									  
								  }
							  }  
						  }
							  printObj.setList(fishesList);
							  //查询火锅打印机
							  Map<String,Object> paramMap = new HashMap<String, Object>();
							  paramMap.put("status", "1");
							  paramMap.put("printertype", "1");
							  paramMap.put("tableid", printObj.getTableid());
							
							  //需要把所有的菜品配置的打印机全部打印
							  paramMap.put("dishid", pd.getDishId());
							  List<String> IPList = new ArrayList<String>();
							  List<TbPrinterManager> printers = tbPrinterManagerDao.findDishPrinter(paramMap);
							  TbPrinterManager tbPrinter = new TbPrinterManager();
							  if(printers!=null&&printers.size()>0){
								  tbPrinter=printers.get(0);
							  }else{
								  System.out.println("----------------------------------------------------------------------");
								  System.out.println("--------------------------------该桌未配置打印机-----------------------------");
								  System.out.println("----------------------------------------------------------------------");
							  }
							  if(!"(退)".equals(printObj.getAbbrbillName())){
								  int printNum = (tbPrinter.getPrintNum()==null?0:tbPrinter.getPrintNum())+1;
								  tbPrinter.setPrintNum(printNum);
								  int flagB =tbPrinterManagerDao.update(tbPrinter);
								  if(flagB<=0){
									  System.out.println("printnum更新失败！");
								  }else{
									  for(PrintDish Pdish:alllist){
										  PrintDish printDish = new PrintDish();
										  printDish.setOrderseq(printNum);
										  printDish.setPrimarykey(Pdish.getPrimarykey());
										  printDish.setIslatecooke(Pdish.getIslatecooke());
//										  if("(备菜)".equals(printObj.getAbbrbillName())){
//											  printDish.setIslatecooke(1);
//											  printObj.getList().get(0).setIslatecooke(1);
//										  }
										  int flagC =tbPrintObjDao.updateDish(printDish);
									  }
									  
									  printObj.setOrderseq(printNum);
									  
								  }
								 
							  }else{
								  
								  printObj.setOrderseq(pd.getOrderseq());
							  }
							  if(printers != null){
								  for(TbPrinterManager pm :  printers){
									  if(IPList!=null){
										  if(IPList.contains(tbPrinter.getIpaddress())){
											  continue;
										  }
								  			IPList.add(tbPrinter.getIpaddress());
								  		}
									  printObj.setCustomerPrinterIp(pm.getIpaddress());
									  printObj.setCustomerPrinterPort(pm.getPort());
									  new Thread(new PrintMutiThread(printObj)).run();
//									  executor.execute(new PrintMutiThread(printObj));
						          }
							  }
							 if(flag == 1 ){
								  Map<String,Object> printertypeMap = new HashMap<String, Object>();
								  printertypeMap.put("printertype", 4);
								  List<Map<String,Object>> findPrinterByType = tbPrinterManagerDao.find(printertypeMap);
								  IPList.clear();
								  if(printers != null&&"(退)".equals(printObj.getAbbrbillName())&&findPrinterByType!=null){
									  for(Map<String,Object> tbPrinterMap :  findPrinterByType){
										  if(IPList!=null){
											  if(IPList.contains(tbPrinterMap.get("ipaddress"))){
												  continue;
											  }
									  			IPList.add((String) tbPrinterMap.get("ipaddress"));
									  		}
										  printObj.setOrderseq(pd.getOrderseq());
										  printObj.setCustomerPrinterIp((String)tbPrinterMap.get("ipaddress"));
										  printObj.setCustomerPrinterPort((String)tbPrinterMap.get("port"));
										  new Thread(new PrintMutiThread(printObj)).run();
//										  executor.execute(new PrintMutiThread(printObj));
									  }
								  }
							 }
					  }
					  
	    		}
		  }
	}
	  
	  /**
		 * 判断是否是重复下单
		 * @param orderDetails
		 * @return
		 */
		private boolean isRepetitionOrder(List<TorderDetail> orderDetails){
			int repeteNum = 0;
			for(TorderDetail t:orderDetails){
				String primarykey = t.getPrimarykey();
				TorderDetail orderDetail = torderDetailMapper.getOrderDetailByPrimaryKey(primarykey);
				if(orderDetail != null && orderDetail.getOrderdetailid() != ""){
					repeteNum++;
				}
			}
			return repeteNum == orderDetails.size() ? true : false;
		}
		
	  /**
	   * 退菜处理
	   */
		public String discardDishList(UrgeDish urgeDish,ToperationLog toperationLog){
		   if(urgeDish == null){
			  return Constant.FAILUREMSG;
		   }
		    Map<String, Object> params=new HashMap<String, Object>();
			params.put("tableNo", urgeDish.getCurrenttableid());
			List<Map<String, Object>> tableList=tableService.find(params);
			if(tableList!=null&&tableList.size()>0){
				urgeDish.setOrderNo(String.valueOf(tableList.get(0).get("orderid")));
			}else{
				return Constant.FAILUREMSG;
			}
			  String orderId = urgeDish.getOrderNo();
			  String discardUserId = urgeDish.getDiscardUserId();
			  String discardReason = urgeDish.getDiscardReason();
			  if(discardUserId == null ){
				  discardUserId = urgeDish.getUserName();
			  }
			  
			  Map<String, Object> mapStatus = torderMapper.findOne(orderId);
			  if(!"0".equals(String.valueOf(mapStatus.get("orderstatus")))){
				  return Constant.FAILUREMSG;
			  }
			  String actionType = urgeDish.getActionType();
	          Map<String,Object> map = new HashMap<String, Object>();
			  map.put("orderno", orderId);
			  PrintObj printObj  = tbPrintObjDao.find(map);
			  printObj.setPrintType(Constant.PRINTTYPE.DISCARD_DISH);
			  printObj.setBillName(Constant.DISHBILLNAME.DISCARDDISHNAME);
			  printObj.setAbbrbillName(Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR);
			  if(urgeDish.getDiscardUserId()!=null&&!"".equals(urgeDish.getDiscardUserId())){
				  User disUser = userService.getUserByjobNum(urgeDish.getDiscardUserId());
				  printObj.setDiscardUserId(disUser.getName());
			  }
			  //根据桌号配置客户打印机
//			  Map<String,Object> selectmap=new HashMap<String, Object>();
			  
			  if("0".equals(actionType)){
				  //单品退  
					  //判断退的数量是否和原始点的相同
					  //查询当前的菜品是否是和 退订的数量一致
					  BigDecimal detailNum = new BigDecimal("0");//下单的数量
					  BigDecimal urgeNum = new BigDecimal("0");//退的数量
					  TorderDetail orderDetail =  torderDetailMapper.getOrderDetailByPrimaryKey(urgeDish.getPrimarykey());
					  if(orderDetail==null){
						  return Constant.FAILUREMSG;
					  }
					  if(orderDetail != null){
						  detailNum =  new BigDecimal(orderDetail.getDishnum()) ;
					  }
					  orderDetail.setDiscardReason(discardReason);
					  orderDetail.setDiscardUserId(discardUserId);
					  urgeNum = detailNum.subtract(urgeDish.getDishNum());
				      String dishType = orderDetail.getDishtype();
				      int isMaster = orderDetail.getIsmaster();
					if(urgeNum.compareTo(detailNum) <=0){	 
						//退单品
						  if("0".equals(dishType)){
				     	      Map<String, Object>   map0 = new HashMap<String, Object>();
            				  map0.put("printobjid", printObj.getId());
            				  map0.put("dishtype", 0);
            				  map0.put("discardNum",urgeDish.getDishNum());
            				  map0.put("primarykey", urgeDish.getPrimarykey());
            				  map0.put("discardReason", discardReason);
            				  printSingleDish(map0,printObj,1,null);
            				  
            				  
            				  PrintDish pd = new PrintDish();
            				  pd.setPrintobjid( printObj.getId());
            				  pd.setPrimarykey(urgeDish.getPrimarykey());
							  updateOrderDetail(urgeDish.getPrimarykey(),orderDetail,urgeDish.getDishNum(),pd,"0");
						  }else if("1".equals(dishType)){
							  //退火锅 如果是主锅
							  if(isMaster == 1){
								  Map<String, Object> map0  = new HashMap<String, Object>();
								  map0.put("printobjid", printObj.getId());
								  map0.put("primarykey",urgeDish.getPrimarykey());
								  map0.put("discardReason", discardReason);
								  printMutilDish(map0,printObj,1,null);
								  
								  PrintDish pd = new PrintDish();
	            				  pd.setPrintobjid( printObj.getId());
	            				  pd.setPrimarykey(urgeDish.getPrimarykey());
								  updateOrderDetail(urgeDish.getPrimarykey(),orderDetail,urgeDish.getDishNum(),pd,"1");
							  }else {
								  if(orderDetail.getIspot()==1){
									  Map<String, Object> map0  = new HashMap<String, Object>();
									  map0.put("printobjid", printObj.getId());
									  map0.put("primarykey",orderDetail.getParentkey());
									  map0.put("discardReason", discardReason);
									  printMutilDish(map0,printObj,1,null);
									  
									  PrintDish pd = new PrintDish();
		            				  pd.setPrintobjid( printObj.getId());
		            				  pd.setPrimarykey(orderDetail.getParentkey());
									  updateOrderDetail(orderDetail.getParentkey(),orderDetail,urgeDish.getDishNum(),pd,"1");
								  }
								  if(orderDetail.getIspot()==0){
									  if(urgeDish.getDishNum().compareTo(new BigDecimal(orderDetail.getDishnum()))<0){
										  Map<String, Object>   map0 = new HashMap<String, Object>();
			            				  map0.put("printobjid", printObj.getId());
			            				  map0.put("discardNum",urgeDish.getDishNum());
			            				  map0.put("primarykey", urgeDish.getPrimarykey());
			            				  map0.put("discardReason", discardReason);
			            				  printSingleDish(map0,printObj,1,null);
			            				  
			            				  PrintDish pd = new PrintDish();
			            				  pd.setPrintobjid( printObj.getId());
			            				  pd.setPrimarykey(urgeDish.getPrimarykey());
										  updateOrderDetail(urgeDish.getPrimarykey(),orderDetail,urgeDish.getDishNum(),pd,"0");  
									  }else{
										  params=new HashMap<String,Object>();
										  params.put("parentkey", orderDetail.getParentkey());
										  List<TorderDetail> list=torderDetailMapper.find(params);
										  boolean flag=false;
										  if(list!=null&&list.size()>0){
											  for(TorderDetail t:list){
												  if(t.getIspot()==1){
													  flag=true;
												  }
											  }
										  }
										  if(flag){
											  Map<String, Object>   map0 = new HashMap<String, Object>();
				            				  map0.put("printobjid", printObj.getId());
				            				  map0.put("discardNum",urgeDish.getDishNum());
				            				  map0.put("primarykey", urgeDish.getPrimarykey());
				            				  map0.put("discardReason", discardReason);
				            				  printSingleDish(map0,printObj,1,null);
				            				  
				            				  PrintDish pd = new PrintDish();
				            				  pd.setPrintobjid( printObj.getId());
				            				  pd.setPrimarykey(urgeDish.getPrimarykey());
											  updateOrderDetail(urgeDish.getPrimarykey(),orderDetail,urgeDish.getDishNum(),pd,"0");   
										  }else{
											  Map<String, Object> map0  = new HashMap<String, Object>();
											  map0.put("printobjid", printObj.getId());
											  map0.put("primarykey",orderDetail.getParentkey());
											  map0.put("discardReason", discardReason);
											  printMutilDish(map0,printObj,1,null);
											  
											  PrintDish pd = new PrintDish();
				            				  pd.setPrintobjid( printObj.getId());
				            				  pd.setPrimarykey(orderDetail.getParentkey());
											  updateOrderDetail(orderDetail.getParentkey(),orderDetail,urgeDish.getDishNum(),pd,"1");
										  }
									  }
						     	     
								  }
							   }
							  
						  }else if("2".equals(dishType)){
							  //退套餐  如果是主套餐
	                           if(isMaster == 1){
	                        	   //套餐中的单品
	                        	  Map<String, Object>   map0 = new HashMap<String, Object>();
	             				  map0.put("printobjid", printObj.getId());
	             				  map0.put("dishtype", "2");
	             				  map0.put("childdishtype","0" );
	             				  map0.put("parentkey",urgeDish.getPrimarykey());
	             				  map0.put("discardReason", discardReason);
	             				  printSingleDish(map0,printObj,1,null);
	             				  
	             				  //套餐中的鱼锅
	            				  map0 = new HashMap<String, Object>();
	            				  map0.put("printobjid", printObj.getId());
	            				  map0.put("dishtype", "2");
	            				  map0.put("childdishtype","1" );
	            				  map0.put("ismaster","1" );
	            				  map0.put("discardReason", discardReason);
	            				  map0.put("parentkey",urgeDish.getPrimarykey());
	            				  printMutilDish(map0,printObj,1,null);
	            				  
	            				  
	            				  Map<String, Object> paramsDish = new HashMap<String, Object>();
	            				  paramsDish.put("orderid", orderId);
	            				  paramsDish.put("superkey", urgeDish.getPrimarykey());
	            				  torderDetailMapper.insertDiscardDishSetOnce(paramsDish);
	            				  
	            				  TorderDetail orderDetailDel = new TorderDetail();
	            				  orderDetailDel.setOrderid(orderId);
	            				  orderDetailDel.setSuperkey(urgeDish.getPrimarykey());
	            				  orderDetailDel.setDiscardUserId(discardUserId);
	            				  orderDetailDel.setDiscardReason(discardReason);
	            				  torderDetailMapper.updateDiscardDishSetUserId(orderDetailDel);
	            				  
	            				  Map<String, Object> deleteMap  = new HashMap<String, Object>();
	            				  deleteMap.put("orderid", orderId);
	            				  deleteMap.put("superkey", urgeDish.getPrimarykey());
	            				  torderDetailMapper.deleteDish(deleteMap);
	            				  
	            				  deleteMap  = new HashMap<String, Object>();
	            				  deleteMap.put("printobjid", printObj.getId());
	            				  deleteMap.put("superkey", urgeDish.getPrimarykey());
	            				  tbPrintObjDao.deleteDish(deleteMap);
	            				  
							   } 
						  }
						  //TODO 设置也打印到前台一份
					
					  }
			  }else if("1".equals(actionType)) {
				  //整单退菜
				  
			      Map<String, Object> map0 = new HashMap<String, Object>();
				  map0.put("printobjid", printObj.getId());
				  map0.put("dishtype", "0");
				  map0.put("discardReason", discardReason);
				  printSingleDish(map0,printObj,1,null);
				  
			  //打印火锅
				  map0   = new HashMap<String, Object>();
				  map0.put("printobjid", printObj.getId());
				  map0.put("dishtype", "1");
				  map0.put("ismaster","1" );
				  map0.put("discardReason", discardReason);
				  printMutilDish(map0,printObj,1,null);
				  
				//打印套餐   套餐中的单品
				  map0 = new HashMap<String, Object>();
				  map0.put("printobjid", printObj.getId());
				  map0.put("dishtype", "2");
				  map0.put("childdishtype","0" );
				  map0.put("discardReason", discardReason);
				  printSingleDish(map0,printObj,1,null);
				
				  //打印套餐   套餐中的火锅
				  map0 = new HashMap<String, Object>();
				  map0.put("printobjid", printObj.getId());
				  map0.put("dishtype", "2");
				  map0.put("childdishtype","1" );
				  map0.put("ismaster","1" );
				  map0.put("discardReason", discardReason);
				  printMutilDish(map0,printObj,1,null);
 
				  torderDetailMapper.insertDiscardDishOnce(orderId);
				  TorderDetail orderDetail = new TorderDetail();
				  orderDetail.setOrderid(orderId);
				  orderDetail.setDiscardUserId(discardUserId);
				  orderDetail.setDiscardReason(discardReason);
				  torderDetailMapper.updateDiscardDishUserIdOnce(orderDetail);
				  
				  Map<String, Object> deleteMap  = new HashMap<String, Object>();
				  deleteMap.put("orderid", orderId);
				  torderDetailMapper.deleteDish(deleteMap);
				  
				  deleteMap  = new HashMap<String, Object>();
				  deleteMap.put("printobjid", printObj.getId());
				  tbPrintObjDao.deleteDish(deleteMap);
				  
			}
			    if(toperationLogService.save(toperationLog)){
			   		return Constant.SUCCESSMSG;
			   	}else{
			   		return Constant.FAILUREMSG;
			   	}
		}

/**
 * 退菜使用的更新退菜的表和下单的明细表
 * @author tom_zhao
 * @param orderDetail
 * @param discardNum
 * 0 退单品  1退火锅
 */
     private void updateOrderDetail(String primarykey,TorderDetail orderDetail,BigDecimal discardNum,PrintDish printDish,String flag) {
    	 if("0".equals(flag)){
    	  BigDecimal num=new BigDecimal(orderDetail.getDishnum()).subtract(discardNum);
   		  if(num.compareTo(new BigDecimal("0"))<=0){
   			  Map<String,Object> params=new HashMap<String,Object>();
   			  params.put("primarykey", primarykey);
   			  torderDetailMapper.deleteDish(params);
   			  params.put("printobjid", printDish.getPrintobjid());
   			  tbPrintObjDao.deleteDish(params);
   		  }else{
   			  orderDetail.setDishnum(String.valueOf(num));
   			  torderDetailMapper.update(orderDetail);
   			  printDish.setDishNum(String.valueOf(num));
   			  tbPrintObjDao.updateDish(printDish);
   		  }
   		  orderDetail.setDishnum(String.valueOf(discardNum));
   		  torderDetailMapper.insertDiscardDish(orderDetail);
    	 }
    	 if("1".equals(flag)){
    		 Map<String,Object> params=new HashMap<String,Object>();
			  params.put("primarykey", primarykey);
    		  torderDetailMapper.insertDiscardfishpot(params);
			  torderDetailMapper.deletefishpot(params);
			  params.put("printobjid", printDish.getPrintobjid());
			  tbPrintObjDao.deletefishpot(params);
			  TorderDetail uporderDetail = new TorderDetail();
			  uporderDetail.setDiscardUserId(orderDetail.getDiscardUserId());
			  uporderDetail.setDiscardReason(orderDetail.getDiscardReason());
			  uporderDetail.setPrimarykey(primarykey);
			  torderDetailMapper.updateFishpotReason(uporderDetail);
    	 }
	  }
     /**
      * 退整个菜
      * @author tom_zhao
      * @param orderDetail
      * @param discardNum
      */
     @Override
     public void discardOrderDetail(TorderDetail orderDetail,String discardUserId,String discardReason) {
		  torderDetailMapper.insertDiscardDish(orderDetail);
		  orderDetail.setDiscardUserId(discardUserId);
		  orderDetail.setDiscardReason(discardReason);
		  torderDetailMapper.updateDiscardUserId(orderDetail);
		  torderDetailMapper.delete(orderDetail.getOrderdetailid());
     }


     public class StatementThread implements Runnable{
		   
		   	PrintObj printObj ;
		   
		   	StatementThread(PrintObj printObj){
			   this.printObj = printObj;
		   }
	 
		   @Override
		   public void run(){
			   //根据动作打印不同的小票
			   statentMentProducerService.sendMessage( printObj );
			//0.正常下单
			//1.加菜下单
		   }
		   
	   }
     /**
	    * 打印结账单
	    */
		@Override
		public void printStatement(String orderno) {
			
			 Map<String,Object> map = new HashMap<String, Object>();
			 map.put("orderno", orderno);
			 PrintObj printObj  = tbPrintObjDao.find(map);
			 printObj.setPrintType(Constant.PRINTTYPE.ADD_DISH);
	    	 printObj.setBillName(Constant.DISHBILLNAME.STATEMENTDISHNAME);
	    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.ADDDISHNAME_ABBR);
	    	 map.put("printobjid",printObj.getId());
	    	 
	    	 List<Map<String, Object>>  dishes= torderDetailMapper.getDishesInfoByOrderId(orderno);
	    	 printObj.setDishes(dishes);
	    	Map<String, Object>  ordermap= orderService.findOrderById(orderno);
	    	if(ordermap!=null){
	    		printObj.setOrdermap(ordermap);
	    	}
	    	//得到打印机配置
	    	  Map<String,Object> printertypeMap = new HashMap<String, Object>();
			  printertypeMap.put("printertype", 4);
			  List<Map> ipconfigs = tbPrinterManagerDao.find(printertypeMap);
			  if(ipconfigs!=null && ipconfigs.size()>0){
				  for(Map tbPrinter :  ipconfigs){
					 Object ipaddress=  tbPrinter.get("ipaddress");
					 Object port= tbPrinter.get("port");
					 if(ipaddress!=null){
						  printObj.setCustomerPrinterIp(ipaddress.toString());
						  printObj.setCustomerPrinterPort(port.toString());
					 }
				  }
			  }
			new Thread(new StatementThread(printObj)).run();
			
		}
		
		
	/**
	 * 催菜
	 */
	@Override
	public String urgeDishList(UrgeDish urgeDish) {
		if(urgeDish == null) {
			return Constant.FAILUREMSG;
		}
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("tableNo", urgeDish.getCurrenttableid());
		List<Map<String, Object>> tableList=tableService.find(params);
		if(tableList!=null&&tableList.size()>0){
			urgeDish.setOrderNo(String.valueOf(tableList.get(0).get("orderid")));
		}
		String orderNo = urgeDish.getOrderNo();
		
		 Map<String, Object> mapStatus = torderMapper.findOne(orderNo);
		  if(!"0".equals(String.valueOf(mapStatus.get("orderstatus")))){
			  return Constant.FAILUREMSG;
		  }
		  
		String actionType = urgeDish.getActionType();
		if("0".equals(actionType)){
			// 0 单餐 催菜
			//1 整桌催菜
			commonDishList(urgeDish,"0","0");
			
		}else if("1".equals(actionType)){
			//1 整桌催菜
			commonDishList(urgeDish,"0","1");
			
		}
		return Constant.SUCCESSMSG;
	}



	@Override
	public String cookiedishList(UrgeDish urgeDish) {
		if(urgeDish == null) {
			return Constant.FAILUREMSG;
		}
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("tableNo", urgeDish.getCurrenttableid());
		List<Map<String, Object>> tableList=tableService.find(params);
		if(tableList!=null&&tableList.size()>0){
			urgeDish.setOrderNo(String.valueOf(tableList.get(0).get("orderid")));
		}
		String orderNo = urgeDish.getOrderNo();
		
		 Map<String, Object> mapStatus = torderMapper.findOne(orderNo);
		  if(!"0".equals(String.valueOf(mapStatus.get("orderstatus")))){
			  return Constant.FAILUREMSG;
		  }
 
		String actionType = urgeDish.getActionType();
		if("0".equals(actionType)){
			// 0 单餐  叫起
			//1 整桌 叫起
			commonDishList(urgeDish,"1","0");
			//update t_printdish status 
		}else if("1".equals(actionType)){
			//1 整桌 叫起
			commonDishList(urgeDish,"1","1");
			//update t_printdish status 
		}
		return Constant.SUCCESSMSG;
	}
	/**
	 * 
	 * @param urgeDish
	 * @param type 0催菜  1叫起
	 * @param batch  0单个 1 整桌
	 * @return
	 */
	private String commonDishList(UrgeDish urgeDish,String type,String batch){
		
		 Map<String,Object> mapAll = new HashMap<String, Object>();
		  mapAll.put("orderno", urgeDish.getOrderNo());
		  PrintObj printObj  = tbPrintObjDao.find(mapAll);
		  if(printObj==null){
			  return Constant.FAILUREMSG;
		  }
		  if ("0".equals(type)) {
			  printObj.setPrintType(Constant.PRINTTYPE.URGE_DISH);
			  printObj.setBillName(Constant.DISHBILLNAME.URGEDISHNAME);
			  printObj.setAbbrbillName(Constant.DISHBILLNAME.URGEDISHNAME_ABBR);
		  }else {
			  printObj.setPrintType(Constant.PRINTTYPE.NOW_DISH);
			  printObj.setBillName(Constant.DISHBILLNAME.NOWDISHNAME);
			  printObj.setAbbrbillName(Constant.DISHBILLNAME.CALL_ABBR);
		  }

		//0 催单个菜品
		if("0".equals(batch)){
			singleDishUrgeAndCall(urgeDish.getPrimarykey(),urgeDish.getOrderNo(), type, printObj); 
 
		}else if("1".equals(batch)){
			 Map<String,Object> map0 = new HashMap<String, Object>();
			 map0.put("printobjid", printObj.getId());
			 map0.put("orderid",  urgeDish.getOrderNo());
			 if("0".equals(type)){
				  //催菜
				   map0.put("islatecooke", "0");
			  }
			  if("1".equals(type)){
				  //叫菜
				  map0.put("islatecooke", "1");
			  }
			  
			  List<PrintDish>  pdList = tbPrintObjDao.findDishByObjId(map0);
				 if(pdList != null){
					 for(PrintDish pd : pdList){
						singleDishUrgeAndCall(pd.getPrimarykey(),urgeDish.getOrderNo(), type, printObj); 
					 }
				 }
		}
	    return Constant.SUCCESSMSG;
	}



	private void singleDishUrgeAndCall(String primaryKey,String orderNo, String type,
			PrintObj printObj) {
		Map<String,Object> map0 = new HashMap<String, Object>();
		 
		  map0.put("printobjid", printObj.getId());
		  map0.put("primarykey", primaryKey);
		  map0.put("orderid", orderNo);
		 
		  if("0".equals(type)){
			  //催菜
			   map0.put("islatecooke", "0");
		  }
		  if("1".equals(type)){
			  //叫菜
			  map0.put("islatecooke", "1");
		  }
		  
		  //根据primarykey 和 parentkey 取单品或者鱼锅的数据
		 urgeAndCallDish(printObj, map0,type);
	}



	private void urgeAndCallDish(PrintObj printObj, Map<String, Object> map0,String flag) {
		List<PrintDish>  pdList = tbPrintObjDao.findDishByPrimaryKey(map0);
		 if(pdList != null){
			 for(PrintDish pf : pdList){
				  pf.setAbbrname(printObj.getAbbrbillName());
				  if(!"".equals(pf.getDishNum())&&pf.getDishNum().endsWith(".0")){
					  pf.setDishNum(pf.getDishNum().substring(0, pf.getDishNum().lastIndexOf(".")));
				  }
			  }
			 printObj.setList(pdList);
			 printObj.setOrderseq(pdList.get(0).getOrderseq() );
			 List<TbPrinterManager> printers = getPrinter(printObj.getTableid(),pdList.get(0).getDishId(),"1");
			 if(printers != null){
				for(TbPrinterManager printer : printers){
					printObj.setCustomerPrinterIp(printer.getIpaddress());
					printObj.setCustomerPrinterPort(printer.getPort());
//					executor.execute(new PrintMutiThread(printObj));
					new Thread(new PrintMutiThread(printObj)).run();
//					executor.execute(new PrintMutiThread(printObj));
				}
			   
			 }
		 }
		 //如果是叫起的单子需要更新状态为0
		 if("1".equals(flag)){
			   tbPrintObjDao.updateDishByPrimaryKey(map0);
			   tbPrintObjDao.updateDetailByPrimaryKey(map0);
		 }
	}



	private List<TbPrinterManager> getPrinter(String tableId,String dishId,String printertype) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		  paramMap.put("status", "1");
		  paramMap.put("printertype",printertype );
		  paramMap.put("tableid", tableId);
		   paramMap.put("dishid", dishId);
		   
	     return tbPrinterManagerDao.findDishPrinter(paramMap);
	}
	
	@SuppressWarnings("unused")
	@Override
	public String getOrderDetailByOrderId(String orderid) {
		List<TorderDetailSimple> torderDetail = torderDetailMapper.getOrderDetailByOrderId(orderid);
//		List<TorderDetail> torderDetail = null;
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("orderid", orderid);
		if(torderDetail != null ){
			//TODO 没有完成
			 retMap.put("orderMap", torderDetail);
		}
		return JacksonJsonMapper.objectToJson(retMap);
	}
	
public class PrintThread  implements Runnable{
		   
		   PrintObj printObj ;
		   
		   PrintThread(PrintObj printObj ){
			   this.printObj = printObj;
		   }
	 
		   @Override
		   public void run(){
			   //根据动作打印不同的小票
		    producerService.sendMessage( printObj );
			//0.正常下单
			//1.加菜下单
		   }
	   }
	   
public class WeigthThread  implements Runnable{
	
	PrintObj printObj ;
	
	WeigthThread(PrintObj printObj ){
		this.printObj = printObj;
	}
	
	@Override
	public void run(){
		//根据动作打印不同的小票
		printCommonService.setDestination(printDishQueue);
		printCommonService.sendMessage( printObj );
		//0.正常下单
		//1.加菜下单
	}
}

	   public class PrintMutiThread  implements Runnable{
		   
		   PrintObj printObj ;
		   
		   PrintMutiThread(PrintObj printObj ){
			   this.printObj = printObj;
		   }
	 
		   @Override
		   public void run(){
			   //根据动作打印不同的小票
			   mutilService.sendMessage( printObj );
			//0.正常下单
			//1.加菜下单
		   }
	   }
	   
	   public class PrintDishSetThread implements Runnable{
		   
		   PrintObj printObj;
		   
		   public PrintDishSetThread(PrintObj printObj ) {
			   this.printObj = printObj;
		   }

		   @Override
		   public void run() {
			   dishSetService.sendMessage(printObj);
		   }
		   
	   }
	   
	   public class PrintCustThread  implements Runnable{
		   
		   PrintObj printObj ;
		   
		   PrintCustThread(PrintObj printObj ){
			   this.printObj = printObj;
		   }
	 
		   @Override
		   public void run(){
			   //根据动作打印不同的小票
			   custService.sendMessage( printObj );
			//0.正常下单
			//1.加菜下单
		   }
	   }

	 
	@Autowired
	TorderDetailMapper  torderDetailMapper;
	
	@Autowired
	TableService  tableService;
	
	@Autowired
	TorderMapper  torderMapper;
	
	@Autowired
	OrderService  orderService;
	
	@Autowired
	DishService  dishService;
	
	@Autowired
	PrinterService  printerService;
	
	@Autowired
	TableAreaService  tableAreaService;
	
	@Autowired
	ToperationLogService  toperationLogService;
	
//	@Autowired
//	OrderDetailService  orderDetailService;
		
	@Autowired
	private NormalDishProducerService producerService;
	
	@Autowired
	private MutilDishProducerService  mutilService;
	
	
	@Autowired
	private CustDishProducerService custService;
	
	@Autowired
	TbPrintObjDao  tbPrintObjDao;

    @Autowired
    TbPrinterManagerDao  tbPrinterManagerDao;
    @Autowired
    PrintCommonServiceImpl  printCommonService;

	@Autowired
	@Qualifier("weightQueue")
	private Destination printDishQueue;
	
	@Autowired
	 DataSourceTransactionManager transactionManager ;
	@Autowired
	@Qualifier("t_userService")
	UserService userService ;
	
	@Autowired
	private DishSetProducerService dishSetService;

	@Autowired
	StatentMentProducerService statentMentProducerService;  
}
