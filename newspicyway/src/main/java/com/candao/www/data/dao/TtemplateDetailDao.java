package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TtemplateDetail;

public interface TtemplateDetailDao {
	public final static String PREFIX = TtemplateDetailDao.class.getName();
	/**
	 * 添加模板详细
	 * @param list
	 * @return
	 */
	public int addTtemplateDetail(List<TtemplateDetail> list);
	/**
	 * 删除模板详细
	 * @param menuid
	 * @return
	 */
	public int delTtemplateDetail(String menuid);
	/**
	 * 查询详细
	 * @param params
	 * @return
	 */
	public <E, K, V> List<E> getTtemplateDetailByparams(Map<K, V> params);
	/**
	 * pad端使用的模板详细的信息
	 * @author shen
	 * @date:2015年5月6日下午10:23:38
	 * @Description: TODO
	 */
	public <E, K, V> List<E> getTtemplateDetailByparamsPad(Map<K, V> params);

}
