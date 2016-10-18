package com.candao.www.bossstore.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.bossstore.dao.TUserDao;

import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: 张文栋
 * Date: 15/7/30
 * Desc: 用户业务类
 */
@Service("bUserService")
@Transactional(readOnly=false)
public class UserService {
	
	@Autowired
	private TUserDao tUserDao;
    /**
     * 获取当天值班经理信息
     * @return map
     */
    public JSONObject getOnDutyManagerInfo() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("branchId", PropertiesUtils.getValue("current_branch_id"));
        Map<String,String> openUser = tUserDao.getOpenUser(params);
       /* if (openUser != null) {
            Object[] objects = (Object[]) openUser;
            map.put("name", objects[0]);
            map.put("mobile", objects[1]);
        }*/

        return JSONObject.fromObject(openUser);
    }
}
