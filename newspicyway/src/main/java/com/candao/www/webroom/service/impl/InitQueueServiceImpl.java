//package com.candao.www.webroom.service.impl;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//import javax.jms.Destination;
//
//import org.springframework.stereotype.Service;
//
///**
// * 
// *  <pre>
// * 
// * Copyright : Copyright  Pandoranews 2015 ,Inc. All right
// * Company : 凯盈资讯科技有限公司
// * </pre>
// * @author  tom_zhao
// * @version 1.0
// * @date 2015年5月7日 下午7:17:44
// * @history
// *
// * 根据分店号码创建队列
// */
//
////@Service
//public class InitQueueServiceImpl {
//
////	@Autowired
////	SingleConnectionFactory connectionFactory;
//	
////	@Autowired
////	PooledConnectionFactory   connectionFactory; 
////	
////	@Autowired
////	BranchShopService  branchService ;
////	
////	@Autowired
////	ShopMgService  shopMgService;
//	
//    private static  ConcurrentHashMap<String, Destination> map = new ConcurrentHashMap<String, Destination>();
//    
//    public static  ConcurrentHashMap<String, Destination> mapBranch = new ConcurrentHashMap<String, Destination>();
//    
//    
//    public  Destination getInstance(String destName) {
//	         return map.get(destName);
//	 } 
//	    
////	@PostConstruct
//	public void initQueue(){
//		
////		String isBranch = PropertiesUtils.getValue("isbranch");
////		if(isBranch != null && "N".equals(isBranch) ){
////			try {
////				List<Tbranchshop> branchlist = branchService.getAllListList();
////				Connection   conn = connectionFactory.createConnection();
////				 // 创建Session对象，不开启事务
////		        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
////		     // 创建目标，可以是 Queue 或 Topic
////		
////		        for(Tbranchshop branch : branchlist){
////		        	 Destination destination = session.createQueue("queue_"+branch.getId());
////				     map.put("branchcode", destination);
////				     mapBranch.put(branch.getId(), destination);
////		        }
////		        
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
////		}else if(isBranch != null && "Y".equals(isBranch)){
////			try {
////				Connection   conn = connectionFactory.createConnection();
////				 // 创建Session对象，不开启事务
////		        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
////		        
////		        Map<String ,Object>  branchInfo = shopMgService.getBranchInfo();
////		        String branchId = (String)branchInfo.get("branchid");
////		   
////			    MessageListenerAdapter adapter = new MessageListenerAdapter();
////			    adapter.setDelegate(new QueueMessageListener());
////			    adapter.setDefaultListenerMethod("receiveMessage");
////			     Destination dest_response = session.createQueue("queue_"+branchId);
////			     DefaultMessageListenerContainer  listenerContainer = new DefaultMessageListenerContainer();
////			     listenerContainer.setConnectionFactory(connectionFactory);
////			     listenerContainer.setDestination(dest_response);	
////			     listenerContainer.setMessageListener(adapter);
////		        
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
////		}
//	}
//	
//	public  ConcurrentHashMap<String, Destination> getBranchMap(){
//		  return  mapBranch;
//	}
//}
