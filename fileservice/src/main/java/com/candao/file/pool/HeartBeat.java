package com.candao.file.pool;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.file.fastdfs.ProtoCommon;
import com.candao.file.fastdfs.TrackerServer;



public class HeartBeat {
	
	LoggerHelper logger = LoggerFactory.getLogger(HeartBeat.class);
	
	private ConnectionPool pool = null;
	
	public HeartBeat(ConnectionPool pool){
		this.pool=pool;
	}

	public void beat(){
		TimerTask task=new TimerTask() {
			@Override
			public void run() {
				BlockingQueue<TrackerServer> idleConnectionPool = pool.getIdleConnectionPool();
				TrackerServer ts=null;
				logger.debug("ConnectionPool execution HeartBeat to fdfs server","");
				for(int i=0;i<idleConnectionPool.size();i++){
					try {
						ts=idleConnectionPool.poll(waitTimes,
								TimeUnit.SECONDS);
						if(ts!=null){
							 ProtoCommon.activeTest(ts.getSocket());
							idleConnectionPool.add(ts);
						}else{
							break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						pool.drop(ts);
						e.printStackTrace();
					}finally{
					}
				}
			}
		};
		Timer timer=new Timer();
		timer.schedule(task, ahour, ahour);
	}
	public static int ahour=1000*60*60*1;
	public static int waitTimes=0;
	
}
