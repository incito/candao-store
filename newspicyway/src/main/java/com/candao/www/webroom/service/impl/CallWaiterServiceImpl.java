package com.candao.www.webroom.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.candao.www.utils.DataServerUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.IdentifierUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.CallWaiterDao;
import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.data.dao.TbUserInstrumentDao;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.model.TbMessage;
import com.candao.www.data.model.TbUserInstrument;
import com.candao.www.data.model.Torder;
import com.candao.www.webroom.service.CallWaiterService;
import com.candao.www.webroom.service.ComplainService;
import com.candao.www.webroom.service.TableService;

import net.sf.json.JSONObject;

/**
 * 呼叫服务员Service接口实现类
 *
 * @author Administrator
 */
@Service
public class CallWaiterServiceImpl implements CallWaiterService {

    private static final Logger logger = LoggerFactory.getLogger(CallWaiterServiceImpl.class);

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5000));

    private String touStr = "1";

    @Autowired
    private CallWaiterDao callWaiterDao;

    @Autowired
    private TbDataDictionaryDao tbDataDictionaryDao;

    @Autowired
    TableService tableService;

    @Autowired
    private ComplainService complainService;

    @Autowired
    TorderMapper torderMapper;

    @Autowired
    private TbUserInstrumentDao tbUserInstrumentDao;

    @Override
    public int saveCallInfo(JSONObject data) {
        long begintime = System.currentTimeMillis();
        int returnnum = -1;
        if (data == null) {
            return returnnum;
        }

        if (!data.containsKey("msgType") || data.getString("msgType") == null || data.getString("msgType").equals("")) {
            return returnnum;
        }

        if (!data.containsKey("tableno") || data.getString("tableno") == null || data.getString("tableno").equals("")) {
            return returnnum;
        }
        if (!data.containsKey("userid") || data.getString("userid") == null || data.getString("userid").equals("")) {
            return returnnum;
        }
        if (!data.containsKey("orderid") || data.getString("orderid") == null || data.getString("orderid").equals("")) {
            return returnnum;
        }
        if (!data.containsKey("deviceType") || data.getString("deviceType") == null || data.getString("deviceType").equals("")) {
            return returnnum;
        }
        if (!data.containsKey("deviceNo") || data.getString("deviceNo") == null || data.getString("deviceNo").equals("")) {
            return returnnum;
        }
        if (!data.containsKey("callStatus") || data.getString("callStatus") == null || data.getString("callStatus").equals("")) {
            return returnnum;
        }
        System.out.println("1:" + (System.currentTimeMillis() - begintime));
        String msgType = data.getString("msgType");
        String orderid = data.getString("orderid");
        String callStatus = data.getString("callStatus");
        String tableno = data.getString("tableno");
        if (msgType.equals(touStr) && callStatus.equals("1")) {//投诉保存投诉信息
            if (!data.containsKey("complaintType") || data.getString("complaintType") == null || data.getString("complaintType").equals("")) {
                return 3;
            }
        }
        //首先查询有没有对应的呼叫类型的信息
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("msgType", msgType);
        paramMap.put("orderid", orderid);
        paramMap.put("type", "RESPONSETIME");
        TbMessage message = callWaiterDao.queryCallInfo(paramMap);
        List<Map<String, String>> dList = tbDataDictionaryDao.find(paramMap);
        int outtime = 180;
        if (dList != null && dList.size() == 3) {
            for (Map<String, String> timeset : dList) {
                if (timeset.containsKey("itemid") && timeset.get("itemid").toString().equals(msgType)) {
                    outtime = timeset.containsKey("item_value") ? getNum(timeset.get("item_value").toString()) : 180;
                    break;
                }
            }
        }
        System.out.println("3:" + (System.currentTimeMillis() - begintime));
        if (message == null && callStatus.equals("1")) {//没有此类型的呼叫信息，保存
            message = getSaveInfo(data, outtime);
            returnnum = callWaiterDao.saveCallInfo(message);
            if (msgType.equals(touStr)) {
                if (!data.containsKey("complaintOpinion") || data.getString("complaintOpinion").equals("")) {
                    data.put("complaintOpinion", "投诉");
                }
                data.put("messageid", message.getId());
                returnnum = complainService.saveComplain(data);
                returnnum = returnnum == -1 ? 3 : returnnum;
            }
        } else if (message != null && message.getCallStatus().equals("3") && callStatus.equals("1")) {//记录是已经响应的状态，并且请求不是响应的数据，新增
            TbMessage saveinfo = getSaveInfo(data, outtime);
            message = saveinfo;
            returnnum = callWaiterDao.saveCallInfo(saveinfo);
            if (msgType.equals(touStr)) {
                if (!data.containsKey("complaintOpinion") || data.getString("complaintOpinion").equals("")) {
                    data.put("complaintOpinion", "投诉");
                }
                data.put("messageid", message.getId());
                returnnum = complainService.saveComplain(data);
                returnnum = returnnum == -1 ? 3 : returnnum;
            }
        } else if (message != null && callStatus.equals("2") && !message.getCallStatus().equals("3")) {//催促修改数据
            message.setCallNums(message.getCallNums() + 1);
            message.setUrgeNums(message.getUrgeNums() + 1);
            message.setCallOverTime(new Date());
            message.setCallStatus("2");
            returnnum = callWaiterDao.updateCallInfo(message);
        } else if (message != null && callStatus.equals("3") && !message.getCallStatus().equals("3")) {//响应修改数据
            message.setResponseNums(1);
            message.setCallStatus("3");
            message.setReplyTime(new Date());
            message.setCallOverTime(new Date());
            int timeoutDuration = (int) (new Date().getTime() - message.getTimeoutTime().getTime()) / 1000;
            if (timeoutDuration > 0) {
                message.setTimeoutDuration(timeoutDuration);
            }
            returnnum = callWaiterDao.updateCallInfo(message);
        } else {
            returnnum = 99;
        }
        if (returnnum > 0 && returnnum != 99) {
            executor.execute(new WiterThread(tableno, msgType, callStatus, message.getId()));
        }
        return returnnum;
    }

    /**
     * 查找最近桌子的服务员的编号
     *
     * @return
     */
    @Override
    public String findrelateUserid(List<Map<String, Object>> retableList, String tableno) {
        String useridStr = "";
        int tablenoint = parInt(tableno);
        if (tablenoint <= 0) {
            return useridStr;
        }
        //没有查询到同一区域的桌子信息
        if (retableList == null || retableList.size() <= 0) {
            return useridStr;
        }
        //遍历获取所有的桌子的订单信息
        Set<String> tableiddSet = new HashSet<String>();

        //桌子id与桌子编号的对应关系
        Map<String, Integer> tableidInfoMap = new HashMap<String, Integer>();
        for (Map<String, Object> map : retableList) {
            if (map == null || !map.containsKey("tableid") || !map.containsKey("tableNo")) {
                continue;
            }
            String tableid = String.valueOf(map.get("tableid"));
            if (StringUtils.isBlank(tableid)) {
                continue;
            }
            String tableNo = String.valueOf(map.get("tableNo"));
            if (StringUtils.isBlank(tableNo)) {
                continue;
            }
            if (parInt(tableNo) <= 0) {
                continue;
            }
            tableiddSet.add(tableid);
            tableidInfoMap.put(tableid, parInt(tableNo));
        }

        if (tableiddSet.size() <= 0) {
            return useridStr;
        }
        //查询所有的订单信息
        Map<String, Object> ordermap = new HashMap<String, Object>();
        ordermap.put("ids", new ArrayList<String>(tableiddSet));
        List<Torder> orderList = torderMapper.findontimeOrdersByTableids(ordermap);
        //获取所有登录的手环信息
        Map<String, Object> instrumentmap = new HashMap<String, Object>();
        instrumentmap.put("status", "0");
        List<TbUserInstrument> listuser = tbUserInstrumentDao.findByParams(instrumentmap);
        if (orderList == null || orderList.size() <= 0) {
            return useridStr;
        }
        if (listuser == null || listuser.size() <= 0) {
            return useridStr;
        }
        //遍历所有登录手环的服务员信息，将服务员编号放入集合中
        Set<String> onlineUserSet = new HashSet<String>();
        for (TbUserInstrument instrment : listuser) {
            if (instrment == null) {
                continue;
            }
            String userid = instrment.getUserid();
            if (StringUtils.isBlank(userid)) {
                continue;
            }
            onlineUserSet.add(userid);
        }
        int absolutetableno = 10000;
        //遍历所有的订单信息，获取桌号信息，并通过绝对值的大小判断是否是最近的桌号
        for (Torder order : orderList) {
            if (order == null || StringUtils.isBlank(order.getCurrenttableid()) || StringUtils.isBlank(order.getUserid())) {
                continue;
            }
            //桌号是否可以转换成数字，不能转换成数字的桌号，暂时不在排序范围之内
            int temptableno = tableidInfoMap.containsKey(order.getCurrenttableid()) ? tableidInfoMap.get(order.getCurrenttableid()) : 0;
            if (temptableno <= 0) {
                continue;
            }
            //判断当前订单的服务员的手环是否在线
            String tempuserid = order.getUserid();
            if (!onlineUserSet.contains(tempuserid)) {
                continue;
            }
            //取与已知桌绝对值最小并且手环在线的服务员的编号进行推送消息
            int temp = Math.abs(temptableno - tablenoint);
            if (temp < absolutetableno) {
                absolutetableno = temp;
                useridStr = tempuserid;
            }

        }
        return useridStr;

    }

    private TbMessage getSaveInfo(JSONObject data, int outtime) {
        String msgType = data.getString("msgType");
        String orderid = data.getString("orderid");
        String callStatus = data.getString("callStatus");
        String id = IdentifierUtils.getId().generate().toString();
        String tableno = data.getString("tableno");
        String userid = data.getString("userid");
        String deviceType = data.getString("deviceType");
        String deviceNo = data.getString("deviceNo");
        TbMessage message = new TbMessage();
        message.setMsgType(msgType);
        message.setOrderid(orderid);
        message.setCallStatus(callStatus);
        message.setId(id);
        message.setTableno(tableno);
        message.setUserid(userid);
        message.setDeviceType(deviceType);
        message.setDeviceNo(deviceNo);
        message.setInserttime(new Date());
        message.setStartTime(new Date());
        message.setUrgeNums(0);
        message.setTimeoutTime(getDate(outtime));
        message.setCallNums(1);
        return message;
    }

    private Date getDate(int outtime) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.SECOND, outtime);
        date = calendar.getTime();
        return date;
    }

    private int getNum(String numstr) {
        try {
            return Integer.parseInt(numstr) < 1 ? 1 : Integer.parseInt(numstr);
        } catch (Exception ex) {
            logger.error("-->", ex);
        }
        return 1;
    }

    private int parInt(String numstr) {
        try {
            return Integer.parseInt(numstr);
        } catch (Exception ex) {
            logger.error("-->", ex);
        }
        return 0;
    }

    @Override
    public void updateCallStatus(String orderid) {
        try {
            callWaiterDao.updateCallInfoStatus(orderid);
        } catch (Exception ex) {
            logger.error("-->", ex);
        }
    }

    class WiterThread implements Runnable {
        private Logger logger = LoggerFactory.getLogger(WiterThread.class);
        private boolean restarted = false;
        private int times = 0;
        String finaltableno;
        String finalmsgType;
        String finalcallStatus;
        String finalmessid;

        public WiterThread(String finaltableno, String finalmsgType, String finalcallStatus, String finalmessid) {
            this.finaltableno = finaltableno;
            this.finalmsgType = finalmsgType;
            this.finalcallStatus = finalcallStatus;
            this.finalmessid = finalmessid;
        }

        @Override
        public void run() {
            StringBuilder messageinfo = new StringBuilder(Constant.TS_URL + Constant.MessageType.msg_2001 + "/");
            Map<String, Object> params = new HashMap<String, Object>(1);
            params.put("tableNo", finaltableno);
            List<Map<String, Object>> tableList = tableService.find(params);
            if (tableList != null && tableList.size() > 0) {
                String orderinfoid = String.valueOf(tableList.get(0).get("orderid"));
                Torder torder = torderMapper.get(orderinfoid);
                if (torder != null && torder.getUserid() != "") {
                    String userid = torder.getUserid();
                    Map<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("status", "0");
                    map1.put("userid", userid);
                    List<TbUserInstrument> listuser = tbUserInstrumentDao.findByParams(map1);
                    if (listuser != null && listuser.size() > 0) {
                        //服务员还在线
                        //服务员编号|消息类型|区号|台号|消息id
                    } else {
                        //服务员退出了,找到同一区的服务员 进行推送
                        String areaid = String.valueOf(tableList.get(0).get("areaid"));
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("areaid", areaid);
                        map.put("status", "1");
                        List<Map<String, Object>> retableList = tableService.find(map);
                        String useridstr = findrelateUserid(retableList, finaltableno);
                        if (useridstr != null && !useridstr.equals("")) {
                            userid = useridstr;
                        }
                    }
                    String areaname = null;
                    String tableNo = null;
                    try {
                        areaname = java.net.URLEncoder.encode(String.valueOf(tableList.get(0).get("areaname")), "utf-8");
                        tableNo = java.net.URLEncoder.encode(finaltableno, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        logger.error("-->", e);
                        e.printStackTrace();
                    }
                    if (!userid.equals("")) {
                        messageinfo.append(userid + "|" + finalmsgType + "|" + finalcallStatus + "|" + areaname + "|" + tableNo + "|" + finalmessid);
                        //new TsThread(messageinfo.toString()).start();
                        URL urlobj;
                        try {
                            urlobj = new URL(messageinfo.toString());
                            URLConnection urlconn = urlobj.openConnection();
                            urlconn.connect();
                            InputStream myin = urlconn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
                            String content = reader.readLine();
                            /**
                             * 如果DataServer推送异常(特征值判断)，则重启Dataserver后重新推送
                             */
                            if (null != content && content.contains("Access violation")) {
                                restartAndRetry();
                            }
                            JSONObject object = JSONObject.fromObject(content.trim());
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> resultList = (List<Map<String, Object>>) object.get("result");
                            if ("1".equals(String.valueOf(resultList.get(0).get("Data")))) {
                                System.out.println("推送成功");
                            } else {
                                System.out.println("推送失败");
                            }
                        } catch (IOException e) {
                            restartAndRetry();
                            logger.error("-->", e);
                            e.printStackTrace();
                        }
                    }
                }
            }
            //根据动作打印不同的小票
        }

        private void restartAndRetry() {
            //已重启过则放弃
            if (restarted) {
                return;
            }
            restarted = true;
            if (DataServerUtil.restart()) {
                run();
            } else {
                logger.error("尝试重启DataServer失败");
            }
        }
    }
}
