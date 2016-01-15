/**
 * 
 */
package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.SchedulingDao;

/**
 * 排班报表分析
 * @author zhouyao
 * @serialData 2015-12-29
 */
@Repository
public class SchedulingDaoImpl implements SchedulingDao {
  
  @Autowired
  private DaoSupport daoSupport;
  
  @Override
 	public <T, K, V> List<T> schedulingReport(Map<K, V> params) {
 	     return  daoSupport.find(PREFIX + ".scheduling", params);
 	}

}
