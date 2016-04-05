/**
 * 
 */
package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
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
public interface TbPreferentialActivityDao{

  public final static String PREFIX = TbPreferentialActivityDao.class.getName();
  
  public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

  public TbPreferentialActivity get(String id);
  
  public <T, K, V> List<T> find(Map<K, V> params);
  
  public int insert(TbPreferentialActivity tPreferentialActivity);
  
  public int update(TbPreferentialActivity tPreferentialActivity);
  
  public int delete(String id );
  
  /**
   * 取最后的优惠活动编码
   * @return
   */
  public String getLastPreferentialActivityCode();
  /**
   * 删除优惠子表记录
   * @param sql
   * @deprecated  此方法废弃。参见方法 deletePreferentialDetail(String preferential);
   * @return
   */
  public int deleteSubCoupon(String sql);

  /**
   * 根据优惠活动ID删除优惠活动 具体的分类的信息（比如特价信息、折扣信息、抵用、团购信息等）。注意：此操作不删除优惠活动信息。
   * @param preferential
   * @return
   */
  public int deletePreferentialDetail(String preferential);
  /**
   * 查询所有的合作单位
   * @param params
   * @return
   */
  public List<Map<String,Object>> findCooperationUnit(Map params);

	/**
	 *  根据优惠活动ID 逻辑删除 status=1 删除优惠活动 具体的分类的信息（比如特价信息、折扣信息、抵用、团购信息等）。注意：此操作不删除优惠活动信息。
	 * @param preferential
	 * @return
	 */
	public int deletePreferentialDetailforStatus(String preferential);
  
  /**
   * 删除优惠对应的门店
   * @param preferential 优惠主键
   * @return
   */
  public int deleteBranchs(String preferential);
  
  /**
   * 查询优惠类型
   * @param params
   * @return
   */
  public <K, V> List<TbPreferentialTypeDict> findPreferentialType(Map<K, V> params);
  /**
   * 根据编码查询优惠类型
   * @param code
   * @return
   */
  public TbPreferentialTypeDict getPreferentialType(String code);
  
  
  /**
   * 批量保存 优惠券中的优惠门店。
   * <pre>
   * 	注意：
   * 	1.需要已经存在对应的优惠券主键
   * <pre>
   * @param branchs 要保存的 优惠券对应的门店
   * @return
   */
  public int savePreferentialActivityBranchs(List<TbPreferentialActivityBranch> branchs);
  
  
  /**
   * 批量保存 优惠券-->特价券
   * <pre>
   * 注意：
   * 	1.需要已经存在主键
   * 	2.需要存在对应的 优惠券的主键（属性：preferential 应当存放一个 TbPreferentialActivity对象的主键）
   * </pre>
   * @param stamp
   * @return
   */
  public int savePreferentialActivitySpecialStamp( List<TbPreferentialActivitySpecialStamp> stamps );
  
  /**
   * 根据优惠券 ID，获取该优惠券对应的优惠门店列表
   * @param activity_id 优惠活动ID
   * @return
   */
  public List<TbPreferentialActivityBranch> findPreferentialBranchs(String preferential_id);
  
  /**
   * 根据优惠券ID，获取该优惠券对应的 优惠菜品列表
   * @param preferential_id 优惠活动ID
   * @return
   */
  public List<TbPreferentialActivitySpecialStamp> findPreferentialSpecialStamps(String preferential_id);
  
  
  //-----团购券-------\\
  /**
   * 根据活动ID获取优惠活动对应的具体的 团购信息
   * @param preferential_id
   * @return
   */
  public TbGroupon findPreferentialGroupon(String preferential_id);
  
  /**
   * 保存优惠券->团购券
   * @param groupon
   * @return
   */
  public int savePreferentialGroupon(TbGroupon groupon);
  
  /**
   * 保存内部减免（合作单位）优惠
   * @param innerfree
   * @return
   */
  public int saveInnerFree(TbInnerFree innerfree);
  
  /**
   * 更新 内部减免/合作单位优惠
   * @param innerfree
   * @return
   */
  public int updateInnerFree(TbInnerFree innerfree);
  
  /**
   * 根据优惠ID 获取 内部减免/合作单位 优惠列表
   * @param params
   * @return
   */
  public Page<Map<String, Object>> pageInnerFree(Map params,int current, int pagesize);
  
  /**
   * 根据主键获取 内部减免对象
   * @param id
   * @return
   */
  public TbInnerFree findInnerFreeById(String id );
  
  /**
   * 根据ID 删除 内部减免/合作单位 对象
   * @param id
   * @return
   */
  public int deleteInnerFree(String id);
  
  /**
   * 根据参数查询 优惠的具体信息表的数据
   * @param params
   * @return
   */
  public List<Map<String,Object>> findPreferentialDetail(Map params);
  
  /**
   * 根据查询条件删除优惠活动
   * @param params
   * @return
   */
  public int deletePreferential(Map params);
  
  /**
   * 根据优惠ID 获取 内部减免/合作单位 优惠列表
   * @param params
   * @return
   */
  public List<TbInnerFree> findInnerFree(Map params);
  
  /**
   * 分页获取优惠活动。该优惠活动是根据门店获取的：门店只能看到特定门店，云端用户能看到他有权限的对应的门店的优惠，租户可以看到所有的
   * @param params 目前没有用到，用做以后扩展用
   * @param branchIds  存放门店编号
   * @param current
   * @param pagesize
   * @return
   */
  public <E, K, V> Page<E> pageForBranchs(Map<K, V> params,String[] branchIds, int current, int pagesize);
  
  
  /**
   * 根据查询条件删除优惠活动
   * @param params
   * @return
   */
  public int deleteByPreferenStatus(String id);
}
