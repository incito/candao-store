package com.candao.www.webroom.service;

import java.util.List;

import com.candao.www.webroom.model.TreeNode;


public interface ResourceService {
/**
 * 得到树列表
 * @param pid
 * @return
 */
 public TreeNode getList(String pid);
	
/**
 * 给角色增加多个资源
 * @param userid
 * @param roleids
 * @return
 */
 public boolean addResourceRole(String roleid,String[] resourceids);
 
 /**
  * 得到某个角色所有的资源Id
  * @param roleid
  * @return
  */
 public List<String>  getRoleResource(String roleid);
}
