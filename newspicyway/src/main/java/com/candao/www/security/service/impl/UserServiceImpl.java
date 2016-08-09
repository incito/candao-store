package com.candao.www.security.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.common.utils.MD5;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.dao.TbResourceDao;
import com.candao.www.data.dao.TbUserDao;
//import com.candao.www.data.dao.impl.TbUserDaoImpl;
//import com.candao.www.data.model.TbResource;
import com.candao.www.data.model.TbUser;
import com.candao.www.security.service.UserService;
@Service
public class UserServiceImpl implements UserService {
@Autowired
  private TbUserDao tbuserDao;
@Autowired
  private TbResourceDao tbResourceDao;
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
		return tbuserDao.page(params, current, pagesize);
	}
	@Override
	public boolean save(TbUser tbUser) {
		return tbuserDao.insert(tbUser)>0;
	}
	@Override
	public TbUser findById(String userid) {
		return tbuserDao.get(userid);
	}
	@Override
	public boolean update(TbUser tbUser) {
		TbUser user=tbuserDao.get(tbUser.getUserid());
		if(!tbUser.getPassword().equals(user.getPassword())){
			tbUser.setPassword(MD5.md5(tbUser.getPassword()));
		}
		return tbuserDao.update(tbUser)>0;
	}
	@Override
	public boolean deleteById(String userid,int status) {
		if(status==1){
			return tbuserDao.delete(userid,0)>0;
		}else{
			return tbuserDao.delete(userid,1)>0;
		}
	
	}
	@Override
	public List<Map<String, Object>> getUserTat() {
		return tbuserDao.getUserTat(1);
	}
  
	public boolean updatePassword(String userid,String password){
		TbUser tbUser=tbuserDao.get(userid);
		tbUser.setPassword(MD5.md5(password));
		return tbuserDao.update(tbUser)>0;
	}
	@Override
	public boolean checkUesrExist(String username) {
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("username", username);
		List<TbUser> list=tbuserDao.find(params);
		return !ValidateUtils.isEmpty(list);
	}
	@Override
	public boolean checkAllowAccessUrl(String userid, String url) {
	  boolean ret = true;
	    List<String> urlList=tbResourceDao.findAllPath();
	    if(!urlList.contains(url)){
	    	return true;
	    }
 		List<String> list=tbuserDao.getAllowAccessUrl(userid);
		ret = list.contains(url);
		return ret;
	}
	@Override
	public List<String> getAllowAccessButton(String userid) {
			return tbuserDao.getAllowAccessButton(userid);
	}
	
	@Override
	public TbUser findByuserName(String userName){
		return tbuserDao.findByuserName(userName);
	}
	
	@Override
	public List<TbUser> findAllServiceUser(){
		return tbuserDao.findAllServiceUser();
	}
	
	@Override
	public int updateLoginTime(String userid){
		return tbuserDao.updateLoginTime(userid);
	}
	@Override
	public TbUser findMaxOrderNum() {
		return tbuserDao.findMaxOrderNum();
	}
	@Override
	public void updateUserOrderNum(String username,int ordernum) {
		tbuserDao.updateUserOrderNum(username,ordernum);
	}
}
