package com.candao.file.pool;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.file.common.Constant;
import com.candao.file.fastdfs.ClientGlobal;
import com.candao.file.fastdfs.ProtoCommon;
import com.candao.file.fastdfs.TrackerClient;
import com.candao.file.fastdfs.TrackerGroup;
import com.candao.file.fastdfs.TrackerServer;

/**
 * 
 *  <pre>
 * 
 * Copyright : Copyright  Pandoranews 2014 ,Inc. All right
 * Company : 凯盈资讯科技有限公司
 * </pre>
 * @author  tom
 * @version 1.0
 * @date 2014年9月26日 上午11:06:21
 * @history
 *
 */
public class ConnectionPool {

	static LoggerHelper logger = LoggerFactory.getLogger(ConnectionPool.class);
	
	// busy connection instances
	private  static ConcurrentHashMap<TrackerServer, Object> busyConnectionPool = null;
	// idle connection instances
//	private static ArrayBlockingQueue<TrackerServer> idleConnectionPool = null;
	
	private static  BlockingQueue<TrackerServer> idleConnectionPool = new LinkedBlockingQueue<TrackerServer>(2);  
	  
	// delay lock for initialization
	
	

	// the connection string for ip
	private static  String tgStr = null;
	// the server port
	private static int port = 22122;
	// the limit of connection instance
	private static  int size = 3;
	
	private static Object obj = new Object();
	
	//heart beat
	HeartBeat beat=null;

	public ConnectionPool(String tgStr, int port, int size) {
		this.tgStr = tgStr;
		this.port = port;
		this.size = size;
//		init();
		//注册心跳
		beat = new HeartBeat(this);
		beat.beat();
	}
	
	public ConnectionPool() {
//		init();
		//注册心跳
		beat = new HeartBeat(this);
		beat.beat();
	}
	
   static {
	   init();
   }
   
	/**
	 * init the connection pool
	 * 
	 * @param size
	 */
	private static void init() {
		initClientGlobal();
		busyConnectionPool = new ConcurrentHashMap<TrackerServer, Object>();
		idleConnectionPool = new ArrayBlockingQueue<TrackerServer>(size);
		//TrackerServer trackerServer = null;
		try {
			TrackerClient trackerClient = new TrackerClient();
			for (int i = 0; i < size; i++) {
				TrackerServer trackerServer = trackerClient.getConnection();
				 ProtoCommon.activeTest(trackerServer.getSocket());
					idleConnectionPool.put(trackerServer);
			   }
		} catch (IOException e) {
			e.printStackTrace();
		}  catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
//			if (trackerServer != null) {
//				try {
//					trackerServer.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
 
	public static  TrackerServer checkout(int waitTimes) throws InterruptedException {
		TrackerServer client1 = idleConnectionPool.take();
		if (client1 == null) {
//			logger.debug("TrackerServerPool wait time out ,return null","");
//			throw new NullPointerException(
//					"ImageServerPool wait time out ,return null");
//			busyConnectionPool.put(client1, obj);
			init();
		}
		
		return client1;
	}
 
	public static void checkin(TrackerServer client1) {
//		if (idleConnectionPool.contains(client1)) {
			try {
				idleConnectionPool.put(client1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//		}
	}
 
	public static synchronized void drop(TrackerServer trackerServer) {
		// first less connection
		//删除一个无效的连接，如果得到新连建也是无效，则启动detector线程，用于检测什么时候可以正常连接起来
		//一旦检查成功，将相应属性修改hasConnectionException修改为false，释放先前的连接，并重新建立连接池。
		try {
			trackerServer.close();
		} catch (IOException e1) {
		}
		if (busyConnectionPool.remove(trackerServer) != null) {
			try {
				logger.debug("TrackerServerPool drop a connnection","");
				logger.debug("ImageServerPool size:"
						+ (busyConnectionPool.size() + idleConnectionPool
								.size()),"");
				TrackerClient trackerClient = new TrackerClient();
				trackerServer = trackerClient.getConnection();
			} catch (IOException e) {
				trackerServer = null;
				logger.debug( "trackerServerPool getConnection generate exception","");
				e.printStackTrace();
			} finally {
				if(!isContinued(trackerServer)){
					return;
				}
				//变成传过数据的
				try {
					 ProtoCommon.activeTest(trackerServer.getSocket());
					idleConnectionPool.add(trackerServer);
					logger.debug("trackerServerPool add a connnection","");
					logger.debug("trackerServerPool size:"
							+ (busyConnectionPool.size() + idleConnectionPool
									.size()),"");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static boolean isContinued(TrackerServer trackerServer){
		if (trackerServer == null && hasConnectionException) {
			return false;
		}
		if (trackerServer == null) {
			hasConnectionException = true;
			// only a thread;
			detector();
			init();
		}
		if (hasConnectionException) {
			//代表detector正在运行，就算获得连接，也要等detector做完
			return false;
		}
		return true;
	}

	private static void detector() {

		new Thread() {
			@Override
			public void run() {
				String msg = "detector connection fail to "+tgStr;
				while (true) {
					TrackerServer trackerServer = null;
					TrackerClient trackerClient = new TrackerClient();
					try {
						trackerServer = trackerClient.getConnection();
						Thread.sleep(5000);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally{
						if(trackerServer!=null){
							msg="detector connection success to "+tgStr;
							break;
						}
						logger.debug("trackerServerPool    has size:"
								+ (busyConnectionPool.size() + idleConnectionPool
										.size()),"");
						logger.debug(msg,"");
					}
				}
				logger.debug(msg,"");
 
				
				if(idleConnectionPool.size()!=0){
					logger.debug("idleConnectionPool start close trackerserver","");
					logger.debug(msg,"");

					for(int i=0;i < size;i++){
						TrackerServer ts=idleConnectionPool.poll();
						if(ts!=null){
							try {
								ts.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				//re init
				hasConnectionException=false;
				//init();
			}
		}.start();
	}

	static boolean hasConnectionException = false;

	private static void initClientGlobal() {
		
		
		InetSocketAddress[] trackerServers = new InetSocketAddress[1];
		
		if(tgStr == null || "".equals(tgStr.trim())){
			String [] trackerConfig = Constant.TRACKERSERVER.split(",");
			trackerServers = new InetSocketAddress[trackerConfig.length];
			for(int i = 0;i<trackerConfig.length;i++){
				trackerServers[i] =  new InetSocketAddress(trackerConfig[i].split(":")[0], Integer.parseInt(trackerConfig[i].split(":")[1]));
			}
		}else {
			 trackerServers = new InetSocketAddress[1];
			 trackerServers[0] = new InetSocketAddress(tgStr, port);
		}
		
		ClientGlobal.setG_tracker_group(new TrackerGroup(trackerServers));
		ClientGlobal.setG_connect_timeout(Integer.parseInt(Constant.TRACKERSERVER_CONNET_TIMEOUT) * 1000);
		ClientGlobal.setG_network_timeout(Integer.parseInt(Constant.TRACKERSERVER_NETWORK_TIMEOUT) * 1000);
		ClientGlobal.setG_anti_steal_token(false);
		ClientGlobal.setG_charset(Constant.TRACKERSERVER_CHARSET);
		ClientGlobal.setG_secret_key(null);
 }

	public BlockingQueue<TrackerServer> getIdleConnectionPool() {
		return idleConnectionPool;
	}
	
}