package com.candao.print.listener;

import org.springframework.stereotype.Service;

import com.candao.print.entity.PrintObj;

@Service
public class AddDishListener {

	public void handleMessage(PrintObj  object) {
		 
	}
// 
//	/**
//	 *  
//	 * @param message
//	 * @return
//	 */
//	public String receiveMessage(String message) {
//		System.out.println("ConsumerListener receiveMessage" + message);
//		return "ConsumerListener receiveMessage ";
//	}
// 
//	public String receiveMessage(PrintObj  object) {
//		
////		  return	addDishPrintServiceImpl.print(object.getPrintType(), object.getMessageMsg());
//		return null;
//			
////			 producerService.sendMessage(responseDestination, "ok-----");
//	 }
//	
//	@Autowired
//	private JmsTemplate jmsTemplate;
//	@Autowired
//	@Qualifier("responseQueue")
//	private Destination responseDestination;
//	
//	@Autowired
//	AddDishPrintServiceImpl addDishPrintServiceImpl;
//	
//	@Autowired
////	@Qualifier("producerService")
//	private NormalDishProducerService producerService;
// 
	
}
