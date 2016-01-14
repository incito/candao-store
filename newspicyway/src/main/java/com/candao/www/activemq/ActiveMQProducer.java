//package com.candao.www.activemq;
//
//import javax.jms.DeliveryMode;
//import javax.jms.MessageProducer;
//import javax.jms.Session;
//import javax.jms.TextMessage;
//
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.broker.Connection;
//import org.apache.activemq.broker.region.Destination;
//
//class ActiveMQProducer implements Runnable {
//    public void run() {
//        try {
//            // 创建连接工厂
//            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
//            
//            // 创建JMS连接实例，并启动连接
//            Connection connection = connectionFactory.createConnection();
//            connection.start();
//            
//            // 创建Session对象，不开启事务
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            
//            // 创建目标，可以是 Queue 或 Topic
//            Destination destination = session.createQueue("ling.wcaccepted");
//            
//            // 创建生成者
//            MessageProducer producer = session.createProducer(destination);
//            
//            // 设置消息不需持久化。默认消息需要持久化
//            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//            
//            // 创建文本消息
//            TextMessage message = session.createTextMessage("Hello World!");
//            
//            // 发送消息。non-persistent 默认异步发送；persistent 默认同步发送
//            producer.send(message);
//            
//            // 关闭会话和连接
//            producer.close();
//            session.close();
//            connection.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//}