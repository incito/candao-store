package com.candao.www.timedtask;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.candao.common.compress.impl.GzipCompress;
import com.candao.common.dao.SynDataTools;
import com.candao.common.dto.ResultDto;
import com.candao.common.enums.ErrorMessage;
import com.candao.common.enums.Module;
import com.candao.common.enums.ResultMessage;
import com.candao.common.exception.SysException;
import com.candao.common.utils.CompressOperate;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.FileOperate;
import com.candao.common.utils.HttpUtil;
import com.candao.common.utils.MD5;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.StringUtils;
import com.candao.www.data.dao.BranchDataSynDao;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.permit.service.SendService;
import com.candao.www.webroom.model.SynSqlObject;
import com.candao.www.webroom.service.BranchProducerService;
import com.candao.www.webroom.service.BranchShopService;
import com.candao.www.weixin.utils.HttpOperate;

/**
 * 
 * <pre>
 * 
 * Copyright : Copyright  Pandoranews 2015 ,Inc. All right
 * Company : 上海餐道互联网金融服务有限公司
 * </pre>
 * 
 * @author tom_zhao
 * @version 1.0
 * @date 2015年6月1日 下午9:03:43
 * @history
 *
 */
@Service
public class BranchDataSyn {

	private static final Logger logger = LoggerFactory
			.getLogger(BranchDataSyn.class);

	@Autowired
	BranchDataSynDao branchDataSynDao;
	@Autowired
	private SendService sendService;

	@Autowired
	TbBranchDao branchDao;

	// @Autowired
	// ZkDqQueuer zkDqQueuer;
	@Autowired
	BranchShopService branchShopService;

	@Autowired
	BranchProducerService service;
	// 保存压缩文件到磁盘的线程池,上传失败短信发送通知现场
	private ExecutorService singleThreadPool = Executors
			.newSingleThreadExecutor();

	// 存储压缩包的压缩包名
	private final String SQL_FILE_NAME = "sql.gz";
	//需要同步数据的controller
	private final String CONTROLLER = "/dataDealController";
	
	public static final ThreadLocal<Map<String,Object>> TL = new ThreadLocal<Map<String,Object>>();

	public boolean synBranchData() throws Exception {
		synData();
		return updateSynRecord();
	}

