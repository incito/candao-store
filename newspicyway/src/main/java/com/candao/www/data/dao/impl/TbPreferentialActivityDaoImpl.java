/**
 * 
 */
package com.candao.www.data.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.model.TbGroupon;
import com.candao.www.data.model.TbInnerFree;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TbPreferentialActivityBranch;
import com.candao.www.data.model.TbPreferentialActivitySpecialStamp;
import com.candao.www.data.model.TbPreferentialTypeDict;

/**
 * @author zhao
 *
 */
@Repository
public class TbPreferentialActivityDaoImpl implements TbPreferentialActivityDao {
  
  @Autowired
  private DaoSupport dao;

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TPreferentialActivityDao#page(java.util.Map, int, int)
   */
  @Override
  public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
    return dao.page(PREFIX + ".page", params, current, pagesize);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TPreferentialActivityDao#get(java.lang.String)
   */
  @Override
  public TbPreferentialActivity get(String id) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.get(PREFIX + ".get", params);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TPreferentialActivityDao#find(java.util.Map)
   */
  @Override
  public <T, K, V> List<T> find(Map<K, V> params) {
    return dao.find(PREFIX + ".find", params);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TPreferentialActivityDao#insert(com.candao.www.data.model.TPreferentialActivity)
   */
  @Override
  public int insert(TbPreferentialActivity tPreferentialActivity) {
    return dao.insert(PREFIX + ".insert", tPreferentialActivity);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TPreferentialActivityDao#update(com.candao.www.data.model.TPreferentialActivity)
   */
  @Override
  public int update(TbPreferentialActivity tPreferentialActivity) {
    return dao.update(PREFIX + ".updateByPrimaryKey", tPreferentialActivity);
  }

  /* (non-Javadoc)
   * @see com.candao.www.data.dao.TPreferentialActivityDao#delete(java.lang.String)
   */
  @Override
  public int delete(String id) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.delete(PREFIX + ".delete", params);
  }

  @Override
  public String getLastPreferentialActivityCode() {
    // TODO Auto-generated method stub
    return dao.get(PREFIX + ".getLastCode", null);
  }
  
  @Override
  public int deleteSubCoupon(String sql) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("sql", sql);
    return dao.delete(PREFIX + ".deleteSubCoupon", params);
  }

  @Override
  public <K, V> List<TbPreferentialTypeDict> findPreferentialType(Map<K, V> params) {
    return dao.find(PREFIX + ".findPreferentialType", params);
  }

  @Override
  public TbPreferentialTypeDict getPreferentialType(String code) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("code", code);
    return dao.get(PREFIX + ".getPreferentialType", params);
  }

	@Override
	public int savePreferentialActivityBranchs(
			List<TbPreferentialActivityBranch> branchs) {
		//判断传入的要保存的list大小。必须大于0才可以保存，否则mybatis 报错
		if( null!=branchs && branchs.size()>0){
			return dao.insert(PREFIX+".batchSavePreferentialBranch", branchs);
		}else{
			return 0;
		}
	}
	
	@Override
	public int savePreferentialActivitySpecialStamp(
			List<TbPreferentialActivitySpecialStamp> stamps) {
		if( null!=stamps && stamps.size()>0 ){
			return dao.insert(PREFIX+".batchSavePreferentialSpecialStamp", stamps);
		}else{
			return 0;
		}
	}

	@Override
	public int deleteBranchs(String preferential) {
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("preferential", preferential);
	    return dao.delete(PREFIX + ".deleteBranch", params);
	}

	@Override
	public List<TbPreferentialActivityBranch> findPreferentialBranchs(
			String preferential_id) {
		Map<String, Object> params = new HashMap<String, Object>();
	  params.put("preferential_id", preferential_id);
		return dao.find(PREFIX + ".findPreferentialBranchs", params);
	}

	@Override
	public List<TbPreferentialActivitySpecialStamp> findPreferentialSpecialStamps(
			String preferential_id) {
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("preferential_id", preferential_id);
		return dao.find(PREFIX + ".findPreferentialSpecialStamps", params);
	}

	@Override
	public TbGroupon findPreferentialGroupon(String preferential_id) {
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("preferential_id", preferential_id);
	    List<TbGroupon> list=dao.find(PREFIX + ".findPreferentialGroupon", params);
	    
	    if( list.size()>0){
	    	return list.get(0);
	    }else{
	    	return null;
	    }
	    
	}

	@Override
	public int savePreferentialGroupon(TbGroupon groupon) {
		return dao.insert(PREFIX + ".insertGroupon", groupon);
	}

	@Override
	public int saveInnerFree(TbInnerFree innerfree) {
		// TODO Auto-generated method stub
		return dao.insert(PREFIX + ".insertInnerfree", innerfree);
	}

	@Override
	public Page<Map<String, Object>> pageInnerFree(Map params,int current, int pagesize) {
		return dao.page(PREFIX + ".pageInnerfree", params,current,pagesize);
	}

	@Override
	public int updateInnerFree(TbInnerFree innerfree) {
		// TODO Auto-generated method stub
		return dao.update(PREFIX + ".updateInnerfree", innerfree);
	}

	@Override
	public TbInnerFree findInnerFreeById(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    return  dao.get( PREFIX + ".getInnerfree", params);
	}

	@Override
	public int deleteInnerFree(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    return dao.delete(PREFIX+".deleteInnerfree", params);
	}

	@Override
	public int deletePreferentialDetail(String preferential) {
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("preferential", preferential);
	    return dao.delete(PREFIX+".deletePreferentialDetail", params);
	}

	@Override
	public int deletePreferentialDetailforStatus(String preferential) {
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("preferential", preferential);
	    return dao.delete(PREFIX+".deletePreferentialDetailforStatus", params);
	}

	@Override
	public List<Map<String, Object>> findPreferentialDetail(Map params) {
		 
		return dao.find(PREFIX+".findPreferentialDetail", params);
	}
	
	@Override
  public int deletePreferential(Map params) {
    return dao.delete(PREFIX+".deletePreferential", params);
  }
  
  @Override
  public List<TbInnerFree> findInnerFree(Map params) {
      return  dao.find( PREFIX + ".findInnerfree", params);
  }

	@Override
	public <E, K, V> Page<E> pageForBranchs(Map<K, V> params, String[] branchIds,
			int current, int pagesize) {
		Map<String,Object> p=new HashMap();
		String branch_ids= StringUtils.join(branchIds, ",");
		p.put("branch_ids", branch_ids);
		p.putAll((Map<? extends String, ? extends Object>) params);
		return dao.page(PREFIX + ".pageForBranchs", p, current, pagesize);
	}
	
	/**
	  * 根据查询条件删除优惠活动
	  * @param params
	  * @return
	  */
	public int deleteByPreferenStatus(String id) {
		return dao.update(PREFIX + ".deleteByPreferenStatus", id);
	}
	
	/**
	 * 查询挂账单位
	 */
	@Override
    public List<Map<String,Object>> findCooperationUnit(Map params){
		return dao.find(PREFIX+".findCooperationUnit", params);
	}

}
