package com.candao.www.webroom.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.IdentifierUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.ManagerWatchDao;
import com.candao.www.data.dao.TbUserInstrumentDao;
import com.candao.www.data.model.AreaManager;
import com.candao.www.data.model.TbUserInstrument;
import com.candao.www.webroom.service.ManagerWatchService;

@Service
public class ManagerWatchServiceImpl implements ManagerWatchService {
	
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5000));

	private LoggerHelper logger = LoggerFactory.getLogger(ManagerWatchServiceImpl.class);
	
	@Autowired
	private ManagerWatchDao managerWatchDao;
	
	@Autowired
    private TbUserInstrumentDao tbUserInstrumentDao;
	
	/**
	 * 根据工号删除经理区域信息
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param jobNumber
	 */
	public int deleteAreaManager(String jobNumber){
		return managerWatchDao.deleteAreaManager(jobNumber);
	}
	
	/**
	 * 批量保存经理区域
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param ams
	 * @return
	 */
	public int batchInsertAreaManager(List<AreaManager> ams){
		return managerWatchDao.batchInsertAreaManager(ams);
	}
	
	/**
	 * 推送经理手环消息
	 * @author weizhifang
	 * @since 2016-3-22
	 * @param tableno
	 * @param userid
	 * @param status
	 * @return
	 */
	public int sendManagerWatchMsg(Map<String,String> params){
		String tableno = params.get("tableno");
		String userid = params.get("userid");
		String orderid = params.get("orderid");
		String callStatus = params.get("status");
		String msgType = Constant.MessageType.msg_2004;
		String randomeData = IdentifierUtils.getId().generate().toString();
		try {
			//查询当前桌子所在区域，由于一个区域可能有多个经理管理，所以可能返回多条经理数据
			List<Map<String,Object>> amList = managerWatchDao.queryAreaAndManagerJobNumber(orderid, userid, tableno);
			Map<String,Object> map = new HashMap<String,Object>();
	        boolean flag = false;
	        String areaname = managerWatchDao.getTableName(tableno);
	        areaname = java.net.URLEncoder.encode(areaname,"utf-8");
			tableno = java.net.URLEncoder.encode(tableno,"utf-8");
	        List<String> jobNumberList = new ArrayList<String>();
	        //如果当前区域有经理，推送到当前区域
			for(int i=0;i<amList.size();i++){
				Map<String,Object> aMap = amList.get(i);
				map.put("status", "0");
			    map.put("userid", aMap.get("job_number"));
			    //查询当前经理手环登录状态
				List<TbUserInstrument> listuser = tbUserInstrumentDao.findByParams(map);
				if(listuser.size() > 0){
					//当前区域经理手环登录，推送到该经理手环
					executor.execute(new WiterThread(tableno,msgType,callStatus,randomeData,tableno,amList.get(i).get("job_number").toString(),areaname));
					logger.info("超时消息推送到当前区域值班经理!", "1");
					flag = true;
				}else{
					jobNumberList.add(amList.get(i).get("job_number").toString());
				}
			}
			//当前区域经理手环未登录，或当前区域没有经理，推送到邻近其他经理手环
			if(flag == false || amList.size() == 0){
				List<Map<String,Object>> othersAreaMag = new ArrayList<Map<String,Object>>();
				if(jobNumberList.size() > 0){
					othersAreaMag = managerWatchDao.queryOthersAreaManager(jobNumberList);
				}else{
					othersAreaMag = managerWatchDao.queryAllAreaManager();
				}
				for(int j=0;j<othersAreaMag.size();j++){
					//查询其他区域经理手环登录状态
					Map<String,Object> oMap = othersAreaMag.get(j);
					oMap.put("status", "0");
					oMap.put("userid", oMap.get("job_number"));
					List<TbUserInstrument> listOtherUser = tbUserInstrumentDao.findByParams(oMap);
					if(listOtherUser.size() > 0){
						//推送其他区域经理手环状态
						executor.execute(new WiterThread(tableno,msgType,callStatus,randomeData,tableno,othersAreaMag.get(j).get("job_number").toString(),areaname));
						logger.info("超时消息推送到其他区域值班经理!", "1");
					}else{
						logger.info("门店没有值班经理，手环超时消息不推送!", "1");
						return 1;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	class WiterThread implements Runnable {
		   String finaltableno;
    	   String finalmsgType;
    	   String finalcallStatus;
    	   String finalmessid;
    	   String tableno;
    	   String userid;
    	   String areaname;
    	
		   public WiterThread(String finaltableno,String finalmsgType,String finalcallStatus,String finalmessid,String tableno,String userid,String areaname){
			   this.finaltableno = finaltableno;
			   this.finalmsgType = finalmsgType;
			   this.finalcallStatus = finalcallStatus;
			   this.finalmessid = finalmessid;
			   this.tableno = tableno;
			   this.userid = userid;
			   this.areaname = areaname;
		   }
		   
		   @Override
		   public void run(){
	        	StringBuilder messageinfo = new StringBuilder(Constant.TS_URL+finalmsgType+"/");
				messageinfo.append(userid+"|"+finalcallStatus+"|"+finalmsgType+"|"+areaname+"|"+tableno+"|"+finalmessid);
				URL urlobj;
				try {
					urlobj = new URL(messageinfo.toString());
					URLConnection	urlconn = urlobj.openConnection();
					urlconn.connect();
					InputStream myin = urlconn.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
					String content = reader.readLine();
					JSONObject object = JSONObject.fromObject(content.trim());
					@SuppressWarnings("unchecked")
					List<Map<String,Object>> resultList = (List<Map<String, Object>>) object.get("result");
					if("1".equals(String.valueOf(resultList.get(0).get("Data")))){
						System.out.println("经理手环消息推送成功");
					}else{
						System.out.println("经理手环消息推送失败");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		   }
	}
}
