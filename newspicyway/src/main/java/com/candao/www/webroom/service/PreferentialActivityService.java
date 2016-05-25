/**
 * 
 */
package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbHandFree;
import com.candao.www.data.model.TbInnerFree;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.User;
import com.candao.www.webroom.model.DiscountTicketsVo;
import com.candao.www.webroom.model.GrouponTicketsVO;
import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.model.PreferentialActivitySpecialStampVO;
import com.candao.www.webroom.model.VoucherVo;

/**
 * @author zhao
 *
 */
public interface PreferentialActivityService {
  /**
   * 分页查询数据
   * @param params
   * @param current
   * @param pagesize
   * @return
   */
   public Page<Map<String,Object>> page(Map<String, Object> params, int current, int pagesize);
   
   /**
    * <pre>
    * 分页查询数据. 获取适用于某个门店的优惠。
    * 门店下地用户，应该只能看到适用于当前门店的。
    * 云端用户： 可以看到他拥有权限的门店的优惠
    * 租户：    可以看到所有优惠
    * </pre>
    * @param params
    * @param current
    * @param pagesize
    * @return
    */
    public Page<Map<String,Object>> pageForBranchs(User currentUser,Map<String, Object> params, int current, int pagesize);
    
   /**
    * 新增优惠活动
    * @param preferentialActivity
    * @param type
    * @return
    */
   public boolean add(TbPreferentialActivity preferentialActivity, String type);
   
   /**
    * 根据主键更新优惠活动
    * @param preferentialActivity
    * @return
    */
   public boolean updateById(TbPreferentialActivity preferentialActivity);
   
   /**
    * 删除优惠活动
    * @param id
    * @return
    */
   public boolean deleteById(String id);
   
   
   /**
    * 保存页面提交后的 特价券对象（VO）
    * @param specialStampVO
    * @return
    */
   public boolean addSpecialStamp(PreferentialActivitySpecialStampVO specialStampVO);
   
   
   /**
    * 更新 优惠券->特价券 对象（VO）
    * @param specialStampVO 封装的特价券VO
    * @return
    */
   public boolean updateSpecialStamp(PreferentialActivitySpecialStampVO specialStampVO );
   
   /**
    * 获取优惠券->特价券 的VO对象
    * <pre>
    * 该对象包含了 
    * 	优惠券对象
    * 	门店列表 
    * 	优惠菜品列表
    * </pre>
    * @param id 优惠券主键
    * @return
    */
   public PreferentialActivitySpecialStampVO getPreferentialActivitySpecialStampVO(String activity_id);
   

   /**
    * 添加 团购券
    * @param groupon
    */
   public boolean addGrouponTicket(GrouponTicketsVO groupon);

   /**
    * 根据优惠券ID获取团购券(VO)对象
    * @param activity_id 优惠券ID
    * @return
    */
   public GrouponTicketsVO getGrouponTicket(String activity_id);
   
   /**
    * 更新团购券
    * @param groupon
    * @return
    */
   public boolean updateGrouponTicket(GrouponTicketsVO groupon);
   
   
   /**
    * 新增折扣券
    * @param discountTicket
    * @return
    */
   public boolean addDiscountTicket(DiscountTicketsVo discountTicket);
   
   /**
    * 根据优惠主键查询折扣券
    * @param activity_id
    * @return
    */
   public DiscountTicketsVo getDiscountTicketsVo(String activity_id);
   
   /**
    * 更新折扣券
    * @param discountTicket
    * @return
    */
   public boolean updateDiscountTicket(DiscountTicketsVo discountTicket);
   
   /**
    * 新增代金券
    * @param voucher
    * @return
    */
   public boolean addVoucher(VoucherVo voucher);
   /**
    * 根据优惠主键查询代金券
    * @param activity_id
    * @return
    */
   public VoucherVo getVoucherVo(String activity_id);
   /**
    * 更新代金券
    * @param voucher
    * @return
    */
   public boolean updateVoucher(VoucherVo voucher);
   
   /**
    * 新增手工优免
    * @param handFree
    * @return
    */
   public boolean addHandFree(TbHandFree handFree);
   /**
    * 删除手工优免
    * @param id
    * @return
    */
   public boolean deleteHandFree(String id);
   /**
    * 查询手工优免
    * @param param
    * @return
    */
   public List<TbHandFree> findHandFree(Map param);
   /**
    * 修改手工优免
    * @param handFree
    * @return
    */
   public boolean updateHandFree(TbHandFree handFree);
   
   /**
    * 保存内部减免（合作单位优惠）
    * @param innerfree
    * @return
    */
   public boolean saveInnerFree(TbInnerFree innerfree) ;
   
   /**
    * 删除 内部减免（合作单位优惠）
    * @param id
    * @return
    */
   public boolean deleteInnerFree(String id);
   
   /**
    * 更新 内部减免（合作单位优惠）
    * @param innerfree
    * @return
    */
   public boolean updateInnerFree(TbInnerFree innerfree) ;
   
   /**
    * 获取内部减免（合作单位）优惠 列表
    * @param param
    * @return
    */
   public Page<Map<String, Object>> pageInnerFree(Map param,int current, int pagesize);
   
   /**
    * 根据主键获取 内部减免对象
    * @param id
    * @return
    */
   public TbInnerFree findInnerFreeById(String id );
   
   
   /**
    * 根据主键 删除内部减免对象
    * @param id
    * @return
    */
   public boolean deleteInnerFreeById(String id );
   
   
   /**
    * pad接口专用。
    * 根据优惠分类，获取优惠内容。 特价券不包含菜品信息（不包含此优惠对应的 优惠菜品等，仅仅是优惠信息）
    * @param typeid 优惠分类
    * @return
    */
   public List<Map<String , Object>> findCouponsByType4Pad(String typeid);
   
   
   public List<Map<String , Object>> findCouponsByType4Pad(Map param);

   /**
 	 * 查询所有的可挂账的合作单位
 	 *
 	 */
    public List<Map<String , Object>> findCooperationUnit(Map params);
   
   /**
    * 对账单使用优惠
    * @param type  类型
    * @param sub_type 子类型（当类型为 更多优惠 06的时候，这里用子类型区分是 哪个优惠）
    * @param orderid  账单id
    * @param preferentialid  如果是特价券，则是优惠活动表的ID，否则，是 优惠活动详细表的ID
    * @param disrate  默认0（手工折扣类会上传一个>0的折扣）
    * @return
    */
   public OperPreferentialResult updateOrderDetailWithPreferential( String type , String sub_type ,String orderid , String preferentialid ,String disrate,String preferentialAmt);
   
   /**
    * 取消 账单使用的优惠
    * @param orderid
    * @param preferentialid
    * @return
    */
   public OperPreferentialResult cancelPreferentialItemInOrder(String orderid,String preferentialid);
   
   /**
    * 逻辑删除优惠活动
    * @author weizhifang
    * @since 2015-06-15
    * @param id
    * @return
    */
   public boolean deleteByStatus(String id);
   
   /**
    * 根据条件更新优惠活动
    * @param params
    * @return
    */
   public int updateBySelective(Map params);
}
