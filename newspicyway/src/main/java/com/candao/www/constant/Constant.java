package com.candao.www.constant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.candao.common.utils.PropertiesUtils;

public class Constant {
	
	public final static int SOCKET_DEFAULT_COUNT = 10;
	
	public final static int SOCKET_CHECK_TIME  = 3000;
	
  /**
   * 项目类的根路径 D:/Program%20Files/tomcat/webapps/项目名/WEB-INF/classes/ 
   * */
  public static final String PROJECT_CLASS_PATH = Constant.class.getClassLoader().getResource("/").getFile().replaceAll("%20", " "); 
 
  /**
   * webapps的路径 D:/Program%20Files/tomcat/webapps/
   *  */
  public static final String PROJECT_WEBAPPS_PATH = PROJECT_CLASS_PATH.replaceAll("webapps/.*", "webapps/");
  
  /**
   * 文件上传目录的路径 D:/Program%20Files/tomcat/upload/
   *  */
  public static final String PROJECT_UPLOAD_PATH = PROJECT_WEBAPPS_PATH+"upload/";
  
	public final static String UPLOAD_PATH = PropertiesUtils.getValue("upload_path");
	
	public final static String DEFAULT_TABLE_SORT = PropertiesUtils.getValue("DEFAULT_TABLE_SORT");
	
	/** 当前用户 */
	public final static String CURRENT_USER = "currentUser";
	/**
	 * 用户管理字典定义
	 */
	public final static String USER_DATADICTIONARY="USER";	
	/**
	 * 菜品分类字典管理
	 */
	public final static String DISH_DATADICTIONARY="DISH";
	
	/** 允许访问的按钮 */
	public final static String ALLOW_ACCESS_BUTTONS = "allowAccessButtons";
	
	public static final String url=PropertiesUtils.getValue("zip.path");
	/**
	 * 估清地址
	 */
	public static final String clearurl=PropertiesUtils.getValue("url");
	
	public static final String tempurl=PropertiesUtils.getValue("zip.temp.path");
	
    public static final String PHONEURL = PropertiesUtils.getValue("phoneurl");
	
	public static final String  KAIYING  =   "/kaiying/"; 
	
	public static final String TRACKERSERVER = PropertiesUtils.getValue("fastdfs.tracker_server");
	
	public static final String TRACKERSERVER_NETWORK_TIMEOUT = PropertiesUtils.getValue("fastdfs.network_timeout");
	
	public static final String TRACKERSERVER_CONNET_TIMEOUT = PropertiesUtils.getValue("fastdfs.connect_timeout");
	
	public static final String TRACKERSERVER_HTTP_PORT = PropertiesUtils.getValue("fastdfs.tracker_http_port");
	
	public static final String TRACKERSERVER_HTTP_TOKEN = PropertiesUtils.getValue("fastdfs.secret_key");
	
	public static final String TRACKERSERVER_CHARSET = PropertiesUtils.getValue("fastdfs.charset");
	// 门店代理文件服务器地址
	public static final String FILEURL_PREFIX = PropertiesUtils.getValue("fastdfs.url");
	// 云端文件服务器地址
	public static final String CLOUD_FILEURL_PREFIX = PropertiesUtils.getValue("cloud_fastdfs.url");
	public static final String INNERFILEURL_PREFIX = PropertiesUtils.getValue("fastdfs.innerurl");
	
	public static final String IMG_GROUP = PropertiesUtils.getValue("fastdfs.group.img");
	
	public static final String VIDEO_GROUP = PropertiesUtils.getValue("fastdfs.group.video");
 
	public static final String FAILUREMSG = "{\"result\":\"1\"}";
	
	public static final String FAILUROPEMSG = "{\"result\":\"2\"}";
	
	public static final String SUCCESSMSG = "{\"result\":\"0\"}";
	
	public static final String WEIXINSUCCESSMSG = "{\"result\":\"0\",\"msg\":\"反结算成功，已通过微信退款！\"}";
	
	public static final String PRINTERENCODE = "GBK";
	
	public static final String NOMSG = "无";
	
	public static final String SPLITER = "#";
	
	public static final int PRINTLENTH = 48;
	
	public static final String NAMESPACE =   PropertiesUtils.getValue("zookeeper.root.node");
	
	public static final String ZOOKEEPERURL = PropertiesUtils.getValue("zookeeper.server");
	
	public static final String ZOOKEEPERQUEUE = PropertiesUtils.getValue("zookeeper.server.queue");
	
	public static final String CHARSET = "utf-8";
 
	
	public class ORDERSTATUS{
		
		public  static final int  ORDER_STATUS = 0;
		
//		public  static final int ONE_SETTLED_STATUS = 1;
		
		public  static final int  INTERNAL_SETTLED_STATUS = 3;
		
//		public  static final int  ORDERING_STATUS = 4;
		
//		public  static final int  DISCARD_STATUS = 5;
	}
	
	public class TABLESTATUS {
		
		public  static final int  FREE_STATUS = 0;
		
		public  static final int  EAT_STATUS = 1;
		
		public  static final int  READY_STATUS = 3;
		
		public  static final int  SETTLED_STATUS = 4;
		
		public  static final int  DELETE_STATUS = 5;
	}
	
	public class  PRINTTYPE {
		
