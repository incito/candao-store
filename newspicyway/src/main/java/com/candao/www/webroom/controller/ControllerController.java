package com.candao.www.webroom.controller;

import com.candao.www.constant.Constant;
import com.candao.www.utils.DataServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


/**
 *HTTP命令控制接口
 */
@Controller
@RequestMapping("/controller")
public class ControllerController {
	private static Logger logger= LoggerFactory.getLogger(ControllerController.class);

	/**
	 * 重启dataserver
	 *
	 * @return
	 */
	@RequestMapping(value = "/restartDataserver", produces = {"application/html;charset=UTF-8"})
	@ResponseBody
	public String restartDataServer() {
		return DataServerUtil.restart() ? "{\"result\":\"0\"}" : "{\"result\":\"1\"}";
	}

	static {
		/**
		 *开启一守护线程，用于持续检测dataserver的pad调用接口是否可用，如果不可用则重启dataserver
		 */
		Thread t = new Thread() {
			@Override
			public void run() {
				logger.info("【Dataserver监控线程】启动");
				if (org.springframework.util.StringUtils.isEmpty(Constant.TS_URL)) {
					logger.error("【Dataserver监控线程】ts_url未配置，退出线程");
					return;
				}
				String urlFlag = "datasnap/rest/TServerMethods1/";
				String[] split = Constant.TS_URL.split(urlFlag);
				String url = split[0] + urlFlag;
                /*要监控的接口*/
				String[] uris={"QueryBalance3/1/1","setMemberPrice2/1/1/1","broadcastok/1/1"};
				for (; ; ) {
					try {
						for(String uri:uris){
							String result = get(url + uri);
							if(!volidateResult(result)){
								logger.info("【Dataserver监控线程】结果解析失败，重启dataserver");
								DataServerUtil.restart();
								break;
							}
						}
					} catch (IOException e) {
						logger.info("【Dataserver监控线程】连接超时，重启dataserver");
						DataServerUtil.restart();
					}
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			private String get(String str) throws IOException {
				URL urlobj = new URL(str);
				URLConnection urlconn = urlobj.openConnection();
				urlconn.connect();
				InputStream myin = urlconn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
				String content = reader.readLine();
				return content;
			}

			private boolean volidateResult(String str) {
				if (null == str) {
					return true;
				}
				return !str.contains("Access violation");
			}
		};
//		t.setDaemon(true);
//		t.start();

	}
}
