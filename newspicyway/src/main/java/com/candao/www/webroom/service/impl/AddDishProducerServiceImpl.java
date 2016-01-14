package com.candao.www.webroom.service.impl;
//package com.candao.www.webroom.service.impl;
//
//import javax.jms.Destination;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Session;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.core.MessageCreator;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import com.candao.newspicyway.printqueue.service.AddDishProducerService;
//import com.candao.www.webroom.model.PrintObj;
//
//@Component
//@Service
//public class AddDishProducerServiceImpl implements AddDishProducerService {
//
//	@Autowired
//	private JmsTemplate jmsTemplate;
////	@Autowired
////	@Qualifier("addDishQueue")
//	private Destination responseDestination;
//	
//	public void sendMessage(final String message) {
//		System.out.println("--------------- 开始打印-----------------"+message);
//		jmsTemplate.send(responseDestination, new MessageCreator() {
//			public Message createMessage(Session session) throws JMSException {
//				/*TextMessage textMessage = session.createTextMessage(message);
//				textMessage.setJMSReplyTo(responseDestination);
//				return textMessage;*/
//				return session.createTextMessage(message);
//			}
//		});
//	}
////	
//	public void sendMessage( final PrintObj obj) {
//	 
//		 if(obj == null){
//			 return ;
//		 }
//		
//		 
//		jmsTemplate.convertAndSend(responseDestination, obj);
//		
////		 jmsTemplate.send(destination, new MessageCreator() {   
////	            public Message createMessage(Session session) throws JMSException {   
////	                ObjectMessage objectMessage = session.createObjectMessage(obj);   
////	                objectMessage.setJMSReplyTo(responseDestination);   
////	                return objectMessage;   
////	            }   
////	        });   
//     	//jmsTemplate.send(responseDestination, messageCreator);setJMSReplyTo(responseDestination);  
//		
////		jmsTemplate.send(responseDestination, new MessageCreator() {  
////            public Message createMessage(Session session) throws JMSException {  
////                TextMessage textMessage = session.createTextMessage();//createTextMessage(message);  
////                textMessage.setText("ok");
////                return textMessage;  
////            }  
////        });  
////		
//		
////		jmsTemplate.execute(new SessionCallback<Object>() {
////
////			public Object doInJms(Session session) throws JMSException {
////				MessageProducer producer = session.createProducer(destination);
////				Message message = session.createObjectMessage(obj);
////				producer.send(message);
////				return null;
////			}
////			
////		});
//		
////		jmsTemplate.execute(new ProducerCallback<Object>() {
////
////			public Object doInJms(Session session, MessageProducer producer)
////					throws JMSException {
////				Message message = session.createObjectMessage(obj);
////				producer.send(destination, message);
////				return null;
////			}
////			
////		});
//	}
//	 
//}