	    public  static  final int URGE_DISH = 0;
	    
	    public  static  final int DISCARD_DISH = 1;
	    
	    public  static  final int COOKIE_DISH = 2;
	    
	    public  static  final int NORMAL_DISH = 3;
	    
	    public  static  final int  ADD_DISH = 4;
	    
	    public  static  final int  NOW_DISH = 5;
	    
	}
	public class  INCOMETYPE{
		
	   public static final int   NORMALINCOME = 0 ;
	   
	   public static final int   	ODDCHANGE = 1;
	}
	
	public class PAYWAY {
		
		public static final int PAYWAY_CASH = 0;
		
		public static final int PAYWAY_BANK_CARD = 1;
		
		public static final int PAYWAY_MEMBER_CARD = 2;
		
		public static final int PAYWAY_COUPON_CARD = 3;
		
		public static final int PAYWAY_DISCOUNT = 4;
		
		public static final int DEBITE_ACCOUNT = 5;
		
		//优免
		public static final int PAYWAY_FREE = 6;
		
		//微信扫码支付
		
		public static final String PAYWAY_WEIXIN = "30";
	}
	
	public class SETTLE{
		public static final int NORMALSETTLE = 0;
		
		public static final int REBACKSETTLE = 1;
	}
	
	public class DISHBILLNAME {
	    
		public static final String URGEDISHNAME = "催菜单";
		
		public static final String URGEDISHNAME_ABBR  = "(催)";
		
		public static final String DISCARDDISHNAME = "退菜单";
		
		public static final String DISCARDDISHNAME_ABBR = "(退)";
		
		public static final String READYNAME = "备菜单";
		public static final String READY_ABBR = "(备菜)";
		
		public static final String NOWDISHNAME = "起菜单";
		
		public static final String NOWDISHNAME_ABBR  = "(即)";
		
		public static final String CALL_ABBR  = "(起菜)";
		
		public static final String ADDDISHNAME = "加单   客用";
		
		public static final String ADDDISHNAME_ABBR  = "(加)";
		
		public static final String WELCOMEMSG = "WELCOME";
		
		public static final String NORMALCUSTDISHNAME = "点单    客用单  ";
		
		public static final String CUSTNORMALDISHNAME = "厨打单";
		
		public static final String NORMALDISHNAME = "厨打单";
		
		public static final String CUSTADDDISHNAME = "加菜单";
		
		public static final String DISHSETNAME = "套餐";
		
		public static final String STATEMENTDISHNAME = "结账单";
 
		
	}
	/**
	 * 操作状态（1：下单;2 :退菜 3：并台  4换台） 
	 * @author admin
	 *
	 */
	public class operationType{
		/**
		 * 下单
		 */
		public static final int SAVEORDERINFOLIST = 1;
		/**
		 * 退菜
		 */
		public static final int DISCARDDISH = 2;
		/**
		 * 并台
		 */
		public static final int MERGETABLE = 3;
		/**
		 * 换台
		 */
		public static final int SWITCHTABLE = 4;
	}
	
	/**
	 * 优惠分类
	 * @author zhao
	 *
	 */
	public class CouponType{
	  /**
	   * 特价券
	   */
	  public static final String SPECIAL_TICKET = "01";
	  /**
     * 折扣券 
     */
    public static final String DISCOUNT_TICKET = "02";
    
    /**
     * 在线支付 
     */
    public static final String ONLINEPAY_TICKET = "09";
    
    /**
     * 代金券
     */
    public static final String VOUCHER = "03";
    /**
     * 礼品券
     */
    public static final String GIFT_CERTIFICATE = "04";
    /**
     * 团购券
     */
    public static final String GROUPON = "05";
    /**
     * 其它优惠
     */
    public static final String OTHER = "06";
    /**
     * 手工优免
     */
    public static final String HANDFREE = "07";
    /**
     * 内部优免
     */
    public static final String INNERFREE = "08";
	  
	}
	
	/**
	 * 优惠码初始编码
	 */
	public static final String PREFERENTIAL_INIT_CODE = "80001";
	/**
	 * 推送的url
	 */
	public static final String TS_URL = PropertiesUtils.getValue("ts_url");
	
	public class MessageType {
		// 用户之间的普通消息
		public static final String msg_1001 = "1001"; // 普通显示提示消息，通知消息
		public static final String msg_1002 = "1002"; // 结帐消息
		public static final String msg_1003 = "1003"; // 菜品沽清消息
		public static final String msg_1005 = "1005"; // 清除PAD开台信息消息，用于转台和其它 可以用于更换PAD后清除原PAD，参数是MEID
		public static final String msg_1006 = "1006"; // 刷新菜谱		
		public static final String msg_1007 = "1007"; // 菜品取消沽清消息			
		public static final String msg_9999 = "9999"; // 退出系统消息
		public static final String msg_2001 = "2001"; // 手环消息推送
		public static final String msg_2011 = "2011"; // 开发票消息推送
		public static final String msg_2003 = "2003"; // 手环登录消息推送
		public static final String msg_2101 = "2101"; // 社交 送菜信息
		public static final String msg_2102 = "2102"; // 社交 菜品处理信息
		public static final String msg_2103 = "2103"; // 送礼 消息推送
		
		public static final String msg_2104 = "2104"; // 微信推送
	}
	 
}
