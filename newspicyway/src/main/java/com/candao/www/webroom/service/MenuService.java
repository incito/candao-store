package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.Tmenu;
import com.candao.www.data.model.TtemplateDishUnit;
import com.candao.www.webroom.model.MenuGroup;

public interface MenuService {
	/**
	 * 保存菜谱
	 * @param menuGroup
	 * @return
	 */
	public boolean saveMenu(MenuGroup menuGroup);
	/**
	 * 修改菜谱
	 * @param menuGroup
	 * @return
	 */
	public boolean updateMenu(MenuGroup menuGroup);
	/**
	 * 查看所有菜谱
	 * @param params
	 * @return
	 */
	public List<Tmenu> getMenuList(Map<String,Object> params);
	/**
	 * 查看单个菜谱信息
	 * @param menuid
	 * @return
	 */
	public MenuGroup getMenuById(String menuid);
	
	/**
	 * 删除菜谱
	 * @param menuid
	 * @return
	 */
	public boolean deleteMenuById(String menuid);
	/**
	 * 复制菜谱
	 * @param menuid
	 * @return
	 */
	public boolean copyMenu(String oldmenuid,MenuGroup menuGroup);
	/**
	 * 
	 * @author shen
	 * @date:2015年5月6日下午1:37:12
	 * @Description: TODO获取菜谱数据，pad接口中调用
	 */
	public Map<String,Object> getMenuData();
	/**
	 * 获取菜谱分类 pad端调用
	 * @author shen
	 * @date:2015年5月6日下午11:35:37
	 * @Description: TODO
	 */
	public Map<String,Object> getMenuColumn();
	/**
	 * 门店查看菜谱，获取当前菜谱的分类
	 * @author shen
	 * @date:2015年6月2日下午6:19:18
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getBranchMenuColumn(Map<String,Object> params);
	/**
	 * 根据菜谱id和分类id，获取该分类下的菜品信息
	 * @author shen
	 * @date:2015年6月2日下午6:28:30
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getBranchMenuDishByType(Map<String,Object> params);
	/**
	 * 获取菜谱中菜品的信息
	 * @author shen
	 * @date:2015年6月2日下午8:19:12
	 * @Description: TODO
	 */
	public Map<String,Object> getMenuDishDetailById(Map<String,Object> params);
	/**
	 * 获取菜谱鱼锅数据,逗号分隔鱼锅的id
	 * @author shen
	 * @date:2015年5月7日上午10:21:29
	 * @Description: TODO
	 */
	public List<Map<String, Object>> getMenuFishPot(String jsonString);
	/**
	 * 获取菜谱套餐数据,逗号分隔鱼锅的id
	 * @author shen
	 * @date:2015年5月7日上午10:21:29
	 * @Description: TODO
	 */
	public Map<String,Object> getMenuCombodish(String jsonString);
	/**
	 * 获取菜谱双拼鱼锅,逗号分隔鱼锅的id
	 * @author shen
	 * @date:2015年5月7日上午10:21:29
	 * @Description: TODO
	 */
	public List<Map<String,Object>> getMenuSpfishpot(String jsonString);
	/**
	 * 更新菜谱中某个菜的估清
	 * @author shen
	 * @date:2015年5月11日下午8:12:25
	 * @Description: TODO
	 */
	public boolean updateDishStatus(Map<String,Object> params);
	public TtemplateDishUnit findOneTtd(Map<String, Object> paramsTtd);
	/**
	 * 查询出所有已启用和定时启用的菜谱
	 * @author shen
	 * @date:2015年5月13日下午7:58:01
	 * @Description: TODO
	 */
	public List<Tmenu> findEffectMenu(Map<String,Object> params);
	/**
	 * 根据门店找菜谱,查询所有启用中的菜谱
	 * @author shen
	 * @date:2015年5月14日下午2:45:47
	 * @Description: TODO
	 */
	public List<Map<String,Object>> findMenuByBrachid();
	/**
	 * 获取菜谱人气菜品
	 * @author shen
	 * @date:2015年5月26日下午4:15:57
	 * @Description: TODO
	 */
	public Map<String,Object> getHeatDishList(Map<String,Object> params);
	public Tmenu getMenuNameById(String menuid);


	/**
	 * 通知其他设备菜品状态改变（估清、取消估清）
	 * @param dishId 菜品ID
	 * @param code  操作代码 1估清，2取消估清
     */
	public void  notifyDishStatus(String dishId,short code);
}
