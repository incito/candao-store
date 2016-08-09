package com.candao.www.permit.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.page.Page;
import com.candao.www.data.dao.BusinessUserDao;
import com.candao.www.data.model.BusinessUser;
import com.candao.www.data.model.User;
import com.candao.www.permit.common.BusinessException;
import com.candao.www.permit.common.Constants;
import com.candao.www.utils.SessionUtils;

@Service("businessUserService")
public class BusinessUserServiceImpl extends UserServiceImpl {
  @Autowired
  private BusinessUserDao businessUserDao;

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public User addUser(User user) {
	  
	User currentUser = SessionUtils.getCurrentUser();
	user.setStatus(Constants.ENABLE);
    user.setUserType(Constants.BUSINESS);
    user.setCreatetime(new Timestamp(System.currentTimeMillis()));
    user.setAccount(generalAccount());
    user.setTenantid(currentUser.getTenantid());
    User addUser = super.addUser(user);
    if (addUser == null) {
      return null;
    }
    BusinessUser businessUser = (BusinessUser) user;
//    String name = businessUser.getBusinessName();
//    if (name == null || name.trim().length() <= 0) {
//      throw new BusinessException("企业名称不能为空！");
//    }
//    List<BusinessUser> businessUsers = businessUserDao.queryBusinessUserbyName(name);
//    if (businessUsers != null && businessUsers.size() > 0) {
//      throw new BusinessException("系统内已经存在企业名为（" + name + "）的租户。");
//    }
    businessUser.setUserId(user.getId());
    
    return businessUser;
    
//    int result = businessUserDao.addUser(businessUser);
//    if (result == 0) {
//      return null;
//    } else {
//      return businessUser;
//    }
  }

  /**
   * 生成账号
   * 
   * @return
   */
  private String generalAccount() {
    String account = null;
    List<String> accounts = businessUserDao.findLastAccount();
    if (accounts != null && accounts.size() > 0) {
      try {
        account = accounts.get(0);
        Integer temp = Integer.parseInt(account) + 1;
        // 不能有连续4位重复数字
        Pattern pattern = Pattern.compile("(\\d)*(\\d)\\2{3}(\\d)*");
        while (pattern.matcher(temp.toString()).matches()) {
          temp++;
        }
        account = temp.toString();
      } catch (Exception e) {
        throw new BusinessException("创建企业账户失败。");
      }
      // 最长6位
      if (account.length() > 6) {
        throw new BusinessException("创建企业账户失败，超出账户长度限制。");
      }
    } else {
      account = Constants.FIRST_BUSINESS_ACCOUNT;
    }
    return account;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public User updateUser(User user) {
    User updateUser = super.updateUser(user);
    if (updateUser == null) {
      return null;
    }
    BusinessUser businessUser = (BusinessUser) user;
    businessUser.setUserId(user.getId());
    String name = businessUser.getBusinessName();
    
    return businessUser;
//    
//    if (name == null || name.trim().length() <= 0) {
//      throw new BusinessException("企业名称不能为空！");
//    }
//    // 查询是否有重复的账号
//    Map<String, Object> queryMap = new HashMap<String, Object>();
//    queryMap.put("businessName", name);
//    // 查重时需精确匹配
//    queryMap.put("exactFind", true);
//    List<BusinessUser> users = businessUserDao.queryUserList(queryMap);
//    if (users != null && users.size() > 0) {
//      boolean isDup = false;
//      for (int i = 0; i < users.size(); i++) {
//        if (!users.get(i).getId().equals(user.getId())) {
//          isDup = true;
//        }
//      }
//      if (isDup) {
//        throw new BusinessException("系统内已经存在企业名称为（" + businessUser.getBusinessName() + "）的租户。");
//      }
//    }
//    int result = businessUserDao.updateUser(businessUser);
//    if (result == 0) {
//      return null;
//    } else {
//      return businessUser;
//    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Integer deleteUserById(String userId) {
    int ret = super.deleteUserById(userId);
    if (ret != 1) {
      return ret;
    }
//    ret = businessUserDao.deleteUserById(userId);
    return ret;
  }

  /**
   * 分页查询用户
   * 
   * @param parameterObject
   * @param skipResults
   * @param maxResults
   * @return
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Page<Map<String, Object>> queryUserPage(Map<String, Object> userMap, Integer currentPage,
      Integer pageSize) {
	  userMap.put("userType",Constants.BUSINESS);
	  return businessUserDao.queryUserPage(userMap, currentPage, pageSize);
  }

  /**
   * 不分页查询用户
   * 
   * @param parameterObject
   * @param skipResults
   * @param maxResults
   * @return
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public List<User> queryUserList(Map<String, Object> userMap) {
    List<BusinessUser> businessUsers = businessUserDao.queryUserList(userMap);
    List<User> users = new ArrayList<User>();
    users.addAll(businessUsers);
    return users;
  }

  /**
   * 根据id列查询用户
   * 
   * @param userId
   * @return
   */
  @Override
  public User getUserById(String userId) {
    if (userId == null || userId.trim().length() <= 0) {
      throw new BusinessException("缺少查询用户id");
    }
    User user = businessUserDao.getUserById(userId);
    return user;
  }
  
  
  
  @Override
  public HashSet<String> getAuthedUrls(User user){
    HashSet<String> authed = new HashSet<String>();
    
    return authed;
  }

}
 