	//凌晨1-9点定时执行没有同步起的数据重传
	public void reSynData() {
		logger.info("reSynData-start");
		int bizFlag = branchDataSynDao.checkLastSynDataFinish();
		if (bizFlag > 0) {
			try {
				//获取同步数据的传送方式
				String type = PropertiesUtils.getValue("SYN_DATA_TYPE");
				logger.info("上传方式type:"+type);
				if(type.equals("1"))
					syn();
				else if(type.equals("2"))
					synLocalData();
				else
					synData();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		logger.info("reSynData-end");
	}

	/**
	 * 
	 * @throws IOException
	 * @throws HttpException
	 * @throws Exception
	 * @Description:同步分店的数据到总店
	 * @create: 余城序
	 * @Modification: void
	 */
	public ResultDto synLocalData() throws SysException {
		logger.info("synLocalData-start");
		int bizFlag = branchDataSynDao.checkBizData();
		ResultDto dto = null;
		// 表示已经结业或未开业状态
		if (bizFlag == 0) {
			// 获取分店id
			String branchId = PropertiesUtils.getValue("current_branch_id");
			logger.info("分店id:"+branchId);
			// 如果分店id存在
			if (branchId != null) {
				// 添加同步记录
				addSynRecord();
				// 获取需要同步的表
				String[] tables = getSynTables();
				// 需要存储的sql
				String sql = getSynSql(tables);
				// 同步数据到总店
				String result = synData(sql, branchId);
				//结果集处理
				dto = resultDeal(result);
			} else {
				throw new SysException(ErrorMessage.NO_BRANCH_ID, Module.LOCAL_SHOP);
			}
		}else{
			throw new SysException(ErrorMessage.NO_CLOSE_SHOP, Module.LOCAL_SHOP);
		}
		logger.info("synLocalData-end:" + dto);
		return dto;
	}
	
	public ResultDto synData() throws SysException, ParseException{
		logger.info("synData-start");
		int bizFlag = branchDataSynDao.checkBizData();
		ResultDto dto = null;
		// 表示已经结业或未开业状态
		if (bizFlag == 0) {
			// 获取分店id
			String branchId = PropertiesUtils.getValue("current_branch_id");
			logger.info("分店id:"+branchId);
			// 如果分店id存在
			if (branchId != null) {
				// 添加同步记录
				addSynRecord();
				// 获取需要同步的表
				String[] tables = getSynTablesFromMaster();
				//执行数据上传
				executeSyn(tables,dto,branchId);
			} else {
				throw new SysException(ErrorMessage.NO_BRANCH_ID, Module.LOCAL_SHOP);
			}
		}else{
			throw new SysException(ErrorMessage.NO_CLOSE_SHOP, Module.LOCAL_SHOP);
		}
		logger.info("synData-end:" + dto);
		return dto;
	
	}
	
	//分天执行数据同步数据上传
	private void executeSyn(String[] tables,ResultDto dto,String branchId) throws SysException, ParseException{
			//需要处理的结果
			String result = "";
			// 获取开业日期 和结业日期
			Map<String,Object> map = getDate();
			String openDate = map.get("opendate").toString();
			Calendar startCalendar = (Calendar)map.get("startCalendar");
			String CompletionDate = map.get("enddate").toString();
			//获取开业时间到结业时间这段时间的天数集合
			List<String> list = DateUtils.getDateDayArrayByParams(openDate, CompletionDate);
			// 需要存储的对象
			Map<String,List<Map<String,String>>> datas = null;
			//如果是当天结业
			if(list.size() == 1){
				datas = getSynData(tables,openDate,CompletionDate);
				//同步数据到总店
				result = synData(datas, branchId);
				//结果集处理
				dto = resultDeal(result);
			//如果是跨天结业
			}else{
				for(int i=0;i<list.size();i++){
					String date[] = getStartAndEndDate(startCalendar);
					//如果是第一天
					if(i == 0){
						datas = getSynData(tables,openDate,date[1]);
					//如果是最后一天
					}else if(i == list.size() - 1){
						datas = getSynData(tables,date[0],CompletionDate);
					//如果是中间天数
					}else{
						datas = getSynData(tables,date[0],date[1]);
					}
					// 同步数据到总店
					result = synData(datas, branchId);
					//结果集处理
					dto = resultDeal(result);
				}
			}
		}
	// 获取开业日期 和结业日期
	private Map<String,Object> getDate() throws ParseException{
		Map<String,Object> map = TL.get();
		if(map == null){
			map.putAll(branchDataSynDao.getBizDate());
		}
		String openDate = map.get("opendate").toString();
		Calendar startCalendar = DateUtils.StringToCalendar(openDate);
		map.put("startCalendar", startCalendar);
		return map;
	}
		/**
		 * 
		 * @Description:获取每天00:00:01和23:59:59秒的时间
		 * @create: 余城序
		 * @Modification:
		 * @param str 当天时间
		 * @return String[]
		 */
		private String[] getStartAndEndDate(Calendar cal){
			Date date = cal.getTime();
			String[] str = new String[2];
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			str[0] = sdf.format(date) + "00:00:01";
			str[1] = sdf.format(date) + "23:59:59";
			return str;
		}
	
	//门店同步数据成功后结果的处理
	private ResultDto resultDeal(String result) throws SysException{
		ResultDto dto = null;
		if(!"".equals(result)){
			dto = JSON.parseObject(result, ResultDto.class);
		}
		if(dto.getCode().equals(ResultMessage.SUCCESS.getCode())){
			Integer id = branchDataSynDao.getMaxId();
			updateSynRecord(id.toString());
		}else{
			//云端执行失败,通知相关人员
			singleThreadPool.execute(new Runnable() {
				public void run() {
					sendErrSms();
				}
			});
			throw new SysException(ErrorMessage.SYNDATA_FAIL, Module.LOCAL_SHOP);
		}
		return dto;
	}
	//上传失败短信通知
	private void sendErrSms(){
		//云端执行失败,通知相关人员
		String value = PropertiesUtils.getValue("UPDATA_ERR_MOBILE");
		String[] mobiles = value.split(",");
		Map<String,String> map;
		Map<String, Object> branchInfo = branchDao.getBranchInfo();
		//获取分店名称
		String branchName = branchInfo.get("branchname").toString();
		for(String mobile : mobiles){
			try {
				 map = new HashMap<String,String>();
				 map.put("{client}", branchName);
				sendService.sendMessageBySms(mobile, "/template/UptoDataErrSms.template",map);
			} catch (IOException e) {
				logger.error("短信发送失败");
			}
		}
	}

	public void syn() throws Exception {
		int bizFlag = branchDataSynDao.checkBizData();
		// bizFlag = 0;
		// 表示已经结业或未开业状态
		if (bizFlag == 0) {
			// 只有结业状态菜同步数据
			// bizFlag = branchDataSynDao.checkSynDataFinish();
			// bizFlag = 1 表示 还没有同步
			String branchId = PropertiesUtils.getValue("current_branch_id");
			if (branchId != null) {
				try {

					Map<String, String> mapValue = new HashMap<String, String>();
					mapValue.put("branchid",
							PropertiesUtils.getValue("current_branch_id"));
					mapValue.put("datapath", "暂时不用");
					branchDataSynDao.insertSynRecord(mapValue);

					String need_syn_tables = PropertiesUtils
							.getValue("need_syn_tables");
					String[] tables = need_syn_tables.split(",");
					String[] tablesCopy = new String[tables.length - 1];
					String synTable = null;
					if (tables != null) {
						System.arraycopy(tables, 0, tablesCopy, 0,
								tables.length - 1);
						synTable = tables[tables.length - 1];
					}
					Map branchinfo = branchDao.getBranchInfo();
					// 获取开业日期 和结业日期
					Map<String, String> bizMap = branchDataSynDao.getBizDate();
					String openDate = bizMap.get("opendate");
					String endDate = bizMap.get("enddate");

					int sequenceNo = 0;
					for (String table : tables) {
						String listsql = branchDataSynDao.getSynSql(table,
								SynDataTools.getConditionSql(table, openDate,
										endDate));
						if (listsql != null && !"".equals(listsql)) {
							List<String> pageSql = StringUtils.split(listsql,
									";", Integer.valueOf(PropertiesUtils
											.getValue("MSGSIZE")));

							if (pageSql != null && !pageSql.isEmpty()) {
								for (String it : pageSql) {
									SynSqlObject synSqlObject = new SynSqlObject();
									synSqlObject.setId(UUID.randomUUID()
											.toString());
									synSqlObject.setBranchid(branchId);
									synSqlObject
											.setTenantid((String) branchinfo
													.get("tenantid"));
									synSqlObject.setSql(it);
									synSqlObject.setFlag("1");
									synSqlObject.setSequenceNo(""
											+ sequenceNo++);
									service.sendMessage(synSqlObject);
								}
							}
						}

					}

					// branchDataSynDao.updateBizLog();

					// 同步分店 状态
					String listsql = branchDataSynDao.getSynSql(synTable,
							SynDataTools.getConditionSql(synTable, openDate,
									endDate));

					if (listsql != null && !"".equals(listsql)) {
						List<String> pageSql = StringUtils.split(listsql, ";",
								Integer.valueOf(PropertiesUtils
										.getValue("MSGSIZE")));

						if (pageSql != null && !pageSql.isEmpty()) {
							for (String it : pageSql) {
								SynSqlObject synSqlObject = new SynSqlObject();
								synSqlObject
										.setId(UUID.randomUUID().toString());
								synSqlObject.setBranchid(branchId);
								synSqlObject.setTenantid((String) branchinfo
										.get("tenantid"));
								synSqlObject.setSql(it);
								synSqlObject.setFlag("1");
								synSqlObject.setSequenceNo("" + sequenceNo++);
								service.sendMessage(synSqlObject);
							}
						}
					}

					// branchDataSynDao.transferToHistory();

					// branchDataSynDao.deleteSynRecord();

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new Exception(e.getMessage());
				}

			} else {
				throw new RuntimeException("门店未配置current_branch_id");
			}
		}
	}

	private boolean updateSynRecord() {
		return branchDataSynDao.updateSynRecord(null) > 0;
	}
	
	private boolean updateSynRecord(String id) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(id, "id");
		return branchDataSynDao.updateSynRecord(map) > 0;
	}

	public void deleteRecord() {
		branchDataSynDao.transferToHistory();
		branchDataSynDao.deleteSynRecord();
	}

	public SynSqlObject wrapSqlObjec(String branchId, String directoryPath) {
		SynSqlObject synSqlObject = new SynSqlObject();

		try {

			StringBuffer sb = new StringBuffer("");
			FileReader reader = new FileReader(directoryPath);
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str + "/n");

			}
			synSqlObject.setBranchid(branchId);
			synSqlObject.setGenerattime(DateUtils.dateToString(new Date()));
			synSqlObject.setSqltext(sb.toString());
			synSqlObject.setStatus(0);

			br.close();
			reader.close();

			return synSqlObject;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws HttpException
	 * @Description:上传数据
	 * @create: 余城序
	 * @Modification:
	 * @param data
	 *            需要上传的json类型的字符串
	 * @return String 返回的结果
	 */
	public String upToData(String data) throws SysException {
		logger.info("upToData-start:" + data);

		// 创建压缩对象
		CompressOperate compress = new CompressOperate(new GzipCompress());
		// 获取压缩后的字符串
		final byte[] bt = compress.getCompress(data);

		data = JSON.toJSONString(bt);

		Map<String, String> map = new HashMap<String, String>();
		// 存放要传输的数据
		map.put("data", data);
		// 存放传输数据的大小
		map.put("size", String.valueOf(data.length()));
		// 存放字符串校验码
		map.put("code", MD5.md5(data));

		String url = PropertiesUtils.getValue("MASTER_URL");

		// 上传数据到总店
		String result = HttpOperate.post(url, map);

		logger.info("upToData-end:" + result);
		return result;
	}

	/**
	 * 
	 * @Description:同步数据到总店
	 * @create: 余城序
	 * @Modification:
	 * @param sql
	 *            需要同步的sql
	 * @param branchId
	 *            分店id
	 * @throws HttpException
	 * @throws IOException
	 *             void
	 */
	private String synData(String sql, String branchId) throws SysException {
		// 创建需要同步的数据
		SynSqlObject synSqlObject = createSynData(sql, branchId);
		// 将数据转换成json,禁用循环引用检测
		String json = JSON.toJSONString(synSqlObject);
		// 上传数据到总店
		String result = upToData(json);

		return result;
	}
	
	private String synData(Map<String,List<Map<String,String>>> datas, String branchId) throws SysException {
		// 创建需要同步的数据
		SynSqlObject synSqlObject = createSynData(datas, branchId);
		// 将数据转换成json
		String json = JSON.toJSONString(synSqlObject);
		// 上传数据到总店
		String result = upToData(json);

		return result;
	}
	
	/**
	 * 
	 * @Description:创建要同步数据的对象
	 * @create: 余城序
	 * @Modification:
	 * @param sql 需要同步的sql
	 * @param branchId 分店id
	 * @return SynSqlObject
	 */
	private SynSqlObject createSynData(String sql, String branchId) {
		SynSqlObject synSqlObject = new SynSqlObject();
		setSynSqlObject(synSqlObject);
		synSqlObject.setBranchid(branchId);
		synSqlObject.setSql(sql);
		return synSqlObject;
	}
	
	private SynSqlObject createSynData(Map<String,List<Map<String,String>>> datas, String branchId) {
		SynSqlObject synSqlObject = new SynSqlObject();
		setSynSqlObject(synSqlObject);
		synSqlObject.setBranchid(branchId);
		synSqlObject.setSynDatas(datas);
		return synSqlObject;
	}
	
	private void setSynSqlObject(SynSqlObject synSqlObject){
		Map branchinfo = branchDao.getBranchInfo();
		synSqlObject.setId(UUID.randomUUID().toString());
		synSqlObject.setTenantid((String) branchinfo.get("tenantid"));
		synSqlObject.setFlag("1");
		int sequenceNo = 0;
		synSqlObject.setSequenceNo("" + sequenceNo++);
	}

	/**
	 * 
	 * @Description:获取需要同步的数据库表
	 * @create: 余城序
	 * @Modification:
	 * @return String[] 数据库表数组
	 */
	private String[] getSynTables() {
		String need_syn_tables = PropertiesUtils.getValue("need_syn_tables");
		String[] tables = need_syn_tables.split(",");
		
		logger.info("需要同步的表数量:" + tables.length);

		return tables;
	}
	/**
	 * 
	 * @Description:从云端获取需要同步的数据库表
	 * @create: 余城序
	 * @Modification:
	 * @return String[]
	 * @throws SysException 
	 */
	private String[] getSynTablesFromMaster() throws SysException{
		String masterUrl = PropertiesUtils.getValue("cloud.host");
		masterUrl = masterUrl + CONTROLLER + "/synTables.do";
		String tables = HttpOperate.post(masterUrl,null);
		logger.info("获取需要同步的表:" + tables);
		return tables.split(",");
	}

	// 添加同步记录
	private int addSynRecord() {
		Map<String, String> mapValue = new HashMap<String, String>();
		mapValue.put("branchid", PropertiesUtils.getValue("current_branch_id"));
		mapValue.put("datapath", "暂时不用");
		return branchDataSynDao.insertSynRecord(mapValue);
	}
	
	private Map<String,List<Map<String,String>>> getSynData(String[] tables,String startDate,String endDate) throws SysException{
		Map<String,List<Map<String,String>>> tableMap = new HashMap<String,List<Map<String,String>>>(32);
		List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
		for (String table : tables) {
			try{
				datas = branchDataSynDao.getSynData(table,
						SynDataTools.getConditionSql(table, startDate, endDate));
			}catch(SysException e){
				if(e.getCode().equals(ErrorMessage.SQLEXE_ERROR))
					logger.info("数据库表不存在",e);
				else
					throw e;
			}
			tableMap.put(table, datas);
		}
		return tableMap;
	}

	// 获取需要同步的sql
	private String getSynSql(String[] tables) throws SysException{
		// 获取开业日期 和结业日期
		Map<String, String> bizMap = branchDataSynDao.getBizDate();
		String openDate = bizMap.get("opendate");
		String endDate = bizMap.get("enddate");
		Date date = DateUtils.stringToDate(endDate);
		//需要同步的sql
		String synSql = "";
		// 压缩文件存放路径
		String fileName = "/" + date.getTime()
				+ SQL_FILE_NAME;
		String path = PropertiesUtils.getValue("COMPRESS_DATA_PATH");
		String url = path + "/" + fileName;
		final File file = FileOperate.createFile(path, fileName);
		final CompressOperate compress = new CompressOperate(new GzipCompress());
		//判断是否存在sql数据压缩包
		if (!isExistCompress(url)) {
			//------------------执行数据打包在本地 start----------------------
			// 拼接需要同步的sql
			StringBuffer sqlBuf = new StringBuffer();
			for (String table : tables) {
				String sql = branchDataSynDao.getSynSql(table,
						SynDataTools.getConditionSql(table, openDate, endDate));
				sqlBuf.append(sql);
			}
			final String sql = sqlBuf.toString();
			synSql = sql;
			// 异步保存sql压缩文件
			singleThreadPool.execute(new Runnable() {
				public void run() {
					try {
						compress.compressTo(sql, file);
					} catch (SysException e) {
						logger.error("压缩文件失败",e);
					}
				}
			});
			//------------------执行数据打包在本地 end----------------------
		}else{
			//直接获取已打包的数据
			synSql = compress.getUnCompress(file);
		}
		logger.info("获取的需要同步的sql:"+synSql);
		return synSql;
	}

	/**
	 * 
	 * @Description:判断是否存在最近一次已经打包并且未结业的压缩文件
	 * @create: 余城序
	 * @Modification:
	 * @param path
	 *            压缩包路径
	 * @return boolean
	 */
	private boolean isExistCompress(String path) {
		return FileOperate.isExist(path);
	}
	
}
