package com.candao.www.security.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.exception.AuthException;
import com.candao.common.utils.MD5;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.dao.TbResourceDao;
import com.candao.www.data.dao.TbUserDao;
import com.candao.www.data.dao.UserDao;
import com.candao.www.data.model.TbResource;
import com.candao.www.data.model.TbUser;
import com.candao.www.data.model.User;
import com.candao.www.permit.common.Constants;
import com.candao.www.security.model.Credentials;
import com.candao.www.security.service.LoginService;
import com.candao.www.webroom.model.LoginInfo;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.WorkLogService;

@Service
public class LoginServiceImpl implements LoginService {
	//@Autowired
	//private TbUserDao tbUserDao;
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private WorkLogService workLogService;
	
	@Autowired
	private DataDictionaryService datadictionaryService;
	
	@Autowired
	TbResourceDao tbResourceDao;

	@Override
	public User authUser(Credentials credentials,int tag) throws AuthException {
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		if (!ValidateUtils.isEmpty(username) && !ValidateUtils.isEmpty(password)) {
			if(tag==1){//如果是后台就转成加密   （要求手机传过来的数据已经是md5加密）
			password = MD5.md5(password);
			}
			
			//TbUser user = userDao.getCurrentUser(username, password);
			User user=userDao.login(username, password);
//			Map<String,Object>  userMap = tbUserDao.getCurrentUser(username, password);
//			TbUser user = convertValue(userMap);
			if (user != null) {
				if (user.getStatus()==Constants.ENABLE) {
					return user;
				} else {
					throw new AuthException("您的账号已被停用。请联系管理员");
				}
			} else {
				throw new AuthException("账号或密码错误，请重新输入。");
			}
		} else {
			throw new AuthException("账号或密码不能为空。");
		}
	}
	
	@Override
	public User authPadUser(LoginInfo credentials,int tag,String loginType) throws AuthException {
		String username = credentials.getUsername();
		String password = credentials.getPassword();
//		TbUser user = new TbUser();
		
		if (!ValidateUtils.isEmpty(username) && !ValidateUtils.isEmpty(password)) {
			if(tag==1){//如果是后台就转成加密   （要求手机传过来的数据已经是md5加密）
			  password = MD5.md5(password);
			}
			
//			Map<String,Object>  userMap = tbUserDao.getCurrentUser(username, password);
			//TbUser user = tbUserDao.getCurrentUser(username, password);
			User user=userDao.login(username, password);
//			 user = convertValue(userMap);
			if (user != null) {
				if (user.getStatus()==Constants.DISABLE) {
					
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("userid", user.getId());
					params.put("resourcespath", loginType);
					List<TbResource> list = tbResourceDao.findByRoleid(params);
					if(list != null && list.size() > 0){
						return user;	
					}else {
						return null;
					}
					//添加日志
//					Tworklog tworklog = new Tworklog();
//					tworklog.setWorkid(UUID.randomUUID().toString());
//					List<Map<String, Object>> list = datadictionaryService.getDatasByType("WORKTYPE");
//					for(int i=0;i<list.size();i++){
//						if(list.get(i).get("itemDesc").equals("登陆")){
//							tworklog.setWorktype(list.get(i).get("itemid").toString());
//						};
//					}
//					tworklog.setUserid(username);
//					tworklog.setBegintime(new Date());
//					tworklog.setEndtime(new Date());
//					tworklog.setIpaddress("127.0.0.1");
//					tworklog.setStatus(1);
//					workLogService.saveLog(tworklog);
									
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			 return null;
		}
	}
	
	
	@Override
	public int existUser(LoginInfo loginInfo ){
		
		Map<String ,Object> param=new HashMap();
		param.put("exactFind", true) ;//是否是完全匹配
		param.put("jobNumber", loginInfo.getUsername());
		List l=userDao.queryUserList(param);
		if( null!=l && l.size()>0){
			return 0;
		}else {
			return 1;
		}
	}
	
//	private TbUser convertValue(Map<String,Object> userMap){
//		TbUser user = new TbUser();
//		//userId, userName, password, fullName, phone,  userType,createTime,status
//		user.setCreatetime(userMap.get("createTime") == null?null:DateUtils.toString((java.sql.Timestamp)userMap.get("createTime")));
//		user.setFullname(String.valueOf(userMap.get("fullName") == null?"":userMap.get("fullName")));
//		user.setPassword(String.valueOf(userMap.get("password") == null?"":userMap.get("password")));
//		user.setPhone(userMap.get("phone") == null? "":String.valueOf(userMap.get("phone")));
//		user.setStatus(userMap.get("status") == null? null:(int)userMap.get("status"));
//		user.setUserid(userMap.get("userId") == null? "":String.valueOf(userMap.get("userId")));
//		user.setUsername(userMap.get("userName") == null?"":String.valueOf(userMap.get("userName")));
//		user.setUsertype(userMap.get("userType") == null?"":String.valueOf(userMap.get("userType")));
//		return user;
//	}
}
