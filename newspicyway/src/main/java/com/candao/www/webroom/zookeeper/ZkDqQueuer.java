package com.candao.www.webroom.zookeeper;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.ensemble.fixed.FixedEnsembleProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.retry.RetryOneTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.constant.Constant;
import com.candao.www.webroom.model.SynSqlObject;


@Service
public class ZkDqQueuer {
	
	
	Log log  = LogFactory.getLog(ZkDqQueuer.class);
	
	
	
	@Autowired
	@Qualifier("appconfig")
	private Properties properties;
	
	public   final String NAMESPACE =   "zookeeper.root.node";
	
	public   final String ZOOKEEPERURL =  "zookeeper.server";
	
	public   final String ZOOKEEPERQUEUE =  "zookeeper.server.queue";
	
	public   final String ISBRANCH =  "isbranch";
	
	DistributedQueue<SynSqlObject> queue;

	CuratorFramework client ;
	
	public ZkDqQueuer() throws Exception {
	 }
	
//	@PostConstruct
	public void init(){
		 client = CuratorFrameworkFactory.builder()
				.retryPolicy(new RetryOneTime(10)).namespace(properties.getProperty(NAMESPACE))
				.ensembleProvider(new FixedEnsembleProvider(properties.getProperty(ZOOKEEPERURL)))
				.build();
		client.start();

		//如果是分店会执行下面的代码
		
		 String isbranch = properties.getProperty(ISBRANCH);
	     if("Y".equalsIgnoreCase(isbranch)){
                String yumQueue = properties.getProperty(ZOOKEEPERQUEUE);//
				ZkDqConsumer consumer = new ZkDqConsumer();
				ZkDqSerializer serializer = new ZkDqSerializer();
				QueueBuilder<SynSqlObject> builder;
				try {
					builder = QueueBuilder.builder(client,consumer, serializer, yumQueue);
					queue = builder.buildQueue();
					queue.start();
				} catch (Exception e1) {
					log.error("初始化zookeeper 队列失败 =["+e1.getMessage()+"]");
				}
	 
	     }
	}
	
	/**
	 * 创建或更新一个节点
	 * 
	 * @param path 路径
	 * @param content 内容
	 * **/
	public   void createrOrUpdate(String path,String content)throws Exception{
		client.newNamespaceAwareEnsurePath(path).ensure(client.getZookeeperClient());
	 
//		ZkDqConsumer consumer = new ZkDqConsumer();
//		ZkDqSerializer serializer = new ZkDqSerializer();
//	
//		QueueBuilder<String> builder = 
//				QueueBuilder.builder(client,consumer, serializer, path);
//		queue = builder.buildQueue();
//		queue.start();
//		queue.put(content);
		
//	    zkclient.setData().forPath(path,content.getBytes());	
	    System.out.println("添加成功！！！");
	}
	
	public void sendMsg(SynSqlObject msg){
		try {
			queue.put(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void queueMessages() throws Exception {
//		for (int i = 0; i < 10; i++) {
//			this.queue.put("testWork [" + i + "]");
//			System.out.println("Queued [" + i + "]");
//		}
//		Thread.sleep(5000);
//	}

}
