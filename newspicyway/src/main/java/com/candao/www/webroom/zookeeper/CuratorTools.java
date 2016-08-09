package com.candao.www.webroom.zookeeper;
import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.model.SynSqlObject;

 


/**
 * 基本例子
 * **/
public class CuratorTools {
	
	static CuratorFramework zkclient=null;
	static String nameSpace =   PropertiesUtils.getValue("zookeeper.root.node");
	static String zooKeeperUrl = PropertiesUtils.getValue("zookeeper.server");
	static String CHARSET = "utf-8";
	
	private static Log log = LogFactory.getLog(CuratorTools.class);
	
	static {
		  String zkhost = zooKeeperUrl;//zk的host
		  RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);//重试机制
		  Builder builder = CuratorFrameworkFactory.builder().connectString(zkhost)
				  .connectionTimeoutMs(5000)
				  .sessionTimeoutMs(5000)
				  .retryPolicy(rp);
		  builder.namespace(nameSpace);
		  CuratorFramework zclient = builder.build();
		  zkclient = zclient;
		  zkclient.start();// 放在这前面执行
		  zkclient.newNamespaceAwareEnsurePath(nameSpace);
		  
//		  createWatcher();
	}
	
	
	public static void main(String[] args)throws Exception {
		CuratorTools ct=new  CuratorTools();
		//ct.getListChildren("/zk/bb");
		//ct.upload("/jianli/123.txt", "D:\\123.txt");
		//ct.createrOrUpdate("/zk/cc334/zzz","c");
		//ct.delete("/qinb/bb");
		//ct.checkExist("/zk");
		ct.read("/jianli/123.txt");
		zkclient.close();
	}
	
	/**
	 * 添加watcher 
	 * 对于不同分支的数据节点，增加不同的观察者获取到数据
	 */
	public static void createWatcher(){
//		 zkclient.getData().watched();
//		 zkclient.setData().usingWatcher( new  Watcher() {  
//	          @Override   
//	          public   void  process(WatchedEvent event) {  
//	             System.out.println( "node is changed" );  
//	         }  
//	     }).inBackground().forPath( "/test" );   
		 
		 PathChildrenCache cache = new PathChildrenCache(zkclient, nameSpace, false); 
		    try {
				cache.start();
			} catch (Exception e) {
				log.error("创建数据节点发生错误 ["+ e.getMessage() +"]");
			} 

		System.out.println("监听开始/root........"); 
		PathChildrenCacheListener plis = new PathChildrenCacheListener() { 

		@Override 
		public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception { 
		  switch ( event.getType() ) 
		                { 
		                    case CHILD_ADDED: 
		                    { 
		                        System.out.println("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath())); 
		                        break; 
		                    } 

	          	case CHILD_UPDATED: 
		                    { 
		                        System.out.println("Node changed: " + ZKPaths.getNodeFromPath(event.getData().getPath())); 
		                        break; 
		                    } 

		                    case CHILD_REMOVED: 
		                    { 
		                        System.out.println("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath())); 
		                        break; 
		                    }
		default:
			break; 
		                } 

		   } 
		}; 
		//注册监听 
		cache.getListenable().addListener(plis); 
//		try {
//			cache.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}   
     } 
 
	/**
	 * 创建或更新一个节点
	 * 
	 * @param path 路径
	 * @param content 内容
	 * **/
	public static  void createrOrUpdate(String path,SynSqlObject content)throws Exception{
		zkclient.newNamespaceAwareEnsurePath(path).ensure(zkclient.getZookeeperClient());
	 
		ZkDqConsumer consumer = new ZkDqConsumer();
		ZkDqSerializer serializer = new ZkDqSerializer();
		DistributedQueue<SynSqlObject> queue;
		QueueBuilder<SynSqlObject> builder = 
				QueueBuilder.builder(zkclient,consumer, serializer, path);
		queue = builder.buildQueue();
		queue.start();
		queue.put(content);
		
//	    zkclient.setData().forPath(path,content.getBytes());	
	    System.out.println("添加成功！！！");
	}
	/**
	 * 删除zk节点
	 * @param path 删除节点的路径
	 * 
	 * **/
	public static void delete(String path)throws Exception{
		zkclient.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
		System.out.println("删除成功!");
	}
	/**
	 * 判断路径是否存在
	 * @param path
	 * **/
	public static void checkExist(String path)throws Exception{
		if(zkclient.checkExists().forPath(path)==null){
			System.out.println("路径不存在!");
		}else{
			System.out.println("路径已经存在!");
		}
	}
	/**
	 * 读取的路径
	 * @param path
	 * **/
	public static String read(String path )throws Exception{
		String data = new String(zkclient.getData().forPath(path),CHARSET);
		System.out.println("读取的数据:"+ data);
		return data;
	}
	/**
	 * @param path 路径
	 * 获取某个节点下的所有子文件
	 * */
	public static void getListChildren(String path)throws Exception{
		List<String> paths=zkclient.getChildren().forPath(path);
		for(String p:paths){
			System.out.println(p);
		}
	}
	/**
	 * @param zkPath zk上的路径
	 * @param localpath 本地上的文件路径
	 * 
	 * **/
//	public static void upload(String zkPath,String localpath)throws Exception{
//		createrOrUpdate(zkPath, "");//创建路径
//		byte[] bs=FileUtils.readFileToByteArray(new File(localpath));
//		zkclient.setData().forPath(zkPath, bs);
//		System.out.println("上传文件成功！");
//	}

	public static void createQueue(String string) {
		// TODO Auto-generated method stub
		
	}
}