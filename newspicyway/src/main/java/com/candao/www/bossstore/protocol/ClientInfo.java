package com.candao.www.bossstore.protocol;

/**
 * Created by IntelliJ IDEA.
 * User: johnson
 * Date: 6/30/15
 * Time: 10:40 上午
 */
public class ClientInfo {

    // 基本字段
    private String command         = ""; // 请求命令
    private String imei            = ""; // 手机标识
    private String imsi            = ""; // 手机标识
    private String softwareVersion = ""; // 版本号
    private String coopId          = ""; // 渠道号
    private String machineId       = ""; // 机型
    private String phonenum        = ""; // 手机号
    private String platform        = ""; // 平台(android|iPhone|塞班)
    private String isCompress      = ""; // 是否压缩,1为压缩
    private String isemulator      = ""; // 是否是模拟器
    private String phoneSIM        = ""; // SIM卡手机号
    private String mac             = ""; // 网卡地址
    private String ip              = ""; // ip

    private String username = ""; // 用户名

    private String branchId = "";


    //类型
    private String requestType     = ""; // 请求类型
    private String transactionType = ""; // 交易类型


    private String type = "";// 查询类型
    private String test = "";

    private String sysSessionid = "";

    private String email = "";// 邮箱

    private String oldPwd = "";// 旧密码
    private String pwd    = "";// 密码

    private String userId = "";// 用户id


    private String token   = "";//握手协议编码
    private String codeKey = "";// 验证码key

    private String currDay = "";// 当天时间
    private String currMon = "";// 当月时间

	private String data;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getCoopId() {
		return coopId;
	}

	public void setCoopId(String coopId) {
		this.coopId = coopId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getIsCompress() {
		return isCompress;
	}

	public void setIsCompress(String isCompress) {
		this.isCompress = isCompress;
	}

	public String getIsemulator() {
		return isemulator;
	}

	public void setIsemulator(String isemulator) {
		this.isemulator = isemulator;
	}

	public String getPhoneSIM() {
		return phoneSIM;
	}

	public void setPhoneSIM(String phoneSIM) {
		this.phoneSIM = phoneSIM;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getSysSessionid() {
		return sysSessionid;
	}

	public void setSysSessionid(String sysSessionid) {
		this.sysSessionid = sysSessionid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}

	public String getCurrDay() {
		return currDay;
	}

	public void setCurrDay(String currDay) {
		this.currDay = currDay;
	}

	public String getCurrMon() {
		return currMon;
	}

	public void setCurrMon(String currMon) {
		this.currMon = currMon;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	

}
