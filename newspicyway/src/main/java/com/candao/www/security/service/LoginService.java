package com.candao.www.security.service;

import com.candao.common.exception.AuthException;
import com.candao.www.data.model.TbUser;
import com.candao.www.data.model.User;
import com.candao.www.security.model.Credentials;
import com.candao.www.webroom.model.LoginInfo;

public interface LoginService {
   /**
    * 用户登录认证
    * @param credentials 
    * @param tag  1:从后台登陆  2：从app登陆
    * @return
    * @throws AuthException
    */
	public User authUser(Credentials credentials,int tag) throws AuthException;
	
	
	/**
	 * 校验Pos 和 pad 端用户
	 * @author zhao
	 * @param credentials
	 * @param tag
	 * @return
	 * @throws AuthException
	 */
	public User authPadUser(LoginInfo credentials,int tag,String loginType) throws AuthException  ;
	
	
	/**
	 * 校验用户是否存在或正常
	 * @author zhao
	 * @param loginInfo
	 * @return
	 */
	public int existUser(LoginInfo loginInfo );
}
