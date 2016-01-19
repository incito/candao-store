package com.candao.file.common;

import com.candao.common.utils.PropertiesUtils;

 



public class Constant {
	
	public final static int SOCKET_DEFAULT_COUNT = 10;
	
	public final static int SOCKET_CHECK_TIME  = 3000;
	
	public final static String UPLOAD_PATH = PropertiesUtils.getValue("upload_path");;
	
	public static final String TRACKERSERVER = PropertiesUtils.getValue("fastdfs.tracker_server");
	
	public static final String TRACKERSERVER_NETWORK_TIMEOUT = PropertiesUtils.getValue("fastdfs.network_timeout");
	
	public static final String TRACKERSERVER_CONNET_TIMEOUT = PropertiesUtils.getValue("fastdfs.connect_timeout");
	
	public static final String TRACKERSERVER_HTTP_PORT = PropertiesUtils.getValue("fastdfs.tracker_http_port");
	
//	public static final String TRACKERSERVER_HTTP_TOKEN = PropertiesUtils.getValue("fastdfs.secret_key");
	
	public static final String TRACKERSERVER_CHARSET = PropertiesUtils.getValue("fastdfs.charset");
	
	public static final String FILEURL_PREFIX = PropertiesUtils.getValue("fastdfs.url");
	
	public static final String IMG_GROUP = PropertiesUtils.getValue("fastdfs.group.img");
	
	public static final String VIDEO_GROUP = PropertiesUtils.getValue("fastdfs.group.video");
	
	public static final String ISBRANCH =  PropertiesUtils.getValue("isbranch");
	
	 
}
