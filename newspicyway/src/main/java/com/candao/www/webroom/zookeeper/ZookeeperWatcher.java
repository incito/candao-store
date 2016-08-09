package com.candao.www.webroom.zookeeper;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ZookeeperWatcher {

	private static Log log = LogFactory.getLog(ZookeeperWatcher.class);
	
//	public static Watcher getWatcher(){
//		rturn 
//	}
//		Watcher wh = new Watcher() {
//	        public void process(WatchedEvent event) {
//	            log.debug("触发回调watcher实例： 路径" + event.getPath() + " 类型："   + event.getType());
//
//	            if (event.getType() == EventType.None) {
//	                try { //
//	                        // 判断userauth权限是否能访问userpath
//	                    String auth_type = "digest";
//	                    zk.addAuthInfo(auth_type, userauth.getBytes());
//	                    zk.getData(userpath, null, null);
//	                } catch (Exception e) {
////	                    e.printStackTrace();
//	                    Log.AC_ERROR("get node faild:userpath=" + userpath
//	                            + ",auth=" + userauth + " e:" + e.getMessage());
//	                    return;
//	                }
//	                Log.AC_INFO("userpath=" + userpath + " userauth=" + userauth);
//	            }
//
//	            try {
//
//	                switchinfo = getallswitch(); // 更新userpath和匿名用户路径下的配置信息，监听这些节点
//
//	                // 监听用户路径节点
//	                log.debug("lesson user=" + userpath + " node...");
//	                zk.exists(userpath, true); // 监听匿名用户路径节点
//	                log.debug("lesson user=" + AnonymousUSERpath + " node...");
//	                zk.exists(AnonymousUSERpath, true);
//
//	                // 监听用户路径下的开关节点
//	                if (zk.exists(userpath, false) != null) {
//	                    Log.AC_DEBUG("lesson user=" + userpath
//	                            + " 's swich node...");
//	                    List<String> swnodes = zk.getChildren(userpath, true); //
//	                    // 监听switch层节点的变化
//	                    Iterator<String> it_sw = swnodes.iterator();
//	                    while (it_sw.hasNext()) {
//	                        String swpath = userpath + "/" + it_sw.next();
//	                        Log.AC_DEBUG("lesson user=" + swpath + " node...");
//	                        zk.exists(swpath, true);
//	                    }
//	                }
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	                Log.AC_ERROR("lesson znode error:" + e.getMessage());
//	            }
//	        }
	        
	        
//	    };
//	}
}
